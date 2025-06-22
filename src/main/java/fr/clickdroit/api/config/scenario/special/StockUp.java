package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class StockUp extends ScenarioManager implements Listener {
    private int health;

    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        if (this.scenario.isEnabled()) {
            Player player = event.getEntity();
            if (API.getAPI().getGameManager().getInGamePlayers().contains(player.getUniqueId())) {
                this.health += 2;
                for (UUID uuid : API.getAPI().getGameManager().getInGamePlayers()) {
                    Player players = Bukkit.getPlayer(uuid);
                    if (players != null)
                        players.setMaxHealth(players.getMaxHealth() + this.health);
                }
            }
        }
    }

    public void configure() {
        this.scenario = Scenario.STOCKUP;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {
        this.health = 0;
    }
}

