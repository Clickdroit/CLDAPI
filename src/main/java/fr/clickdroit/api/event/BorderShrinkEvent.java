package fr.clickdroit.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Événement déclenché lors du démarrage de la réduction de bordure.
 */
public class BorderShrinkEvent extends UHCEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private final int currentSize;
    private int targetSize;
    private int blocksPerSecond;

    public BorderShrinkEvent(int currentSize, int targetSize, int blocksPerSecond) {
        this.currentSize = currentSize;
        this.targetSize = targetSize;
        this.blocksPerSecond = blocksPerSecond;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public int getTargetSize() {
        return targetSize;
    }

    public void setTargetSize(int targetSize) {
        this.targetSize = targetSize;
    }

    public int getBlocksPerSecond() {
        return blocksPerSecond;
    }

    public void setBlocksPerSecond(int blocksPerSecond) {
        this.blocksPerSecond = blocksPerSecond;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

