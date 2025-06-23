package fr.clickdroit.api.commands;

import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupeCommand implements CommandExecutor {
    private GameManager gameManager;

    public GroupeCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if (this.gameManager.hasHostAccess(player)) {
                if (arguments.length > 0)
                    try {
                        int value = Integer.parseInt(arguments[0]);
                        if (value < 0 || value > 10)
                            return false;
                        this.gameManager.setGroupe(value);
                        player.sendMessage("§bGroupes défini sur §e" + value);
                                String title = (value > 0) ? ("§c⚠ Groupes de §l"+ value + "§c⚠") : "§c⚠ Limite de groupe retirée §c⚠";
                        for (Player players : Bukkit.getOnlinePlayers())
                            Title.sendTitle(players, 10, 60, 10, "", title);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
            } else {
                player.sendMessage("§cPermission insuffisante");
            }
        }
        return true;
    }
}
