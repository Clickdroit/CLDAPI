package fr.clickdroit.api.config.common;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import fr.clickdroit.api.utils.InventoryAPI;
import java.util.function.Supplier;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DefaultInvGUI implements CustomInventory {
    public String getName() {
        return "Définir l'inventaire";
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
        slots[52] = (new ItemCreator(Material.BANNER)).setName("§8| §fDéfinir §cl'inventaire").setDurability(Integer.valueOf(10)).getItem();
        slots[53] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        GamePlayer gamePlayer;
        switch (slot) {
            case 52:
                gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
                gamePlayer.setEditing(true);
                player.setGameMode(GameMode.CREATIVE);
                player.setAllowFlight(false);
                player.sendMessage(CommonString.BAR.getMessage());
                player.sendMessage("");
                player.sendMessage("§c§lDéfinir l'inventaire par défaut");
                player.sendMessage("");
                player.sendMessage("§cVoici les commandes §4:");
                player.sendMessage(" §8| §4/§cfinish §8: §fSauvegarder l'inventaire.");
                player.sendMessage(" §8| §4/§cenchant §8: §fEnchanter l'objet dans votre main.");
                player.sendMessage("");
                player.sendMessage(CommonString.BAR.getMessage());
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.closeInventory();
                InventoryAPI.giveInvent(player);
                break;
            case 53:
                API.getAPI().openInventory(player, ConfigOptionsGUI.class);
                break;
        }
    }

    public int getRows() {
        return 6;
    }
}

