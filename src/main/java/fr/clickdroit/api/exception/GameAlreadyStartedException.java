package fr.clickdroit.api.exception;

import fr.clickdroit.api.game.GameState;

/**
 * Exception levée lorsqu'une partie est déjà démarrée.
 */
public class GameAlreadyStartedException extends GameException {

    private final GameState currentState;

    public GameAlreadyStartedException() {
        super("La partie a déjà démarré");
        this.currentState = null;
    }

    public GameAlreadyStartedException(GameState currentState) {
        super("La partie est dans l'état: " + currentState.name());
        this.currentState = currentState;
    }

    public GameState getCurrentState() {
        return currentState;
    }
}

