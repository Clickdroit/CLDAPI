package fr.clickdroit.api.common.world;

import fr.clickdroit.api.API;
import fr.clickdroit.api.game.GameState;
import fr.clickdroit.api.listener.world.ChunkUnloadListener;
import fr.clickdroit.api.utils.Title;
import fr.clickdroit.api.utils.msg.ProgressBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LoadingChunkTask extends BukkitRunnable {
    private double percent;

    private int ancientPercent;

    private double currentChunkLoad;

    private double totalChunkToLoad;

    private int cx;

    private int cz;

    private final int radius;

    private boolean finished;

    private final World world;

    public LoadingChunkTask(World world, int r) {
        r += 150;
        this.percent = 0.0D;
        this.ancientPercent = 0;
        this.totalChunkToLoad = Math.pow(r, 2.0D) / 64.0D;
        this.currentChunkLoad = 0.0D;
        this.cx = -r;
        this.cz = -r;
        this.world = world;
        this.radius = r;
        this.finished = false;
        runTaskTimer((Plugin)API.getAPI(), 0L, 5L);
    }

    public void run() {
        (new Thread(() -> {
            int i = 0;
            while (i < 30 && !this.finished) {
                Location loc = new Location(this.world, this.cx, 0.0D, this.cz);
                ChunkUnloadListener.keepChunk.add(loc.getChunk());
                loc.getWorld().loadChunk(loc.getChunk().getX(), loc.getChunk().getZ(), true);
                this.cx += 16;
                this.currentChunkLoad++;
                if (this.cx > this.radius) {
                    this.cx = -this.radius;
                    this.cz += 16;
                    if (this.cz > this.radius) {
                        this.currentChunkLoad = this.totalChunkToLoad;
                        this.finished = true;
                    }
                }
                i++;
            }
            this.percent = this.currentChunkLoad / this.totalChunkToLoad * 100.0D;
            for (Player player : Bukkit.getOnlinePlayers())
                Title.sendActionBar(player, ChatColor.GRAY + "Prégénération : " + ChatColor.GREEN + this.percent + "% §8[§r" + ProgressBar.getProgressBar((int)this.percent, 100, 40, "|", ChatColor.GREEN, ChatColor.GRAY) + "§8]");
            if (this.ancientPercent < this.percent)
                this.ancientPercent = (int)this.percent;
            if (this.finished) {
                API.getAPI().getGameManager().setGameState(GameState.WAITING);
                cancel();
            }
        })).run();
    }
}

