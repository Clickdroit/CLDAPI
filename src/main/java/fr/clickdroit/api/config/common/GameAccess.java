package fr.clickdroit.api.config.common;

public enum GameAccess {
    OPEN("§aOuvert"),
    CLOSE("§cFermer");

    private final String message;

    GameAccess(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
