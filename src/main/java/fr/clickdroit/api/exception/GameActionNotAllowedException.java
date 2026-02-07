package fr.clickdroit.api.exception;

/**
 * Exception levée lors d'une action non autorisée sur une partie.
 */
public class GameActionNotAllowedException extends GameException {

    private final String action;
    private final String reason;

    public GameActionNotAllowedException(String action, String reason) {
        super("Action '" + action + "' non autorisée: " + reason);
        this.action = action;
        this.reason = reason;
    }

    public String getAction() {
        return action;
    }

    public String getReason() {
        return reason;
    }
}

