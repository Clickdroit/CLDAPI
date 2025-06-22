package fr.clickdroit.api.commands;

import fr.clickdroit.api.game.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WhitelistCommand implements CommandExecutor {
    private final GameManager gameManager;

    public WhitelistCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments) {
        if (!(sender instanceof Player))
            return true;
        Player player = (Player)sender;
        if (!this.gameManager.hasHostAccess(player)) {
            sender.sendMessage("insuffisante.");
            return true;
        }
        if (arguments.length == 0) {
            sendHelp(player);
        } else {
            switch (arguments[0]) {
                case "add":
                    if (arguments.length > 1) {
                        String target = arguments[1];
                        if (!this.gameManager.getWhitelistedPlayers().contains(target)) {
                            this.gameManager.getWhitelistedPlayers().add(target);
                            player.sendMessage("avez ajout"+ target + " la liste blanche.");
                            break;
                        }
                        player.sendMessage(""+ target + " est dprdans la liste blanche.");
                        break;
                    }
                    sendHelp(player);
                    break;
                case "remove":
                    if (arguments.length > 1) {
                        String target = arguments[1];
                        if (this.gameManager.getWhitelistedPlayers().contains(target)) {
                            this.gameManager.getWhitelistedPlayers().remove(target);
                            player.sendMessage("avez retir" + target + " la liste blanche.");
                            break;
                        }
                        player.sendMessage(""+ target + " ne fait pas parti de la liste blanche.");
                        break;
                    }
                    sendHelp(player);
                    break;
                case "list":
                case "liste":
                    player.sendMessage("la liste des joueurs prdans la liste blanche :");
                    for (String string : this.gameManager.getWhitelistedPlayers())
                        player.sendMessage(""+ string);
                    break;
                case "clear":
                    this.gameManager.getWhitelistedPlayers().clear();
                    player.sendMessage("avez retirtous les joueurs de la liste blanche.");
                    break;
            }
        }
        return true;
    }

    private final void sendHelp(Player sender) {
        sender.sendMessage("");
        sender.sendMessage("de syntaxe, voici de l'aide :");
        sender.sendMessage("<add/remove/list/clear> [joueur]");
        sender.sendMessage("");
    }
}
