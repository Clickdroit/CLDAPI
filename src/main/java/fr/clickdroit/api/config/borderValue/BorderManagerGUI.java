package fr.clickdroit.api.config.borderValue;

import fr.clickdroit.api.config.ConfigMainGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class BorderManagerGUI implements CustomInventory {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    public BorderManagerGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    public String getName() {
        return "";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        Integer[] glass = { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(25), Integer.valueOf(26) };
        Integer[] arrayOfInteger1;
        int i;
        byte b;
        for (arrayOfInteger1 = glass, i = arrayOfInteger1.length, b = 0; b < i; ) {
            int j = arrayOfInteger1[b].intValue();
            slots[j] = (new ItemCreator(Material.STAINED_GLASS_PANE)).setDurability(Integer.valueOf(8)).getItem();
            b = (byte)(b + 1);
        }
        slots[12] = (new ItemCreator(Material.STAINED_GLASS)).setDurability(Integer.valueOf(3)).setName("initiale + this.gameConfig.getBorderStartSize() + ")
                .addLore("")
                .addLore("  ici pour dla taille")
                .addLore("  la bordure initiale de la partie.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
        slots[14] = (new ItemCreator(Material.STAINED_GLASS)).setDurability(Integer.valueOf(14)).setName("finale + this.gameConfig.getBorderEndSize() + ")
                .addLore("")
                .addLore("  ici pour dla taille")
                .addLore("  la bordure finale de la partie.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
        slots[13] = (new ItemCreator(Material.WATCH)).setName("de la bordure "+ this.gameConfig.getBorderBlocksPerSecond() + " bloc(s)/s")
                .addLore("")
                .addLore("  ici pour dla vitesse")
                .addLore("  rde la bordure.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
        slots[22] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case STAINED_GLASS:
                if (clickedItem.getDurability() == 3) {
                    this.gameManager.getApi().openInventory(player, BorderStartSizeGUI.class);
                    break;
                }
                if (clickedItem.getDurability() == 14)
                    this.gameManager.getApi().openInventory(player, BorderEndSizeGUI.class);
                break;
            case WATCH:
                this.gameManager.getApi().openInventory(player, BorderSpeedGUI.class);
                break;
            case ARROW:
                this.gameManager.getApi().openInventory(player, ConfigMainGUI.class);
                break;
        }
    }

    public int getRows() {
        return 3;
    }
}

