package fr.clickdroit.api.utils;

import fr.clickdroit.api.GamePlayer;

import java.util.concurrent.TimeUnit;

public class CombatUtilsAPI {
    public static long getTimeBeforeLastFight(GamePlayer player) {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) - TimeUnit.MILLISECONDS.toSeconds(player.getLastFight());
    }
}
