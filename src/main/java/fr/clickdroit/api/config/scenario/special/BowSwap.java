package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.ThreadLocalRandom;

public class BowSwap extends ScenarioManager implements Listener {
    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (Rules.pvp.isActive() && this.scenario
                .isEnabled() && event
                .getEntity() instanceof Player && event.getDamager() instanceof Arrow && ((Arrow)event
                .getDamager()).getShooter() instanceof Player) {
            Player shooter = (Player)((Arrow)event.getDamager()).getShooter();
            Player player = (Player)event.getEntity();
            if (shooter.getUniqueId() != player.getUniqueId()) {
                int next = ThreadLocalRandom.current().nextInt(100);
                if (next <= this.scenario.getValue()) {
                    Location l1 = shooter.getLocation();
                    Location l2 = player.getLocation();
                    player.teleport(l1);
                    shooter.teleport(l2);
                    player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 5.0F, 0.0F);
                    shooter.playSound(shooter.getLocation(), Sound.ENDERMAN_TELEPORT, 5.0F, 0.0F);
                }
            }
        }
    }

    public void configure() {
        this.scenario = Scenario.BOWSWAP;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin) API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {}
}
