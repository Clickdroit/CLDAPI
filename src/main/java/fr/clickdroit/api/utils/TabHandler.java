package fr.clickdroit.api.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TabHandler {
    public void updateTab(Player whoIsModified, Player whoSee, String prefix, String suffix) {
        Scoreboard scoreboard;
        boolean newScoreboard = false;
        if (!whoIsModified.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            scoreboard = whoIsModified.getScoreboard();
        } else {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            newScoreboard = true;
        }
        Team see = getExistingOrCreateNewTeam("d", scoreboard, prefix, suffix);
        see.addEntry(whoSee.getName());
        if (newScoreboard)
            whoIsModified.setScoreboard(scoreboard);
    }

    public Team getExistingOrCreateNewTeam(String string, Scoreboard scoreboard, String prefix, String suffix) {
        Team toReturn = scoreboard.getTeam(string);
        if (toReturn == null) {
            toReturn = scoreboard.registerNewTeam(string);
            toReturn.setPrefix(prefix + "");
            toReturn.setSuffix(suffix + "");
        }
        return toReturn;
    }

    public static void removePrefixFor(Player player) {
        Team team = player.getScoreboard().getPlayerTeam((OfflinePlayer)player);
        if (team != null)
            team.removePlayer((OfflinePlayer)player);
    }
}
