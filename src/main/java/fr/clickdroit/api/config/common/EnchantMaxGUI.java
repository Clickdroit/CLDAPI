package fr.clickdroit.api.config.common;

import java.util.function.Supplier;

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
        PROTECTION_ENVIRONMENTAL((String)Enchantment.PROTECTION_ENVIRONMENTAL, "", 0, 50, 4),
        FIRE_PROTECTION((String)Enchantment.PROTECTION_FIRE, "Protection", 0, 50, 4),
        FEATHER_FALLING((String)Enchantment.PROTECTION_FALL, "Falling", 0, 50, 4),
        BLAST_PROTECTION((String)Enchantment.PROTECTION_EXPLOSIONS, "Protection", 0, 50, 4),
        PROJECTILE_PROTECTION((String)Enchantment.PROTECTION_PROJECTILE, "Protection", 0, 50, 4),
        RESPIRATION((String)Enchantment.OXYGEN, "", 0, 50, 3),
        AQUA_AFFINITY((String)Enchantment.WATER_WORKER, "Affinity", 0, 50, 1),
        THORNS((String)Enchantment.THORNS, "", 0, 50, 3),
        DEPTH_STRIDERS((String)Enchantment.DEPTH_STRIDER, "Strider", 0, 50, 3),
        SHARPNESS((String)Enchantment.DAMAGE_ALL, "", 0, 50, 5),
        SMITE((String)Enchantment.DAMAGE_UNDEAD, "", 0, 50, 5),
        BANE_OF_ARTHROPODS((String)Enchantment.DAMAGE_ARTHROPODS, "of Arthropods", 0, 50, 5),
        KNOCKBACK((String)Enchantment.KNOCKBACK, "", 0, 50, 2),
        FIRE_ASPECT((String)Enchantment.FIRE_ASPECT, "Aspect", 0, 50, 2),
        LOOTING((String)Enchantment.LOOT_BONUS_MOBS, "", 0, 50, 3),
        POWER((String)Enchantment.ARROW_DAMAGE, "", 0, 50, 5),
        PUNCH((String)Enchantment.ARROW_KNOCKBACK, "", 0, 50, 2),
        FLAME((String)Enchantment.ARROW_FIRE, "", 0, 1, 1),
        INFINITY((String)Enchantment.ARROW_INFINITE, "", 0, 1, 1),
        EFFICIENCY((String)Enchantment.DIG_SPEED, "", 0, 50, 5),
        SILK_TOUCH((String)Enchantment.SILK_TOUCH, "Touch", 0, 1, 1),
        UNBREAKING((String)Enchantment.DURABILITY, "", 0, 50, 3),
        FORTUNE((String)Enchantment.LOOT_BONUS_BLOCKS, "", 0, 50, 3),
        LUCK_OF_THE_SEA((String)Enchantment.LUCK, "of the Sea", 0, 100, 3),
        LURE((String)Enchantment.LURE, "", 0, 100, 3);

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

