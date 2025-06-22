package fr.clickdroit.api.module.standard;

import fr.clickdroit.api.game.GameManager;

public class UHCStandard {
    private final GameManager gameManager;

    public UHCStandard(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }
}