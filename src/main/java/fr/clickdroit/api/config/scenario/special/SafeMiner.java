package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

public class SafeMiner extends ScenarioManager implements Listener {
    public void configure() {
        this.scenario = Scenario.SAFEMINER;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin) API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void init() {}

    public void onStart() {}

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            Location playerLocation = player.getLocation().clone();
            if (playerLocation.getY() <= Scenario.SAFEMINER.getValue() &&
                    !(event.getDamager() instanceof Player)) {
                double damage = event.getDamage();
                damage /= 2.0D;
                event.setDamage(damage);
            }
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            Location playerLocation = player.getLocation().clone();
            if (playerLocation.getY() <= Scenario.SAFEMINER.getValue()) {
                double damage;
                EntityDamageEvent.DamageCause cause = event.getCause();
                switch (cause) {
                    case FIRE:
                    case FIRE_TICK:
                    case LAVA:
                        event.setCancelled(true);
                        break;
                    case FALL:
                        damage = event.getFinalDamage();
                        damage /= 2.0D;
                        event.setDamage(damage);
                        break;
                }
            }
        }
    }
}

