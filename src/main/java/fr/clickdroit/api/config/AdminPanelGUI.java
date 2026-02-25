package fr.clickdroit.api.config;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.item.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AdminPanelGUI implements CustomInventory {
    public String getName() {
        return "§f(§c!§f) §cPanel";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        Integer[] glass = {
                Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(17), Integer.valueOf(27), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(37),
                Integer.valueOf(43), Integer.valueOf(44) };
        Integer[] arrayOfInteger1;
        int i;
        byte b;
        for (arrayOfInteger1 = glass, i = arrayOfInteger1.length, b = 0; b < i; ) {
            int j = arrayOfInteger1[b].intValue();
            slots[j] = (new ItemCreator(Material.STAINED_GLASS_PANE)).setDurability(Integer.valueOf(14)).getItem();
            b = (byte)(b + 1);
        }
        slots[13] = (new ItemCreator(Material.EMPTY_MAP)).setName("§6Game Dev §f- " + (API.getAPI().getGameManager().getGameConfig().isGameDev() ? "§eOui": "§cNon")).getItem();
        slots[40] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case EMPTY_MAP:
                API.getAPI().getGameManager().getGameConfig().setGameDev(!API.getAPI().getGameManager().getGameConfig().isGameDev());
                API.getAPI().openInventory(player, getClass());
                break;
            case ARROW:
                API.getAPI().openInventory(player, ConfigMainGUI.class);
                break;
        }
    }

    public int getRows() {
        return 5;
    }
}
