package fr.clickdroit.api.utils.block;

import fr.clickdroit.api.API;
import fr.clickdroit.api.utils.jnbt.ByteArrayTag;
import fr.clickdroit.api.utils.jnbt.CompoundTag;
import fr.clickdroit.api.utils.jnbt.NBTInputStream;
import fr.clickdroit.api.utils.jnbt.ShortTag;
import fr.clickdroit.api.utils.jnbt.StringTag;
import fr.clickdroit.api.utils.jnbt.Tag;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;

public class Schematic {
    private byte[] data;

    private byte[] blockId;

    private byte[] addId;

    private short[] blocks;

    private short width;

    private short lenght;

    private short height;

    public Schematic(byte[] data, byte[] blockId, byte[] addId, short[] blocks, short width, short lenght, short height) {
        this.data = data;
        this.blockId = blockId;
        this.addId = addId;
        this.blocks = blocks;
        this.width = width;
        this.lenght = lenght;
        this.height = height;
    }

    public static void pasteSchematic(Location loc, Schematic schematic) {
        short[] blocks = schematic.getBlocks();
        byte[] blockData = schematic.getData();
        short length = schematic.getLenght();
        short width = schematic.getWidth();
        short height = schematic.getHeight();
        for (int y = 0; y < height; y++) {
            for (int z = 0; z < length; z++) {
                for (int x = 0; x < width; x++)
                    schematic.pasteBlock(loc, x, y, z);
            }
        }
    }

    public void paste(Location loc) {
        pasteSchematic(loc, this);
    }

    public int getIndex(int x, int y, int z) {
        return y * this.width * this.lenght + z * this.width + x;
    }

    public Block getBlock(Location location, int x, int y, int z) {
        return (new Location(location.getWorld(), x + location.getX(), y + location.getY(), z + location.getZ())).getBlock();
    }

    public short getBlock(int x, int y, int z) {
        return this.blocks[getIndex(x, y, z)];
    }

    public void pasteBlock(Location location, int x, int y, int z) {
        int index = getIndex(x, y, z);
        getBlock(location, x, y, z).setTypeIdAndData(this.blocks[index], this.data[index], true);
    }

    public static Schematic loadSchematic(String fileName) {
        InputStream inputStream = API.getAPI().getResource(fileName);
        if (inputStream != null) {
            File rep = API.getAPI().getDataFolder();
            File file = new File(rep, fileName);
            try {
                FileUtils.copyInputStreamToFile(inputStream, file);
                return loadSchematic(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Schematic loadSchematic(File file) throws IOException {
        return loadSchematic(new FileInputStream(file));
    }

    public static Schematic loadSchematic(InputStream stream) throws IOException {
        NBTInputStream nbtStream = new NBTInputStream(stream);
        CompoundTag schematicTag = (CompoundTag)nbtStream.readTag();
        nbtStream.close();
        Map<String, Tag> schematic = schematicTag.getValue();
        short width = ((ShortTag)getChildTag(schematic, "Width", ShortTag.class)).getValue().shortValue();
        short length = ((ShortTag)getChildTag(schematic, "Length", ShortTag.class)).getValue().shortValue();
        short height = ((ShortTag)getChildTag(schematic, "Height", ShortTag.class)).getValue().shortValue();
        String materials = ((StringTag)getChildTag(schematic, "Materials", StringTag.class)).getValue();
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
        return new Schematic(blockData, blockId, addId, blocks, width, length, height);
    }

    private static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws IllegalArgumentException {
        if (!items.containsKey(key))
            throw new IllegalArgumentException("Schematic file is missing a \"" + key + "\" tag");
        Tag tag = items.get(key);
        if (!expected.isInstance(tag))
            throw new IllegalArgumentException(key + " tag is not of tag displayer " + expected.getName());
        return expected.cast(tag);
    }

    public byte[] getData() {
        return this.data;
    }

    public byte[] getBlockId() {
        return this.blockId;
    }

    public byte[] getAddId() {
        return this.addId;
    }

    public short[] getBlocks() {
        return this.blocks;
    }

    public short getWidth() {
        return this.width;
    }

    public short getLenght() {
        return this.lenght;
    }

    public short getHeight() {
        return this.height;
    }
}
