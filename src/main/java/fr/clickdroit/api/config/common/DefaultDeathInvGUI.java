package fr.clickdroit.api.config.common;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.InventoryAPI;
import fr.clickdroit.api.utils.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DefaultDeathInvGUI implements CustomInventory {
    public String getName() {
        return "Dl'inventaire de" ;
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        for (int slot = 0; slot < 36; slot++) {
            ItemStack item = InventoryAPI.itemsDeath[slot];
            if (item != null)
                slots[slot] = item;
        }
        slots[36] = InventoryAPI.itemsDeath[36];
        slots[37] = InventoryAPI.itemsDeath[37];
        slots[38] = InventoryAPI.itemsDeath[38];
        slots[39] = InventoryAPI.itemsDeath[39];
        slots[52] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(10)).getItem();
        slots[53] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        GamePlayer gamePlayer;
        switch (slot) {
            case 52:
                gamePlayer = GamePlayer.getPlayer(player.getUniqueId());
                gamePlayer.setEditingDeathInv(true);
                player.setGameMode(GameMode.CREATIVE);
                player.setAllowFlight(false);
                player.sendMessage(CommonString.BAR.getMessage());
                player.sendMessage("");
                player.sendMessage("l'inventaire de mort");
                player.sendMessage("");
                player.sendMessage("les commandes ");
                player.sendMessage(" l'inventaire.");
                player.sendMessage(" l'objet dans votre main.");
                player.sendMessage("");
                player.sendMessage(CommonString.BAR.getMessage());
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.closeInventory();
                InventoryAPI.giveDeathInvent(player);
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

