package fr.clickdroit.api.config.common;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class EnchantMaxGUI implements CustomInventory {
    public String getName() {
        return "Enchant' Max.";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        int slot = 0;
        for (Enchants enchants : Enchants.values()) {
            int level = enchants.getConfigValue();
            ItemCreator item = (new ItemCreator(Material.ENCHANTED_BOOK)).setName(enchants.getName());
            item.addLore("");
            item.addLore("  :" + level);
            item.addLore("");
            if (level < enchants.getMax())
                item.addLore(" gauche : un niveau");
            if (level > enchants.getMin())
                item.addLore(" droit : un niveau");
            item.addLore("");
            item.setAmount(Integer.valueOf(level));
            slots[slot] = item.getItem();
            slot++;
        }
        slots[31] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        GameManager gameManager = API.getAPI().getGameManager();
        switch (clickedItem.getType()) {
            case ENCHANTED_BOOK:
                if (clickType.equals(ClickType.LEFT)) {
                    for (Enchants enchants : Enchants.values()) {
                        if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(enchants.getName()) && enchants
                                .getConfigValue() < enchants.getMax()) {
                            enchants.addConfigValue();
                            gameManager.getApi().openInventory(player, getClass());
                        }
                    }
                    break;
                }
                if (clickType.equals(ClickType.RIGHT))
                    for (Enchants enchants : Enchants.values()) {
                        if (clickedItem.getItemMeta().getDisplayName().contains(enchants.getName()) && enchants
                                .getConfigValue() > enchants.getMin()) {
                            enchants.removeConfigValue();
                            gameManager.getApi().openInventory(player, getClass());
                        }
                    }
                break;
            case ARROW:
                gameManager.getApi().openInventory(player, ConfigOptionsGUI.class);
                break;
        }
    }

    public int getRows() {
        return 4;
    }

    public enum Enchants {
        PROTECTION_ENVIRONMENTAL(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection", 0, 50, 4),
        FIRE_PROTECTION(Enchantment.PROTECTION_FIRE, "Fire Protection", 0, 50, 4),
        FEATHER_FALLING(Enchantment.PROTECTION_FALL, "Feather Falling", 0, 50, 4),
        BLAST_PROTECTION(Enchantment.PROTECTION_EXPLOSIONS, "Blast Protection", 0, 50, 4),
        PROJECTILE_PROTECTION(Enchantment.PROTECTION_PROJECTILE, "Projectile Protection", 0, 50, 4),
        RESPIRATION(Enchantment.OXYGEN, "Respiration", 0, 50, 3),
        AQUA_AFFINITY(Enchantment.WATER_WORKER, "Aqua Affinity", 0, 50, 1),
        THORNS(Enchantment.THORNS, "Thorns", 0, 50, 3),
        DEPTH_STRIDERS(Enchantment.DEPTH_STRIDER, "Depth Strider", 0, 50, 3),
        SHARPNESS(Enchantment.DAMAGE_ALL, "Sharpness", 0, 50, 5),
        SMITE(Enchantment.DAMAGE_UNDEAD, "Smite", 0, 50, 5),
        BANE_OF_ARTHROPODS(Enchantment.DAMAGE_ARTHROPODS, "Bane of Arthropods", 0, 50, 5),
        KNOCKBACK(Enchantment.KNOCKBACK, "Knockback", 0, 50, 2),
        FIRE_ASPECT(Enchantment.FIRE_ASPECT, "Fire Aspect", 0, 50, 2),
        LOOTING(Enchantment.LOOT_BONUS_MOBS, "Looting", 0, 50, 3),
        POWER(Enchantment.ARROW_DAMAGE, "Power", 0, 50, 5),
        PUNCH(Enchantment.ARROW_KNOCKBACK, "Punch", 0, 50, 2),
        FLAME(Enchantment.ARROW_FIRE, "Flame", 0, 1, 1),
        INFINITY(Enchantment.ARROW_INFINITE, "Infinity", 0, 1, 1),
        EFFICIENCY(Enchantment.DIG_SPEED, "Efficiency", 0, 50, 5),
        SILK_TOUCH(Enchantment.SILK_TOUCH, "Silk Touch", 0, 1, 1),
        UNBREAKING(Enchantment.DURABILITY, "Unbreaking", 0, 50, 3),
        FORTUNE(Enchantment.LOOT_BONUS_BLOCKS, "Fortune", 0, 50, 3),
        LUCK_OF_THE_SEA(Enchantment.LUCK, "Luck of the Sea", 0, 100, 3),
        LURE(Enchantment.LURE, "Lure", 0, 100, 3);

        private final Enchantment enchantment;
        private final String name;
        private final int min;
        private final int max;
        private int configValue;

        Enchants(Enchantment enchantment, String name, int min, int max, int configValue) {
            this.enchantment = enchantment;
            this.name = name;
            this.min = min;
            this.max = max;
            this.configValue = configValue;
        }

        public static Enchants getEnchant(Enchantment enchant) {
            for (Enchants item : values()) {
                if (enchant.equals(item.getEnchantment()))
                    return item;
            }
            return null;
        }

        public Enchantment getEnchantment() {
            return this.enchantment;
        }

        public String getName() {
            return this.name;
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        public void addConfigValue() {
            if (this.configValue < this.enchantment.getMaxLevel())
                this.configValue++;
        }

        public void removeConfigValue() {
            if (this.configValue > 0)
                this.configValue--;
        }

        public int getConfigValue() {
            return this.configValue;
        }

        public void setConfigValue(int configValue) {
            this.configValue = configValue;
        }
    }
}

