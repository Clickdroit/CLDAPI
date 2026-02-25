package fr.clickdroit.api.listener;

import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.config.common.GameAccess;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

/**
 * Listener qui gère l'accès au serveur basé sur le GameAccess
 * Fonctionne comme une whitelist automatique
 */
public class GameAccessListener implements Listener {

    private final GameManager gameManager;

    public GameAccessListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();

        // Si l'événement est déjà annulé, ne rien faire
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        // Vérifier si la partie est fermée
        if (gameManager.getGameConfig().getGameAccess() == GameAccess.CLOSE) {

            // Permettre aux OPs de toujours rejoindre
            if (player.isOp()) {
                return;
            }

            // Permettre aux hosts de toujours rejoindre
            if (gameManager.hasHostAccess(player)) {
                return;
            }

            // Permettre aux joueurs whitelistés de rejoindre
            if (gameManager.getWhitelistedPlayers().contains(player.getName())) {
                return;
            }

            // Dans certains états de jeu, permettre aux joueurs déjà en partie
            GameState state = gameManager.getGameState();
            if (state == GameState.PLAYING || state == GameState.TELEPORTATION) {
                // Permettre aux joueurs qui étaient déjà dans la partie
                if (gameManager.getInGamePlayers().contains(player.getUniqueId()) ||
                        gameManager.getPlayedPlayers().contains(player.getUniqueId())) {
                    return;
                }
            }

            // Bloquer tous les autres joueurs
            event.disallow(
                    PlayerLoginEvent.Result.KICK_WHITELIST,
                    "§c§lAccès refusé !\n\n" +
                            "§fLa partie est actuellement §cfermée§f.\n" +
                            "§fSeuls les §6administrateurs§f et les joueurs\n" +
                            "§fwhitelistés peuvent rejoindre.\n\n" +
                            "§eContactez un administrateur pour plus d'informations."
            );
            return;
        }

        // Si la partie est ouverte, vérifier les autres conditions existantes
        // (slots pleins, spectateurs désactivés pendant le jeu, etc.)
        checkOtherConditions(event);
    }

    /**
     * Vérifie les autres conditions de connexion (slots pleins, etc.)
     */
    private void checkOtherConditions(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        GameState state = gameManager.getGameState();

        // Vérifier si le serveur est plein (pour les non-OPs)
        if (!player.isOp() &&
                !state.equals(GameState.PLAYING) &&
                !state.equals(GameState.FINISH)) {

            int currentPlayers = org.bukkit.Bukkit.getOnlinePlayers().size();
            int maxSlots = gameManager.getGameConfig().getGameSlot();

            if (currentPlayers >= maxSlots) {
                event.disallow(
                        PlayerLoginEvent.Result.KICK_FULL,
                        "§c§lServeur plein !\n\n" +
                                "§fLe serveur a atteint sa capacité maximale de §e" + maxSlots + " §fjoueurs.\n" +
                                "§fVeuillez réessayer plus tard."
                );
                return;
            }
        }

        // Vérifier si on peut rejoindre pendant la téléportation
        if (state == GameState.TELEPORTATION) {
            event.disallow(
                    PlayerLoginEvent.Result.KICK_OTHER,
                    "§c§lPartie en cours de démarrage !\n\n" +
                            "§fLa partie est en cours de téléportation.\n" +
                            "§fVous ne pouvez pas rejoindre maintenant.\n\n" +
                            "§eVeuillez attendre la fin du démarrage."
            );
            return;
        }

        // Vérifier les spectateurs pendant la partie
        if ((state == GameState.PLAYING || state == GameState.FINISH) &&
                !gameManager.getGameConfig().isSpectators() &&
                !gameManager.getInGamePlayers().contains(player.getUniqueId()) &&
                !player.isOp()) {

            event.disallow(
                    PlayerLoginEvent.Result.KICK_OTHER,
                    "§c§lSpectateurs désactivés !\n\n" +
                            "§fLes spectateurs sont désactivés durant cette partie.\n" +
                            "§fSeuls les joueurs en jeu peuvent se reconnecter."
            );
            return;
        }
    }
}
