package fr.clickdroit.api.utils.anvil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class AnvilGUI implements Listener {
    public static final int SLOT_INPUT_LEFT = 0;

    public static final int SLOT_INPUT_RIGHT = 1;

    public static final int SLOT_OUTPUT = 2;

    private Player player;

    private AnvilClickHandler clickHandler;

    private ItemStack[] items;

    private Inventory inv;

    private Plugin plugin;

    private Listener listener;

    String itemName = "";

    public AnvilGUI(Plugin plugin, final Player player, AnvilClickHandler clickHandler) {
        this.plugin = plugin;
        this.player = player;
        this.clickHandler = clickHandler;
        this.items = new ItemStack[3];
        this.listener = new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getInventory().equals(AnvilGUI.this.inv))
                    event.setCancelled(true);
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getPlayer().equals(player))
                    AnvilGUI.this.onClose();
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                if (event.getPlayer().equals(player))
                    AnvilGUI.this.onClose();
            }
        };
    }

    public static interface AnvilClickHandler {
        boolean onClick(AnvilGUI param1AnvilGUI, String param1String);
    }

    public Player getPlayer() {
        return this.player;
    }

    public AnvilClickHandler getClickHandler() {
        return this.clickHandler;
    }

    public void setInputName(String name) {
        setItem(0, new ItemStack(Material.PAPER), name);
    }

    public void setOutputName(String name) {
        ItemStack item = this.inv.getItem(2);
        if (item == null) {
            item = this.inv.getItem(0);
            if (item == null)
                return;
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        this.inv.setItem(2, item);
    }

    public Inventory getInventory() {
        return this.inv;
    }

    public boolean isDead() {
        return (this.player == null && this.clickHandler == null && this.items == null && this.inv == null && this.listener == null && this.plugin == null);
    }

    public AnvilGUI open() {
        if (this.items[0] == null)
            setInputName(" ");
        this.inv = AnvilNMS.open(this);
        Bukkit.getPluginManager().registerEvents(this.listener, this.plugin);
        return this;
    }

    private void destroy() {
        this.player = null;
        this.clickHandler = null;
        this.items = null;
        this.inv = null;
        HandlerList.unregisterAll(this.listener);
        this.listener = null;
        this.plugin = null;
    }

    public void setItem(int slot, ItemStack item, String name) {
        if (name != null) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName() && meta.getDisplayName().equals(name)) {
                if (this.inv != null)
                    return;
            } else {
                meta.setDisplayName(name);
                item.setItemMeta(meta);
            }
        }
        this.items[slot] = item;
        if (slot != 2)
            this.items[2] = item;
        if (this.inv != null) {
            this.inv.setItem(slot, item);
            if (slot != 2)
                this.inv.setItem(2, item);
        }
    }

    public void onClose() {
        if (this.inv != null)
            this.inv.clear();
        destroy();
    }

    public ItemStack[] getItems() {
        return this.items;
    }
}
