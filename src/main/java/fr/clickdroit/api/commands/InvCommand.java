package fr.clickdroit.api.commands;

import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.InventoryAPI;
import java.util.function.Supplier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvCommand implements CommandExecutor, CustomInventory {
    private final GameManager gameManager;

    public InvCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] arguments) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            this.gameManager.getApi().openInventory(player, getClass());
        }
        return false;
    }

    public String getName() {
        return "Inventaire par défaut";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        for (int slot = 0; slot < 36; slot++) {
            ItemStack item = InventoryAPI.items[slot];
            if (item != null)
                slots[slot] = item;
        }
        slots[36] = InventoryAPI.items[36];
        slots[37] = InventoryAPI.items[37];
        slots[38] = InventoryAPI.items[38];
        slots[39] = InventoryAPI.items[39];
        slots[53] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (slot) {
            case 53:
                player.closeInventory();
                break;
        }
    }

    public int getRows() {
        return 6;
    }
}
