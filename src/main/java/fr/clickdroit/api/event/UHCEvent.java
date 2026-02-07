package fr.clickdroit.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Classe de base pour tous les événements UHC.
 *
 * @author Clickdroit
 * @version 1.0
 */
public abstract class UHCEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public UHCEvent() {
        super();
    }

    public UHCEvent(boolean isAsync) {
        super(isAsync);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

