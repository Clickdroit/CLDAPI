package fr.clickdroit.api.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemCreator {
    private ItemStack item;

    private Player possesseur;

    private String creator_name;

    private ArrayList<String> tag;

    private int slot;

    private ArrayList<Pattern> patterns;

    public ItemCreator(Material material) {
        this.item = new ItemStack(material);
    }

    public ItemCreator(ItemStack item) {
        setMaterial(item.getType());
        setAmount(Integer.valueOf(item.getAmount()));
        setDurability(Short.valueOf(item.getDurability()));
        setName(item.getItemMeta().getDisplayName());
        setEnchantments(item.getItemMeta().getEnchants());
        setLores(item.getItemMeta().getLore());
    }

    public ItemCreator(ItemCreator itemcreator) {
        this.item = itemcreator.getItem();
        this.possesseur = itemcreator.getPossesseur();
        this.creator_name = itemcreator.getCreator_name();
        this.tag = new ArrayList<>(itemcreator.getTag());
    }

    public ItemCreator(String itemcreatorstring) {
        this.item = new ItemStack(Material.STONE);
        fromString(itemcreatorstring);
    }

    public String toString() {
        StringBuilder itemcreator = new StringBuilder();
        itemcreator.append("ItemCreator:{");
        itemcreator.append("Slot:{" + Integer.toString(this.slot) + "}");
        itemcreator.append(",Amount:{" + getAmount() + "}");
        itemcreator.append(",Durability:{" + getDurability() + "}");
        if (getName() != null)
            itemcreator.append(",Name:{" + getName() + "}");
        if (getLores() != null) {
            itemcreator.append(",Lores:{");
            for (String lore : getLores())
                itemcreator.append("['" + lore + "'],");
            itemcreator.replace(itemcreator.length() - 1, itemcreator.length(), "}");
        }
        if (getEnchantments() != null) {
            itemcreator.append(",Enchantments:{");
            for (Map.Entry<Enchantment, Integer> e : getEnchantments().entrySet())
                itemcreator.append("[" + ((Enchantment)e.getKey()).toString() + "," + e.getValue() + "],");
            itemcreator.replace(itemcreator.length() - 1, itemcreator.length(), "}");
        }
        if (getItemFlags() != null) {
            itemcreator.append(",ItemFlags:{");
            for (ItemFlag itemflag : getItemFlags())
                itemcreator.append("[" + itemflag.toString() + "],");
            itemcreator.replace(itemcreator.length() - 1, itemcreator.length(), "}");
        }
        if (getPossesseur() != null)
            itemcreator.append(",Possesseur:{" + getPossesseur().getUniqueId().toString() + "}");
        if (getCreator_name() != null)
            itemcreator.append(",Creator_name:{" + getCreator_name() + "}");
        if (getTag() != null) {
            itemcreator.append(",Tag:{");
            for (String tag : getTag())
                itemcreator.append("[" + tag + "],");
            itemcreator.replace(itemcreator.length() - 1, itemcreator.length(), "}");
        }
        itemcreator.append("}");
        return itemcreator.toString();
    }

    public ItemCreator fromString(String itemcreatorstring) {
        if (itemcreatorstring.substring(0, 10).equals("ItemCreator")) {
            itemcreatorstring = itemcreatorstring.substring(12, itemcreatorstring.length() - 2);
            while (itemcreatorstring != "") {
                ArrayList<String> lores;
                HashMap<Enchantment, Integer> enchantments;
                ArrayList<ItemFlag> itemflags;
                ArrayList<Pattern> patterns;
                HashMap<Enchantment, Integer> storedenchantments;
                ArrayList<String> taglist;
                int i = 0;
                while (itemcreatorstring.charAt(i) != ':')
                    i++;
                String currentname = itemcreatorstring.substring(0, i - 1);
                itemcreatorstring = itemcreatorstring.substring(i);
                Integer f = Integer.valueOf(0);
                boolean instring = false;
                while (itemcreatorstring.charAt(f.intValue()) != '}' && !instring) {
                    if (itemcreatorstring.substring(f.intValue(), f.intValue()).equals("'"))
                        instring = !instring;
                    Integer integer1 = f, integer2 = f = Integer.valueOf(f.intValue() + 1);
                }
                String currentpacket = itemcreatorstring.substring(0, f.intValue());
                itemcreatorstring = itemcreatorstring.substring(f.intValue() + 1);
                System.out.println("  ");
                System.out.println("  ");
                System.out.println("  ");
                System.out.println("  ITEM CREATOR   ");
                System.out.println(currentname);
                System.out.println("  ");
                switch (currentname) {
                    case "Type":
                        System.out.println("  TYPE: " + Material.valueOf(currentpacket.substring(0, currentpacket.length() - 1)).toString());
                        setMaterial(Material.valueOf(currentpacket.substring(0, currentpacket.length() - 1)));
                    case "Slot":
                        setSlot(Integer.valueOf(currentpacket.substring(1, currentpacket.length() - 2)).intValue());
                    case "Amount":
                        setAmount(Integer.valueOf(currentpacket.substring(1, currentpacket.length() - 1)));
                    case "Durability":
                        setDurability(Short.valueOf(currentpacket.substring(1, currentpacket.length() - 1)));
                    case "Name":
                        setName(currentpacket.substring(1, currentpacket.length() - 1));
                    case "Lores":
                        lores = new ArrayList<>();
                        currentpacket = currentpacket.substring(1, currentpacket.length() - 1);
                        while (currentpacket != "") {
                            Boolean inlore = Boolean.valueOf(false);
                            Integer c = Integer.valueOf(0);
                            if (currentpacket.charAt(0) == ',')
                                currentpacket = currentpacket.substring(1);
                            while (currentpacket.charAt(c.intValue()) != ']' && !inlore.booleanValue()) {
                                if (currentpacket.substring(c.intValue(), c.intValue()).equals("'"))
                                    inlore = Boolean.valueOf(!inlore.booleanValue());
                                Integer integer1 = c, integer2 = c = Integer.valueOf(c.intValue() + 1);
                            }
                            lores.add(currentpacket.substring(1, c.intValue() - 1));
                            currentpacket = currentpacket.substring(c.intValue());
                        }
                        setLores(lores);
                    case "Glow":
                        setGlow(Boolean.valueOf(currentpacket.substring(1, currentpacket.length() - 1)));
                    case "Enchantments":
                        enchantments = new HashMap<>();
                        currentpacket = currentpacket.substring(1, currentpacket.length() - 1);
                        while (currentpacket != "") {
                            if (currentpacket.charAt(0) == ',')
                                currentpacket = currentpacket.substring(1);
                            Integer c = Integer.valueOf(0);
                            while (currentpacket.charAt(c.intValue()) != ']')
                                Integer integer1 = c, integer2 = c = Integer.valueOf(c.intValue() + 1);
                            String current = currentpacket.substring(1, c.intValue() - 1);
                            enchantments.put(Enchantment.getByName(current.split(",")[0]),
                                    Integer.valueOf(current.split(",")[1]));
                            currentpacket = currentpacket.substring(c.intValue());
                        }
                        setEnchantments(enchantments);
                    case "ItemFlags":
                        itemflags = new ArrayList<>();
                        currentpacket = currentpacket.substring(1, currentpacket.length() - 1);
                        while (currentpacket != "") {
                            if (currentpacket.charAt(0) == ',')
                                currentpacket = currentpacket.substring(1);
                            Integer c = Integer.valueOf(0);
                            while (currentpacket.charAt(c.intValue()) != ']')
                                Integer integer1 = c, integer2 = c = Integer.valueOf(c.intValue() + 1);
                            String current = currentpacket.substring(1, c.intValue() - 1);
                            itemflags.add(ItemFlag.valueOf(current));
                            currentpacket = currentpacket.substring(c.intValue());
                        }
                        setItemFlags(itemflags);
                    case "Owner":
                        setOwner(currentpacket.substring(1, currentpacket.length() - 1));
                    case "BaseColor":
                        setBasecolor(DyeColor.valueOf(currentpacket.substring(1, currentpacket.length() - 1)));
                    case "Patterns":
                        patterns = new ArrayList<>();
                        currentpacket = currentpacket.substring(1, currentpacket.length() - 1);
                        while (currentpacket != "") {
                            if (currentpacket.charAt(0) == ',')
                                currentpacket = currentpacket.substring(1);
                            Integer c = Integer.valueOf(0);
                            while (currentpacket.charAt(c.intValue()) != ']')
                                Integer integer1 = c, integer2 = c = Integer.valueOf(c.intValue() + 1);
                            String current = currentpacket.substring(1, c.intValue() - 1);
                            patterns.add(new Pattern(DyeColor.valueOf(current.split(",")[0]),
                                    PatternType.valueOf(current.split(",")[1])));
                            currentpacket = currentpacket.substring(c.intValue());
                        }
                        setPatterns(patterns);
                    case "StoredEnchantments":
                        storedenchantments = new HashMap<>();
                        currentpacket = currentpacket.substring(1, currentpacket.length() - 1);
                        while (currentpacket != "") {
                            if (currentpacket.charAt(0) == ',')
                                currentpacket = currentpacket.substring(1);
                            Integer c = Integer.valueOf(0);
                            while (currentpacket.charAt(c.intValue()) != ']')
                                Integer integer1 = c, integer2 = c = Integer.valueOf(c.intValue() + 1);
                            String current = currentpacket.substring(1, c.intValue() - 1);
                            storedenchantments.put(Enchantment.getByName(current.split(",")[0]),
                                    Integer.valueOf(current.split(",")[1]));
                            currentpacket = currentpacket.substring(c.intValue());
                        }
                        setStoredEnchantments(storedenchantments);
                    case "Possesseur":
                        setPossesseur(
                                Bukkit.getPlayer(UUID.fromString(currentpacket.substring(1, currentpacket.length() - 1))));
                    case "Creator_name":
                        setCreator_name(currentpacket.substring(1, currentpacket.length() - 1));
                    case "Tag":
                        taglist = new ArrayList<>();
                        currentpacket = currentpacket.substring(1, currentpacket.length() - 1);
                        while (currentpacket != "") {
                            Boolean intag = Boolean.valueOf(false);
                            Integer c = Integer.valueOf(0);
                            if (currentpacket.charAt(0) == ',')
                                currentpacket = currentpacket.substring(1);
                            while (currentpacket.charAt(c.intValue()) != ']' && !intag.booleanValue()) {
                                if (currentpacket.substring(c.intValue(), c.intValue()).equals("'"))
                                    intag = Boolean.valueOf(!intag.booleanValue());
                                Integer integer1 = c, integer2 = c = Integer.valueOf(c.intValue() + 1);
                            }
                            taglist.add(currentpacket.substring(1, c.intValue() - 1));
                            currentpacket = currentpacket.substring(c.intValue());
                        }
                        setTag(taglist);
                }
            }
        }
        return this;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public ItemCreator setMaterial(Material material) {
        if (this.item == null) {
            this.item = new ItemStack(material);
        } else {
            this.item.setType(material);
        }
        return this;
    }

    public ItemCreator setUnbreakable(Boolean unbreakable) {
        ItemMeta meta = this.item.getItemMeta();
        meta.spigot().setUnbreakable(unbreakable.booleanValue());
        this.item.setItemMeta(meta);
        return this;
    }

    public Integer getAmount() {
        return Integer.valueOf(this.item.getAmount());
    }

    public ItemCreator setAmount(Integer amount) {
        this.item.setAmount(amount.intValue());
        return this;
    }

    public ItemCreator setSkull(String paramString1) {
        SkullMeta localSkullMeta = (SkullMeta)this.item.getItemMeta();
        GameProfile localGameProfile = new GameProfile(UUID.randomUUID(), "domei_heads");
        PropertyMap localPropertyMap = localGameProfile.getProperties();
        localPropertyMap.put("textures", new Property("textures", paramString1));
        try {
            Field localField = localSkullMeta.getClass().getDeclaredField("profile");
            localField.setAccessible(true);
            localField.set(localSkullMeta, localGameProfile);
        } catch (NoSuchFieldException|IllegalAccessException localNoSuchFieldException) {
            localNoSuchFieldException.printStackTrace();
        }
        this.item.setItemMeta((ItemMeta)localSkullMeta);
        return this;
    }

    public Short getDurability() {
        return Short.valueOf(this.item.getDurability());
    }

    public ItemCreator setDurability(Short durability) {
        this.item.setDurability(durability.shortValue());
        return this;
    }

    public ItemCreator setDurability(Integer durability) {
        Short shortdurability = Short.valueOf((short)durability.intValue());
        this.item.setDurability(shortdurability.shortValue());
        return this;
    }

    public Integer getDurabilityInteger() {
        return Integer.valueOf(this.item.getDurability());
    }

    public ItemMeta getMeta() {
        return this.item.getItemMeta();
    }

    public ItemCreator setMeta(ItemMeta meta) {
        this.item.setItemMeta(meta);
        return this;
    }

    public String getName() {
        return this.item.getItemMeta().getDisplayName();
    }

    public ItemCreator setName(String name) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        this.item.setItemMeta(meta);
        return this;
    }

    public ArrayList<String> getLores() {
        return (ArrayList<String>)this.item.getItemMeta().getLore();
    }

    public ItemCreator setLores(List<String> list) {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(list);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCreator clearLores() {
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(new ArrayList());
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCreator setSkullURL(String url) {
        if (url.isEmpty())
            return this;
        SkullMeta headMeta = (SkullMeta)this.item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "domei_heads");
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[] { url }).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException|IllegalArgumentException|IllegalAccessException e1) {
            e1.printStackTrace();
        }
        this.item.setItemMeta((ItemMeta)headMeta);
        return this;
    }

    public ItemCreator insertLores(String lore, Integer position) {
        ItemMeta meta = this.item.getItemMeta();
        ArrayList<String> lores = (ArrayList<String>)meta.getLore();
        if (lores == null)
            lores = new ArrayList<>();
        lores.add(position.intValue(), lore);
        meta.setLore(lores);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCreator addLore(String lore) {
        ItemMeta meta = this.item.getItemMeta();
        ArrayList<String> lores = (ArrayList<String>)meta.getLore();
        if (lores == null)
            lores = new ArrayList<>();
        if (lore != null) {
            lores.add(lore);
        } else {
            lores.add(" ");
        }
        meta.setLore(lores);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCreator removeLore(String lore) {
        ItemMeta meta = this.item.getItemMeta();
        ArrayList<String> lores = (ArrayList<String>)meta.getLore();
        if (lores != null && lores
                .contains(lore)) {
            lores.remove(lore);
            meta.setLore(lores);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public String[] getTableauLores() {
        String[] tableaulores = new String[0];
        if (this.item.getItemMeta().getLore() != null) {
            Integer i = Integer.valueOf(0);
            for (String lore : this.item.getItemMeta().getLore()) {
                tableaulores[i.intValue()] = lore;
                Integer integer1 = i, integer2 = i = Integer.valueOf(i.intValue() + 1);
            }
        }
        return tableaulores;
    }

    public ItemCreator setTableauLores(String[] lores) {
        ArrayList<String> tableaulores = new ArrayList<>();
        for (String lore : lores)
            tableaulores.add(lore);
        ItemMeta meta = this.item.getItemMeta();
        meta.setLore(tableaulores);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCreator replaceallLores(String replacelore, String newlore) {
        ItemMeta meta = this.item.getItemMeta();
        ArrayList<String> lores = (ArrayList<String>)meta.getLore();
        if (lores != null && lores
                .contains(replacelore)) {
            meta.setLore(lores);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemCreator replaceoneLore(Integer ligne, String newlore) {
        ItemMeta meta = this.item.getItemMeta();
        ArrayList<String> lores = (ArrayList<String>)meta.getLore();
        if (lores != null && lores
                .get(ligne.intValue()) != null) {
            lores.remove(ligne);
            lores.add(ligne.intValue(), newlore);
            meta.setLore(lores);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemCreator replacefirstLores(String replacelore, String newlore, Integer nombre) {
        ItemMeta meta = this.item.getItemMeta();
        ArrayList<String> lores = (ArrayList<String>)meta.getLore();
        if (lores != null && lores
                .contains(replacelore)) {
            Integer replaced = Integer.valueOf(0);
            meta.setLore(lores);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemCreator replacelastLores(String replacelore, String newlore, Integer nombre) {
        ItemMeta meta = this.item.getItemMeta();
        ArrayList<String> lores = (ArrayList<String>)meta.getLore();
        if (lores != null && lores
                .contains(replacelore)) {
            Integer replaced = Integer.valueOf(0);
            meta.setLore(lores);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemCreator setGlow(Boolean glow) {
        if (glow.booleanValue()) {
            ItemStack minecraftitemstack = CraftItemStack.asNMSCopy(this.item);
            NBTTagCompound tag = null;
            if (!minecraftitemstack.hasTag()) {
                tag = new NBTTagCompound();
                minecraftitemstack.setTag(new NBTTagCompound());
            } else {
                tag = minecraftitemstack.getTag();
            }
            NBTTagList ench = new NBTTagList();
            tag.set("ench", (NBTBase)ench);
            minecraftitemstack.setTag(tag);
            this.item = (ItemStack)CraftItemStack.asCraftMirror(minecraftitemstack);
        } else {
            ItemStack minecraftitemstack = CraftItemStack.asNMSCopy(this.item);
            NBTTagCompound tag = null;
            if (!minecraftitemstack.hasTag()) {
                tag = minecraftitemstack.getTag();
                if (tag.hasKey("ench")) {
                    tag.remove("ench");
                    minecraftitemstack.setTag(tag);
                    this.item = (ItemStack)CraftItemStack.asCraftMirror(minecraftitemstack);
                }
            }
        }
        return this;
    }

    public HashMap<Enchantment, Integer> getEnchantments() {
        return new HashMap<>(this.item.getItemMeta().getEnchants());
    }

    public ItemCreator setEnchantments(Map<Enchantment, Integer> map) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta.getEnchants() != null) {
            ArrayList<Enchantment> cloneenchantments = new ArrayList<>(meta.getEnchants().keySet());
            for (Enchantment enchantment : cloneenchantments)
                meta.removeEnchant(enchantment);
        }
        for (Map.Entry<Enchantment, Integer> e : map.entrySet())
            meta.addEnchant(e.getKey(), ((Integer)e.getValue()).intValue(), true);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCreator clearEnchantments() {
        ItemMeta meta = this.item.getItemMeta();
        if (meta.getEnchants() != null) {
            ArrayList<Enchantment> cloneenchantments = new ArrayList<>(meta.getEnchants().keySet());
            for (Enchantment enchantment : cloneenchantments)
                meta.removeEnchant(enchantment);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemCreator addEnchantment(Enchantment enchantment, Integer lvl) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addEnchant(enchantment, lvl.intValue(), true);
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCreator removeEnchantment(Enchantment enchantment) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta.getEnchants() != null && meta
                .getEnchants().containsKey(enchantment)) {
            meta.removeEnchant(enchantment);
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public Enchantment[] getTableauEnchantments() {
        Enchantment[] enchantments = new Enchantment[0];
        if (this.item.getItemMeta().getEnchants() != null) {
            Integer i = Integer.valueOf(0);
            for (Enchantment enchantment : this.item.getItemMeta().getEnchants().keySet()) {
                enchantments[i.intValue()] = enchantment;
                Integer integer1 = i, integer2 = i = Integer.valueOf(i.intValue() + 1);
            }
        }
        return enchantments;
    }

    public Integer[] getTableauEnchantmentslvl() {
        Integer[] enchantmentslvl = new Integer[0];
        if (this.item.getItemMeta().getEnchants() != null) {
            Integer i = Integer.valueOf(0);
            for (Integer enchantmentlvl : this.item.getItemMeta().getEnchants().values()) {
                enchantmentslvl[i.intValue()] = enchantmentlvl;
                Integer integer1 = i, integer2 = i = Integer.valueOf(i.intValue() + 1);
            }
        }
        return enchantmentslvl;
    }

    public ItemCreator setTableauEnchantments(Enchantment[] enchantments, Integer[] enchantmentslvl) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta.getEnchants() != null) {
            ArrayList<Enchantment> cloneenchantments = new ArrayList<>(meta.getEnchants().keySet());
            for (Enchantment enchantment : cloneenchantments)
                meta.removeEnchant(enchantment);
        }
        this.item.setItemMeta(meta);
        return this;
    }

    public ArrayList<ItemFlag> getItemFlags() {
        ArrayList<ItemFlag> itemflags = new ArrayList<>();
        if (this.item.getItemMeta().getItemFlags() != null)
            for (ItemFlag itemflag : this.item.getItemMeta().getItemFlags())
                itemflags.add(itemflag);
        return itemflags;
    }

    public ItemCreator setItemFlags(ArrayList<ItemFlag> itemflags) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta.getItemFlags() != null) {
            ArrayList<ItemFlag> cloneitemflags = new ArrayList<>();
            for (ItemFlag itemflag : meta.getItemFlags())
                cloneitemflags.add(itemflag);
            for (ItemFlag itemflag : cloneitemflags) {
                meta.removeItemFlags(new ItemFlag[] { itemflag });
            }
        }
        for (ItemFlag itemflag : itemflags) {
            meta.addItemFlags(new ItemFlag[] { itemflag });
        }
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCreator addFlag(ItemFlag... flag) {
        ItemMeta im = this.item.getItemMeta();
        im.addItemFlags(flag);
        this.item.setItemMeta(im);
        return this;
    }

    public ItemCreator clearItemFlags() {
        ItemMeta meta = this.item.getItemMeta();
        if (meta.getItemFlags() != null) {
            ArrayList<ItemFlag> cloneitemflags = new ArrayList<>();
            for (ItemFlag itemflag : meta.getItemFlags())
                cloneitemflags.add(itemflag);
            for (ItemFlag itemflag : cloneitemflags) {
                meta.removeItemFlags(new ItemFlag[] { itemflag });
            }
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemCreator addItemFlags(ItemFlag itemflag) {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(new ItemFlag[] { itemflag });
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCreator removeItemFlags(ItemFlag itemflag) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta.getItemFlags() != null && meta
                .getItemFlags().contains(itemflag)) {
            meta.removeItemFlags(new ItemFlag[] { itemflag });
            this.item.setItemMeta(meta);
        }
        return this;
    }

    public ItemFlag[] getTableauItemFlags() {
        ItemMeta meta = this.item.getItemMeta();
        ItemFlag[] itemflags = new ItemFlag[0];
        Integer i = Integer.valueOf(0);
        if (meta.getItemFlags() != null)
            for (ItemFlag itemflag : meta.getItemFlags()) {
                itemflags[i.intValue()] = itemflag;
                Integer integer1 = i, integer2 = i = Integer.valueOf(i.intValue() + 1);
            }
        return itemflags;
    }

    public ItemCreator setTableauItemFlags(ItemFlag[] itemflags) {
        ItemMeta meta = this.item.getItemMeta();
        if (meta.getItemFlags() != null) {
            ArrayList<ItemFlag> cloneitemflags = new ArrayList<>();
            for (ItemFlag itemflag : meta.getItemFlags())
                cloneitemflags.add(itemflag);
            for (ItemFlag itemflag : cloneitemflags) {
                meta.removeItemFlags(new ItemFlag[] { itemflag });
            }
        }
        for (ItemFlag itemflag : itemflags) {
            meta.addItemFlags(new ItemFlag[] { itemflag });
        }
        this.item.setItemMeta(meta);
        return this;
    }

    public SkullMeta getSkullMeta() {
        if (this.item.getType().equals(Material.SKULL_ITEM))
            return (SkullMeta)this.item.getItemMeta();
        return null;
    }

    public ItemCreator setSkullMeta(SkullMeta skullmeta) {
        if (this.item.getType().equals(Material.SKULL_ITEM))
            this.item.setItemMeta((ItemMeta)skullmeta);
        return this;
    }

    public String getOwner() {
        if (this.item.getType().equals(Material.SKULL_ITEM))
            return ((SkullMeta)this.item.getItemMeta()).getOwner();
        return null;
    }

    public ItemCreator setOwner(String owner) {
        if (this.item.getType().equals(Material.SKULL_ITEM)) {
            SkullMeta meta = (SkullMeta)this.item.getItemMeta();
            meta.setOwner(owner);
            this.item.setItemMeta((ItemMeta)meta);
        }
        return this;
    }

    public BannerMeta getBannerMeta() {
        if (this.item.getType().equals(Material.BANNER))
            return (BannerMeta)this.item.getItemMeta();
        return null;
    }

    public ItemCreator setBannerMeta(BannerMeta bannermeta) {
        if (this.item.getType().equals(Material.BANNER))
            this.item.setItemMeta((ItemMeta)bannermeta);
        return this;
    }

    public DyeColor getBasecolor() {
        if (this.item.getType().equals(Material.BANNER))
            return ((BannerMeta)this.item.getItemMeta()).getBaseColor();
        return null;
    }

    public ItemCreator setBasecolor(DyeColor basecolor) {
        if (this.item.getType().equals(Material.BANNER)) {
            BannerMeta meta = (BannerMeta)this.item.getItemMeta();
            meta.setBaseColor(basecolor);
            this.item.setItemMeta((ItemMeta)meta);
        }
        return this;
    }

    public ArrayList<Pattern> getPatterns() {
        if (this.item.getType().equals(Material.BANNER))
            return (ArrayList<Pattern>)((BannerMeta)this.item.getItemMeta()).getPatterns();
        return null;
    }

    public ItemCreator setPatterns(ArrayList<Pattern> petterns) {
        if (this.item.getType().equals(Material.BANNER)) {
            BannerMeta meta = (BannerMeta)this.item.getItemMeta();
            meta.setPatterns(petterns);
            this.item.setItemMeta((ItemMeta)meta);
        }
        return this;
    }

    public ItemCreator clearPatterns() {
        if (this.item.getType().equals(Material.BANNER)) {
            BannerMeta meta = (BannerMeta)this.item.getItemMeta();
            meta.setPatterns(new ArrayList());
            this.item.setItemMeta((ItemMeta)meta);
        }
        return this;
    }

    public ItemCreator addPattern(Pattern pattern) {
        if (this.item.getType().equals(Material.BANNER)) {
            BannerMeta meta = (BannerMeta)this.item.getItemMeta();
            meta.addPattern(pattern);
            this.item.setItemMeta((ItemMeta)meta);
        }
        return this;
    }

    public ItemCreator removePattern(Pattern pattern) {
        if (this.item.getType().equals(Material.BANNER)) {
            BannerMeta meta = (BannerMeta)this.item.getItemMeta();
            ArrayList<Pattern> patterns = (ArrayList<Pattern>)meta.getPatterns();
            if (patterns != null && patterns
                    .contains(pattern)) {
                patterns.remove(pattern);
                meta.setPatterns(patterns);
                this.item.setItemMeta((ItemMeta)meta);
            }
        }
        return this;
    }

    public Pattern[] getTableauPatterns() {
        if (this.item.getType().equals(Material.BANNER)) {
            BannerMeta meta = (BannerMeta)this.item.getItemMeta();
            Pattern[] tableaupatterns = new Pattern[0];
            if (meta.getPatterns() != null) {
                Integer i = Integer.valueOf(0);
                for (Pattern pattern : meta.getPatterns()) {
                    tableaupatterns[i.intValue()] = pattern;
                    Integer integer1 = i, integer2 = i = Integer.valueOf(i.intValue() + 1);
                }
            }
            return tableaupatterns;
        }
        return null;
    }

    public ItemCreator setTableauPatterns(Pattern[] patterns) {
        if (this.item.getType().equals(Material.BANNER)) {
            BannerMeta meta = (BannerMeta)this.item.getItemMeta();
            if (meta.getPatterns() != null)
                meta.setPatterns(new ArrayList());
            for (Pattern pattern : patterns)
                meta.addPattern(pattern);
            this.item.setItemMeta((ItemMeta)meta);
        }
        return this;
    }

    public EnchantmentStorageMeta getEnchantmentStorageMeta() {
        if (this.item.getType().equals(Material.ENCHANTED_BOOK))
            return (EnchantmentStorageMeta)this.item.getItemMeta();
        return null;
    }

    public ItemCreator setEnchantmentStorageMeta(EnchantmentStorageMeta enchantmentstoragemeta) {
        if (this.item.getType().equals(Material.ENCHANTED_BOOK))
            this.item.setItemMeta((ItemMeta)enchantmentstoragemeta);
        return this;
    }

    public HashMap<Enchantment, Integer> getStoredEnchantments() {
        if (this.item.getType().equals(Material.ENCHANTED_BOOK))
            return (HashMap<Enchantment, Integer>)((EnchantmentStorageMeta)this.item.getItemMeta()).getEnchants();
        return null;
    }

    public ItemCreator setStoredEnchantments(HashMap<Enchantment, Integer> storedenchantments) {
        if (this.item.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)this.item.getItemMeta();
            if (meta.getStoredEnchants() != null) {
                ArrayList<Enchantment> clonestoredenchantments = new ArrayList<>(meta.getStoredEnchants().keySet());
                for (Enchantment storedenchantment : clonestoredenchantments)
                    meta.removeStoredEnchant(storedenchantment);
            }
            for (Map.Entry<Enchantment, Integer> e : storedenchantments.entrySet())
                meta.addEnchant(e.getKey(), ((Integer)e.getValue()).intValue(), true);
            this.item.setItemMeta((ItemMeta)meta);
        }
        return this;
    }

    public ItemCreator clearStoredEnchantments() {
        if (this.item.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)this.item.getItemMeta();
            if (meta.getStoredEnchants() != null) {
                ArrayList<Enchantment> clonestoredenchantments = new ArrayList<>(meta.getStoredEnchants().keySet());
                for (Enchantment storedenchantment : clonestoredenchantments)
                    meta.removeStoredEnchant(storedenchantment);
                this.item.setItemMeta((ItemMeta)meta);
            }
        }
        return this;
    }

    public ItemCreator addStoredEnchantment(Enchantment storedenchantment, Integer lvl) {
        if (this.item.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)this.item.getItemMeta();
            meta.addStoredEnchant(storedenchantment, lvl.intValue(), true);
            this.item.setItemMeta((ItemMeta)meta);
        }
        return this;
    }

    public ItemCreator removeStoredEnchantment(Enchantment enchantment) {
        if (this.item.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)this.item.getItemMeta();
            if (meta.getStoredEnchants() != null && meta
                    .getStoredEnchants().containsKey(enchantment)) {
                meta.removeEnchant(enchantment);
                this.item.setItemMeta((ItemMeta)meta);
            }
        }
        return this;
    }

    public Enchantment[] getTableauStoredEnchantments() {
        if (this.item.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)this.item.getItemMeta();
            Enchantment[] storedenchantments = new Enchantment[0];
            if (meta.getStoredEnchants() != null) {
                Integer i = Integer.valueOf(0);
                for (Enchantment storedenchantment : meta.getStoredEnchants().keySet()) {
                    storedenchantments[i.intValue()] = storedenchantment;
                    Integer integer1 = i, integer2 = i = Integer.valueOf(i.intValue() + 1);
                }
            }
            return storedenchantments;
        }
        return null;
    }

    public Integer[] getTableauStoredEnchantmentslvl() {
        if (this.item.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)this.item.getItemMeta();
            Integer[] storedenchantmentslvl = new Integer[0];
            if (meta.getStoredEnchants() != null) {
                Integer i = Integer.valueOf(0);
                for (Integer storedenchantmentlvl : meta.getStoredEnchants().values()) {
                    storedenchantmentslvl[i.intValue()] = storedenchantmentlvl;
                    Integer integer1 = i, integer2 = i = Integer.valueOf(i.intValue() + 1);
                }
            }
            return storedenchantmentslvl;
        }
        return null;
    }

    public ItemCreator setTableauStoredEnchantments(Enchantment[] storedenchantments, Integer[] storedenchantmentslvl) {
        if (this.item.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)this.item.getItemMeta();
            if (meta.getStoredEnchants() != null) {
                ArrayList<Enchantment> clonestoredenchantments = new ArrayList<>(meta.getStoredEnchants().keySet());
                for (Enchantment storedenchantment : clonestoredenchantments)
                    meta.removeStoredEnchant(storedenchantment);
            }
            this.item.setItemMeta((ItemMeta)meta);
        }
        return this;
    }

    public ItemCreator addallItemsflags() {
        ItemMeta meta = this.item.getItemMeta();
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ENCHANTS });
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_PLACED_ON });
        meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_POTION_EFFECTS });
        this.item.setItemMeta(meta);
        return this;
    }

    public ItemCreator addBannerPreset(Integer ID, DyeColor patterncolor) {
        switch (ID.intValue()) {
            case 1:
                addBannerPreset(BannerPreset.barre, patterncolor);
                break;
            case 2:
                addBannerPreset(BannerPreset.precedent, patterncolor);
                break;
            case 3:
                addBannerPreset(BannerPreset.suivant, patterncolor);
                break;
            case 4:
                addBannerPreset(BannerPreset.coeur, patterncolor);
                break;
            case 5:
                addBannerPreset(BannerPreset.cercleEtoile, patterncolor);
                break;
            case 6:
                addBannerPreset(BannerPreset.croix, patterncolor);
                break;
            case 7:
                addBannerPreset(BannerPreset.yinYang, patterncolor);
                break;
            case 8:
                addBannerPreset(BannerPreset.losange, patterncolor);
                break;
            case 9:
                addBannerPreset(BannerPreset.moin, patterncolor);
                break;
            case 10:
                addBannerPreset(BannerPreset.plus, patterncolor);
                break;
        }
        return this;
    }

    public ItemCreator addBannerPreset(BannerPreset type, DyeColor patterncolor) {
        if (type == null)
            return this;
        if (this.item.getType().equals(Material.BANNER)) {
            BannerMeta meta = (BannerMeta)this.item.getItemMeta();
            DyeColor dyeColor = meta.getBaseColor();
        }
        return this;
    }

    private void addasyncronePattern(Pattern pattern, Boolean calcul) {
        if (calcul.booleanValue()) {
            this.patterns.add(pattern);
            BannerMeta meta = (BannerMeta)this.item.getItemMeta();
            for (Pattern currentpattern : this.patterns)
                meta.addPattern(currentpattern);
            this.patterns.clear();
            this.item.setItemMeta((ItemMeta)meta);
        } else {
            if (this.patterns == null)
                this.patterns = new ArrayList<>();
            this.patterns.add(pattern);
        }
    }

    public Player getPossesseur() {
        return this.possesseur;
    }

    public ItemCreator setPossesseur(Player possesseur) {
        this.possesseur = possesseur;
        return this;
    }

    public String getCreator_name() {
        return this.creator_name;
    }

    public ItemCreator setCreator_name(String creator_name) {
        this.creator_name = creator_name;
        return this;
    }

    public ArrayList<String> getTag() {
        return this.tag;
    }

    public ItemCreator setTag(ArrayList<String> tag) {
        this.tag = tag;
        return this;
    }

    public ItemCreator clearTag() {
        if (this.tag != null)
            this.tag.clear();
        return this;
    }

    public ItemCreator addTag(String tag) {
        if (this.tag == null)
            this.tag = new ArrayList<>();
        this.tag.add(tag);
        return this;
    }

    public ItemCreator removeTag(String tag) {
        if (this.tag != null && this.tag
                .contains(tag))
            this.tag.remove(tag);
        return this;
    }

    public String[] getTableauTag() {
        String[] taglist = new String[0];
        Integer i = Integer.valueOf(0);
        for (String currenttag : this.tag) {
            taglist[i.intValue()] = currenttag;
            Integer integer1 = i, integer2 = i = Integer.valueOf(i.intValue() + 1);
        }
        return taglist;
    }

    public ItemCreator setTableaTag(String[] tag) {
        if (this.tag == null) {
            this.tag = new ArrayList<>();
        } else {
            this.tag.clear();
        }
        for (String currenttag : tag)
            this.tag.add(currenttag);
        return this;
    }

    public Boolean comparate(ItemCreator item, ComparatorType type) {
        switch (type) {
            case All:
                return Boolean.valueOf((comparate(item, ComparatorType.Material).booleanValue() && comparate(item, ComparatorType.Amount).booleanValue() &&
                        comparate(item, ComparatorType.Durability).booleanValue() && comparate(item, ComparatorType.Name).booleanValue() &&
                        comparate(item, ComparatorType.Lores).booleanValue() && comparate(item, ComparatorType.Enchantements).booleanValue() &&
                        comparate(item, ComparatorType.ItemsFlags).booleanValue() && comparate(item, ComparatorType.Owner).booleanValue() &&
                        comparate(item, ComparatorType.BaseColor).booleanValue() && comparate(item, ComparatorType.Patterns).booleanValue() &&
                        comparate(item, ComparatorType.StoredEnchantements).booleanValue() &&
                        comparate(item, ComparatorType.Creator_Name).booleanValue() && comparate(item, ComparatorType.Possesseur).booleanValue() &&
                        comparate(item, ComparatorType.TAG).booleanValue()));
            case Similar:
                return Boolean.valueOf((comparate(item, ComparatorType.Material).booleanValue() && comparate(item, ComparatorType.Durability).booleanValue() &&
                        comparate(item, ComparatorType.Name).booleanValue() && comparate(item, ComparatorType.Lores).booleanValue() &&
                        comparate(item, ComparatorType.Enchantements).booleanValue() && comparate(item, ComparatorType.ItemsFlags).booleanValue() &&
                        comparate(item, ComparatorType.Owner).booleanValue() && comparate(item, ComparatorType.BaseColor).booleanValue() &&
                        comparate(item, ComparatorType.Patterns).booleanValue() && comparate(item, ComparatorType.StoredEnchantements).booleanValue()));
            case ItemStack:
                return Boolean.valueOf((comparate(item, ComparatorType.Material).booleanValue() && comparate(item, ComparatorType.Amount).booleanValue() &&
                        comparate(item, ComparatorType.Durability).booleanValue() && comparate(item, ComparatorType.Name).booleanValue() &&
                        comparate(item, ComparatorType.Lores).booleanValue() && comparate(item, ComparatorType.Enchantements).booleanValue() &&
                        comparate(item, ComparatorType.ItemsFlags).booleanValue() && comparate(item, ComparatorType.Owner).booleanValue() &&
                        comparate(item, ComparatorType.BaseColor).booleanValue() && comparate(item, ComparatorType.Patterns).booleanValue() &&
                        comparate(item, ComparatorType.StoredEnchantements).booleanValue()));
            case Amount:
                return Boolean.valueOf((getAmount() == item.getAmount()));
            case Durability:
                return Boolean.valueOf((getDurability() == item.getDurability()));
            case Name:
                return Boolean.valueOf((getName() == item.getName()));
        }
        return Boolean.valueOf(false);
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemCreator setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public enum BannerPreset {
        barre, precedent, suivant, coeur, cercleEtoile, croix, yinYang, losange, moin, plus;
    }

    public enum ComparatorType {
        All, ItemStack, Similar, Material, Amount, Durability, Name, Lores, Enchantements, ItemsFlags, Owner, BaseColor, Patterns, StoredEnchantements, Possesseur, Creator_Name, TAG;
    }

    private class comparaison<type1, type2> {
        public Boolean islistequal(List<type1> list1, List<type1> list2) {
            if (list1 == null && list2 == null)
                return Boolean.valueOf(true);
            if (list1 == null || list2 == null)
                return Boolean.valueOf(false);
            if (list1.size() == list2.size())
                return Boolean.valueOf(true);
            return Boolean.valueOf(false);
        }

        public Boolean ismapequal(Map<type1, type2> map1, Map<type1, type2> map2) {
            if (map1 == null && map2 == null)
                return Boolean.valueOf(true);
            if (map1 == null || map2 == null)
                return Boolean.valueOf(false);
            if (map1.size() == map2.size()) {
                for (Map.Entry<type1, type2> e : map1.entrySet()) {
                    if (map2.get(e.getKey()) == null)
                        return Boolean.valueOf(false);
                    if (map2.get(e.getKey()) != e.getValue())
                        return Boolean.valueOf(false);
                }
                return Boolean.valueOf(true);
            }
            return Boolean.valueOf(false);
        }
    }
}
