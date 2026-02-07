package fr.clickdroit.api.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Événement déclenché lorsque la partie est sur le point de démarrer.
 * Peut être annulé pour empêcher le démarrage.
 */
public class GameStartEvent extends UHCEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private final int playerCount;

    public GameStartEvent(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getPlayerCount() {
        return playerCount;
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

