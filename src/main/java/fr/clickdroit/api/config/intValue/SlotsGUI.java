package fr.clickdroit.api.config.intValue;

import fr.clickdroit.api.config.ConfigMainGUI;
import fr.clickdroit.api.config.GameConfig;
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

public class SlotsGUI implements CustomInventory {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    public SlotsGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    public String getName() {
        return "Slots";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        slots[0] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(14)).getItem();
        slots[1] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(11)).getItem();
        slots[4] = OpenVar.SLOTS.getItem();
        slots[7] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(12)).getItem();
        slots[8] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(10)).getItem();
        slots[13] = (new ItemCreator(Material.ARROW)).setName("en arri").getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case BANNER:
                if (clickedItem.getDurability() == 11) {
                    this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() - 1);
                } else if (clickedItem.getDurability() == 14) {
                    this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() - 5);
                } else if (clickedItem.getDurability() == 12) {
                    this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() + 1);
                } else if (clickedItem.getDurability() == 10) {
                    this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() + 5);
                }
                if (this.gameConfig.getGameSlot() > 1000) {
                    this.gameConfig.setGameSlot(1000);
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                }
                if (this.gameConfig.getGameSlot() < 1) {
                    this.gameConfig.setGameSlot(1);
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                }
                this.gameManager.getApi().openInventory(player, getClass());
                break;
            case ARROW:
                this.gameManager.getApi().openInventory(player, ConfigMainGUI.class);
                break;
        }
    }

    public int getRows() {
        return 2;
    }
}
