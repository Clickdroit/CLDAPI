package fr.clickdroit.api.commands;

import fr.clickdroit.api.common.world.LoadingChunkV2;
import fr.clickdroit.api.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PreLoadCommand implements CommandExecutor {
    private final GameManager gameManager;

    public PreLoadCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player))
            return true;
        Player player = (Player)commandSender;
        if (this.gameManager.hasHostAccess(player))
            if (!this.gameManager.isPreload()) {
                this.gameManager.setPreload(true);
                player.sendMessage("§aDébut de la prégénération.");
                new LoadingChunkV2(Bukkit.getWorld("world"));
            } else {
                player.sendMessage("§cLe serveur est déjà pré-chargé.");
            }
        return false;
    }
}
