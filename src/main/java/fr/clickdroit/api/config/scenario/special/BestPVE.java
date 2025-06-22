package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.config.scenario.special.runnable.BestPVERunnable;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BestPVE extends ScenarioManager implements Listener {
    public static List<UUID> playersBestPVE = new ArrayList<>();

    private void initBestPVE() {
        playersBestPVE.clear();
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (API.getAPI().getGameManager().getInGamePlayers().contains(players.getUniqueId()))
                playersBestPVE.add(players.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onDamage(EntityDamageEvent event) {
        if (!event.isCancelled() && event.getEntity() instanceof Player &&
                !Rules.noDamage.isActive()) {
            Player player = (Player)event.getEntity();
            if (playersBestPVE.contains(player.getUniqueId())) {
                playersBestPVE.remove(player.getUniqueId());
                player.sendMessage("ne faites plus parti de la liste BestPVE !");
                player.playSound(player.getLocation(), Sound.VILLAGER_HIT, 5.0F, 0.0F);
            }
        }
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        if (victim.getKiller() instanceof Player) {
            Player killer = victim.getKiller();
            if (!playersBestPVE.contains(killer.getUniqueId()))
                playersBestPVE.add(killer.getUniqueId());
        }
    }

    public void configure() {
        this.scenario = Scenario.BESTPVE;
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {
        initBestPVE();
        (new BestPVERunnable(API.getAPI().getGameManager())).runTaskTimer((Plugin)API.getAPI(), (Scenario.BESTPVE.getValue() * 1200), (Scenario.BESTPVE.getValue() * 1200));
    }
}

