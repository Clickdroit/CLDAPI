package fr.clickdroit.api.module.games;

import fr.clickdroit.api.API;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.game.team.TeamManager;
import fr.clickdroit.api.game.team.Teams;
import fr.clickdroit.api.module.Modules;
import fr.clickdroit.api.utils.InventoryAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class UHCModule extends Modules {
    private final API api;

    private UHCFinisher uhcFinisher;

    public UHCModule(API api) {
        this.api = api;
    }

    public void onLoad() {
        this.uhcFinisher = new UHCFinisher(this.api);
    }

    public void onStart(API api) {
        api.getLobbyPopulator().loadCenter();
        api.getCommon().getScoreboardManager().setScoreboardContents(() -> new UHCScoreboard(api.getGameManager()));
    }

    public void onEpisodeSwitch() {}

    public void init() {}

    public void onPlayerDeath(Player player, Player killer) {
        String deathMessage;
        super.onPlayerDeath(player, killer);
        if (GameUtils.isSoloMode()) {
            if (killer == null) {
                deathMessage = "§8| §c"+ player.getName() + " §fest mort.";
            } else {
                deathMessage = "§8| §c"+ player.getName() + " §fa été tué par §c"+ killer.getName() + "§f.";
            }
        } else {
            TeamManager teamManager = this.api.getGameManager().getTeamManager();
            Teams playerTeam = (Teams)teamManager.getPlayerTeam().get(player.getUniqueId());
            if (killer == null) {
                deathMessage = "§8| §c"+ playerTeam.getColor() + playerTeam.getName() + " " + player.getName() + " est mort.";
            } else {
                Teams killerTeam = (Teams)teamManager.getPlayerTeam().get(killer.getUniqueId());
                deathMessage = "§8| §c"+ playerTeam.getColor() + playerTeam.getName() + " " + player.getName() + " a été tué par " + killerTeam.getColor() + killerTeam.getName() + " " + killer.getName() + ".";
            }
        }
        for (ItemStack item : InventoryAPI.itemsDeath) {
            if (item != null && item.getType() != Material.AIR)
                player.getLocation().getWorld().dropItem(player.getLocation(), item);
        }
        Bukkit.broadcastMessage(deathMessage);
        this.uhcFinisher.tryFinishGame();
    }

    public void onPlayerDieByDisconnect(UUID uuid) {
        super.onPlayerDieByDisconnect(uuid);
        this.uhcFinisher.tryFinishGame();
    }

    public void onPlayerChat(Player player, String message) {
        if (this.api.getGameManager().getInGamePlayers().contains(player.getUniqueId())) {
            TeamManager teamManager = this.api.getGameManager().getTeamManager();
            if (!GameUtils.isSoloMode() && teamManager.getPlayerTeam().containsKey(player.getUniqueId())) {
                Teams teams = (Teams)teamManager.getPlayerTeam().get(player.getUniqueId());
                if (message.startsWith("!")) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendMessage(teams.getColor() + teams.getName() + " " + player.getName() + " §8> " + (player.isOp() ? "§f": "§f") + message.replaceFirst("!", "").replaceAll(players.getName(), message.contains(players.getName()) ? ("§b@"+ players.getName() + (player.isOp() ? "§f": "§f")) : players.getName()));
                        if (message.contains(players.getName()))
                            players.playSound(players.getLocation(), Sound.ORB_PICKUP, 5.0F, 0.0F);
                    }
                } else {
                    for (Player players : teamManager.getPlayersInTeam(teams)) {
                        players.sendMessage("§f(§féquipe§f" + teams.getColor() + teams.getName() + " " + player.getName() + " §8> " + (player.isOp() ? "§f": "§f") + message.replaceFirst("!", "").replaceAll(players.getName(), message.contains(players.getName()) ? ("§b@"+ players.getName() + (player.isOp() ? "§f": "§f")) : players.getName()));
                        if (message.contains(players.getName()))
                            players.playSound(players.getLocation(), Sound.ORB_PICKUP, 5.0F, 0.0F);
                    }
                }
            } else {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage((player.isOp() ? ("§c§lOP" + player.getName()) : ("§f"+ player.getName())) + " §8> " + (player.isOp() ? "§f": "§f") + message.replaceAll(players.getName(), message.contains(players.getName()) ? ("§b@"+ players.getName() + (player.isOp() ? "§f": "§f")) : players.getName()));
                    if (message.contains(players.getName()))
                        players.playSound(players.getLocation(), Sound.ORB_PICKUP, 5.0F, 0.0F);
                }
            }
        } else {
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (!this.api.getGameManager().getInGamePlayers().contains(players.getUniqueId())) {
                    players.sendMessage("§f[§fSpectateurs§f] §f"+ player.getName() + " §8> §f" + message.replaceAll(players.getName(), message.contains(players.getName()) ? ("§b@"+ players.getName() + "§f") : players.getName()));
                    if (message.contains(players.getName()))
                        players.playSound(players.getLocation(), Sound.ORB_PICKUP, 5.0F, 0.0F);
                }
            }
        }
    }

    public void onClockUpdate(int gameTime) {
        this.api.getGameManager().getTeamManager().setPlayersTeamArrow();
    }

    public void onPlayerReconnect(Player player) {}

    public void onPlayerDisconnect(Player player) {}

    public void onDay(boolean sendMessage) {
        super.onDay(false);
    }

    public void onNight(boolean sendMessage) {
        super.onNight(false);
    }

    public UHCFinisher getUhcFinisher() {
        return this.uhcFinisher;
    }
}
