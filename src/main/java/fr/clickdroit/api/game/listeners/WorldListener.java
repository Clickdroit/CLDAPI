package fr.clickdroit.api.game.listeners;

import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.UHCConstants;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.util.Random;

public class WorldListener implements Listener {
    private final GameManager gameManager;
    private final Random random;

    public WorldListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.random = new Random();
    }

    @EventHandler
    private void onCreatureSpawnEvent(CreatureSpawnEvent event) {
        LivingEntity livingEntity = event.getEntity();
        CreatureSpawnEvent.SpawnReason spawnReason = event.getSpawnReason();
        if (spawnReason.equals(CreatureSpawnEvent.SpawnReason.NATURAL)) {
            if (livingEntity.getWorld().getName().equalsIgnoreCase(UHCConstants.LOBBY_WORLD_NAME)) {
                event.setCancelled(true);
                return;
            }
            if (livingEntity instanceof org.bukkit.entity.Enderman) {
                int next = this.random.nextInt(100);
                if (next > 50) {
                    event.setCancelled(true);
                    return;
                }
            }
            World entityWorld = event.getEntity().getWorld();
            if (entityWorld.getName().equalsIgnoreCase(UHCConstants.LOBBY_WORLD_NAME))
                event.setCancelled(true);
        }
    }

    @EventHandler
    private void onThunder(ThunderChangeEvent event) {
        if (event.toThunderState())
            event.setCancelled(true);
    }

    @EventHandler
    private void onRain(WeatherChangeEvent event) {
        if (event.toWeatherState())
            event.setCancelled(true);
    }
}
