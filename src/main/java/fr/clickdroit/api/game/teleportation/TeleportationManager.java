package fr.clickdroit.api.game.teleportation;

import fr.clickdroit.api.API;
import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.game.teleportation.form.Form;
import fr.clickdroit.api.game.teleportation.plate.Plate;
import fr.clickdroit.api.game.teleportation.plate.SquarePlate;
import fr.clickdroit.api.game.teleportation.player.PlayerPlate;
import fr.clickdroit.api.utils.Title;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TeleportationManager {
    private final List<PlayerPlate> players;

    private final long delay;

    private final Form form;

    private final Map<String, Plate> playerPlates;

    public TeleportationManager(PlayerPlate[] players, long delay, Form form) {
        this(Arrays.asList(players), delay, form);
    }

    public TeleportationManager(List<PlayerPlate> players, long delay, Form form) {
        this.players = players;
        this.delay = delay;
        this.form = form;
        this.playerPlates = new HashMap<>();
    }

    public void teleportAllAndRun(Runnable runnable) {
        createPlate(runnable, this.delay, this.form, this.players);
    }

    public void startCoundown(final API main) {
        (new BukkitRunnable() {
            int count = 0;

            public void run() {
                double[] tps = (MinecraftServer.getServer()).recentTps;
                if (this.count >= 20 || tps[0] >= 19.85D) {
                    cancel();
                    (new BukkitRunnable() {
                        int time = 10;

                        public void run() {
                            if (this.time <= 0) {
                                Bukkit.getOnlinePlayers().forEach(players -> {
                                    Title.sendTitle(players, 0, 20, 10, "chance !", "le gagne");
                                            players.playSound(players.getLocation(), Sound.EXPLODE, 5.0F, 1.0F);
                                });
                                TeleportationManager.this.finishCountdown();
                                cancel();
                            } else if (this.time <= 5 || this.time == 10) {
                                Bukkit.getOnlinePlayers().forEach(players -> {
                                    Title.sendTitle(players, 0, 30, 0, "dans", ""+ this.time);
                                            players.playSound(players.getLocation(), Sound.NOTE_PLING, 5.0F, 1.0F);
                                });
                            }
                            this.time--;
                        }
                    }).runTaskTimer((Plugin)main, 0L, 20L);
                } else {
                    Bukkit.getOnlinePlayers().forEach(player -> Title.sendActionBar(player, "quelques" ));
                }
                this.count++;
            }
        }).runTaskTimer((Plugin)API.getAPI(), 0L, 20L);
    }

    public void teleportAllAndStart(API main) {
        Rules.noDamage.setActive(true);
        teleportAllAndRun(() -> startCoundown(main));
    }

    public void finishCountdown() {
        launchAll();
        API.getAPI().getGameManager().startGame();
    }

    public void teleportPlayersAndStart(final Runnable runnable) {
        (new BukkitRunnable() {
            int count = 0;

            public void run() {
                double[] tps = (MinecraftServer.getServer()).recentTps;
                if (this.count >= 20 || tps[0] >= 19.85D) {
                    cancel();
                    runnable.run();
                } else {
                    Bukkit.getOnlinePlayers().forEach(player -> Title.sendActionBar(player, "quelques "));
                }
                this.count++;
            }
        }).runTaskTimer((Plugin)API.getAPI(), 0L, 20L);
    }

    public void createPlate(final Runnable runnable, long delay, final Form form, Collection<PlayerPlate> players) {
        final int length = players.size();
        final Iterator<PlayerPlate> playerPlateIterator = players.iterator();
        (new BukkitRunnable() {
            int i = 0;

            public void run() {
                if (playerPlateIterator.hasNext()) {
                    PlayerPlate playerPlate = playerPlateIterator.next();
                    Plate plate = TeleportationManager.this.initPlate(playerPlate, form.calc(this.i, length));
                    TeleportationManager.this.playerPlates.put(playerPlate.getName(), plate);
                    Bukkit.getWorld("world").loadChunk(Bukkit.getWorld("world").getChunkAt(plate.getTeleportLocation()));
                    Bukkit.getOnlinePlayers().forEach(player -> Title.sendActionBar(player, ""+ playerPlate.getName() + "a t"+ (this.i + 1) + "/" + length + "]"));
                    Bukkit.getWorld("world").loadChunk(Bukkit.getWorld("world").getChunkAt(plate.getTeleportLocation()));
                    playerPlate.getName();
                    this.i++;
                } else {
                    cancel();
                    Bukkit.getScheduler().runTaskLater((Plugin)API.getAPI(), () -> TeleportationManager.this.teleportPlayersAndStart(runnable), 40L);
                }
            }
        }).runTaskTimer((Plugin)API.getAPI(), 0L, delay);
    }

    private Plate initPlate(PlayerPlate playerPlate, Location location) {
        SquarePlate squarePlate = new SquarePlate(location, 3, Material.STAINED_GLASS, 14);
        playerPlate.assignPlate((Plate)squarePlate);
        return (Plate)squarePlate;
    }

    private void teleport(PlayerPlate playerPlate, Location location) {
        SquarePlate squarePlate = new SquarePlate(location, 3, Material.STAINED_GLASS, 14);
        playerPlate.assignPlate((Plate)squarePlate);
    }

    public void launchAll() {
        launchAll(this.players);
    }

    public static void launchAll(List<PlayerPlate> players) {
        players.forEach(PlayerPlate::removePlate);
    }

    public Collection<PlayerPlate> getPlayers() {
        return this.players;
    }
}
