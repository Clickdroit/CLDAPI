package fr.clickdroit.api.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class GUIListenerView implements Listener {
    private GUIView gui;

    public GUIListenerView(GUIView gui) {
        this.gui = gui;
    }

    @EventHandler
    private void interactEvent(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if (this.gui.getUser().equals(player.getUniqueId())) {
            this.gui.guiInteractEvent(event.getSlot(), event.getCurrentItem(), event);
        } else if (player.getUniqueId().equals(this.gui.getTarget())) {
            this.gui.guiUserInteractEvent(event.getSlot(), event.getCurrentItem(), event);
        }
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        if (this.gui.getPlayer().equals(player))
            this.gui.close();
    }

    public GUIView getGui() {
        return this.gui;
    }
}
