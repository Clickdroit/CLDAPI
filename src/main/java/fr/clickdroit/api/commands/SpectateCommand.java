package fr.clickdroit.api.commands;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameState;
import fr.clickdroit.api.game.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Commande pour passer en mode spectateur et téléporter vers un joueur.
 * 
 * <p>Usage:</p>
 * <ul>
 *   <li>/spectate - Passer en mode spectateur (ou en sortir)</li>
 *   <li>/spectate [joueur] - Se téléporter vers un joueur (en mode spec)</li>
 *   <li>/spectate list - Lister les joueurs en vie</li>
 * </ul>
 * 
 * @author Clickdroit
 * @version 1.0
 */
public class SpectateCommand implements CommandExecutor, TabCompleter {
    
    /** Diviseur pour convertir les points de vie en cœurs (2 HP = 1 cœur) */
    private static final double HEALTH_TO_HEARTS_DIVISOR = 2.0;
    
    private final API api;
    private final GameManager gameManager;
    
    public SpectateCommand(API api) {
        this.api = api;
        this.gameManager = api.getGameManager();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cCette commande ne peut être exécutée que par un joueur.");
            return true;
        }
        
        Player player = (Player) sender;
        GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
        
        // Vérifier si la partie est en cours
        if (!GameUtils.isGameStarted()) {
            player.sendMessage("§cLa partie n'a pas encore commencé.");
            return true;
        }
        
        // Vérifier les permissions
        boolean hasHostAccess = gameManager.hasHostAccess(player);
        boolean isAlive = gamePlayer != null && gamePlayer.isAlive();
        boolean isInGame = gameManager.getInGamePlayers().contains(player.getUniqueId());
        
        // Les joueurs en vie ne peuvent pas utiliser cette commande (sauf les hosts)
        if (isAlive && isInGame && !hasHostAccess) {
            player.sendMessage("§cVous ne pouvez pas utiliser cette commande tant que vous êtes en vie.");
            return true;
        }
        
        // Traiter les sous-commandes
        if (args.length == 0) {
            // Toggle mode spectateur
            if (hasHostAccess) {
                toggleSpectatorMode(player, gamePlayer);
            } else {
                // Joueurs morts: montrer la liste des joueurs
                showAlivePlayersList(player);
            }
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        if (subCommand.equals("list")) {
            showAlivePlayersList(player);
            return true;
        }
        
        // Téléportation vers un joueur
        teleportToPlayer(player, args[0], hasHostAccess);
        
        return true;
    }
    
    /**
     * Bascule le mode spectateur pour un host.
     */
    private void toggleSpectatorMode(Player player, GamePlayer gamePlayer) {
        if (player.getGameMode() == GameMode.SPECTATOR) {
            // Retour au mode survie
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(api.getLobbyPopulator().getLobbyLocation());
            player.sendMessage("§a✓ Vous êtes sorti du mode spectateur.");
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        } else {
            // Passer en mode spectateur
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage("§a✓ Vous êtes maintenant en mode spectateur.");
            player.sendMessage("§7Utilisez §f/spectate <joueur>§7 pour vous téléporter.");
            player.sendMessage("§7Utilisez §f/spectate list§7 pour voir les joueurs en vie.");
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }
    }
    
    /**
     * Téléporte le joueur vers un autre joueur.
     */
    private void teleportToPlayer(Player spectator, String targetName, boolean hasHostAccess) {
        Player target = Bukkit.getPlayer(targetName);
        
        if (target == null || !target.isOnline()) {
            spectator.sendMessage("§cJoueur non trouvé: " + targetName);
            return;
        }
        
        // Vérifier que le joueur cible est en vie (sauf pour les hosts)
        if (!hasHostAccess) {
            GamePlayer targetGamePlayer = GamePlayer.getPlayer(target.getUniqueId());
            if (targetGamePlayer == null || !targetGamePlayer.isAlive()) {
                spectator.sendMessage("§cCe joueur n'est plus en vie.");
                return;
            }
        }
        
        // Mettre en mode spectateur si ce n'est pas déjà le cas
        if (spectator.getGameMode() != GameMode.SPECTATOR) {
            spectator.setGameMode(GameMode.SPECTATOR);
        }
        
        // Téléporter
        spectator.teleport(target.getLocation());
        spectator.sendMessage("§a✓ Téléporté vers §f" + target.getName());
        spectator.playSound(spectator.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
    }
    
    /**
     * Affiche la liste des joueurs en vie.
     */
    private void showAlivePlayersList(Player player) {
        List<String> alivePlayers = new ArrayList<>();
        
        for (UUID uuid : gameManager.getInGamePlayers()) {
            Player p = Bukkit.getPlayer(uuid);
            GamePlayer gp = GamePlayer.getPlayer(uuid);
            
            if (p != null && p.isOnline() && gp != null && gp.isAlive()) {
                String health = String.format("%.1f", p.getHealth() / HEALTH_TO_HEARTS_DIVISOR);
                alivePlayers.add("§f" + p.getName() + " §7(§c" + health + "❤§7)");
            }
        }
        
        if (alivePlayers.isEmpty()) {
            player.sendMessage("§7Aucun joueur en vie.");
            return;
        }
        
        player.sendMessage("§6§l=== Joueurs en vie (" + alivePlayers.size() + ") ===");
        for (String line : alivePlayers) {
            player.sendMessage("§8- " + line);
        }
        player.sendMessage("§7Utilisez §f/spectate <joueur>§7 pour vous téléporter.");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) {
            return new ArrayList<>();
        }
        
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("list");
            
            // Ajouter les joueurs en vie
            for (UUID uuid : gameManager.getInGamePlayers()) {
                Player p = Bukkit.getPlayer(uuid);
                GamePlayer gp = GamePlayer.getPlayer(uuid);
                
                if (p != null && p.isOnline() && gp != null && gp.isAlive()) {
                    completions.add(p.getName());
                }
            }
            
            return completions.stream()
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
}
