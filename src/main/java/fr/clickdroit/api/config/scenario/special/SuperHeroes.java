package fr.clickdroit.api.config.scenario.special;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class SuperHeroes extends ScenarioManager implements Listener {
    private final Map<UUID, String> player_effect = new HashMap<>();

    @EventHandler
    private void onFood(FoodLevelChangeEvent event) {
        Player player = (Player)event.getEntity();
        if (this.player_effect.containsKey(player.getUniqueId()) && ((String)this.player_effect
                .get(player.getUniqueId())).equalsIgnoreCase("Jump"))
            event.setFoodLevel(20);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (API.getAPI().getGameManager().getGameState().equals(GameState.PLAYING) && event
                .getEntity() instanceof Player) {
            Player player = (Player)event.getEntity();
            if (this.scenario.isEnabled() && (event
                    .getCause() == EntityDamageEvent.DamageCause.FALL || event.getCause() == EntityDamageEvent.DamageCause.FALLING_BLOCK) && this.player_effect
                    .containsKey(player.getUniqueId()) && ((String)this.player_effect
                    .get(player.getUniqueId())).equalsIgnoreCase("Jump"))
                event.setCancelled(true);
        }
    }

    public void configure() {
        this.scenario = Scenario.SUPERHEROES;
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin)API.getAPI());
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    public void onStart() {
        List<Player> list = new ArrayList<>();
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (API.getAPI().getGameManager().getInGamePlayers().contains(players.getUniqueId()))
                list.add(players);
        }
        List<String> effects = new ArrayList<>();
        effects.add("Strenght");
        effects.add("Speed");
        effects.add("Jump");
        effects.add("DoubleHealth");
        effects.add("Resistance");
        effects.add("Invisibility");
        for (Player players : list) {
            String effect = effects.get((new Random()).nextInt(effects.size()));
            this.player_effect.put(players.getUniqueId(), effect);
            switch (effect) {
                case "Strenght":
                    players.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 255555, 0, true, false));
                case "Speed":
                    players.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 255555, 1, true, false));
                case "Jump":
                    players.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 255555, 3, true, false));
                    players.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 255555, 0, true, false));
                case "Resistance":
                    players.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 255555, 0, true, false));
                case "DoubleHealth":
                    players.setMaxHealth(40.0D);
                    players.setHealth(40.0D);
                case "Invisibility":
                    players.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 255555, 0, true, false));
            }
        }
    }
}

