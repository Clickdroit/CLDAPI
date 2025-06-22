package fr.clickdroit.api.utils.block;

import fr.clickdroit.api.utils.jnbt.v2.*;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class SchematicV2 {
    private File schematicFile;

    private Method nbtCreateTagMethod;

    private SchematicEvent defEvent = null;

    private ArrayList<BlockPosition> blockPositions = new ArrayList<>();

    public SchematicV2(File file) {
        try {
            this.schematicFile = file;
            (this.nbtCreateTagMethod = NBTBase.class.getDeclaredMethod("createTag", new Class[] { byte.class })).setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SchematicV2(File file, SchematicEvent defEvent) {
        this(file);
        this.defEvent = defEvent;
    }

    public void paste(Location loc, boolean log, boolean pasteAir) {
        paste(loc, log, this.defEvent, pasteAir);
    }

    public void paste(Location loc, boolean log, SchematicEvent event, boolean pasteAir) {
        if (event == null)
            event = new SchematicEvent() {
                public SchematicV2.BlockInfo onPaste(Location loc, SchematicV2.BlockInfo info) {
                    return info;
                }

                public void onPasteEnd(Short width, Short length) {}

                public void onFileNotFound() {}
            };
        try {
            long start = System.currentTimeMillis();
            if (log)
                Bukkit.getLogger().info("Pasting " + this.schematicFile.getAbsolutePath() + "...");
            if (!this.schematicFile.exists()) {
                event.onFileNotFound();
                return;
            }
            NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(new FileInputStream(this.schematicFile)));
            NamedTag rootTag = nbtStream.readNamedTag();
            nbtStream.close();
            if (!rootTag.getName().equals("Schematic"))
                throw new DataException("Tag \"Schematic\" does not exist or is not first");
            CompoundTag schematicTag = (CompoundTag)rootTag.getTag();
            Map<String, Tag> schematic = schematicTag.getValue();
            if (!schematic.containsKey("Blocks"))
                throw new DataException("Schematic file is missing a \"Blocks\" tag");
            short width = ((ShortTag)getChildTag(schematic, "Width", ShortTag.class)).getValue().shortValue();
            short length = ((ShortTag)getChildTag(schematic, "Length", ShortTag.class)).getValue().shortValue();
            short height = ((ShortTag)getChildTag(schematic, "Height", ShortTag.class)).getValue().shortValue();
            byte[] blockId = ((ByteArrayTag)getChildTag(schematic, "Blocks", ByteArrayTag.class)).getValue();
            byte[] blockData = ((ByteArrayTag)getChildTag(schematic, "Data", ByteArrayTag.class)).getValue();
            byte[] addId = new byte[0];
            short[] blocks = new short[blockId.length];
            if (schematic.containsKey("AddBlocks"))
                addId = ((ByteArrayTag)getChildTag(schematic, "AddBlocks", ByteArrayTag.class)).getValue();
            for (int index = 0; index < blockId.length; index++) {
                if (index >> 1 >= addId.length) {
                    blocks[index] = (short)(blockId[index] & 0xFF);
                } else if ((index & 0x1) == 0) {
                    blocks[index] = (short)(((addId[index >> 1] & 0xF) << 8) + (blockId[index] & 0xFF));
                } else {
                    blocks[index] = (short)(((addId[index >> 1] & 0xF0) << 4) + (blockId[index] & 0xFF));
                }
            }
            List<Tag> tileEntities = ((ListTag)getChildTag(schematic, "TileEntities", ListTag.class)).getValue();
            Map<BlockPosition, Map<String, Tag>> tileEntitiesMap = new HashMap<>();
            for (Tag tag : tileEntities) {
                if (!(tag instanceof CompoundTag))
                    continue;
                CompoundTag t = (CompoundTag)tag;
                int x = 0;
                int y = 0;
                int z = 0;
                Map<String, Tag> values = new HashMap<>();
                for (Map.Entry<String, Tag> entry : (Iterable<Map.Entry<String, Tag>>)t.getValue().entrySet()) {
                    if (((String)entry.getKey()).equals("x")) {
                        if (entry.getValue() instanceof IntTag)
                            x = ((Integer)((Tag)entry.getValue()).getValue()).intValue();
                    } else if (((String)entry.getKey()).equals("y")) {
                        if (entry.getValue() instanceof IntTag)
                            y = ((Integer)((Tag)entry.getValue()).getValue()).intValue();
                    } else if (((String)entry.getKey()).equals("z") && entry.getValue() instanceof IntTag) {
                        z = ((Integer)((Tag)entry.getValue()).getValue()).intValue();
                    }
                    values.put(entry.getKey(), entry.getValue());
                }
                BlockPosition vec = new BlockPosition(x, y, z);
                this.blockPositions.add(vec);
                tileEntitiesMap.put(vec, values);
            }
            if (log)
                Bukkit.getLogger().info("Starting paste of " + (width * height * length) + " blocks");
            Long lastLog = Long.valueOf(System.currentTimeMillis());
            for (int x2 = 0; x2 < width; x2++) {
                for (int y2 = 0; y2 < height; y2++) {
                    for (int z2 = 0; z2 < length; z2++) {
                        int index2 = y2 * width * length + z2 * width + x2;
                        BlockPosition pt = new BlockPosition(x2, y2, z2);
                        Location l = new Location(loc.getWorld(), x2 + loc.getX(), y2 + loc.getY(), z2 + loc.getZ());
                        Block block = l.getBlock();
                        byte d = blockData[index2];
                        Material m = Material.getMaterial(blocks[index2]);
                        if (!pasteAir) {
                            if (m != Material.AIR) {
                                BlockInfo info = event.onPaste(l, new BlockInfo(m, d));
                                if (info != null) {
                                    m = info.material;
                                    d = info.data;
                                }
                                if (info.material != Material.AIR) {
                                    block.setTypeIdAndData(m.getId(), d, false);
                                    if (tileEntitiesMap.containsKey(pt)) {
                                        CraftWorld cw = (CraftWorld)l.getWorld();
                                        TileEntity tileEntity = cw.getHandle().getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
                                        CompoundTag nativeTag = new CompoundTag(tileEntitiesMap.get(pt));
                                        if (tileEntity != null) {
                                            NBTTagCompound tag2 = (NBTTagCompound)fromNative((Tag)nativeTag);
                                            tag2.set("x", (NBTBase)new NBTTagInt(l.getBlockX()));
                                            tag2.set("y", (NBTBase)new NBTTagInt(l.getBlockY()));
                                            tag2.set("z", (NBTBase)new NBTTagInt(l.getBlockZ()));
                                            tileEntity.a(tag2);
                                        }
                                    }
                                    if (log && System.currentTimeMillis() - lastLog.longValue() > 10000L) {
                                        float paste = (x2 * height * length + y2 * length + z2);
                                        float total = (width * height * length);
                                        Float progression = Float.valueOf(paste / total);
                                        Integer remaining = Integer.valueOf(Math.round((total - paste) / paste * (float)(System.currentTimeMillis() - start) / 1000.0F));
                                        Bukkit.getLogger().info("Pasting : " + Math.round(100.0F * progression.floatValue()) + " % (" + remaining + " seconds remaining)");
                                        lastLog = Long.valueOf(System.currentTimeMillis());
                                    }
                                }
                            }
                        } else {
                            BlockInfo info = event.onPaste(l, new BlockInfo(m, d));
                            if (info != null) {
                                m = info.material;
                                d = info.data;
                            }
                            block.setTypeIdAndData(m.getId(), d, false);
                            if (tileEntitiesMap.containsKey(pt)) {
                                CraftWorld cw = (CraftWorld)l.getWorld();
                                TileEntity tileEntity = cw.getHandle().getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
                                CompoundTag nativeTag = new CompoundTag(tileEntitiesMap.get(pt));
                                if (tileEntity != null) {
                                    NBTTagCompound tag2 = (NBTTagCompound)fromNative((Tag)nativeTag);
                                    tag2.set("x", (NBTBase)new NBTTagInt(l.getBlockX()));
                                    tag2.set("y", (NBTBase)new NBTTagInt(l.getBlockY()));
                                    tag2.set("z", (NBTBase)new NBTTagInt(l.getBlockZ()));
                                    tileEntity.a(tag2);
                                }
                            }
                            if (log && System.currentTimeMillis() - lastLog.longValue() > 10000L) {
                                float paste = (x2 * height * length + y2 * length + z2);
                                float total = (width * height * length);
                                Float progression = Float.valueOf(paste / total);
                                Integer remaining = Integer.valueOf(Math.round((total - paste) / paste * (float)(System.currentTimeMillis() - start) / 1000.0F));
                                Bukkit.getLogger().info("Pasting : " + Math.round(100.0F * progression.floatValue()) + " % (" + remaining + " seconds remaining)");
                                lastLog = Long.valueOf(System.currentTimeMillis());
                            }
                        }
                    }
                }
            }
            if (log)
                Bukkit.getLogger().info("Pasted successfully in " + (System.currentTimeMillis() - start) + " milliseconds.");
            event.onPasteEnd(Short.valueOf(width), Short.valueOf(length));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Material, List<Location>> search(Location loc, final Collection<Material> types) {
        final Map<Material, List<Location>> found = new HashMap<>();
        for (Material t : types)
            found.put(t, new ArrayList<>());
        paste(loc, false, new SchematicEvent() {
            public SchematicV2.BlockInfo onPaste(Location loc, SchematicV2.BlockInfo info) {
                if (types.contains(info.material))
                    ((List<Location>)found.get(info.material)).add(loc);
                return new SchematicV2.BlockInfo(Material.AIR, (byte)0);
            }

            public void onPasteEnd(Short width, Short length) {}

            public void onFileNotFound() {}
        }, false);
        return found;
    }

    private <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws IllegalArgumentException {
        if (!items.containsKey(key))
            throw new IllegalArgumentException("Schematic file is missing a \"" + key + "\" tag");
        Tag tag = items.get(key);
        if (!expected.isInstance(tag))
            throw new IllegalArgumentException(String.valueOf(key) + " tag is not of tag type " + expected.getName());
        return expected.cast(tag);
    }

    private NBTBase fromNative(Tag foreign) {
        if (foreign == null)
            return null;
        if (foreign instanceof CompoundTag) {
            NBTTagCompound tag = new NBTTagCompound();
            for (Map.Entry<String, Tag> entry : (Iterable<Map.Entry<String, Tag>>)((CompoundTag)foreign).getValue().entrySet())
                tag.set(entry.getKey(), fromNative(entry.getValue()));
            return (NBTBase)tag;
        }
        if (foreign instanceof ByteTag)
            return (NBTBase)new NBTTagByte(((ByteTag)foreign).getValue().byteValue());
        if (foreign instanceof ByteArrayTag)
            return (NBTBase)new NBTTagByteArray(((ByteArrayTag)foreign).getValue());
        if (foreign instanceof DoubleTag)
            return (NBTBase)new NBTTagDouble(((DoubleTag)foreign).getValue().doubleValue());
        if (foreign instanceof FloatTag)
            return (NBTBase)new NBTTagFloat(((FloatTag)foreign).getValue().floatValue());
        if (foreign instanceof IntTag)
            return (NBTBase)new NBTTagInt(((IntTag)foreign).getValue().intValue());
        if (foreign instanceof IntArrayTag)
            return (NBTBase)new NBTTagIntArray(((IntArrayTag)foreign).getValue());
        if (foreign instanceof ListTag) {
            NBTTagList tag2 = new NBTTagList();
            ListTag foreignList = (ListTag)foreign;
            for (Tag t : foreignList.getValue())
                tag2.add(fromNative(t));
            return (NBTBase)tag2;
        }
        if (foreign instanceof LongTag)
            return (NBTBase)new NBTTagLong(((LongTag)foreign).getValue().longValue());
        if (foreign instanceof ShortTag)
            return (NBTBase)new NBTTagShort(((ShortTag)foreign).getValue().shortValue());
        if (foreign instanceof StringTag)
            return (NBTBase)new NBTTagString(((StringTag)foreign).getValue());
        if (foreign instanceof fr.clickdroit.api.utils.jnbt.v2.EndTag)
            try {
                return (NBTBase)this.nbtCreateTagMethod.invoke(null, new Object[] { Byte.valueOf((byte)0) });
            } catch (Exception e) {
                return null;
            }
        throw new IllegalArgumentException("Don't know how to make NMS " + foreign.getClass().getCanonicalName());
    }

    public ArrayList<BlockPosition> getBlockPositions() {
        return this.blockPositions;
    }

    public static class BlockInfo {
        public Material material;

        public byte data;

        public BlockInfo(Material mat, byte data) {
            this.material = mat;
            this.data = data;
        }
    }

    public static interface SchematicEvent {
        SchematicV2.BlockInfo onPaste(Location param1Location, SchematicV2.BlockInfo param1BlockInfo);

        void onPasteEnd(Short param1Short1, Short param1Short2);

        void onFileNotFound();
    }
}
