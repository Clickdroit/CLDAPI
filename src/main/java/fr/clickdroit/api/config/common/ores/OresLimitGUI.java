package fr.clickdroit.api.config.common.ores;

import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.config.value.OpenVar;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OresLimitGUI implements CustomInventory {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    public OresLimitGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    public String getName() {
        return "Limite de minerais";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        slots[3] = OpenVar.DIAMOND_MAX.getItem();
        slots[4] = (new ItemCreator(Material.PAPER)).setName("§cInformation")
                .addLore("")
                .addLore("  §8| §fSi la valeur est définie sur 0")
                .addLore("  §8| §falors il n'y a pas de limite.")
                .addLore("")
                .getItem();
        slots[5] = OpenVar.GOLD_MAX.getItem();
        slots[13] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case DIAMOND:
                this.gameManager.getApi().openInventory(player, DiamondMaxGUI.class);
                break;
            case GOLD_INGOT:
                this.gameManager.getApi().openInventory(player, GoldMaxGUI.class);
                break;
            case ARROW:
                this.gameManager.getApi().openInventory(player, ConfigOptionsGUI.class);
                break;
        }
    }

    public int getRows() {
        return 2;
    }
}

