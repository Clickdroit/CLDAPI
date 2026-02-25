package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.utils.RandomUtils;
import fr.clickdroit.api.utils.item.ItemCreator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

public class BetaZombie extends ScenarioManager implements Listener {
    @EventHandler
    private void EntityDeathEvent(EntityDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        if (livingEntity instanceof org.bukkit.entity.Zombie)
            event.getDrops().add((new ItemCreator(Material.FEATHER)).setAmount(Integer.valueOf(RandomUtils.getRandomInt(1, 4))).getItem());
    }

    public void configure() {
        this.scenario = Scenario.BETAZOMBIE;
    }

    public void onStart() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin) API.getAPI());
    }

    public void onEnable() {}

    public void onDisable() {}
}

