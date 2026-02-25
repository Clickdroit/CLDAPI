package fr.clickdroit.api.game.listeners;

import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {
    private final GameManager gameManager;

    public InventoryListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!GameUtils.isGameStarted() &&
                !player.getGameMode().equals(GameMode.CREATIVE))
            event.setCancelled(true);
    }
}
