package fr.clickdroit.api.utils.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {
    private ItemStack is;

    public ItemBuilder(Material material) {
        this.is = new ItemStack(material);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.is = itemStack.clone();
    }

    public ItemBuilder setDisplayName(String name) {
        ItemMeta meta = this.is.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            this.is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        this.is.setDurability(durability);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.is.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addLore(String lore) {
        ItemMeta meta = this.is.getItemMeta();
        if (meta != null) {
            List<String> lores = meta.getLore();
            if (lores == null) {
                lores = new ArrayList<>();
            }
            if (lore != null) {
                lores.add(lore);
            } else {
                lores.add(" ");
            }
            meta.setLore(lores);
            this.is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setLore(List<String> lore2, String... lore1) {
        ItemMeta meta = this.is.getItemMeta();
        if (meta != null) {
            List<String> finalList = new ArrayList<>(Arrays.asList(lore1));
            if (lore2 != null) {
                finalList.addAll(lore2);
            }
            meta.setLore(finalList);
            this.is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = this.is.getItemMeta();
        if (meta != null) {
            List<String> newLore = new ArrayList<>();
            if (lore != null) {
                newLore.addAll(lore);
            }
            meta.setLore(newLore);
            this.is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta meta = this.is.getItemMeta();
        if (meta != null) {
            meta.setLore(Arrays.asList(lore));
            this.is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
        try {
            if (this.is.getItemMeta() instanceof LeatherArmorMeta) {
                LeatherArmorMeta leatherMeta = (LeatherArmorMeta) this.is.getItemMeta();
                leatherMeta.setColor(color);
                this.is.setItemMeta(leatherMeta);
            }
        } catch (ClassCastException e) {
            // Ignorer si ce n'est pas une armure en cuir
        }
        return this;
    }

    public ItemBuilder addFlag(ItemFlag... flags) {
        ItemMeta meta = this.is.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(flags);
            this.is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder removeFlag(ItemFlag... flags) {
        ItemMeta meta = this.is.getItemMeta();
        if (meta != null) {
            meta.removeItemFlags(flags);
            this.is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder addAllFlags() {
        ItemMeta meta = this.is.getItemMeta();
        if (meta != null) {
            meta.addItemFlags(ItemFlag.values());
            this.is.setItemMeta(meta);
        }
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = this.is.getItemMeta();
        if (meta != null) {
            meta.spigot().setUnbreakable(unbreakable);
            this.is.setItemMeta(meta);
        }
        return this;
    }

    public ItemStack toItemStack() {
        return this.is;
    }

    public ItemStack build() {
        return this.is;
    }
}
