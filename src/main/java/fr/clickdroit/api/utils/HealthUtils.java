package fr.clickdroit.api.utils;

import org.bukkit.entity.Player;

public class HealthUtils {
    public static void addPermanentHeart(Player player, int amount) {
        player.setMaxHealth(player.getMaxHealth() + amount);
    }

    public static void removeHeart(Player player, int amount) {
        if (player.getHealth() <= amount) {
            player.setHealth(0.0D);
        } else {
            player.setHealth(player.getHealth() - amount);
        }
    }

    public static void removePermanentHeart(Player player, int amount) {
        if (player.getMaxHealth() <= amount) {
            player.setHealth(0.0D);
        } else {
            player.setMaxHealth(player.getMaxHealth() - amount);
        }
    }

    public static void heal(Player player, double health) {
        if (health < 0.0D)
            return;
        player.setHealth(Math2.fit(0.0D, player.getHealth() + health, player.getMaxHealth()));
    }

    public static void setPermanentHealth(Player player, int hearthNumber, boolean healOnReceive) {
        player.setMaxHealth(hearthNumber);
        if (healOnReceive)
            player.setHealth(player.getMaxHealth());
    }
}
