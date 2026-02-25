package fr.clickdroit.api.game.teleportation;

import fr.clickdroit.api.API;
import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.game.teleportation.form.Form;
import fr.clickdroit.api.game.teleportation.plate.CirclePlate;
import fr.clickdroit.api.game.teleportation.plate.Plate;
import fr.clickdroit.api.game.teleportation.plate.SquarePlate;
import fr.clickdroit.api.game.teleportation.player.PlayerPlate;
import fr.clickdroit.api.utils.Title;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TeleportationManager {
    private final List<PlayerPlate> players;
    private final long delay;
    private final Form form;

    // Map thread-safe pour les plateformes
    private final Map<String, Plate> playerPlates = new ConcurrentHashMap<>();

    // Cache pour optimiser les accès répétés
    private final World gameWorld;
    private final Collection<? extends Player> onlinePlayersCache;

    // Constantes pour éviter les nombres magiques
    private static final double MIN_TPS_THRESHOLD = 19.85D;
    private static final int MAX_TPS_WAIT_CYCLES = 20;
    private static final int DEFAULT_COUNTDOWN_TIME = 10;
    private static final int PLATE_SIZE = 3;
    private static final Material PLATE_MATERIAL = Material.STAINED_GLASS;
    private static final byte PLATE_DATA = 14;

    // Messages pré-compilés pour éviter les concaténations répétées
    private static final String WAITING_MESSAGE = "§fPatientez quelques §csecondes§f...";
    private static final String GOOD_LUCK_TITLE = "§fBonne chance !";
    private static final String BEST_WINS_SUBTITLE = "§f Que le §cmeilleur gagne§f";
    private static final String LAUNCH_TITLE = "§fLancement dans";

    // Tasks pour pouvoir les annuler si nécessaire
    private BukkitTask tpsWaitTask;
    private BukkitTask countdownTask;
    private BukkitTask teleportationTask;

    public TeleportationManager(PlayerPlate[] players, long delay, Form form) {
        this(Arrays.asList(players), delay, form);
    }

    public TeleportationManager(List<PlayerPlate> players, long delay, Form form) {
        this.players = new CopyOnWriteArrayList<>(players); // Thread-safe copy
        this.delay = delay;
        this.form = form;

        // Cache du monde de jeu pour éviter les recherches répétées
        this.gameWorld = Bukkit.getWorld("world");

        // Cache des joueurs en ligne (snapshot au moment de la création)
        this.onlinePlayersCache = new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    public void teleportAllAndRun(Runnable runnable) {
        createPlateOptimized(runnable, this.delay, this.form, this.players);
    }

    public void startCoundown(final API main) {
        final AtomicInteger count = new AtomicInteger(0);

        tpsWaitTaskActive = true;
        this.tpsWaitTask = new BukkitRunnable() {
            @Override
            public void run() {
                double[] tps = MinecraftServer.getServer().recentTps;
                int currentCount = count.incrementAndGet();

                if (currentCount >= MAX_TPS_WAIT_CYCLES || tps[0] >= MIN_TPS_THRESHOLD) {
                    cancel();
                    tpsWaitTaskActive = false;
                    startActualCountdown(main);
                } else {
                    sendWaitingMessageToAll();
                }
            }
        }.runTaskTimer((Plugin) API.getAPI(), 0L, 20L);
    }

    /**
     * Démarre le compte à rebours optimisé
     */
    private void startActualCountdown(final API main) {
        final AtomicInteger timeLeft = new AtomicInteger(DEFAULT_COUNTDOWN_TIME);

        countdownTaskActive = true;
        this.countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                int currentTime = timeLeft.getAndDecrement();

                if (currentTime <= 0) {
                    // Fin du compte à rebours
                    sendFinalMessages();
                    finishCountdown();
                    cancel();
                    countdownTaskActive = false;
                } else if (currentTime <= 5 || currentTime == 10) {
                    // Messages importants
                    sendCountdownMessage(currentTime);
                }
            }
        }.runTaskTimer((Plugin) main, 0L, 20L);
    }

    /**
     * Envoie les messages finaux de manière optimisée
     */
    private void sendFinalMessages() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Title.sendTitle(player, 0, 20, 10, GOOD_LUCK_TITLE, BEST_WINS_SUBTITLE);
            player.playSound(player.getLocation(), Sound.EXPLODE, 5.0F, 1.0F);
        }
    }

    /**
     * Envoie les messages de compte à rebours
     */
    private void sendCountdownMessage(int time) {
        String timeString = "§c" + time;

        for (Player player : Bukkit.getOnlinePlayers()) {
            Title.sendTitle(player, 0, 30, 0, LAUNCH_TITLE, timeString);
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 5.0F, 1.0F);
        }
    }

    /**
     * Envoie le message d'attente à tous les joueurs
     */
    private void sendWaitingMessageToAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Title.sendActionBar(player, WAITING_MESSAGE);
        }
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
        final AtomicInteger count = new AtomicInteger(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                double[] tps = MinecraftServer.getServer().recentTps;
                int currentCount = count.incrementAndGet();

                if (currentCount >= MAX_TPS_WAIT_CYCLES || tps[0] >= MIN_TPS_THRESHOLD) {
                    cancel();
                    runnable.run();
                } else {
                    sendWaitingMessageToAll();
                }
            }
        }.runTaskTimer((Plugin) API.getAPI(), 0L, 20L);
    }

    /**
     * Version optimisée de createPlate
     */
    public void createPlateOptimized(final Runnable runnable, long delay, final Form form,
            Collection<PlayerPlate> players) {
        final int totalPlayers = players.size();
        final List<PlayerPlate> playersList = new ArrayList<>(players);
        final AtomicInteger currentIndex = new AtomicInteger(0);

        // Pré-charger les messages pour éviter les concaténations répétées
        final List<String> progressMessages = new ArrayList<>(totalPlayers);
        for (int i = 0; i < totalPlayers; i++) {
            progressMessages.add("§f[" + (i + 1) + "/" + totalPlayers + "]");
        }

        teleportationTaskActive = true;
        this.teleportationTask = new BukkitRunnable() {
            @Override
            public void run() {
                int index = currentIndex.get();

                if (index < playersList.size()) {
                    PlayerPlate playerPlate = playersList.get(index);

                    try {
                        // Calculer la position
                        Location plateLocation = form.calc(index, totalPlayers);

                        // Créer et assigner la plateforme
                        Plate plate = initPlateOptimized(playerPlate, plateLocation);
                        playerPlates.put(playerPlate.getName(), plate);

                        // Charger le chunk une seule fois
                        loadChunkOptimized(plateLocation);

                        // Message de progression optimisé
                        String progressMessage = "§c" + playerPlate.getName() + "§f a été téléporté "
                                + progressMessages.get(index);
                        sendProgressMessageToAll(progressMessage);

                        currentIndex.incrementAndGet();

                    } catch (Exception e) {
                        e.printStackTrace();
                        currentIndex.incrementAndGet();
                    }
                } else {
                    // Téléportation terminée
                    cancel();
                    teleportationTaskActive = false;

                    // Attendre 2 secondes avant de démarrer le compte à rebours
                    Bukkit.getScheduler().runTaskLater(
                            (Plugin) API.getAPI(),
                            () -> teleportPlayersAndStart(runnable),
                            40L);
                }
            }
        }.runTaskTimer((Plugin) API.getAPI(), 0L, Math.max(2L, delay)); // Limité à 2 Ticks minimum pour préserver les
                                                                        // TPS
    }

    /**
     * Version optimisée de initPlate
     */
    private Plate initPlateOptimized(PlayerPlate playerPlate, Location location) {
        // Simple remplacement dans TeleportationManager :
        CirclePlate bluePortal = new CirclePlate(location, PLATE_SIZE, PLATE_MATERIAL, PLATE_DATA);
        playerPlate.assignPlate(bluePortal);
        return bluePortal;
    }

    /**
     * Charge un chunk de manière optimisée
     */
    private void loadChunkOptimized(Location location) {
        if (gameWorld != null) {
            int chunkX = location.getBlockX() >> 4;
            int chunkZ = location.getBlockZ() >> 4;

            // Vérifier si le chunk est déjà chargé
            if (!gameWorld.isChunkLoaded(chunkX, chunkZ)) {
                gameWorld.loadChunk(chunkX, chunkZ, true);
            }
        }
    }

    /**
     * Envoie un message de progression à tous les joueurs
     */
    private void sendProgressMessageToAll(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Title.sendActionBar(player, message);
        }
    }

    /**
     * Version non utilisée mais gardée pour compatibilité
     */
    @Deprecated
    private void teleport(PlayerPlate playerPlate, Location location) {
        SquarePlate squarePlate = new SquarePlate(location, PLATE_SIZE, PLATE_MATERIAL, PLATE_DATA);
        playerPlate.assignPlate(squarePlate);
    }

    public void launchAll() {
        launchAllOptimized(this.players);
    }

    /**
     * Version optimisée de launchAll
     */
    public static void launchAllOptimized(List<PlayerPlate> players) {
        // Traitement en parallèle pour de meilleures performances
        players.parallelStream().forEach(PlayerPlate::removePlate);
    }

    public Collection<PlayerPlate> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }

    // Variables pour tracker l'état des tâches
    private volatile boolean tpsWaitTaskActive = false;
    private volatile boolean countdownTaskActive = false;
    private volatile boolean teleportationTaskActive = false;

    /**
     * Méthode pour annuler toutes les tâches en cours
     */
    public void cancelAllTasks() {
        if (tpsWaitTask != null) {
            tpsWaitTask.cancel();
            tpsWaitTaskActive = false;
        }
        if (countdownTask != null) {
            countdownTask.cancel();
            countdownTaskActive = false;
        }
        if (teleportationTask != null) {
            teleportationTask.cancel();
            teleportationTaskActive = false;
        }
    }

    /**
     * Méthode pour obtenir le statut de la téléportation
     */
    public TeleportationStatus getStatus() {
        if (teleportationTaskActive) {
            return TeleportationStatus.TELEPORTING;
        } else if (tpsWaitTaskActive) {
            return TeleportationStatus.WAITING_TPS;
        } else if (countdownTaskActive) {
            return TeleportationStatus.COUNTDOWN;
        } else {
            return TeleportationStatus.COMPLETED;
        }
    }

    /**
     * Méthode pour obtenir les statistiques
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPlayers", players.size());
        stats.put("platesCreated", playerPlates.size());
        stats.put("status", getStatus());
        stats.put("delay", delay);
        return stats;
    }

    /**
     * Enumération pour le statut de téléportation
     */
    public enum TeleportationStatus {
        WAITING_TPS,
        TELEPORTING,
        COUNTDOWN,
        COMPLETED
    }
}
