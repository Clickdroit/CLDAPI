package fr.clickdroit.api.registry;

import fr.clickdroit.api.API;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import java.util.*;
import java.util.logging.Logger;

/**
 * Registre automatique des commandes UHC.
 * Centralise l'enregistrement et la gestion des commandes.
 */
public class CommandRegistry {

    private final API api;
    private final Logger logger;
    private final Map<String, UHCCommand> registeredCommands = new LinkedHashMap<>();

    public CommandRegistry(API api) {
        this.api = api;
        this.logger = api.getLogger();
    }

    /**
     * Enregistre une commande UHC.
     *
     * @param command la commande à enregistrer
     * @return true si l'enregistrement a réussi
     */
    public boolean register(UHCCommand command) {
        String name = command.getName().toLowerCase();

        if (registeredCommands.containsKey(name)) {
            logger.warning("Commande déjà enregistrée: " + name);
            return false;
        }

        PluginCommand pluginCommand = api.getCommand(name);
        if (pluginCommand == null) {
            logger.warning("Commande non trouvée dans plugin.yml: " + name);
            return false;
        }

        pluginCommand.setExecutor(command);

        if (command instanceof TabCompleter) {
            pluginCommand.setTabCompleter((TabCompleter) command);
        }

        if (command.getPermission() != null) {
            pluginCommand.setPermission(command.getPermission());
        }

        if (command.getDescription() != null) {
            pluginCommand.setDescription(command.getDescription());
        }

        if (command.getUsage() != null) {
            pluginCommand.setUsage(command.getUsage());
        }

        if (command.getAliases() != null && command.getAliases().length > 0) {
            pluginCommand.setAliases(Arrays.asList(command.getAliases()));
        }

        registeredCommands.put(name, command);
        logger.info("Commande enregistrée: /" + name);

        return true;
    }

    /**
     * Enregistre plusieurs commandes.
     *
     * @param commands les commandes à enregistrer
     */
    public void registerAll(UHCCommand... commands) {
        for (UHCCommand command : commands) {
            register(command);
        }
    }

    /**
     * Désenregistre une commande.
     *
     * @param name le nom de la commande
     * @return la commande désenregistrée, ou null
     */
    public UHCCommand unregister(String name) {
        name = name.toLowerCase();
        UHCCommand command = registeredCommands.remove(name);

        if (command != null) {
            PluginCommand pluginCommand = api.getCommand(name);
            if (pluginCommand != null) {
                pluginCommand.setExecutor(null);
                pluginCommand.setTabCompleter(null);
            }
            logger.info("Commande désenregistrée: /" + name);
        }

        return command;
    }

    /**
     * Récupère une commande par son nom.
     *
     * @param name le nom de la commande
     * @return la commande, ou null
     */
    public UHCCommand getCommand(String name) {
        return registeredCommands.get(name.toLowerCase());
    }

    /**
     * Vérifie si une commande est enregistrée.
     *
     * @param name le nom de la commande
     * @return true si la commande est enregistrée
     */
    public boolean isRegistered(String name) {
        return registeredCommands.containsKey(name.toLowerCase());
    }

    /**
     * Retourne toutes les commandes enregistrées.
     *
     * @return une collection des commandes
     */
    public Collection<UHCCommand> getAllCommands() {
        return Collections.unmodifiableCollection(registeredCommands.values());
    }

    /**
     * Retourne le nombre de commandes enregistrées.
     *
     * @return le nombre de commandes
     */
    public int getCommandCount() {
        return registeredCommands.size();
    }

    /**
     * Retourne les noms de toutes les commandes enregistrées.
     *
     * @return un set des noms de commandes
     */
    public Set<String> getCommandNames() {
        return Collections.unmodifiableSet(registeredCommands.keySet());
    }
}

