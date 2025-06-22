package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class NoFire extends ScenarioManager implements Listener {
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (this.scenario.isEnabled() && event
                .getEntity() instanceof org.bukkit.entity.Player && (event
                .getCause().equals(EntityDamageEvent.DamageCause.FIRE) || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK) || event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)))
            event.setCancelled(true);
    }

    public void configure() {
        this.scenario = Scenario.NOFIRE;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin) API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void init() {}

    public void onStart() {}
}
