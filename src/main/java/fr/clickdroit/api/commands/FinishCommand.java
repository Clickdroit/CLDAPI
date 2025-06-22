package fr.clickdroit.api.commands;

import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.common.player.PlayerUtils;
import fr.clickdroit.api.config.common.DefaultDeathInvGUI;
import fr.clickdroit.api.config.common.DefaultInvGUI;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.InventoryAPI;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FinishCommand implements CommandExecutor {
    private final GameManager gameManager;

    public FinishCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player)commandSender;
            GamePlayer gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
            if (gamePlayer.isEditing()) {
                InventoryAPI.saveInventory(player);
                gamePlayer.setEditing(false);
                player.sendMessage("par a modifiavec succ");
                        player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                PlayerUtils.giveDefaultItems(player);
                player.getInventory().setHeldItemSlot(4);
                this.gameManager.getApi().openInventory(player, DefaultInvGUI.class);
            } else if (gamePlayer.isEditingDeathInv()) {
                InventoryAPI.saveDeathInventory(player);
                gamePlayer.setEditingDeathInv(false);
                player.sendMessage("de a modifiavec succ");
                        player.setGameMode(GameMode.ADVENTURE);
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                PlayerUtils.giveDefaultItems(player);
                player.getInventory().setHeldItemSlot(4);
                this.gameManager.getApi().openInventory(player, DefaultDeathInvGUI.class);
            } else {
                player.sendMessage("n'pas en train de dl'inventaire par d");
            }
        }
        return false;
    }
}
