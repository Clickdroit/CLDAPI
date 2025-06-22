package fr.clickdroit.api.commands;

import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.game.team.TeamManager;
import fr.clickdroit.api.game.team.Teams;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TCCommand implements CommandExecutor {
    private final GameManager gameManager;

    private final TeamManager teamManager;

    public TCCommand(GameManager gameManager) {
        this.gameManager = gameManager;
        this.teamManager = gameManager.getTeamManager();
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player &&
                !GameUtils.isSoloMode() && GameUtils.isGameStarted()) {
            Player player = (Player)commandSender;
            UUID uuid = player.getUniqueId();
            if (this.gameManager.getInGamePlayers().contains(uuid) && this.teamManager.getPlayerTeam().containsKey(uuid)) {
                Teams teams = (Teams)this.gameManager.getTeamManager().getPlayerTeam().get(uuid);
                if (teams != null) {
                    int x = player.getLocation().getBlockX();
                    int y = player.getLocation().getBlockY();
                    int z = player.getLocation().getBlockZ();
                    for (Player players : this.teamManager.getPlayersInTeam(teams))
                        players.sendMessage("" + teams.getColor() + teams.getName() + " " + player.getName() + " " + (player.isOp() ? "": "") + "X: " + x + ", Y: " + y + ", Z: " + z);
                }
            }
        }
        return false;
    }
}
