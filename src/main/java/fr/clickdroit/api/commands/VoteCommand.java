package fr.clickdroit.api.commands;

import fr.clickdroit.api.API;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.msg.InteractiveMessage;
import fr.clickdroit.api.utils.msg.TextComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VoteCommand implements CommandExecutor {
    private final GameManager gameManager;

    private final Map<UUID, Boolean> voted;

    private boolean vote;

    private int yes;

    private int no;

    public VoteCommand(GameManager gameManager) {
        this.gameManager = gameManager;
        this.voted = new HashMap<>();
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if (arguments.length > 0)
                if (arguments[0].equalsIgnoreCase("answer")) {
                    if (arguments.length >= 2)
                        if (isVote()) {
                            if (!getVoted().containsKey(player.getUniqueId())) {
                                if (arguments[1].equalsIgnoreCase("yes")) {
                                    this.yes++;
                                    getVoted().put(player.getUniqueId(), Boolean.valueOf(true));
                                    player.sendMessage("§aVous avez bien voté. §f(§f§lPio§f)");
                                } else if (arguments[1].equalsIgnoreCase("no")) {
                                    this.no++;
                                    getVoted().put(player.getUniqueId(), Boolean.valueOf(true));
                                    player.sendMessage("§aVous avez bien voté. §f(§f§lNon§f)");
                                }
                            } else {
                                player.sendMessage("§cVous avez déjà voté.");
                            }
                        } else {
                            player.sendMessage("§cIl n'y a aucun vote en cours..");
                        }
                } else if (this.gameManager.hasHostAccess(player)) {
                    if (!isVote()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (String msg : arguments)
                            stringBuilder.append(msg + " ");
                        Bukkit.broadcastMessage("§f§m-----------------------------------------------");
                        Bukkit.broadcastMessage("§aUn vote a été proposé :");
                        Bukkit.broadcastMessage("§f§l> §b"+ stringBuilder);
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage("§fVeuillez cliquer ci-dessous pour y soumettre votre réponse :");
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            (new InteractiveMessage())
                                    .add((new TextComponentBuilder("§a§lOUI")).setHoverMessage(new String[] { "§f§l> §aCliquez ici pour voter Oui." }).setClickAction(ClickEvent.Action.RUN_COMMAND, "/vote answer yes").build())
                                    .add((new TextComponentBuilder("        §c§lNON")).setHoverMessage(new String[] { "§f§l> §cCliquez ici pour voter Non." }).setClickAction(ClickEvent.Action.RUN_COMMAND, "/vote answer no").build()).sendMessage(new Player[] { players });
                        }
                        Bukkit.broadcastMessage("§f§m-----------------------------------------------");
                        setVote(true);
                        setYes(0);
                        setNo(0);
                        getVoted().clear();
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (getVoted().containsKey(players.getUniqueId()))
                                getVoted().remove(players.getUniqueId());
                        }
                        Bukkit.getScheduler().runTaskLater((Plugin)API.getAPI(), () -> {
                            setVote(false);
                            Bukkit.broadcastMessage("§6§l------------");
                            Bukkit.broadcastMessage("§fRéponse du §lvote§f:" );
                            Bukkit.broadcastMessage(" §f> §aOui§f: " + getYes());
                            Bukkit.broadcastMessage(" §f> §cNon§f: " + getNo());
                            Bukkit.broadcastMessage("§6§l------------");
                        },300L);
                    } else {
                        player.sendMessage("§cIl y' a dun vote en cours.");
                    }
                }
        }
        return true;
    }

    public int getYes() {
        return this.yes;
    }

    public void setYes(int yes) {
        this.yes = yes;
    }

    public int getNo() {
        return this.no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Map<UUID, Boolean> getVoted() {
        return this.voted;
    }

    public boolean isVote() {
        return this.vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }
}
