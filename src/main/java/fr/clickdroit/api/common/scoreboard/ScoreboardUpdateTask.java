package fr.clickdroit.api.common.scoreboard;

import fr.clickdroit.api.common.scoreboard.blink.BlinkEffect;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScoreboardUpdateTask extends BukkitRunnable {
    private final ScoreboardManager scoreboardManager;

    private final BlinkEffect blinkEffect;

    public ScoreboardUpdateTask(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
        this.blinkEffect = new BlinkEffect();
    }

    public void run() {
        ScoreboardContents scoreboardContents = (ScoreboardContents)this.scoreboardManager.getScoreboardContents().get();
        this.blinkEffect.next();
        for (Player players : Bukkit.getOnlinePlayers()) {
            BPlayerBoard board = this.scoreboardManager.getNetherboard().getBoard(players);
            scoreboardContents.reloadData(players.getUniqueId());
            scoreboardContents.setLines(board, players.getUniqueId(), this.blinkEffect.getText());
        }
    }
}

