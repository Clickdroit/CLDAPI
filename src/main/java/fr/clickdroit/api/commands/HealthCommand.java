package fr.clickdroit.api.commands;

import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.HealthUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealthCommand implements CommandExecutor {
    private final GameManager gameManager;

    public HealthCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (!(commandSender instanceof Player))
            return true;
        Player player = (Player)commandSender;
        if (this.gameManager.hasHostAccess(player)) {
            if (arguments.length >= 3) {
                String target = arguments[0];
                String action = arguments[1];
                int amount = Integer.parseInt(arguments[2]);
                try {
                    Player targetPlayer = Bukkit.getPlayer(target);
                    if (targetPlayer == null) {
                        player.sendMessage("joueur avec le pseudo '" + arguments[0] + "' n'a trouv");
                    } else if (action.equals("add")) {
                        HealthUtils.addPermanentHeart(targetPlayer, amount);
                        player.sendMessage("avez donn" + amount + "" + targetPlayer.getName() + ".");
                    } else if (action.equals("remove")) {
                        HealthUtils.removePermanentHeart(targetPlayer, amount);
                        player.sendMessage("avez retir" + amount + "" + targetPlayer.getName() + ".");
                    } else {
                        sendHelp(player);
                    }
                } catch (NumberFormatException exception) {
                    player.sendMessage(exception.getMessage());
                }
            } else {
                sendHelp(player);
            }
        } else {
            player.sendMessage("insuffisante.");
        }
        return false;
    }

    public void sendHelp(Player sender) {
        sender.sendMessage("de syntaxe: /health <joueur> <add:remove> <quantit");
    }
}
