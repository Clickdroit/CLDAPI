package fr.clickdroit.api.service.impl;

import fr.clickdroit.api.API;
import fr.clickdroit.api.event.BorderShrinkEvent;
import fr.clickdroit.api.service.GameService;
import fr.clickdroit.api.utils.UHCConstants;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;

/**
 * Service de gestion de la bordure UHC.
 */
public class BorderService implements GameService {

    private API api;
    private boolean initialized = false;
    private WorldBorder worldBorder;
    private int startSize;
    private int endSize;
    private int blocksPerSecond;
    private boolean shrinking = false;

    @Override
    public void initialize(API api) {
        this.api = api;
        World world = api.getGameManager().getWorldPopulator().getGameWorld();
        this.worldBorder = world.getWorldBorder();
        this.startSize = UHCConstants.DEFAULT_BORDER_START_SIZE;
        this.endSize = UHCConstants.DEFAULT_BORDER_END_SIZE;
        this.blocksPerSecond = UHCConstants.DEFAULT_BORDER_SPEED;
        this.initialized = true;
    }

    @Override
    public void shutdown() {
        shrinking = false;
        initialized = false;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public int getPriority() {
        return 70;
    }

    // ===== Configuration =====

    public void setStartSize(int size) {
        this.startSize = size;
    }

    public void setEndSize(int size) {
        this.endSize = size;
    }

    public void setBlocksPerSecond(int speed) {
        this.blocksPerSecond = speed;
    }

    public int getStartSize() {
        return startSize;
    }

    public int getEndSize() {
        return endSize;
    }

    public int getBlocksPerSecond() {
        return blocksPerSecond;
    }

    // ===== Contrôle de la bordure =====

    public void setupBorder() {
        worldBorder.setCenter(0, 0);
        worldBorder.setSize(startSize * 2);
        worldBorder.setDamageAmount(0.5);
        worldBorder.setDamageBuffer(5);
        worldBorder.setWarningDistance(10);
        worldBorder.setWarningTime(15);
    }

    public void startShrinking() {
        int currentSize = (int) worldBorder.getSize() / 2;

        BorderShrinkEvent event = new BorderShrinkEvent(currentSize, endSize, blocksPerSecond);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        int targetSize = event.getTargetSize();
        int speed = event.getBlocksPerSecond();

        int sizeDifference = currentSize - targetSize;
        long timeInSeconds = sizeDifference / speed;

        worldBorder.setSize(targetSize * 2, timeInSeconds);
        shrinking = true;
    }

    public void stopShrinking() {
        worldBorder.setSize(worldBorder.getSize());
        shrinking = false;
    }

    public void setSize(int radius) {
        worldBorder.setSize(radius * 2);
    }

    public void setSizeOverTime(int radius, long seconds) {
        worldBorder.setSize(radius * 2, seconds);
    }

    // ===== Informations =====

    public int getCurrentSize() {
        return (int) worldBorder.getSize() / 2;
    }

    public boolean isShrinking() {
        return shrinking;
    }

    public boolean isAtFinalSize() {
        return getCurrentSize() <= endSize;
    }

    public double getDistanceToCenter(double x, double z) {
        return Math.max(Math.abs(x), Math.abs(z));
    }

    public boolean isOutsideBorder(double x, double z) {
        return getDistanceToCenter(x, z) > getCurrentSize();
    }

    public WorldBorder getWorldBorder() {
        return worldBorder;
    }
}

