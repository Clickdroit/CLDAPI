package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class HasteyBoys extends ScenarioManager implements Listener {
    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        if (this.scenario.isEnabled()) {
            ItemMeta itemMeta;
            ItemStack itemStack = event.getInventory().getResult();
            if (itemStack == null || itemStack.getType() == Material.AIR)
                return;
            switch (itemStack.getType()) {
                case WOOD_PICKAXE:
                case WOOD_AXE:
                case WOOD_SPADE:
                case STONE_PICKAXE:
                case STONE_AXE:
                case STONE_SPADE:
                case IRON_PICKAXE:
                case IRON_AXE:
                case IRON_SPADE:
                case GOLD_PICKAXE:
                case GOLD_AXE:
                case GOLD_SPADE:
                case DIAMOND_PICKAXE:
                case DIAMOND_AXE:
                case DIAMOND_SPADE:
                    itemMeta = itemStack.getItemMeta();
                    itemMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
                    itemMeta.addEnchant(Enchantment.DURABILITY, 3, true);
                    itemStack.setItemMeta(itemMeta);
                    break;
            }
        }
    }

    public void configure() {
        this.scenario = Scenario.HASTEYBOYS;
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

