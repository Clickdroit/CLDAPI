package fr.clickdroit.api.common.world;

import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.module.ModuleType;
import fr.clickdroit.api.utils.Title;
import fr.clickdroit.api.utils.msg.ProgressBar;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class WorldPopulator {
    private final GameManager gameManager;

    private final World gameWorld;

    public WorldPopulator(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameWorld = Bukkit.getWorlds().get(0);
    }

    public void setRoofed() {
        if (this.gameManager.getModuleManager().getCurrentModule().equals(ModuleType.DEMONSLAYER)) {
            (new BukkitRunnable() {
                int yInicial = 50;

                int progress = 0;

                int YChange = this.yInicial;

                public void run() {
                    int radius = 250;
                    if (this.progress == 0)
                        System.out.println("[UHC] Nettoyage du centre de la carte..");
                    for (int x = 0 - radius; x <= 0 + radius; x++) {
                        for (int z = 0 - radius; z <= 0 + radius; z++) {
                            Block block = WorldPopulator.this.gameWorld.getBlockAt(x, this.YChange, z);
                            block.setBiome(Biome.ROOFED_FOREST);
                            if (block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2 || block.getType() == Material.LOG || block.getType() == Material.LOG_2) {
                                block.setType(Material.AIR);
                                if (block.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().getType().equals(Material.DIRT))
                                    block.getLocation().add(0.0D, -1.0D, 0.0D).getBlock().setType(Material.GRASS);
                            } else if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
                                block.setType(Material.GRASS);
                            }
                        }
                    }
                    this.YChange++;
                    this.progress++;
                    for (Player player : Bukkit.getOnlinePlayers())
                        Title.sendActionBar(player, ChatColor.YELLOW + "Nettoyage du centre: " + ChatColor.GREEN + this.progress + "% §8[§r" +
                                ProgressBar.getProgressBar(this.progress, 80, 20, "|", ChatColor.YELLOW, ChatColor.GRAY) + "§8]");
                    if (this.progress >= 80) {
                        cancel();
                        System.out.println("[UHC] Nettoyage du centre de la carte termin!");
                        WorldPopulator.this.addSapling();
                    }
                }
            }).runTaskTimer((Plugin)this.gameManager.getApi(), 1L, 5L);
        } else {
            new LoadingChunkTask(this.gameWorld, 1200);
        }
    }

    private void addSapling() {
        System.out.println("[UHC] Plantage d'arbres au centre de la carte..");
        (new Thread(() -> (new BukkitRunnable() {
            int yInicial = 50;

            int progress = 0;

            int YChange = this.yInicial;

            public void run() {
                for (int radius = 250, x = 0 - radius; x <= radius; x++) {
                    for (int z = 0 - radius; z <= radius; z++) {
                        Block block = WorldPopulator.this.gameWorld.getBlockAt(x, this.YChange, z);
                        if (block.getType() == Material.AIR && (WorldPopulator.this.gameWorld.getBlockAt(x, this.YChange - 1, z).getType().equals(Material.DIRT) || WorldPopulator.this.gameWorld.getBlockAt(x, this.YChange - 1, z).getType().equals(Material.GRASS))) {
                            int i = ThreadLocalRandom.current().nextInt(36);
                            if (i <= 2)
                                block.getWorld().generateTree(block.getLocation(), TreeType.DARK_OAK);
                            if (i == 33) {
                                block.getWorld().generateTree(block.getLocation(), TreeType.BROWN_MUSHROOM);
                            } else if (i == 34) {
                                block.getWorld().generateTree(block.getLocation(), TreeType.RED_MUSHROOM);
                            }
                        }
                    }
                }
                this.YChange++;
                this.progress++;
                for (Player player : Bukkit.getOnlinePlayers())
                    Title.sendActionBar(player, ChatColor.YELLOW + "Création de la forêt:" + ChatColor.GREEN + this.progress + "% §8[§r" +
                            ProgressBar.getProgressBar(this.progress, 60, 20, "|", ChatColor.YELLOW, ChatColor.GRAY) + "§8]");
                if (this.progress >= 60) {
                    new LoadingChunkV2(WorldPopulator.this.gameWorld);
                    cancel();
                }
            }
        }).runTaskTimer((Plugin)this.gameManager.getApi(), 1L, 5L))).run();
    }

    public World getGameWorld() {
        return this.gameWorld;
    }
}

