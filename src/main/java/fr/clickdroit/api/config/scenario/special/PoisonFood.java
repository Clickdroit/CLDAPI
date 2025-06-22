package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class PoisonFood extends ScenarioManager implements Listener {
    @EventHandler
    private void onConsume(PlayerItemConsumeEvent event) {
        if (this.scenario.isEnabled()) {
            Random random = new Random();
            int next = random.nextInt(100);
            if (next <= this.scenario.getValue())
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
        }
    }

    public void configure() {
        this.scenario = Scenario.POISONFOOD;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin) API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {}
}
