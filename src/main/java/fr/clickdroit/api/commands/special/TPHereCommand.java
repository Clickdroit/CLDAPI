package fr.clickdroit.api.commands.special;

import fr.clickdroit.api.utils.CommonString;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPHereCommand implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if (player.isOp()) {
                if (arguments.length == 0) {
                    player.sendMessage("§cErreur de syntaxe: /tphere <joueur:@a>");
                    return false;
                }
                String arg = arguments[0];
                Location location = player.getLocation();
                if (arg.equalsIgnoreCase("@a")) {
                    for (Player players : Bukkit.getOnlinePlayers())
                        players.teleport(location);
                    player.sendMessage("§aTous les joueurs ont été téléportés vers vous.");
                } else {
                    Player target = Bukkit.getPlayer(arg);
                    if (target == null) {
                        player.sendMessage("§cAucun joueur avec le pseudo '" + arg + "' n'a été trouvé...");
                        return false;
                    }
                    target.teleport(location);
                    player.sendMessage("§f"+ target.getName() + " §aa été téléporté tvers vous.");
                }
            } else {
                player.sendMessage(CommonString.NO_PERMISSION.getMessage());
            }
        }
        return false;
    }
}
