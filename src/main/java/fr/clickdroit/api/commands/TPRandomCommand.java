package fr.clickdroit.api.commands;

import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.utils.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPRandomCommand implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if (player.isOp()) {
                if (arguments.length > 0) {
                    Player target = Bukkit.getPlayer(arguments[0]);
                    if (target == null) {
                        player.sendMessage("§cAucun joueur avec le pseudo '" + arguments[0] + "' n'a été trouvé");
                        return false;
                    }
                    GamePlayer gamePlayer = GamePlayer.getPlayer(target.getUniqueId());
                    gamePlayer.addInvincibilityNoFallCount(10);
                    target.teleport(RandomUtils.getRandomLocationInBorder());
                    target.sendMessage("§cVous avez été téléporté aléatoirement par un modérateur");
                            player.sendMessage("§cVous avez téléporté aléatoirement §l"+ target.getName() + "§c.");
                }
            } else {
                player.sendMessage("§cPermission insuffisante.");
            }
        }
        return false;
    }
}
