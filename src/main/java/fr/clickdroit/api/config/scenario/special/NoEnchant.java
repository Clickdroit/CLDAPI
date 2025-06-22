package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class NoEnchant extends ScenarioManager implements Listener {
    @EventHandler
    public void PrepareItemCraftEvent(PrepareItemCraftEvent event) {
        if (event.getRecipe().getResult() == null)
            return;
        Material material = event.getRecipe().getResult().getType();
        if (material == Material.ENCHANTMENT_TABLE)
            event.getInventory().setResult(new ItemStack(Material.AIR));
    }

    @EventHandler
    private void EnchantItemEvent(EnchantItemEvent event) {
        event.setCancelled(true);
        event.getEnchanter().sendMessage("§cLe scénario §6NoEnchant §cest actif, vous ne pouvez pas utiliser d'enchantement.");
                event.getEnchanter().closeInventory();
    }

    public void configure() {
        this.scenario = Scenario.NOENCAHNT;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin) API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {}
}
