package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.utils.item.ItemCreator;
import fr.clickdroit.api.utils.Math2;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

public class Sangsue extends ScenarioManager implements Listener {
    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        if (this.scenario.isEnabled()) {
            Player player = event.getEntity();
            Player killer = player.getKiller();
            heal(killer, 10.0D);
            event.getDrops().add((new ItemCreator(Material.WOOD)).setAmount(Integer.valueOf(64)).getItem());
            event.getDrops().add((new ItemCreator(Material.COBBLESTONE)).setAmount(Integer.valueOf(64)).getItem());
        }
    }

    public void heal(Player player, double health) {
        player.setHealth(Math2.fit(0.0D, player.getHealth() + health, player.getMaxHealth()));
    }

    public void configure() {
        this.scenario = Scenario.SANGSUE;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void init() {}

    public void onStart() {}
}

