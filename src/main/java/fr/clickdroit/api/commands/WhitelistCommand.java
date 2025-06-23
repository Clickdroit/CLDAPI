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
            sender.sendMessage("§cPermission insuffisante.");
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
                            player.sendMessage("§aVous avez ajouté §f"+ target + " §aà la liste blanche.");
                            break;
                        }
                        player.sendMessage("§a"+ target + " est déjà présent dans la liste blanche.");
                        break;
                    }
                    sendHelp(player);
                    break;
                case "remove":
                    if (arguments.length > 1) {
                        String target = arguments[1];
                        if (this.gameManager.getWhitelistedPlayers().contains(target)) {
                            this.gameManager.getWhitelistedPlayers().remove(target);
                            player.sendMessage("§cVous avez retiré §f" + target + " §cde la liste blanche.");
                            break;
                        }
                        player.sendMessage("§c"+ target + " ne fait pas parti de la liste blanche.");
                        break;
                    }
                    sendHelp(player);
                    break;
                case "list":
                case "liste":
                    player.sendMessage("§aVoici la liste des joueurs présents dans la liste blanche :");
                    for (String string : this.gameManager.getWhitelistedPlayers())
                        player.sendMessage("§f- §l"+ string);
                    break;
                case "clear":
                    this.gameManager.getWhitelistedPlayers().clear();
                    player.sendMessage("§cVous avez retiré tous les joueurs de la liste blanche.");
                    break;
            }
        }
        return true;
    }

    private final void sendHelp(Player sender) {
        sender.sendMessage("§c§m--------------------------------");
        sender.sendMessage("§cErreur de syntaxe, voici de l'aide :");
        sender.sendMessage("§c/whitelist <add/remove/list/clear> [joueur]");
        sender.sendMessage("§c§m--------------------------------");
    }
}
