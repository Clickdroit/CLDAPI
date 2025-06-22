package fr.clickdroit.api.utils.gui;

import fr.clickdroit.api.API;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class GUIView {
    protected Player player;

    private final Inventory inventory;

    private final GUIListenerView guiListener;

    private final Listener personnalListener;

    private boolean closed;

    private UUID target;

    private UUID user;

    public GUIView(int size, Player user, Player target) {
        this.inventory = Bukkit.createInventory(null, size, target.getName());
        this.player = user;
        this.target = target.getUniqueId();
        this.user = user.getUniqueId();
        this.guiListener = new GUIListenerView(this);
        this.personnalListener = getPersonnalListener();
        this.closed = false;
    }

    public abstract void guiInteractEvent(int paramInt, ItemStack paramItemStack, InventoryClickEvent paramInventoryClickEvent);

    public abstract void guiUserInteractEvent(int paramInt, ItemStack paramItemStack, InventoryClickEvent paramInventoryClickEvent);

    public Listener getPersonnalListener() {
        return null;
    }

    public void open() {
        this.player.openInventory(this.inventory);
        Bukkit.getPluginManager().registerEvents(this.guiListener, (Plugin)API.getAPI());
        if (this.personnalListener != null)
            Bukkit.getPluginManager().registerEvents(this.personnalListener, (Plugin)API.getAPI());
    }

    public void close() {
        HandlerList.unregisterAll(this.guiListener);
        if (this.personnalListener != null)
            HandlerList.unregisterAll(this.personnalListener);
        this.player.closeInventory();
        this.closed = true;
    }

    public void setItem(int slot, ItemStack itemStack) {
        if (this.inventory.getSize() >= slot)
            this.inventory.setItem(slot, itemStack);
    }

    public ItemStack getItem(int slot) {
        return this.inventory.getItem(slot);
    }

    public void addItem(ItemStack itemStack) {
        this.inventory.addItem(new ItemStack[] { itemStack });
    }

    public void removeItem(ItemStack itemStack) {
        this.inventory.remove(itemStack);
    }

    public void removeItem(int slot) {
        this.inventory.remove(slot);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public UUID getTarget() {
        return this.target;
    }

    public UUID getUser() {
        return this.user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    public boolean isClosed() {
        return this.closed;
    }
}
