package fr.clickdroit.api.event.custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event fired when the UHC game transitions to PLAYING state.
 * Useful for other plugins to initialize things when the actual game begins.
 */
public class UHCGameStartEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public UHCGameStartEvent() {
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
