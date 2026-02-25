package fr.clickdroit.api.config.common;

public enum GameAccess {
    OPEN("§aOuvert"),
    CLOSE("§cFermé");

    private final String message;

    GameAccess(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public GameAccess nextAccess() {
        return this == OPEN ? CLOSE : OPEN;
    }

    public boolean isOpen() {
        return this == OPEN;
    }
}
