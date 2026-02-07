package fr.clickdroit.api.utils.gui;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builder fluide pour créer des ItemStack.
 */
public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    private ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    private ItemBuilder(Material material, int amount) {
        this.item = new ItemStack(material, amount);
        this.meta = item.getItemMeta();
    }

    private ItemBuilder(Material material, int amount, short data) {
        this.item = new ItemStack(material, amount, data);
        this.meta = item.getItemMeta();
    }

    public static ItemBuilder create(Material material) {
        return new ItemBuilder(material);
    }

    public static ItemBuilder create(Material material, int amount) {
        return new ItemBuilder(material, amount);
    }

    public static ItemBuilder create(Material material, int amount, int data) {
        return new ItemBuilder(material, amount, (short) data);
    }

    public static ItemBuilder skull(String playerName) {
        ItemBuilder builder = new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3);
        if (builder.meta instanceof SkullMeta) {
            ((SkullMeta) builder.meta).setOwner(playerName);
        }
        return builder;
    }

    public ItemBuilder name(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public ItemBuilder lore(String... lines) {
        meta.setLore(Arrays.asList(lines));
        return this;
    }

    public ItemBuilder lore(List<String> lines) {
        meta.setLore(lines);
        return this;
    }

    public ItemBuilder addLore(String line) {
        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        lore.add(line);
        meta.setLore(lore);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        meta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder glow() {
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder durability(int durability) {
        item.setDurability((short) durability);
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
        meta.addItemFlags(flags);
        return this;
    }

    public ItemBuilder hideFlags() {
        meta.addItemFlags(ItemFlag.values());
        return this;
    }

    public ItemBuilder unbreakable() {
        meta.spigot().setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }

    // ===== Méthodes statiques utilitaires =====

    public static ItemStack glass(int color) {
        return create(Material.STAINED_GLASS_PANE, 1, color)
                .name(" ")
                .build();
    }

    public static ItemStack air() {
        return new ItemStack(Material.AIR);
    }

    public static ItemStack barrier(String name) {
        return create(Material.BARRIER)
                .name(name)
                .build();
    }
}

