package fr.clickdroit.api.commands;

import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.utils.msg.InteractiveMessage;
import fr.clickdroit.api.utils.msg.TextComponentBuilder;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpopCommand implements CommandExecutor {
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            if (arguments.length == 0) {
                player.sendMessage("Veuillez mettre un message.");
                return false;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (String string : arguments)
                stringBuilder.append(string).append(" ");
            player.sendMessage("demande a bien envoyaux organisateurs de la partie.");
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (canViewHelpop(players)) {
                    players.sendMessage(""+ (canViewIdentity(player) ? player.getName() : "Anonyme") + " "+ stringBuilder);
                    (new InteractiveMessage()).add((new TextComponentBuilder(""))
                                    .setHoverMessage(new String[] { "ici pour vous t"}).setClickAction(ClickEvent.Action.RUN_COMMAND, "/tp " + player.getName()).build())
                            .add((new TextComponentBuilder("        "))
                                    .setHoverMessage(new String[] { "ici pour voir l'inventaire" }).setClickAction(ClickEvent.Action.RUN_COMMAND, "/view " + player.getName()).build())

                            .add((new TextComponentBuilder("        "))
                                    .setHoverMessage(new String[] { "ici pour envoyer un message" }).setClickAction(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + player.getName() + " ").build())
                            .add((new TextComponentBuilder("       " ))
                                    .setHoverMessage(new String[] { "ici pour voir le r"}).setClickAction(ClickEvent.Action.RUN_COMMAND, "/ds who " + player.getName() + " ").build())
                            .sendMessage(new Player[] { players });
                }
            }
        }
        return false;
    }

    private boolean canViewHelpop(Player player) {
        return player.isOp();
    }

    private boolean canViewIdentity(Player player) {
        GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
        return gamePlayer.isAlerts();
    }
}
