package fr.clickdroit.api.config.borderValue;

import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class BorderSpeedGUI implements CustomInventory {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    public BorderSpeedGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    public String getName() {
        return "Vitesse de la bordure";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        slots[0] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(1)).setName("").getItem();
        slots[1] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(14)).setName("").getItem();
        slots[2] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(11)).setName("").getItem();
        slots[4] = (new ItemCreator(Material.WATCH)).setName("de la bordure " + this.gameConfig.getBorderBlocksPerSecond() + " bloc(s)/s").getItem();
        slots[6] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(12)).setName("").getItem();
        slots[7] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(10)).setName("").getItem();
        slots[8] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(2)).setName("").getItem();
        slots[13] = (new ItemCreator(Material.ARROW)).setName("en arri").getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case BANNER:
                if (clickedItem.getDurability() == 1) {
                    this.gameConfig.setBorderBlocksPerSecond(this.gameConfig.getBorderBlocksPerSecond() - 10);
                } else if (clickedItem.getDurability() == 14) {
                    this.gameConfig.setBorderBlocksPerSecond(this.gameConfig.getBorderBlocksPerSecond() - 5);
                } else if (clickedItem.getDurability() == 11) {
                    this.gameConfig.setBorderBlocksPerSecond(this.gameConfig.getBorderBlocksPerSecond() - 1);
                } else if (clickedItem.getDurability() == 12) {
                    this.gameConfig.setBorderBlocksPerSecond(this.gameConfig.getBorderBlocksPerSecond() + 1);
                } else if (clickedItem.getDurability() == 10) {
                    this.gameConfig.setBorderBlocksPerSecond(this.gameConfig.getBorderBlocksPerSecond() + 5);
                } else if (clickedItem.getDurability() == 2) {
                    this.gameConfig.setBorderBlocksPerSecond(this.gameConfig.getBorderBlocksPerSecond() + 10);
                }
                if (this.gameConfig.getBorderBlocksPerSecond() > 15) {
                    this.gameConfig.setBorderBlocksPerSecond(15);
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                }
                if (this.gameConfig.getBorderBlocksPerSecond() < 1) {
                    this.gameConfig.setBorderBlocksPerSecond(1);
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

