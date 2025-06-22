package fr.clickdroit.api.config.scenario.special.runnable;

import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class NetheribusRunnable extends BukkitRunnable {
    private final GameManager game;

    private int time;

    public NetheribusRunnable(GameManager game) {
        this.time = 30;
        this.game = game;
    }

    public void run() {
        if (this.time <= 0) {
            setTime(30);
            for (UUID uuid : this.game.getInGamePlayers()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null &&
                        !player.getLocation().getWorld().getName().equalsIgnoreCase("world_nether")) {
                    player.damage(6.0D);
                    Title.sendActionBar(player, "vous rendre dans le" );
                }
            }
        }
        this.time--;
    }

    public int getTime() {
        return this.time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}

