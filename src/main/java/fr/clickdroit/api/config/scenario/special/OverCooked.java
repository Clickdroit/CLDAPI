package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OverCooked extends ScenarioManager implements Listener {
    @EventHandler
    public void onCook(FurnaceSmeltEvent event) {
        ItemStack result = event.getResult();
        Furnace block = (Furnace)event.getBlock().getState();
        createExplosion(event.getBlock());
        for (ItemStack content : block.getInventory().getContents())
            content.setType(result.getType());
    }

    private void createExplosion(final Block block) {
        (new BukkitRunnable() {
            public void run() {
                block.getLocation().getWorld().createExplosion(block.getLocation(), 3.0F);
            }
        }).runTaskLater((Plugin)API.getAPI(), 1L);
    }

    public void configure() {
        this.scenario = Scenario.OVERCOOKED;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {}
}

