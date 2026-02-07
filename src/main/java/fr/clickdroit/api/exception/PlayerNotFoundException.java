package fr.clickdroit.api.exception;

import java.util.UUID;

/**
 * Exception levée lorsqu'un joueur n'est pas trouvé.
 */
public class PlayerNotFoundException extends GameException {

    private final UUID playerUUID;
    private final String playerName;

    public PlayerNotFoundException(UUID playerUUID) {
        super("Joueur non trouvé avec l'UUID: " + playerUUID);
        this.playerUUID = playerUUID;
        this.playerName = null;
    }

    public PlayerNotFoundException(String playerName) {
        super("Joueur non trouvé: " + playerName);
        this.playerUUID = null;
        this.playerName = playerName;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }
}

