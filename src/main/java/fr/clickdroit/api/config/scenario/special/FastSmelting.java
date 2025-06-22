package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class FastSmelting extends ScenarioManager implements Listener {
    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        startUpdate((Furnace)event.getBlock().getState(), 5);
    }

    private void startUpdate(final Furnace block, final int speed) {
        (new BukkitRunnable() {
            public void run() {
                if (block.getCookTime() > 0 || block.getBurnTime() > 0) {
                    block.setCookTime((short)(block.getCookTime() + speed));
                    block.update();
                } else {
                    cancel();
                }
            }
        }).runTaskTimer((Plugin) API.getAPI(), 1L, 1L);
    }

    public void configure() {
        this.scenario = Scenario.FASTSMELTING;
    }

    public void onStart() {}

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
