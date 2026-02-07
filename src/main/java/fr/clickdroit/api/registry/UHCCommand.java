package fr.clickdroit.api.registry;

import org.bukkit.command.CommandExecutor;

/**
 * Interface pour les commandes UHC enregistrables dans le CommandRegistry.
 */
public interface UHCCommand extends CommandExecutor {

    /**
     * Retourne le nom de la commande (sans le /).
     *
     * @return le nom de la commande
     */
    String getName();

    /**
     * Retourne la permission requise pour exécuter la commande.
     *
     * @return la permission, ou null si aucune permission requise
     */
    default String getPermission() {
        return null;
    }

    /**
     * Retourne les alias de la commande.
     *
     * @return un tableau d'alias, ou null
     */
    default String[] getAliases() {
        return null;
    }

    /**
     * Retourne la description de la commande.
     *
     * @return la description, ou null
     */
    default String getDescription() {
        return null;
    }

    /**
     * Retourne l'usage de la commande.
     *
     * @return l'usage, ou null
     */
    default String getUsage() {
        return null;
    }

    /**
     * Indique si la commande ne peut être exécutée que par un joueur.
     *
     * @return true si réservée aux joueurs
     */
    default boolean isPlayerOnly() {
        return true;
    }

    /**
     * Indique si la commande nécessite d'être host.
     *
     * @return true si réservée aux hosts
     */
    default boolean requiresHost() {
        return false;
    }
}

