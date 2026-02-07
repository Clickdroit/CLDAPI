package fr.clickdroit.api.event;

import fr.clickdroit.api.game.GameState;
import org.bukkit.event.HandlerList;

/**
 * Événement déclenché lors du changement d'état de la partie.
 */
public class GameStateChangeEvent extends UHCEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final GameState previousState;
    private final GameState newState;

    public GameStateChangeEvent(GameState previousState, GameState newState) {
        this.previousState = previousState;
        this.newState = newState;
    }

    public GameState getPreviousState() {
        return previousState;
    }

    public GameState getNewState() {
        return newState;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}

