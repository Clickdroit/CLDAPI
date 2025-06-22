package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class Paranoia extends ScenarioManager implements Listener {
    @EventHandler
    private void onCraft(PrepareItemCraftEvent event) {
        ItemStack itemStack = event.getRecipe().getResult();
        if (itemStack.getType() != Material.AIR && itemStack.getType() == Material.ENCHANTMENT_TABLE) {
            HumanEntity humanEntity = event.getView().getPlayer();
            if (humanEntity instanceof Player) {
                Player player = (Player)humanEntity;
                Location location = player.getLocation();
                Bukkit.broadcastMessage(""+ player.getName() + " craftune d'enchantement coordonsuivantes : X: " + location.getBlockX() + ", Y: " + location.getBlockY() + ", Z: " + location.getBlockZ());
            }
        }
    }

    @EventHandler
    private void onConsume(PlayerItemConsumeEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack != null && itemStack.getType() == Material.GOLDEN_APPLE) {
            Location location = event.getPlayer().getLocation();
            Bukkit.broadcastMessage(""+ event.getPlayer().getName() + " consommune d'or coordonsuivantes : X: " + location.getBlockX() + ", Y: " + location.getBlockY() + ", Z: " + location.getBlockZ());
        }
    }

    @EventHandler
    private void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!event.isCancelled()) {
            Location location = event.getPlayer().getLocation();
            if (block.getType() == Material.DIAMOND_ORE) {
                Bukkit.broadcastMessage(""+ event.getPlayer().getName() + " cassun de diamant coordonsuivantes : X: " + location.getBlockX() + ", Y: " + location.getBlockY() + ", Z: " + location.getBlockZ());
            } else if (block.getType() == Material.GOLD_ORE) {
                Bukkit.broadcastMessage(""+ event.getPlayer().getName() + " cassun d'or coordonsuivantes : X: " + location.getBlockX() + ", Y: " + location.getBlockY() + ", Z: " + location.getBlockZ());
            }
        }
    }

    public void configure() {
        this.scenario = Scenario.PARANOIA;
    }

    public void onStart() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onEnable() {}

    public void onDisable() {}
}

