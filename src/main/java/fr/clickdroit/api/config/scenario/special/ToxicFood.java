package fr.clickdroit.api.config.scenario.special;


import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.Plugin;

public class ToxicFood extends ScenarioManager implements Listener {
    @EventHandler
    private void onFood(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        player.damage((this.scenario.getValue() * 2));
        Title.sendActionBar(player, "scFood activvous avez subit des d!");
    }

    public void configure() {
        this.scenario = Scenario.TOXICFOOD;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {}
}
