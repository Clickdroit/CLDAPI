package fr.clickdroit.api.commands;

import fr.clickdroit.api.commands.special.ViewInventory;
import fr.clickdroit.api.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewCommand implements CommandExecutor {
    private final GameManager gameManager;

    public ViewCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            if (this.gameManager.hasHostAccess(player)) {
                if (arguments.length > 0) {
                    Player target = Bukkit.getPlayer(arguments[0]);
                    if (target != null) {
                        ViewInventory viewInventory = new ViewInventory(player, target);
                        viewInventory.open();
                    } else {
                        player.sendMessage("§cAucun joueur avec le pseudo '" + arguments[0] + "' n'a été trouvé");
                    }
                } else {
                    player.sendMessage("§cErreur de syntaxe: /view <joueur>");
                }
            } else {
                player.sendMessage("§cPermission insuffisante.");
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 3.0F, 0.0F);
            }
        }
        return false;
    }
}

