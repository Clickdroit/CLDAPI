package fr.clickdroit.api.common.rules;

import fr.clickdroit.api.API;
import fr.clickdroit.api.game.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoDamage implements Rule, Listener {
    private boolean noDamage = false;

    private final List<UUID> noDamagePlayers = new ArrayList<>();

    public boolean isActive(UUID uuid) {
        return this.noDamagePlayers.contains(uuid);
    }

    public void active(UUID uuid, boolean activated) {
        this.noDamagePlayers.remove(uuid);
        if (activated)
            this.noDamagePlayers.add(uuid);
    }

    public boolean isActive() {
        return this.noDamage;
    }

    public void setActive(boolean active) {
        this.noDamage = active;
        if (!active) {
            Bukkit.broadcastMessage("est d");
            GameUtils.registerHealth();
        }
    }

    @EventHandler
    private void onFood(FoodLevelChangeEvent event) {
        Player player = (Player)event.getEntity();
        if (this.noDamage || this.noDamagePlayers.contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.setFoodLevel(20);
        }
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            if (this.noDamage || this.noDamagePlayers.contains(player.getUniqueId()))
                event.setCancelled(true);
        }
    }

    public void onLoad(API main) {
        main.getServer().getPluginManager().registerEvents(this, (Plugin)main);
    }
}

