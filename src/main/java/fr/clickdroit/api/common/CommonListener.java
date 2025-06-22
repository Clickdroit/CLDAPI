package fr.clickdroit.api.common;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CommonListener implements Listener {
    private final Common common;

    public CommonListener(Common common) {
        this.common = common;
    }

    @EventHandler
    private void PlayerJoinEvent(PlayerJoinEvent event) {
        this.common.getScoreboardManager().onLogin(event.getPlayer());
    }

    @EventHandler
    private void PlayerQuitEvent(PlayerQuitEvent event) {
        this.common.getScoreboardManager().onLogout(event.getPlayer());
    }
}
