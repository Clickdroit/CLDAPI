package fr.clickdroit.api.config.scenario.special.runnable;

import fr.clickdroit.api.config.scenario.special.BestPVE;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BestPVERunnable extends BukkitRunnable {
    private final GameManager game;

    public BestPVERunnable(GameManager game) {
        this.game = game;
    }

    public void run() {
        for (UUID uuid : this.game.getInGamePlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && BestPVE.playersBestPVE
                    .contains(uuid)) {
                player.setMaxHealth(player.getMaxHealth() + 2.0D);
                player.setHealth(player.getHealth() + 2.0D);
                player.setHealthScale(player.getHealthScale() + 2.0D);
                Title.sendActionBar(player, "avez gagnun coeur car vous dans la liste BestPVE !");
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 5.0F, 0.0F);
            }
        }
    }
}
