package fr.clickdroit.api.config.common.borderValue;

import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class BorderStartSizeGUI implements CustomInventory {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    public BorderStartSizeGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    public String getName() {
        return "Bordure initiale";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        int value = this.gameConfig.getBorderStartSize();
        slots[0] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(1)).setName("").getItem();
                slots[1] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(14)).setName("").getItem();
                        slots[2] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(11)).setName("").getItem();
                                slots[4] = (new ItemCreator(Material.STAINED_GLASS)).setDurability(Integer.valueOf(3)).setName("initiale " + value + "x" + value).getItem();
        slots[6] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(12)).setName("").getItem();
                slots[7] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(10)).setName("").getItem();
                        slots[8] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(2)).setName("").getItem();
                                slots[13] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case BANNER:
                if (clickedItem.getDurability() == 1) {
                    this.gameConfig.setBorderStartSize(this.gameConfig.getBorderStartSize() - 500);
                } else if (clickedItem.getDurability() == 14) {
                    this.gameConfig.setBorderStartSize(this.gameConfig.getBorderStartSize() - 100);
                } else if (clickedItem.getDurability() == 11) {
                    this.gameConfig.setBorderStartSize(this.gameConfig.getBorderStartSize() - 50);
                } else if (clickedItem.getDurability() == 12) {
                    this.gameConfig.setBorderStartSize(this.gameConfig.getBorderStartSize() + 50);
                } else if (clickedItem.getDurability() == 10) {
                    this.gameConfig.setBorderStartSize(this.gameConfig.getBorderStartSize() + 100);
                } else if (clickedItem.getDurability() == 2) {
                    this.gameConfig.setBorderStartSize(this.gameConfig.getBorderStartSize() + 500);
                }
                if (this.gameConfig.getBorderStartSize() > 2500) {
                    this.gameConfig.setBorderStartSize(2500);
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                }
                if (this.gameConfig.getBorderStartSize() < 100) {
                    this.gameConfig.setBorderStartSize(100);
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                }
                this.gameManager.getApi().openInventory(player, getClass());
                break;
            case ARROW:
                this.gameManager.getApi().openInventory(player, BorderManagerGUI.class);
                break;
        }
    }

    public int getRows() {
        return 2;
    }
}
