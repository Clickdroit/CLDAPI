package fr.clickdroit.api.common.world;

import fr.clickdroit.api.API;
import fr.clickdroit.api.game.GameState;
import fr.clickdroit.api.listener.world.ChunkUnloadListener;
import fr.clickdroit.api.utils.Title;
import fr.clickdroit.api.utils.msg.ProgressBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class LoadingChunkTask extends BukkitRunnable {

    /**
     * Obtient le logger de manière lazy pour éviter les problèmes d'initialisation.
     */
    private static Logger getLogger() {
        API api = API.getAPI();
        return api != null ? api.getLogger() : Bukkit.getLogger();
    }

    // Variables atomiques pour la thread-safety
    private final AtomicLong currentChunkLoad = new AtomicLong(0);
    private final AtomicInteger lastReportedPercent = new AtomicInteger(0);
    private final AtomicBoolean finished = new AtomicBoolean(false);

    // Variables finales calculées une seule fois
    private final long totalChunkToLoad;
    private final int radius;
    private final World world;
    private final int chunksPerTick;

    // Variables de position (thread-safe)
    private volatile int cx;
    private volatile int cz;

    // Cache pour les messages de progression
    private String lastProgressMessage = "";
    private int lastProgressPercent = -1;

    // Statistiques de performance
    private long startTime;
    private long chunksProcessedInCurrentSecond = 0;
    private long lastSecondTimestamp = 0;

    public LoadingChunkTask(World world, int r) {
        this.startTime = System.currentTimeMillis();

        // Ajout optimisé du radius
        r += 150;

        // Calcul optimisé du nombre total de chunks
        this.totalChunkToLoad = calculateTotalChunks(r);

        this.cx = -r;
        this.cz = -r;
        this.world = world;
        this.radius = r;

        // Calcul dynamique des chunks par tick basé sur la performance du serveur
        this.chunksPerTick = calculateOptimalChunksPerTick();

        this.lastSecondTimestamp = System.currentTimeMillis();

        // Démarrer la tâche avec un interval optimisé
        runTaskTimer((Plugin) API.getAPI(), 0L, 3L); // 3 ticks au lieu de 5 pour plus de fluidité
    }

    /**
     * Calcule le nombre optimal de chunks à traiter par tick
     */
    private int calculateOptimalChunksPerTick() {
        // Base de 25 chunks par tick, ajustable selon les performances
        int base = 25;

        // Ajuster selon le nombre de joueurs en ligne
        int playerCount = Bukkit.getOnlinePlayers().size();
        if (playerCount > 50) {
            base = 15; // Réduire si beaucoup de joueurs
        } else if (playerCount < 10) {
            base = 35; // Augmenter si peu de joueurs
        }

        return base;
    }

    /**
     * Calcule le nombre total de chunks de manière optimisée
     */
    private long calculateTotalChunks(int radius) {
        // Calcul plus précis du nombre de chunks
        long diameter = (long) radius * 2;
        long chunksPerSide = diameter / 16 + 1;
        return chunksPerSide * chunksPerSide;
    }

    @Override
    public void run() {
        if (finished.get()) {
            return;
        }

        // Utiliser CompletableFuture pour le traitement asynchrone
        CompletableFuture.runAsync(this::processChunks)
                .thenRun(this::updateProgress)
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                });
    }

    /**
     * Traite les chunks de manière optimisée
     */
    private void processChunks() {
        int processed = 0;

        while (processed < chunksPerTick && !finished.get()) {
            if (!processNextChunk()) {
                break;
            }
            processed++;
        }

        // Mettre à jour les statistiques
        updateStatistics(processed);
    }

    /**
     * Traite le prochain chunk
     */
    private boolean processNextChunk() {
        if (finished.get()) {
            return false;
        }

        try {
            // Obtenir le chunk de manière optimisée
            Chunk chunk = world.getChunkAt(cx >> 4, cz >> 4);

            // Ajouter au cache des chunks à garder
            long chunkKey = (long) chunk.getX() << 32 | (chunk.getZ() & 0xFFFFFFFFL);
            ChunkUnloadListener.keepChunk.add(chunkKey);

            // Charger le chunk de manière forcée seulement s'il n'est pas déjà chargé
            if (!chunk.isLoaded()) {
                world.loadChunk(chunk.getX(), chunk.getZ(), true);
            }

            // Incrémenter le compteur
            currentChunkLoad.incrementAndGet();

            // Avancer à la position suivante
            return advancePosition();

        } catch (Exception e) {
            // En cas d'erreur, continuer avec le chunk suivant
            e.printStackTrace();
            currentChunkLoad.incrementAndGet();
            return advancePosition();
        }
    }

    /**
     * Avance à la position suivante et vérifie si terminé
     */
    private boolean advancePosition() {
        cx += 16;

        if (cx > radius) {
            cx = -radius;
            cz += 16;

            if (cz > radius) {
                // Prégénération terminée
                currentChunkLoad.set(totalChunkToLoad);
                finished.set(true);
                return false;
            }
        }

        return true;
    }

    /**
     * Met à jour les statistiques de performance
     */
    private void updateStatistics(int processed) {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastSecondTimestamp >= 1000) {
            // Nouveau cycle d'une seconde
            chunksProcessedInCurrentSecond = processed;
            lastSecondTimestamp = currentTime;
        } else {
            chunksProcessedInCurrentSecond += processed;
        }
    }

    /**
     * Met à jour la progression de manière optimisée
     */
    private void updateProgress() {
        if (finished.get()) {
            completePregeneration();
            return;
        }

        long current = currentChunkLoad.get();
        int currentPercent = (int) ((current * 100) / totalChunkToLoad);

        // Ne mettre à jour que si le pourcentage a changé
        if (currentPercent != lastProgressPercent) {
            lastProgressPercent = currentPercent;

            // Construire le message une seule fois
            String progressMessage = buildProgressMessage(currentPercent);

            // Ne l'envoyer que s'il a changé
            if (!progressMessage.equals(lastProgressMessage)) {
                lastProgressMessage = progressMessage;
                sendProgressToPlayers(progressMessage);

                // Log périodique pour le serveur
                if (currentPercent % 10 == 0 && currentPercent != lastReportedPercent.get()) {
                    lastReportedPercent.set(currentPercent);
                    logProgress(currentPercent, current);
                }
            }
        }
    }

    /**
     * Construit le message de progression de manière optimisée
     */
    private String buildProgressMessage(int percent) {
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.GRAY).append("Prégénération : ");
        sb.append(ChatColor.GREEN).append(percent).append("% ");
        sb.append("§8[§r");
        sb.append(ProgressBar.getProgressBar(percent, 100, 40, "|", ChatColor.GREEN, ChatColor.GRAY));
        sb.append("§8]");

        // Ajouter des informations de performance si en mode debug
        if (percent % 20 == 0) {
            long estimatedTimeRemaining = estimateTimeRemaining(percent);
            if (estimatedTimeRemaining > 0) {
                sb.append(" §7(~").append(formatTime(estimatedTimeRemaining)).append(" restant)");
            }
        }

        return sb.toString();
    }

    /**
     * Estime le temps restant
     */
    private long estimateTimeRemaining(int currentPercent) {
        if (currentPercent <= 0)
            return -1;

        long elapsedTime = System.currentTimeMillis() - startTime;
        long totalEstimatedTime = (elapsedTime * 100) / currentPercent;
        return totalEstimatedTime - elapsedTime;
    }

    /**
     * Formate le temps en format lisible
     */
    private String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        if (seconds < 60) {
            return seconds + "s";
        } else {
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;
            return minutes + "m " + remainingSeconds + "s";
        }
    }

    /**
     * Envoie la progression à tous les joueurs
     */
    private void sendProgressToPlayers(String message) {
        try {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Title.sendActionBar(player, message);
            }
        } catch (Exception e) {
            // En cas d'erreur lors de l'envoi, ne pas arrêter la prégénération
            e.printStackTrace();
        }
    }

    /**
     * Log la progression pour le serveur
     */
    private void logProgress(int percent, long chunksLoaded) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        double chunksPerSecond = chunksProcessedInCurrentSecond;

        getLogger().info(String.format(
                "Pregeneration: %d%% (%d/%d chunks) - %.1f chunks/s - Elapsed: %s",
                percent, chunksLoaded, totalChunkToLoad, chunksPerSecond, formatTime(elapsedTime)));
    }

    /**
     * Finalise la prégénération
     */
    private void completePregeneration() {
        try {
            // Message final aux joueurs
            String completionMessage = ChatColor.GREEN + "✓ Prégénération terminée ! " +
                    ChatColor.GRAY + "(" + formatTime(System.currentTimeMillis() - startTime) + ")";

            for (Player player : Bukkit.getOnlinePlayers()) {
                Title.sendActionBar(player, completionMessage);

                // Message dans le chat pour les hosts
                if (API.getAPI().getGameManager().hasHostAccess(player)) {
                    player.sendMessage("");
                    player.sendMessage("§a§l✓ PRÉGÉNÉRATION TERMINÉE");
                    player.sendMessage("§fTemps total: §e" + formatTime(System.currentTimeMillis() - startTime));
                    player.sendMessage("§fChunks chargés: §e" + totalChunkToLoad);
                    player.sendMessage("§fLa partie peut maintenant commencer !");
                    player.sendMessage("");
                }
            }

            // Log final pour le serveur
            getLogger().info(String.format(
                    "Pregeneration completed! %d chunks loaded in %s",
                    totalChunkToLoad, formatTime(System.currentTimeMillis() - startTime)));

            // Mettre à jour l'état du jeu
            API.getAPI().getGameManager().setGameState(GameState.WAITING);
            API.getAPI().getGameManager().setPreloadFinished(true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Toujours annuler la tâche
            cancel();
        }
    }

    /**
     * Méthode pour obtenir les statistiques de performance
     */
    public String getPerformanceStats() {
        long current = currentChunkLoad.get();
        int percent = (int) ((current * 100) / totalChunkToLoad);
        long elapsedTime = System.currentTimeMillis() - startTime;

        return String.format(
                "Progress: %d%% (%d/%d) | Time: %s | Speed: %.1f chunks/s",
                percent, current, totalChunkToLoad, formatTime(elapsedTime), chunksProcessedInCurrentSecond);
    }

    /**
     * Méthode pour forcer l'arrêt de la prégénération
     */
    public void forceStop() {
        finished.set(true);
        cancel();
        getLogger().info("Pregeneration force stopped at " + lastProgressPercent + "%");
    }

    /**
     * Vérifie si la prégénération est terminée
     */
    public boolean isFinished() {
        return finished.get();
    }

    /**
     * Obtient le pourcentage actuel
     */
    public int getCurrentPercent() {
        return lastProgressPercent;
    }
}
