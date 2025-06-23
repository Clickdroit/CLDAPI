package fr.clickdroit.api.listener;

import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public final class ReconnectListener implements Listener {
    private final GameManager gameManager;

    public ReconnectListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    private void onAsyncPlayerLoginEvent(AsyncPlayerPreLoginEvent event) {
        if (this.gameManager.getGameState() != GameState.TELEPORTATION && this.gameManager
                .getGameState() != GameState.PLAYING)
            return;
        if (!this.gameManager.getGameConfig().isSpectators() &&
                !this.gameManager.getPlayedPlayers().contains(event.getUniqueId()))
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, "§cLes spectateurs ne sont pas autorisdans cette partie.");
    }
}
