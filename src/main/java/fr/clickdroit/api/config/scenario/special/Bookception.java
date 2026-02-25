package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.utils.item.ItemCreator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Bookception extends ScenarioManager implements Listener {
    @EventHandler(priority = EventPriority.HIGH)
    private void onDeath(PlayerDeathEvent event) {
        if (this.scenario.isEnabled()) {
            List<Enchantment> enchants = Arrays.asList(Enchantment.values());
            Enchantment enchantment = enchants.get(ThreadLocalRandom.current().nextInt(enchants.size()));
            int level = ThreadLocalRandom.current().nextInt(enchantment.getMaxLevel() + 1);
            if (level == 0)
                level = 1;
            event.getDrops().add((new ItemCreator(Material.ENCHANTED_BOOK)).addStoredEnchantment(enchantment, Integer.valueOf(level)).getItem());
        }
    }

    public void configure() {
        this.scenario = Scenario.BOOKCEPTION;
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, (Plugin) API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {}
}
