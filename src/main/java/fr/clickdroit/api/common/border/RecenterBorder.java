package fr.clickdroit.api.common.border;

import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.util.Vector;

public class RecenterBorder extends SimpleBorder {
    public RecenterBorder(WorldBorder worldBorder) {
        super(worldBorder);
    }

    public void startReduceAtCenter(Location newCenter, double finalSize, double blocksSecond) {
        WorldBorder worldBorder = getWorldBorder();
        Location center = worldBorder.getCenter();
        Vector vector = center.toVector().add(newCenter.toVector());
        double x = Math.abs(vector.getX());
        double z = Math.abs(vector.getZ());
        double size = worldBorder.getSize();
        double newSize = size + Math.max(x, z);
        worldBorder.setSize(newSize);
        worldBorder.setCenter(newCenter);
        startReduce(finalSize, blocksSecond);
    }

    public void startReduceAtCenterInMinute(Location newCenter, double finalSize, double minuteDuration) {
        WorldBorder worldBorder = getWorldBorder();
        Location center = worldBorder.getCenter();
        Vector vector = center.toVector().add(newCenter.toVector());
        double x = Math.abs(vector.getX());
        double z = Math.abs(vector.getZ());
        double size = worldBorder.getSize();
        double newSize = size + Math.max(x, z);
        worldBorder.setSize(newSize);
        worldBorder.setCenter(newCenter);
        double blocksSecond = (newSize - finalSize) / minuteDuration / 60.0D;
        startReduce(finalSize, blocksSecond);
    }
}

