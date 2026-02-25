package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class SkyHigh extends ScenarioManager implements Listener {
    @EventHandler
    private void onPlaceBlock(BlockPlaceEvent event) {
        if (this.scenario.isEnabled() && event
                .getBlock().getType() == Material.DIRT) {
            ItemStack itemStack = event.getPlayer().getItemInHand();
            if (itemStack != null && itemStack.getType() != Material.AIR && itemStack
                    .getType().equals(Material.DIRT))
                event.getPlayer().setItemInHand((new ItemCreator(Material.DIRT)).setAmount(Integer.valueOf(3)).getItem());
        }
    }

    public void configure() {
        this.scenario = Scenario.SKYHIGH;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {}
}

