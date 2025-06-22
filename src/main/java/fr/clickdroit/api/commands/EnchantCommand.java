package fr.clickdroit.api.commands;


import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.function.Supplier;

import static org.bukkit.Material.ENCHANTED_BOOK;

public class EnchantCommand implements CommandExecutor, CustomInventory {
    private final GameManager gameManager;

    public EnchantCommand(GameManager gameManager) {
        this.gameManager = gameManager;
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

        public void setConfigValue(int configValue) {
            this.configValue = configValue;
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
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (GamePlayer.getPlayer(player.getUniqueId()).isEditing() || GamePlayer.getPlayer(player.getUniqueId()).isEditingDeathInv()) {
                if (player.getItemInHand().getType() != Material.AIR) {
                    this.gameManager.getApi().openInventory(player, getClass());
                } else {
                    player.sendMessage("prendre un objet en mains.");
                }
            } else {
                player.sendMessage("n'pas en train de dl'inventaire par d");
            }
        }
        return false;
    }

    public String getName() {
        return "Enchantement";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        ItemStack current = player.getItemInHand();
        slots[3] = current;
        slots[5] = (new ItemCreator(Material.DIAMOND_AXE)).setName(": " + (current.getItemMeta().spigot().isUnbreakable() ? "": "")).getItem();
        int slot = 9;
        for (EnchantEnum enchantEnum : EnchantEnum.values()) {
            int level = 0;
            if (current.getEnchantments().containsKey(enchantEnum.getEnchantment()))
                level = current.getEnchantmentLevel(enchantEnum.getEnchantment());
            ItemCreator item = (new ItemCreator(ENCHANTED_BOOK)).setName(enchantEnum.getName());
            item.addLore("");
            item.addLore("  : + level");
            item.addLore("");
            if (level < enchantEnum.getMax())
                item.addLore(" gauche : un niveau");
            if (level > enchantEnum.getMin())
                item.addLore(" droit : un niveau");
            item.addLore("");
            item.setAmount(Integer.valueOf(level));
            slots[slot] = item.getItem();
            slot++;
        }
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        ItemStack item;
        switch (clickedItem.getType()) {
            case DIAMOND_AXE:
                if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName() && clickedItem.getItemMeta().getDisplayName().contains("")) {
                    ItemStack itemStack = player.getItemInHand();
                    boolean unbreakable = itemStack.getItemMeta().spigot().isUnbreakable();
                    if (unbreakable) {
                        ItemMeta itemMeta1 = itemStack.getItemMeta();
                        itemMeta1.spigot().setUnbreakable(false);
                        itemStack.setItemMeta(itemMeta1);
                        this.gameManager.getApi().openInventory(player, getClass());
                        break;
                    }
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.spigot().setUnbreakable(true);
                    itemStack.setItemMeta(itemMeta);
                    this.gameManager.getApi().openInventory(player, getClass());
                }
                break;
            case ENCHANTED_BOOK:
                item = player.getItemInHand();
                if (clickType.equals(ClickType.LEFT)) {
                    for (EnchantEnum enchantEnum : EnchantEnum.values()) {
                        if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(enchantEnum.getName())) {
                            int level = item.getEnchantmentLevel(enchantEnum.getEnchantment());
                            if (level < enchantEnum.getMax()) {
                                item.addUnsafeEnchantment(enchantEnum.getEnchantment(), level + 1);
                                this.gameManager.getApi().openInventory(player, getClass());
                            }
                        }
                    }
                    break;
                }
                if (clickType.equals(ClickType.RIGHT))
                    for (EnchantEnum enchantEnum : EnchantEnum.values()) {
                        if (clickedItem.getItemMeta().getDisplayName().contains(enchantEnum.getName())) {
                            int level = item.getEnchantmentLevel(enchantEnum.getEnchantment());
                            if (level > enchantEnum.getMin())
                                if (level == 1) {
                                    item.removeEnchantment(enchantEnum.getEnchantment());
                                    this.gameManager.getApi().openInventory(player, getClass());
                                } else {
                                    item.addUnsafeEnchantment(enchantEnum.getEnchantment(), level - 1);
                                    this.gameManager.getApi().openInventory(player, getClass());
                                }
                        }
                    }
                break;
        }
    }

    public int getRows() {
        return 4;
    }
}