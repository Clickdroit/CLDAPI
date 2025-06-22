package fr.clickdroit.api.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class TargetUtils {
    public static Player getTarget(Player player, int maxRange, double aiming, boolean wallHack) {
        Player target = null;
        double distance = 0.0D;
        Location playerEyes = player.getEyeLocation();
        Vector direction = playerEyes.getDirection().normalize();
        List<Player> targets = new ArrayList<>();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online == player ||
                    !online.getWorld().equals(player.getWorld()) || online
                    .getLocation().distanceSquared(playerEyes) > (maxRange * maxRange) || online
                    .getGameMode().equals(GameMode.SPECTATOR))
                continue;
            targets.add(online);
        }
        if (targets.size() > 0) {
            Location loc = playerEyes.clone();
            Vector progress = direction.clone().multiply(0.7D);
            maxRange = 100 * maxRange / 70;
            int loop = 0;
            while (loop < maxRange) {
                loop++;
                loc.add(progress);
                Block block = loc.getBlock();
                if (!wallHack && block.getType().isSolid())
                    break;
                double lx = loc.getX();
                double ly = loc.getY();
                double lz = loc.getZ();
                for (Player possibleTarget : targets) {
                    if (possibleTarget == player)
                        continue;
                    Location testLoc = possibleTarget.getLocation().add(0.0D, 0.85D, 0.0D);
                    double px = testLoc.getX();
                    double py = testLoc.getY();
                    double pz = testLoc.getZ();
                    boolean dX = (Math.abs(lx - px) < 0.7D * aiming);
                    boolean dY = (Math.abs(ly - py) < 1.7D * aiming);
                    boolean dZ = (Math.abs(lz - pz) < 0.7D * aiming);
                    if (dX && dY && dZ) {
                        target = possibleTarget;
                        break;
                    }
                }
                if (target != null) {
                    distance = (loop * 70 / 100);
                    break;
                }
            }
        }
        if (target != null)
            return target;
        return null;
    }
}
