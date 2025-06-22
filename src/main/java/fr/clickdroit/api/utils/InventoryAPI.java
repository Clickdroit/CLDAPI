package fr.clickdroit.api.utils;

import fr.clickdroit.api.utils.InventoryConvetor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class InventoryAPI {
    public ItemStack[] getItems() {
        return items;
    }

    public static void saveInventory(Player player) {
        int slot;
        for (slot = 0; slot < 36; slot++)
            items[slot] = null;
        for (slot = 0; slot < 36; slot++) {
            ItemStack item = player.getInventory().getItem(slot);
            if (item != null)
                items[slot] = item;
        }
        items[36] = player.getInventory().getHelmet();
        items[37] = player.getInventory().getChestplate();
        items[38] = player.getInventory().getLeggings();
        items[39] = player.getInventory().getBoots();
        inventoryContents = InventoryConvetor.inventoryToBase64(items);
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public static void saveDeathInventory(Player player) {
        int slot;
        for (slot = 0; slot < 36; slot++)
            itemsDeath[slot] = null;
        for (slot = 0; slot < 36; slot++) {
            ItemStack item = player.getInventory().getItem(slot);
            if (item != null)
                itemsDeath[slot] = item;
        }
        itemsDeath[36] = player.getInventory().getHelmet();
        itemsDeath[37] = player.getInventory().getChestplate();
        itemsDeath[38] = player.getInventory().getLeggings();
        itemsDeath[39] = player.getInventory().getBoots();
        inventoryDeathContents = InventoryConvetor.inventoryToBase64(itemsDeath);
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public static void setInventoryFrom64(String b64) {
        ItemStack[] inv = InventoryConvetor.inventoryFromBase64(b64);
        int slot;
        for (slot = 0; slot < 36; slot++)
            items[slot] = null;
        for (slot = 0; slot < 36; slot++) {
            ItemStack item = inv[slot];
            if (item != null)
                items[slot] = item;
        }
    }

    public static void setDeathInventoryFrom64(String b64) {
        ItemStack[] inv = InventoryConvetor.inventoryFromBase64(b64);
        int slot;
        for (slot = 0; slot < 36; slot++)
            itemsDeath[slot] = null;
        for (slot = 0; slot < 36; slot++) {
            ItemStack item = inv[slot];
            if (item != null)
                itemsDeath[slot] = item;
        }
    }

    public static void giveInvent(Player player) {
        player.getInventory().clear();
        for (int slot = 0; slot < 40; slot++) {
            ItemStack item = items[slot];
            if (item != null)
                player.getInventory().setItem(slot, item);
        }
        player.getInventory().setHelmet(items[36]);
        player.getInventory().setChestplate(items[37]);
        player.getInventory().setLeggings(items[38]);
        player.getInventory().setBoots(items[39]);
    }

    public static void giveDeathInvent(Player player) {
        player.getInventory().clear();
        for (int slot = 0; slot < 40; slot++) {
            ItemStack item = itemsDeath[slot];
            if (item != null)
                player.getInventory().setItem(slot, item);
        }
        player.getInventory().setHelmet(itemsDeath[36]);
        player.getInventory().setChestplate(itemsDeath[37]);
        player.getInventory().setLeggings(itemsDeath[38]);
        player.getInventory().setBoots(itemsDeath[39]);
    }

    public static ItemStack[] items = new ItemStack[40];

    public static ItemStack[] itemsDeath = new ItemStack[40];

    public static String inventoryContents;

    public static String inventoryDeathContents;
}
