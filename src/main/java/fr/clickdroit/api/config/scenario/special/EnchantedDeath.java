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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EnchantedDeath extends ScenarioManager implements Listener {
    @EventHandler
    private void onCraft(PrepareItemCraftEvent event) {
        if (this.scenario.isEnabled()) {
            CraftingInventory craftingInventory = event.getInventory();
            if (craftingInventory.getResult().getType() == Material.ENCHANTMENT_TABLE)
                craftingInventory.setResult(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        if (this.scenario.isEnabled())
            event.getDrops().add((new ItemCreator(Material.ENCHANTMENT_TABLE)).getItem());
    }

    public void configure() {
        this.scenario = Scenario.ENCHANTEDDEATH;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin) API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {}
}
