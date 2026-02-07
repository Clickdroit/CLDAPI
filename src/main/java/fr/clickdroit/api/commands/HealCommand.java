package fr.clickdroit.api.commands;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Commande pour soigner les joueurs.
 * 
 * <p>Usage:</p>
 * <ul>
 *   <li>/heal - Se soigner soi-même</li>
 *   <li>/heal [joueur] - Soigner un joueur spécifique</li>
 *   <li>/heal all - Soigner tous les joueurs en vie</li>
 *   <li>/heal [joueur|all] full - Soigner complètement (vie + faim + effets)</li>
 * </ul>
 * 
 * @author Clickdroit
 * @version 1.0
 */
public class HealCommand implements CommandExecutor, TabCompleter {
    
    private final API api;
    private final GameManager gameManager;
    
    public HealCommand(API api) {
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
        
        // Vérifier les permissions
        if (!gameManager.hasHostAccess(player)) {
            player.sendMessage("§cPermission insuffisante.");
            return true;
        }
        
        // Déterminer si c'est un heal complet
        boolean fullHeal = args.length > 0 && args[args.length - 1].equalsIgnoreCase("full");
        
        // Sans arguments: se soigner soi-même
        if (args.length == 0 || (args.length == 1 && fullHeal)) {
            healPlayer(player, fullHeal);
            player.sendMessage("§a✓ Vous vous êtes soigné" + (fullHeal ? " complètement" : "") + ".");
            return true;
        }
        
        String targetArg = args[0].toLowerCase();
        
        // Soigner tous les joueurs
        if (targetArg.equals("all")) {
            healAllPlayers(player, fullHeal);
            return true;
        }
        
        // Soigner un joueur spécifique
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            player.sendMessage("§cJoueur non trouvé: " + args[0]);
            return true;
        }
        
        healPlayer(target, fullHeal);
        player.sendMessage("§a✓ Vous avez soigné §f" + target.getName() + (fullHeal ? "§a complètement" : "") + ".");
        target.sendMessage("§a✓ Vous avez été soigné par §f" + player.getName() + "§a.");
        
        return true;
    }
    
    /**
     * Soigne un joueur.
     *
     * @param player le joueur à soigner
     * @param fullHeal si true, soigne aussi la faim et retire les effets négatifs
     */
    private void healPlayer(Player player, boolean fullHeal) {
        // Restaurer la vie
        player.setHealth(player.getMaxHealth());
        
        if (fullHeal) {
            // Restaurer la faim
            player.setFoodLevel(20);
            player.setSaturation(20.0F);
            
            // Retirer les effets négatifs
            removeNegativeEffects(player);
            
            // Éteindre le feu
            player.setFireTicks(0);
        }
        
        // Effet visuel et sonore
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.0F);
    }
    
    /**
     * Retire tous les effets de potion négatifs d'un joueur.
     */
    private void removeNegativeEffects(Player player) {
        PotionEffectType[] negativeEffects = {
            PotionEffectType.POISON,
            PotionEffectType.WITHER,
            PotionEffectType.HUNGER,
            PotionEffectType.WEAKNESS,
            PotionEffectType.SLOW,
            PotionEffectType.SLOW_DIGGING,
            PotionEffectType.BLINDNESS,
            PotionEffectType.CONFUSION
        };
        
        for (PotionEffectType effectType : negativeEffects) {
            if (player.hasPotionEffect(effectType)) {
                player.removePotionEffect(effectType);
            }
        }
    }
    
    /**
     * Soigne tous les joueurs en vie.
     */
    private void healAllPlayers(Player healer, boolean fullHeal) {
        int healedCount = 0;
        
        // Si la partie est en cours, soigner uniquement les joueurs en jeu
        if (GameUtils.isGameStarted()) {
            for (UUID uuid : gameManager.getInGamePlayers()) {
                Player target = Bukkit.getPlayer(uuid);
                GamePlayer gp = GamePlayer.getPlayer(uuid);
                
                if (target != null && target.isOnline() && gp != null && gp.isAlive()) {
                    healPlayer(target, fullHeal);
                    if (!target.equals(healer)) {
                        target.sendMessage("§a✓ Vous avez été soigné par §f" + healer.getName() + "§a.");
                    }
                    healedCount++;
                }
            }
        } else {
            // Sinon, soigner tous les joueurs en ligne
            for (Player target : Bukkit.getOnlinePlayers()) {
                healPlayer(target, fullHeal);
                if (!target.equals(healer)) {
                    target.sendMessage("§a✓ Vous avez été soigné par §f" + healer.getName() + "§a.");
                }
                healedCount++;
            }
        }
        
        healer.sendMessage("§a✓ Vous avez soigné §f" + healedCount + " joueur(s)" + (fullHeal ? " complètement" : "") + ".");
        
        // Notification broadcast
        if (GameUtils.isGameStarted()) {
            gameManager.broadcast("§6§l[!] §eTous les joueurs ont été soignés par §f" + healer.getName() + "§e.");
        }
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
            List<String> completions = new ArrayList<>();
            completions.add("all");
            completions.add("full");
            
            // Ajouter les noms des joueurs en ligne
            for (Player p : Bukkit.getOnlinePlayers()) {
                completions.add(p.getName());
            }
            
            return completions.stream()
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        if (args.length == 2) {
            return Arrays.asList("full").stream()
                    .filter(s -> s.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
}
