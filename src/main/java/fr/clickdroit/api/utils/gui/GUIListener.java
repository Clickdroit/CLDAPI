package fr.clickdroit.api.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {
    private GUI gui;

    public GUIListener(GUI gui) {
        this.gui = gui;
    }

    @EventHandler
    private void interactEvent(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if (this.gui.getPlayer().equals(player)) {
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null)
                return;
            this.gui.guiInteractEvent(event.getSlot(), itemStack, event);
        }
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        if (this.gui.getPlayer().equals(player))
            this.gui.close();
    }

    public GUI getGui() {
        return this.gui;
    }
}
