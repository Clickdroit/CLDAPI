package fr.clickdroit.api.config.intValue;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class DamageGUI implements CustomInventory {
    public String getName() {
        return "Configuration des d";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        GameConfig gameConfig = API.getAPI().getGameManager().getGameConfig();
        slots[0] = (new ItemCreator(Material.ENDER_PEARL)).setName("Pearl" + gameConfig.getEnderpearlDamage() + "").setAmount(Integer.valueOf(gameConfig.getEnderpearlDamage())).getItem();
        slots[13] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        GameConfig gameConfig = API.getAPI().getGameManager().getGameConfig();
        switch (slot) {
            case 0:
                if (clickType.equals(ClickType.LEFT)) {
                    gameConfig.setEnderpearlDamage(gameConfig.getEnderpearlDamage() + 1);
                } else if (clickType.equals(ClickType.RIGHT)) {
                    gameConfig.setEnderpearlDamage(gameConfig.getEnderpearlDamage() - 1); // FIX: - au lieu de +
                }
                if (gameConfig.getEnderpearlDamage() >= 10) {
                    gameConfig.setEnderpearlDamage(9); // FIX: 9 au lieu de 0
                } else if (gameConfig.getEnderpearlDamage() <= -1) {
                    gameConfig.setEnderpearlDamage(0); // FIX: 0 au lieu de 10
                }
                gameConfig.getGameManager().getApi().openInventory(player, getClass());
                break;
            case 13:
                API.getAPI().openInventory(player, ConfigOptionsGUI.class);
                break;
        }
    }

    public int getRows() {
        return 2;
    }
}
