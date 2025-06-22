package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CutClean extends ScenarioManager implements Listener {
    @EventHandler
    public void onHunt(EntityDeathEvent event) {
        if (this.scenario.isEnabled()) {
            EntityType type = event.getEntity().getType();
            switch (type) {
                case COW:
                    replaceDrop(event, new ItemStack[] { new ItemStack(Material.COOKED_BEEF, 2), new ItemStack(Material.LEATHER) });
                    break;
                case PIG:
                    replaceDrop(event, new ItemStack[] { new ItemStack(Material.COOKED_BEEF, 2) });
                    break;
                case CHICKEN:
                    replaceDrop(event, new ItemStack[] { new ItemStack(Material.COOKED_CHICKEN, 2), new ItemStack(Material.FEATHER) });
                    break;
                case RABBIT:
                    replaceDrop(event, new ItemStack[] { new ItemStack(Material.COOKED_RABBIT, 2) });
                    break;
                case SHEEP:
                    replaceDrop(event, new ItemStack[] { new ItemStack(Material.COOKED_MUTTON, 2) });
                    break;
            }
        }
    }

    public void replaceDrop(EntityDeathEvent event, ItemStack... items) {
        event.getDrops().clear();
        for (ItemStack item : items)
            event.getDrops().add(item);
    }

    public void configure() {
        this.scenario = Scenario.CUTCLEAN;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin) API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {}
}
