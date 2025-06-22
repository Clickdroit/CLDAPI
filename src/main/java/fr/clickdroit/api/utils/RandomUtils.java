package fr.clickdroit.api.utils;

import fr.clickdroit.api.API;
import java.util.Random;
import org.bukkit.Location;

public class RandomUtils {
    public static int getRandomInt(int min, int max) {
        return min + (new Random()).nextInt(max) - min;
    }

    public static int getRandomDeviationValue(int value, int min, int max) {
        int i = max - min;
        int a = (new Random()).nextInt(i * 2) - i;
        return a + ((a < value) ? -1 : 1) * min;
    }

    public static Location getRandomLocationInBorder() {
        int size = (int)(API.getAPI().getGameManager().getBorder().getWorldBorder().getSize() / 2.0D);
        int end = getRandomInt(-size, size);
        return new Location(API.getAPI().getGameManager().getWorldPopulator().getGameWorld(), end, 140.0D, end);
    }
}
