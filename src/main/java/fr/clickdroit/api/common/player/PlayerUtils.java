package fr.clickdroit.api.common.player;

import fr.clickdroit.api.API;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.game.team.TeamManager;
import fr.clickdroit.api.game.team.Teams;
import fr.clickdroit.api.module.ModuleType;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerUtils {
    public static void giveDefaultItems(Player player) {
        TeamManager teamManager = API.getAPI().getGameManager().getTeamManager();
        if (API.getAPI().getGameManager().getModuleManager().getCurrentModule().isHasRole())
            player.getInventory().setItem(1, (new ItemCreator(Material.SKULL_ITEM))

                    .setDurability(Short.valueOf((short)3))
                    .setName("des r")
                    .setSkullURL("http://textures.minecraft.net/texture/d92dde9fff32f7c6d13818754b361e95cb98a19c482d5c535ccfe0c185bc6")
                    .addItemFlags(ItemFlag.HIDE_ENCHANTS).getItem());
        if (API.getAPI().getGameManager().getModuleManager().getCurrentModule().equals(ModuleType.DEMONSLAYER))
            player.getInventory().setItem(0, (new ItemCreator(Material.NETHER_STAR)).setName("au ddu jump ").getItem());
        player.getInventory().setItem(8, (new ItemCreator(Material.BED)).setName("au lobby ").addItemFlags(ItemFlag.HIDE_ENCHANTS).getItem());
        player.getInventory().setItem(2, (new ItemCreator(Material.BOOK)).setName("").addItemFlags(ItemFlag.HIDE_ENCHANTS).getItem());
        if (!GameUtils.isSoloMode()) {
            player.getInventory().setItem(6, (new ItemCreator(Material.BANNER))
                    .setDurability(Integer.valueOf(teamManager.getPlayerTeam().containsKey(player.getUniqueId()) ? ((Teams)teamManager
                            .getPlayerTeam().get(player.getUniqueId())).getDataitem() : 15))

                    .setName("une" ).getItem());
        } else {
            player.getInventory().remove(Material.BANNER);
        }
        giveHostItems(player);
    }

    public static void giveHostItems(Player player) {
        if (API.getAPI().getGameManager().getGameHost() != null && (
                API.getAPI().getGameManager().getGameHost().equals(player.getUniqueId()) ||
                        API.getAPI().getGameManager().getHosts().contains(player.getUniqueId())))
            player.getInventory().setItem(4, (new ItemCreator(Material.REDSTONE_COMPARATOR))
                    .setName("la partie ")
                    .addEnchantment(Enchantment.DURABILITY, Integer.valueOf(1))
                    .addItemFlags(ItemFlag.HIDE_ENCHANTS).getItem());
    }

    public static void setAbsoHearths(Player p, int coeur) {
        ((CraftPlayer)p).getHandle().setAbsorptionHearts(coeur);
    }

    public static double getAbsoHearths(Player p) {
        return ((CraftPlayer)p).getHandle().getAbsorptionHearts();
    }

    public static void makePlayerSeePlayersHealthAboveHead(Player player) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        Objective objective = (scoreboard.getObjective("HP") == null) ? scoreboard.registerNewObjective("HP", "health") : scoreboard.getObjective("HP");
        objective.setDisplayName(ChatColor.RED +" ");
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        player.setScoreboard(scoreboard);
    }

    public static void stopSeeHealthHead(Player player) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = player.getScoreboard();
        player.getScoreboard().clearSlot(DisplaySlot.BELOW_NAME);
    }
}

