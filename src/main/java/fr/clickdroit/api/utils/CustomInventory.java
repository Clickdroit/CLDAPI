package fr.clickdroit.api.utils;

import java.util.function.Supplier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface CustomInventory {
    String getName();

    Supplier<ItemStack[]> getContents(Player paramPlayer);

    void onClick(Player paramPlayer, Inventory paramInventory, ItemStack paramItemStack, int paramInt, ClickType paramClickType);

    int getRows();

    default int getSlots() {
        return getRows() * 9;
    }
}
