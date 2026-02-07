package fr.clickdroit.api.event;

import org.bukkit.event.HandlerList;

/**
 * Événement déclenché lors de l'activation du PvP.
 */
public class PvPEnableEvent extends UHCEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final int secondsSinceStart;

    public PvPEnableEvent(int secondsSinceStart) {
        this.secondsSinceStart = secondsSinceStart;
    }

    public int getSecondsSinceStart() {
        return secondsSinceStart;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

