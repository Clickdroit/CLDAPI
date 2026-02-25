package fr.clickdroit.api.module;

import fr.clickdroit.api.API;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

/**
 * Interface pour les modules de jeu externes.
 * <p>
 * Cette interface permet aux plugins externes d'enregistrer leurs propres modes de jeu
 * dans l'API UHC. Un module de jeu personnalisé doit implémenter cette interface
 * et s'enregistrer auprès du {@link GameModuleRegistry}.
 * </p>
 * 
 * <h3>Exemple d'utilisation dans un plugin externe :</h3>
 * <pre>{@code
 * public class MonPluginModule extends JavaPlugin {
 *     @Override
 *     public void onEnable() {
 *         // Attendre que CLDAPI soit chargé
 *         API api = API.getAPI();
 *         if (api != null) {
 *             // Créer et enregistrer le module
 *             GameModule monModule = new MonGameModule(this);
 *             api.getModuleRegistry().registerModule(monModule);
 *         }
 *     }
 * }
 * }</pre>
 * 
 * @author Clickdroit
 * @version 1.0
 * @see GameModuleRegistry
 * @see Modules
 */
public interface GameModule {

    /**
     * Retourne l'identifiant unique du module.
     * Cet identifiant doit être unique parmi tous les modules enregistrés.
     * 
     * @return l'identifiant unique du module (ex: "DEMON_SLAYER", "NARUTO_UHC")
     */
    String getId();

    /**
     * Retourne le nom affiché du module.
     * Ce nom sera affiché dans les GUIs et menus de sélection.
     * 
     * @return le nom d'affichage du module (ex: "Demon Slayer", "Naruto UHC")
     */
    String getDisplayName();

    /**
     * Retourne la couleur associée au module (code couleur Minecraft).
     * 
     * @return le code couleur (ex: "§c" pour rouge, "§6" pour orange)
     */
    String getColor();

    /**
     * Retourne le matériau de l'icône pour les GUIs.
     * 
     * @return le matériau de l'icône
     */
    Material getIconMaterial();

    /**
     * Retourne la durabilité/data de l'icône (pour les variantes de blocs).
     * 
     * @return la durabilité de l'icône (0 par défaut)
     */
    default int getIconData() {
        return 0;
    }

    /**
     * Indique si ce module utilise un système de rôles.
     * 
     * @return true si le module a des rôles, false sinon
     */
    boolean hasRoles();

    /**
     * Indique si ce module utilise le système d'équipes.
     * 
     * @return true si le module utilise les équipes, false sinon
     */
    boolean hasTeams();

    /**
     * Indique si le spawn doit être supprimé au lancement.
     * 
     * @return true si le spawn doit être supprimé
     */
    default boolean shouldDeleteSpawn() {
        return false;
    }

    /**
     * Retourne le plugin propriétaire de ce module.
     * 
     * @return le plugin propriétaire
     */
    JavaPlugin getOwnerPlugin();

    /**
     * Retourne la description du module pour les GUIs.
     * 
     * @return un tableau de lignes de description
     */
    default String[] getDescription() {
        return new String[] {
            "§7Mode de jeu personnalisé",
            "§7fourni par §e" + getOwnerPlugin().getName()
        };
    }

    /**
     * Appelé lorsque le module est chargé pour la première fois.
     * Utilisez cette méthode pour initialiser vos composants.
     */
    void onLoad();

    /**
     * Appelé lorsque le module est sélectionné et activé.
     * 
     * @param api l'instance de l'API
     */
    void onEnable(API api);

    /**
     * Appelé lorsque le module est désactivé.
     * 
     * @param api l'instance de l'API
     */
    void onDisable(API api);

    /**
     * Appelé au démarrage de la partie.
     * 
     * @param api l'instance de l'API
     */
    void onGameStart(API api);

    /**
     * Appelé lorsqu'un joueur meurt.
     * 
     * @param player le joueur qui est mort
     * @param killer le tueur (peut être null)
     */
    void onPlayerDeath(Player player, Player killer);

    /**
     * Appelé lorsqu'un joueur meurt par déconnexion.
     * 
     * @param uuid l'UUID du joueur
     */
    void onPlayerDeathByDisconnect(UUID uuid);

    /**
     * Appelé à chaque changement d'épisode.
     */
    void onEpisodeSwitch();

    /**
     * Appelé à chaque tick de l'horloge de jeu.
     * 
     * @param gameTime le temps de jeu actuel en secondes
     */
    void onClockUpdate(int gameTime);

    /**
     * Appelé lorsqu'un joueur se reconnecte.
     * 
     * @param player le joueur qui se reconnecte
     */
    void onPlayerReconnect(Player player);

    /**
     * Appelé lorsqu'un joueur se déconnecte.
     * 
     * @param player le joueur qui se déconnecte
     */
    void onPlayerDisconnect(Player player);

    /**
     * Appelé lorsqu'un joueur envoie un message dans le chat.
     * 
     * @param player le joueur qui envoie le message
     * @param message le message envoyé
     */
    void onPlayerChat(Player player, String message);

    /**
     * Appelé lorsque le jour se lève.
     * 
     * @param sendMessage true si un message doit être envoyé aux joueurs
     */
    default void onDay(boolean sendMessage) {}

    /**
     * Appelé lorsque la nuit tombe.
     * 
     * @param sendMessage true si un message doit être envoyé aux joueurs
     */
    default void onNight(boolean sendMessage) {}

    /**
     * Ouvre le GUI de configuration du module pour un joueur.
     * 
     * @param player le joueur qui ouvre la configuration
     */
    default void openConfig(Player player) {
        player.sendMessage("§cAucune configuration disponible pour ce mode.");
    }

    /**
     * Crée l'ItemStack représentant ce module dans les GUIs.
     * 
     * @return l'ItemStack de l'icône
     */
    default ItemStack createIcon() {
        fr.clickdroit.api.utils.item.ItemCreator creator = new fr.clickdroit.api.utils.item.ItemCreator(getIconMaterial())
                .setDurability(getIconData())
                .setName(getColor() + getDisplayName())
                .addLore("");
        
        // Ajouter chaque ligne de description séparément
        for (String line : getDescription()) {
            creator.addLore("§7" + line);
        }
        
        creator.addLore("")
                .addLore("§eCliquez pour sélectionner");
        
        return creator.getItem();
    }
}
