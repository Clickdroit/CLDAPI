package fr.clickdroit.api.commands;

import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.DaMath;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NearCommand implements CommandExecutor {
    private final GameManager gameManager;

    public NearCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if (player.isOp()) {
                int radius = 80;
                if (arguments.length > 0)
                    try {
                        radius = Integer.parseInt(arguments[0]);
                    } catch (NumberFormatException e) {
                        player.sendMessage("erron");
                    }
                Location playerLocation = player.getLocation().clone();
                int totalPlayers = 0;
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (!players.getLocation().getWorld().equals(playerLocation.getWorld()) || player
                            .getUniqueId().equals(players.getUniqueId()))
                        continue;
                    int distance = (int)players.getLocation().distance(playerLocation);
                    if (distance <= radius) {
                        int x = (int)players.getLocation().getX();
                        int y = (int)players.getLocation().getY();
                        int z = (int)players.getLocation().getZ();
                        String arrow = DaMath.getArrow(playerLocation, players.getLocation());
                        player.sendMessage(""+ players.getName() + " "+ distance + " blocs vous." + arrow + " " + x + ", Y: " + y + ", Z: " + z + ")");
                        totalPlayers++;
                    }
                }
                if (totalPlayers > 0) {
                    player.sendMessage("y a au total " + totalPlayers + " joueur(s) autour de vous dans un rayon de " + radius + " bloc(s).");
                } else {
                    player.sendMessage("n'y a aucun joueur autour de vous dans un rayon de " + radius + " bloc(s).");
                }
                player.sendMessage("pouvez changer le rayon de recherche en ajoutant un nombre aprla commande. Exemple: /near 150");
            } else {
                player.sendMessage(CommonString.NO_PERMISSION.getMessage());
            }
        }
        return false;
    }
}