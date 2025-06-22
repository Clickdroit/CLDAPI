package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.config.scenario.special.runnable.NetheribusRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Netheribus extends ScenarioManager implements Listener {
    public void configure() {
        this.scenario = Scenario.NETHERIBUS;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void init() {
        NetheribusRunnable netheribusRunnable = new NetheribusRunnable(API.getAPI().getGameManager());
        netheribusRunnable.runTaskTimer((Plugin) API.getAPI(), 0L, 20L);
        Bukkit.broadcastMessage("§eLe scénrario §cNetheribus §eest désormais actif !");
        Bukkit.broadcastMessage("§eVeuillez vous rendre dans le §cNether" );
        for (Player players : Bukkit.getOnlinePlayers())
            players.playSound(players.getLocation(), Sound.BLAZE_DEATH, 3.0F, 0.0F);
    }

    public void onStart() {
        API.getAPI().getGameManager().getGameConfig().setNether(true);
    }
}
