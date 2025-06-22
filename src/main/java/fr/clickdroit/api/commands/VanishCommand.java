package fr.clickdroit.api.commands;

import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CommonString;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements CommandExecutor {
    private final GameManager gameManager;

    public VanishCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (!(commandSender instanceof Player))
            return true;
        Player player = (Player)commandSender;
        if (!player.isOp()) {
            player.sendMessage(CommonString.NO_PERMISSION.getMessage());
            return true;
        }
        if (this.gameManager.getVanishList().contains(player.getUniqueId())) {
            for (Player p : Bukkit.getOnlinePlayers())
                p.showPlayer(player);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (this.gameManager.getVanishList().contains(p.getUniqueId()))
                    player.hidePlayer(p);
            }
            this.gameManager.getVanishList().remove(player.getUniqueId());
            player.sendMessage("n'plus vanish !");
        } else {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (!this.gameManager.getVanishList().contains(p.getUniqueId()))
                    p.hidePlayer(player);
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (this.gameManager.getVanishList().contains(p.getUniqueId()))
                    player.showPlayer(p);
            }
            this.gameManager.getVanishList().add(player.getUniqueId());
            player.sendMessage("vanish !");
        }
        return false;
    }
}
