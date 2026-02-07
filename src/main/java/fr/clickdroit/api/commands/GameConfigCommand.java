package fr.clickdroit.api.commands;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.GameConfigPersistence;
import fr.clickdroit.api.game.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Commande pour gérer la configuration de partie persistante.
 * 
 * <p>Usage:</p>
 * <ul>
 *   <li>/gameconfig save [nom] - Sauvegarder la configuration actuelle</li>
 *   <li>/gameconfig load [nom] - Charger une configuration</li>
 *   <li>/gameconfig list - Lister les configurations disponibles</li>
 *   <li>/gameconfig delete [nom] - Supprimer une configuration</li>
 * </ul>
 * 
 * <p>Les noms de configuration ne peuvent contenir que des lettres (a-z, A-Z),
 * des chiffres (0-9), des tirets (-) et des underscores (_).</p>
 * 
 * @author Clickdroit
 * @version 1.0
 */
public class GameConfigCommand implements CommandExecutor, TabCompleter {
    
    /** Pattern regex pour valider les noms de configuration */
    private static final String CONFIG_NAME_PATTERN = "^[a-zA-Z0-9_-]+$";
    
    private final GameManager gameManager;
    private final GameConfigPersistence persistence;
    
    public GameConfigCommand(API api) {
        this.gameManager = api.getGameManager();
        this.persistence = new GameConfigPersistence(api);
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCette commande ne peut être exécutée que par un joueur.");
            return true;
        }
        
        Player player = (Player) sender;
        
        // Vérifier les permissions
        if (!gameManager.hasHostAccess(player)) {
            player.sendMessage("§cPermission insuffisante.");
            return true;
        }
        
        if (args.length == 0) {
            sendHelp(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "save":
                handleSave(player, args);
                break;
            case "load":
                handleLoad(player, args);
                break;
            case "list":
                handleList(player);
                break;
            case "delete":
                handleDelete(player, args);
                break;
            default:
                sendHelp(player);
                break;
        }
        
        return true;
    }
    
    private void handleSave(Player player, String[] args) {
        String configName = args.length > 1 ? args[1] : "default";
        
        // Valider le nom
        if (!isValidConfigName(configName)) {
            player.sendMessage("§cNom de configuration invalide. Utilisez uniquement des lettres, chiffres et tirets.");
            return;
        }
        
        // Vérifier si la config existe déjà
        if (persistence.configExists(configName)) {
            player.sendMessage("§eUne configuration avec ce nom existe déjà. Elle sera écrasée.");
        }
        
        if (persistence.saveConfig(gameManager.getGameConfig(), configName)) {
            player.sendMessage("§a✓ Configuration sauvegardée: §f" + configName);
            player.sendMessage("§7Utilisez §f/gameconfig load " + configName + "§7 pour la charger plus tard.");
        } else {
            player.sendMessage("§cErreur lors de la sauvegarde de la configuration.");
        }
    }
    
    private void handleLoad(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /gameconfig load <nom>");
            
            // Montrer les configs disponibles
            List<String> configs = persistence.listConfigs();
            if (!configs.isEmpty()) {
                player.sendMessage("§7Configurations disponibles: §f" + String.join(", ", configs));
            }
            return;
        }
        
        String configName = args[1];
        
        if (!persistence.configExists(configName)) {
            player.sendMessage("§cConfiguration non trouvée: " + configName);
            
            List<String> configs = persistence.listConfigs();
            if (!configs.isEmpty()) {
                player.sendMessage("§7Configurations disponibles: §f" + String.join(", ", configs));
            }
            return;
        }
        
        if (persistence.loadConfig(gameManager.getGameConfig(), configName)) {
            player.sendMessage("§a✓ Configuration chargée: §f" + configName);
            player.sendMessage("§7Les paramètres de la partie ont été mis à jour.");
        } else {
            player.sendMessage("§cErreur lors du chargement de la configuration.");
        }
    }
    
    private void handleList(Player player) {
        List<String> configs = persistence.listConfigs();
        
        if (configs.isEmpty()) {
            player.sendMessage("§7Aucune configuration sauvegardée.");
            player.sendMessage("§7Utilisez §f/gameconfig save <nom>§7 pour créer une configuration.");
            return;
        }
        
        player.sendMessage("§6§l=== Configurations sauvegardées ===");
        for (String config : configs) {
            player.sendMessage("§8- §f" + config);
        }
        player.sendMessage("§7Total: §f" + configs.size() + " configuration(s)");
    }
    
    private void handleDelete(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage("§cUsage: /gameconfig delete <nom>");
            return;
        }
        
        String configName = args[1];
        
        if (!persistence.configExists(configName)) {
            player.sendMessage("§cConfiguration non trouvée: " + configName);
            return;
        }
        
        if (persistence.deleteConfig(configName)) {
            player.sendMessage("§a✓ Configuration supprimée: §f" + configName);
        } else {
            player.sendMessage("§cErreur lors de la suppression de la configuration.");
        }
    }
    
    private void sendHelp(Player player) {
        player.sendMessage("§6§l=== Gestion des configurations ===");
        player.sendMessage("§e/gameconfig save [nom] §7- Sauvegarder la config actuelle");
        player.sendMessage("§e/gameconfig load <nom> §7- Charger une configuration");
        player.sendMessage("§e/gameconfig list §7- Lister les configurations");
        player.sendMessage("§e/gameconfig delete <nom> §7- Supprimer une configuration");
    }
    
    /**
     * Vérifie si un nom de configuration est valide.
     * Les noms valides ne peuvent contenir que des lettres, chiffres, tirets et underscores.
     *
     * @param name le nom à vérifier
     * @return true si le nom est valide, false sinon
     */
    private boolean isValidConfigName(String name) {
        return name != null && name.matches(CONFIG_NAME_PATTERN);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return new ArrayList<>();
        }
        
        Player player = (Player) sender;
        if (!gameManager.hasHostAccess(player)) {
            return new ArrayList<>();
        }
        
        if (args.length == 1) {
            return Arrays.asList("save", "load", "list", "delete").stream()
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("load") || subCommand.equals("delete")) {
                return persistence.listConfigs().stream()
                        .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            }
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Retourne le service de persistance.
     *
     * @return le service de persistance
     */
    public GameConfigPersistence getPersistence() {
        return persistence;
    }
}
