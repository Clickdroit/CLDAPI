package fr.clickdroit.api.common.scoreboard;

import fr.clickdroit.api.common.scoreboard.blink.BlinkEffect;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardUpdateTask extends BukkitRunnable {
    private final ScoreboardManager scoreboardManager;
    private final BlinkEffect blinkEffect;

    // Cache des joueurs et leurs boards pour éviter les recherches répétées
    private final Map<UUID, BPlayerBoard> playerBoardCache = new ConcurrentHashMap<>();

    // Cache du contenu de scoreboard pour éviter les appels getter répétés
    private ScoreboardContents cachedScoreboardContents;
    private long lastContentRefresh = 0;
    private static final long CONTENT_CACHE_TIME = 1000; // 1 seconde en millisecondes

    // Cache des joueurs pour éviter les allocations répétées de Collection
    private final Set<Player> playersCache = new HashSet<>();
    private long lastPlayersCacheUpdate = 0;
    private static final long PLAYERS_CACHE_TIME = 100; // 100ms (5 ticks)

    // Compteur pour optimisations périodiques
    private int tickCounter = 0;
    private static final int CACHE_CLEANUP_INTERVAL = 100; // Nettoyer le cache toutes les 100 exécutions (20 secondes)

    public ScoreboardUpdateTask(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
        this.blinkEffect = new BlinkEffect();
    }

    @Override
    public void run() {
        try {
            tickCounter++;

            // Obtenir le contenu du scoreboard (avec cache)
            ScoreboardContents scoreboardContents = getCachedScoreboardContents();
            if (scoreboardContents == null) {
                return; // Éviter les NPE si le contenu n'est pas disponible
            }

            // Mettre à jour l'effet de clignotement
            blinkEffect.next();
            String blinkText = blinkEffect.getText();

            // Obtenir les joueurs (avec cache)
            Collection<Player> onlinePlayers = getCachedOnlinePlayers();

            // Traitement optimisé des joueurs
            for (Player player : onlinePlayers) {
                if (player == null || !player.isOnline()) {
                    continue; // Skip les joueurs null ou déconnectés
                }

                UUID playerId = player.getUniqueId();
                BPlayerBoard board = getCachedPlayerBoard(player, playerId);

                if (board != null) {
                    try {
                        // Reload data pour ce joueur spécifique
                        scoreboardContents.reloadData(playerId);

                        // Mettre à jour les lignes du scoreboard
                        scoreboardContents.setLines(board, playerId, blinkText);
                    } catch (Exception e) {
                        // Log silencieux pour éviter le spam en cas d'erreur répétée
                        // On pourrait ajouter un système de log avec throttling ici
                    }
                }
            }

            // Nettoyage périodique des caches
            if (tickCounter % CACHE_CLEANUP_INTERVAL == 0) {
                cleanupCaches();
            }

        } catch (Exception e) {
            // Gestion d'erreur globale pour éviter que la task se stop
            e.printStackTrace();
        }
    }

    /**
     * Obtient le contenu du scoreboard avec mise en cache
     */
    private ScoreboardContents getCachedScoreboardContents() {
        long currentTime = System.currentTimeMillis();

        if (cachedScoreboardContents == null ||
                (currentTime - lastContentRefresh) > CONTENT_CACHE_TIME) {

            try {
                cachedScoreboardContents = scoreboardManager.getScoreboardContents().get();
                lastContentRefresh = currentTime;
            } catch (Exception e) {
                // En cas d'erreur, garder l'ancienne valeur si elle existe
                return cachedScoreboardContents;
            }
        }

        return cachedScoreboardContents;
    }

    /**
     * Obtient la liste des joueurs en ligne avec mise en cache
     */
    private Collection<Player> getCachedOnlinePlayers() {
        long currentTime = System.currentTimeMillis();

        if ((currentTime - lastPlayersCacheUpdate) > PLAYERS_CACHE_TIME) {
            playersCache.clear();

            // Utiliser une approche plus efficace pour obtenir les joueurs
            Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
            playersCache.addAll(onlinePlayers);

            lastPlayersCacheUpdate = currentTime;
        }

        return playersCache;
    }

    /**
     * Obtient le board d'un joueur avec mise en cache
     */
    private BPlayerBoard getCachedPlayerBoard(Player player, UUID playerId) {
        BPlayerBoard board = playerBoardCache.get(playerId);

        if (board == null) {
            try {
                board = scoreboardManager.getNetherboard().getBoard(player);
                if (board != null) {
                    playerBoardCache.put(playerId, board);
                }
            } catch (Exception e) {
                // En cas d'erreur, retourner null
                return null;
            }
        }

        return board;
    }

    /**
     * Nettoie les caches des joueurs déconnectés
     */
    private void cleanupCaches() {
        try {
            // Obtenir les UUIDs des joueurs actuellement en ligne
            Set<UUID> onlinePlayerIds = new HashSet<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                onlinePlayerIds.add(player.getUniqueId());
            }

            // Supprimer les entrées du cache pour les joueurs déconnectés
            playerBoardCache.entrySet().removeIf(entry ->
                    !onlinePlayerIds.contains(entry.getKey()));

            // Nettoyer le cache des joueurs si nécessaire
            playersCache.removeIf(player -> !player.isOnline());

        } catch (Exception e) {
            // En cas d'erreur pendant le nettoyage, ne pas arrêter la task
            e.printStackTrace();
        }
    }

    /**
     * Méthode pour forcer le rafraîchissement du cache du contenu
     * Utile quand on change de module de jeu par exemple
     */
    public void forceContentRefresh() {
        cachedScoreboardContents = null;
        lastContentRefresh = 0;
    }

    /**
     * Méthode pour invalider le cache d'un joueur spécifique
     * Utile quand un joueur se reconnecte
     */
    public void invalidatePlayerCache(UUID playerId) {
        playerBoardCache.remove(playerId);
    }

    /**
     * Méthode pour nettoyer manuellement tous les caches
     */
    public void clearAllCaches() {
        playerBoardCache.clear();
        playersCache.clear();
        cachedScoreboardContents = null;
        lastContentRefresh = 0;
        lastPlayersCacheUpdate = 0;
    }

    /**
     * Statistiques pour debug/monitoring
     */
    public Map<String, Integer> getCacheStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("playerBoardCache_size", playerBoardCache.size());
        stats.put("playersCache_size", playersCache.size());
        stats.put("tickCounter", tickCounter);
        return stats;
    }
}
