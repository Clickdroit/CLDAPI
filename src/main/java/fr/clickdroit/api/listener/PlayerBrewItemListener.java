package fr.clickdroit.api.listener;

import fr.clickdroit.api.config.common.potion.PotionManagerGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerBrewItemListener implements Listener {
    @EventHandler
    public void onBrew(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        if (!(event.getInventory() instanceof org.bukkit.inventory.BrewerInventory))
            return;
        int slot = event.getRawSlot();
        if (slot == 3) {
            ItemStack result = event.getInventory().getItem(slot);
            if (result == null)
                return;
            for (PotionManagerGUI.Potions potion : PotionManagerGUI.Potions.values()) {
                if (result.getType() == potion.getMaterial() && !potion.isEnabled()) {
                    event.getInventory().remove(result);
                    event.setCancelled(true);
                    player.sendMessage("potion est d");
                            player.closeInventory();
                }
            }
        }
    }
}
