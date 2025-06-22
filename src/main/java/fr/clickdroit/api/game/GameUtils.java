package fr.clickdroit.api.game;

import fr.clickdroit.api.API;
import fr.clickdroit.api.common.rules.items.GeneralRules;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class GameUtils {
    public static void startPlayer(Player player, GameMode gameMode) {
        player.closeInventory();
        player.setGameMode(gameMode);
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setMaxHealth(20.0D);
        player.setHealth(20.0D);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setTotalExperience(0);
        player.setExp(0.0F);
        clearPlayerEffect(player);
    }

    public static void setSpectator(Player player) {
        startPlayer(player, GameMode.SPECTATOR);
    }

    public static void clearPlayerEffect(Player player) {
        for (PotionEffect potionEffect : player.getActivePotionEffects())
            player.removePotionEffect(potionEffect.getType());
    }

    public static int getPlayerAmount() {
        int amount = 0;
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (!players.getGameMode().equals(GameMode.SPECTATOR))
                amount++;
        }
        return amount;
    }

    public static boolean isSoloMode() {
        return (API.getAPI().getGameManager().getGameConfig().getPlayerPerTeam() == 1);
    }

    public static boolean isGameStarted() {
        return API.getAPI().getGameManager().getGameState().equals(GameState.PLAYING);
    }

    public static boolean hasGameStarted() {
        return (API.getAPI().getGameManager().getGameState().equals(GameState.PLAYING) ||
                API.getAPI().getGameManager().getGameState().equals(GameState.FINISH));
    }

    public static void registerHealth() {
        if (GeneralRules.HEALTH.isEnabled()) {
            Scoreboard scoreboard = API.getAPI().getServer().getScoreboardManager().getMainScoreboard();
            Objective objective = (scoreboard.getObjective("health") == null) ? scoreboard.registerNewObjective("health", "health") : scoreboard.getObjective("health");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            objective.setDisplayName("");
                    Objective objectiveTab = (scoreboard.getObjective("vie") == null) ? scoreboard.registerNewObjective("vie", "health") : scoreboard.getObjective("vie");
            objectiveTab.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            for (Player players : Bukkit.getOnlinePlayers()) {
                double current = players.getHealth();
                players.setHealth(current - 1.0D);
            }
            Bukkit.getScheduler().runTaskLater((Plugin)API.getAPI(), () -> {
                for (Player players : Bukkit.getOnlinePlayers())
                    players.setHealth(players.getMaxHealth());
            },2L);
        }
    }
}