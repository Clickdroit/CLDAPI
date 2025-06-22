package fr.clickdroit.api.game.episode;

import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.module.ModuleType;
import fr.clickdroit.api.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class EpisodeManager {
    private final GameManager gameManager;

    private int episode;

    public EpisodeManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.episode = 1;
    }

    public void switchEpisode() {
        if (this.gameManager.getModuleManager().getCurrentModule() != ModuleType.LG) {
            addEpisode();
            Bukkit.getOnlinePlayers().forEach(players -> {
                Title.sendTitle(players, 10, 10, 10, "", "" + getEpisode());
                players.playSound(players.getLocation(), Sound.ORB_PICKUP, 3.0F, 0.0F);
            });
            this.gameManager.getApi().getModules().onEpisodeSwitch();
        }
    }

    public void addEpisode() {
        this.episode++;
    }

    public int getEpisode() {
        return this.episode;
    }
}

