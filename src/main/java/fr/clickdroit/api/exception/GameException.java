package fr.clickdroit.api.exception;

/**
 * Exception de base pour toutes les exceptions de l'API UHC.
 *
 * @author Clickdroit
 * @version 1.0
 */
public class GameException extends RuntimeException {

    public GameException(String message) {
        super(message);
    }

    public GameException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameException(Throwable cause) {
        super(cause);
    }
}

