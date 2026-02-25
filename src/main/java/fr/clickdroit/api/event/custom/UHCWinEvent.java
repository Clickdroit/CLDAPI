package fr.clickdroit.api.event.custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Event fired when the UHC game ends and a winner is declared.
 * Useful for cosmetics plugins to spawn fireworks or dragons around the winner.
 */
public class UHCWinEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final String winnerName;

    public UHCWinEvent(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getWinnerName() {
        return winnerName;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
