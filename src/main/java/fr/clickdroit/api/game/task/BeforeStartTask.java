package fr.clickdroit.api.game.task;

import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class BeforeStartTask extends BukkitRunnable {
    private final GameManager gameManager;

    public BeforeStartTask(GameManager gameManager) {
        this.gameManager = gameManager;
        GameConfig gameConfig = gameManager.getGameConfig();
    }

    public void run() {
        if (GameUtils.hasGameStarted())
            cancel();
    }
}
