package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;

public class Anonymous extends ScenarioManager {
    private boolean isRegistered = false;

    @Override
    public void configure() {
        this.scenario = Scenario.ANONYMOUS;
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onStart() {
        applyTabListMask();
    }

    private void applyTabListMask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Scenario.ANONYMOUS.isEnabled()) {
                    this.cancel();
                    return;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (API.getAPI().getGameManager().getInGamePlayers().contains(player.getUniqueId())) {
                        player.setPlayerListName("§7Joueur Masqué");
                        player.setCustomName("§7Joueur Masqué");
                        player.setCustomNameVisible(true);
                    }
                }
            }
        }.runTaskTimer((Plugin) API.getAPI(), 20L, 100L); // Applied asynchronously every 5 secs to prevent overriding
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (API.getAPI().getGameManager().getInGamePlayers().contains(event.getPlayer().getUniqueId())) {
            event.getPlayer().setPlayerListName("§7Joueur Masqué");
            event.getPlayer().setCustomName("§7Joueur Masqué");
            event.getPlayer().setCustomNameVisible(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // Obfuscate standard death message to preserve anonymity if necessary
        event.setDeathMessage("§cUn Joueur Masqué §fa poussé son dernier souffle.");
    }
}
