package fr.clickdroit.api.module.external;

import fr.clickdroit.api.API;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Interface pour les modules de jeu externes.
 * Les plugins externes implémentent cette interface pour créer des modes de jeu
 * personnalisés.
 * 
 * <p>
 * Exemple d'utilisation :
 * </p>
 * 
 * <pre>
 * {@code
 * public class MyGameModule implements GameModule {
 *     &#64;Override public String getId() { return "MY_MODE"; }
 *     @Override public void onGameStart(API api) { // ... }
 *     // ... autres méthodes
 * }
 * }
 * </pre>
 * 
 * @author Clickdroit
 * @version 1.0
 */
public interface GameModule {

    /**
     * Identifiant unique du module.
     * Doit être unique parmi tous les modules enregistrés.
     * Convention : UPPERCASE avec underscores (ex: "SOLO_LEVELING")
     * 
     * @return l'identifiant unique
     */
    String getId();

    /**
     * Nom d'affichage du module dans les GUIs.
     * 
     * @return le nom d'affichage
     */
    String getName();

    /**
     * Code couleur Minecraft pour l'affichage (ex: "§5" pour violet).
     * 
     * @return le code couleur
     */
    String getColorCode();

    /**
     * Icône Material à afficher dans le GUI de sélection.
     * 
     * @return le Material de l'icône
     */
    Material getIcon();

    /**
     * Indique si ce mode de jeu utilise des rôles.
     * 
     * @return true si le mode a des rôles
     */
    boolean hasRoles();

    /**
     * Indique si ce mode de jeu utilise des équipes.
     * 
     * @return true si le mode a des équipes
     */
    boolean hasTeams();

    /**
     * Indique si le spawn doit être supprimé au démarrage.
     * 
     * @return true si le spawn doit être supprimé
     */
    boolean deleteSpawn();

    // ========================================
    // LIFECYCLE METHODS
    // ========================================

    /**
     * Appelé lors du chargement du module.
     */
    default void onLoad() {
    }

    /**
     * Appelé lors de l'initialisation du module.
     */
    default void init() {
    }

    /**
     * Appelé au démarrage de la partie.
     * 
     * @param api l'instance de l'API
     */
    void onGameStart(API api);

    /**
     * Appelé lors de la mort d'un joueur.
     * 
     * @param player le joueur mort
     * @param killer le tueur (peut être null)
     */
    void onPlayerDeath(Player player, Player killer);

    /**
     * Appelé lorsqu'un joueur meurt par déconnexion.
     * 
     * @param uuid l'UUID du joueur déconnecté
     */
    void onPlayerDisconnect(UUID uuid);

    /**
     * Appelé à chaque tick du jeu (chaque seconde).
     * 
     * @param gameTime le temps de jeu en secondes
     */
    void onClockUpdate(int gameTime);

    /**
     * Appelé au lever du jour.
     * 
     * @param sendMessage true si un message doit être envoyé
     */
    default void onDay(boolean sendMessage) {
    }

    /**
     * Appelé à la tombée de la nuit.
     * 
     * @param sendMessage true si un message doit être envoyé
     */
    default void onNight(boolean sendMessage) {
    }

    /**
     * Appelé lors du changement d'épisode.
     */
    default void onEpisodeSwitch() {
    }

    /**
     * Appelé lors de la reconnexion d'un joueur.
     * 
     * @param player le joueur reconnecté
     */
    void onPlayerReconnect(Player player);

    /**
     * Appelé lors d'un message de chat d'un joueur.
     * 
     * @param player  le joueur
     * @param message le message
     */
    void onPlayerChat(Player player, String message);

    /**
     * Ouvre le GUI de configuration du module pour un joueur.
     * 
     * @param player le joueur
     */
    void openConfig(Player player);
}
