package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.spigotmc.event.entity.EntityMountEvent;

public class Horseless extends ScenarioManager implements Listener {
    @EventHandler
    private void onDress(EntityMountEvent event) {
        if (this.scenario.isEnabled() && event
                .getMount().getType() == EntityType.HORSE)
            event.setCancelled(true);
    }

    public void configure() {
        this.scenario = Scenario.HORSELESS;
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
