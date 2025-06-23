package fr.clickdroit.api.config.common.ores;

import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.config.value.OpenVar;
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

public class DiamondMaxGUI implements CustomInventory {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    public DiamondMaxGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    public String getName() {
        return "Diamants";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        slots[0] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(14)).setName("§c-5").getItem();
                slots[1] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(11)).setName("§c-1").getItem();
                        slots[4] = OpenVar.DIAMOND_MAX.getItem();
        slots[7] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(12)).setName("§a+1").getItem();
                slots[8] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(10)).setName("§a+5").getItem();
                        slots[13] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case BANNER:
                if (clickedItem.getDurability() == 14) {
                    this.gameConfig.setDiamondMax(this.gameConfig.getDiamondMax() - 5);
                } else if (clickedItem.getDurability() == 11) {
                    this.gameConfig.setDiamondMax(this.gameConfig.getDiamondMax() - 1);
                } else if (clickedItem.getDurability() == 12) {
                    this.gameConfig.setDiamondMax(this.gameConfig.getDiamondMax() + 1);
                } else if (clickedItem.getDurability() == 10) {
                    this.gameConfig.setDiamondMax(this.gameConfig.getDiamondMax() + 5);
                }
                if (this.gameConfig.getDiamondMax() > 100) {
                    this.gameConfig.setDiamondMax(100);
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                }
                if (this.gameConfig.getDiamondMax() < 0) {
                    this.gameConfig.setDiamondMax(0);
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                }
                this.gameManager.getApi().openInventory(player, getClass());
                break;
            case ARROW:
                this.gameManager.getApi().openInventory(player, OresLimitGUI.class);
                break;
        }
    }

    public int getRows() {
        return 2;
    }
}

