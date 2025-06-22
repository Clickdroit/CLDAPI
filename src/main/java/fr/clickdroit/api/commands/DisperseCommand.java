package fr.clickdroit.api.commands;

import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.RandomUtils;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DisperseCommand implements CommandExecutor {
    private final GameManager gameManager;

    public DisperseCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if (player.isOp()) {
                if (GameUtils.isGameStarted()) {
                    if (arguments.length > 0) {
                        if (arguments[0].equalsIgnoreCase("pos")) {
                            Location location = player.getLocation();
                            for (UUID uuid : this.gameManager.getInGamePlayers()) {
                                Player players = Bukkit.getPlayer(uuid);
                                if (players != null && players.isOnline() &&
                                        !players.getUniqueId().equals(player.getUniqueId()) && players
                                        .getLocation().distance(location) <= 20.0D) {
                                    GamePlayer gamePlayers = GamePlayer.getPlayer(uuid);
                                    if (gamePlayers == null)
                                        continue;
                                    gamePlayers.addInvincibilityNoFallCount(10);
                                    players.teleport(RandomUtils.getRandomLocationInBorder());
                                    players.sendMessage("avez talpar un mod");
                                            player.sendMessage("avez tal+ players.getName() + ");
                                }
                            }
                        } else {
                            Player targetPosition = Bukkit.getPlayer(arguments[0]);
                            if (targetPosition != null) {
                                Location location = targetPosition.getLocation();
                                for (UUID uuid : this.gameManager.getInGamePlayers()) {
                                    Player players = Bukkit.getPlayer(uuid);
                                    if (players != null && players.isOnline() &&
                                            !players.getUniqueId().equals(player.getUniqueId()) && players.getLocation().getWorld().equals(location.getWorld()) && players
                                            .getLocation().distance(location) <= 20.0D) {
                                        GamePlayer gamePlayers = GamePlayer.getPlayer(uuid);
                                        if (gamePlayers == null)
                                            continue;
                                        gamePlayers.addInvincibilityNoFallCount(10);
                                        players.teleport(RandomUtils.getRandomLocationInBorder());
                                        players.sendMessage("avez talpar un mod");
                                                player.sendMessage("avez tal+ players.getName() + ");
                                    }
                                }
                            } else {
                                player.sendMessage("joueur avec le pseudo '" + arguments[0] + "' n'a trouv");
                            }
                        }
                    } else {
                        player.sendMessage("de syntaxe: /disperse <pos:joueur>");
                    }
                } else {
                    player.sendMessage("partie n'a pas commenc");
                }
            } else {
                player.sendMessage(CommonString.NO_PERMISSION.getMessage());
            }
        }
        return false;
    }
}
