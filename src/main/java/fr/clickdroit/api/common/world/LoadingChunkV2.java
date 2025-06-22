package fr.clickdroit.api.common.world;

import fr.clickdroit.api.API;
import fr.clickdroit.api.utils.Title;
import fr.clickdroit.api.utils.msg.ProgressBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class LoadingChunkV2 {
    private final World world;

    private final int size;

    private BukkitTask task;

    private int nChunk;

    private int last;

    private long startTime;

    public LoadingChunkV2(World world) {
        this.size = 1100;
        this.world = world;
        world.setGameRuleValue("randomTickSpeed", "0");
        load();
    }

    private void load() {
        System.out.println("Starting pregeneration");
        (new Thread(() -> {
            this.startTime = System.currentTimeMillis();
            this.task = Bukkit.getScheduler().runTaskTimer((Plugin)API.getAPI(), new Runnable() {
                private int todo = LoadingChunkV2.this.size * 2 * LoadingChunkV2.this.size * 2 / 256;

                private int x = -LoadingChunkV2.this.size;

                private int z = -LoadingChunkV2.this.size;

                public void run() {
                    for (int i = 0; i < 50; i++) {
                        Chunk chunk = LoadingChunkV2.this.world.getChunkAt(LoadingChunkV2.this.world.getBlockAt(this.x, 64, this.z));
                        chunk.load(true);
                        chunk.load(false);
                        int percentage = LoadingChunkV2.this.nChunk * 100 / this.todo;
                        if (percentage > LoadingChunkV2.this.last) {
                            LoadingChunkV2.this.last = percentage;
                            LoadingChunkV2.this.sendMessage(percentage);
                        }
                        this.z += 16;
                        if (this.z >= LoadingChunkV2.this.size) {
                            this.z = -LoadingChunkV2.this.size;
                            this.x += 16;
                        }
                        if (this.x >= LoadingChunkV2.this.size) {
                            LoadingChunkV2.this.task.cancel();
                            int calculedTime = Math.round((float)((System.currentTimeMillis() - LoadingChunkV2.this.startTime) / 1000L));
                            System.out.println("Finished preload after " + calculedTime + "s");
                            API.getAPI().getGameManager().setPreloadFinished(true);
                            return;
                        }
                        LoadingChunkV2.this.nChunk++;
                    }
                }
            },1L, 1L);
        })).start();
    }

    private void sendMessage(int percentage) {
        for (Player player : Bukkit.getOnlinePlayers())
            Title.sendActionBar(player, ChatColor.GRAY + "Pr" + ChatColor.GREEN + percentage + "% "+
                    ProgressBar.getProgressBar(percentage, 100, 40, "|", ChatColor.GREEN, ChatColor.GRAY) + "");
    }
}

