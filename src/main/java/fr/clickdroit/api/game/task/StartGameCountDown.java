package fr.clickdroit.api.game.task;

import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameState;
import fr.clickdroit.api.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class StartGameCountDown extends BukkitRunnable {
    private final GameManager gameManager;

    private int time = 10;

    public StartGameCountDown(GameManager gameManager) {
        this.gameManager = gameManager;
        Bukkit.getOnlinePlayers().forEach(players -> players.setExp(1.0F));
    }

    public void run() {
        if (!this.gameManager.getGameState().equals(GameState.STARTING))
            cancel();
        Bukkit.getOnlinePlayers().forEach(players -> {
            players.setLevel(this.time);
            Title.sendActionBar(players, "de la partie dans" + this.time + " " + ((this.time > 1) ? "secondes" : "seconde") + " ");
        });
        switch (this.time) {
            case 10:
                Bukkit.getOnlinePlayers().forEach(players -> {
                    Title.sendTitle(players, 0, 30, 0, "+ this.time, ");
                    players.playSound(players.getLocation(), Sound.ORB_PICKUP, 3.0F, 5.0F);
                });
                break;
            case 9:
                Bukkit.getOnlinePlayers().forEach(players -> players.setExp(0.9F));
                break;
            case 8:
                Bukkit.getOnlinePlayers().forEach(players -> players.setExp(0.8F));
                break;
            case 7:
                Bukkit.getOnlinePlayers().forEach(players -> players.setExp(0.7F));
                break;
            case 6:
                Bukkit.getOnlinePlayers().forEach(players -> players.setExp(0.6F));
                break;
            case 5:
                Bukkit.getOnlinePlayers().forEach(players -> {
                    Title.sendTitle(players, 0, 30, 0, "+ this.time, ");
                    players.playSound(players.getLocation(), Sound.ORB_PICKUP, 3.0F, 5.0F);
                    players.setExp(0.5F);
                });
                break;
            case 4:
                Bukkit.getOnlinePlayers().forEach(players -> {
                    Title.sendTitle(players, 0, 30, 0, "+ this.time, ");
                    players.playSound(players.getLocation(), Sound.ORB_PICKUP, 3.0F, 5.0F);
                    players.setExp(0.4F);
                });
                break;
            case 3:
                Bukkit.getOnlinePlayers().forEach(players -> {
                    Title.sendTitle(players, 0, 30, 0, "+ this.time, ");
                    players.playSound(players.getLocation(), Sound.ORB_PICKUP, 3.0F, 5.0F);
                    players.setExp(0.3F);
                });
                break;
            case 2:
                Bukkit.getOnlinePlayers().forEach(players -> {
                    Title.sendTitle(players, 0, 30, 0, ""+ this.time, "pr?");
                            players.playSound(players.getLocation(), Sound.ORB_PICKUP, 3.0F, 5.0F);
                    players.setExp(0.2F);
                });
                break;
            case 1:
                Bukkit.getOnlinePlayers().forEach(players -> {
                    Title.sendTitle(players, 0, 30, 0, ""+ this.time, "le meilleur gagne !");
                    players.playSound(players.getLocation(), Sound.ORB_PICKUP, 3.0F, 1.0F);
                    players.setExp(0.1F);
                });
                break;
            case 0:
                Bukkit.getOnlinePlayers().forEach(players -> {
                    players.setExp(0.0F);
                    players.setLevel(0);
                });
                this.gameManager.tryStartGame();
                cancel();
                break;
        }
        this.time--;
    }
}

