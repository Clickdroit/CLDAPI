package fr.clickdroit.api.commands;

import fr.clickdroit.api.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertsCommand implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if (player.isOp()) {
                GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
                gamePlayer.setAlerts(!gamePlayer.isAlerts());
                if (gamePlayer.isAlerts()) {
                    player.sendMessage("avez activvos alertes.");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (players.isOp())
                            players.sendMessage(""+ player.getName() + ": a activses alertes.]");
                    }
                } else {
                    player.sendMessage("avez dvos alertes.");
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (players.isOp())
                            players.sendMessage(""+ player.getName() + ": a dses alertes.]");
                    }
                }
            } else {
                player.sendMessage("insuffisante.");
            }
        }
        return false;
    }
}
