package fr.clickdroit.api.config.common;

import fr.clickdroit.api.config.ConfigOptionsGUI;
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

public class CycleManagerGUI implements CustomInventory {
    private GameManager gameManager;

    private GameConfig gameConfig;

    public CycleManagerGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    public String getName() {
        return "Durée cycle jour/nuit";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        slots[0] = (new ItemCreator(Material.BANNER)).setName("§c-5").setDurability(Integer.valueOf(1)).getItem();
        slots[1] = (new ItemCreator(Material.BANNER)).setName("§c-2").setDurability(Integer.valueOf(14)).getItem();
        slots[2] = (new ItemCreator(Material.BANNER)).setName("§c-1").setDurability(Integer.valueOf(11)).getItem();
        slots[4] = OpenVar.CYCLE_DURATION.getItemCycle();
        slots[6] = (new ItemCreator(Material.BANNER)).setName("§a+1").setDurability(Integer.valueOf(12)).getItem();
        slots[7] = (new ItemCreator(Material.BANNER)).setName("§a+2").setDurability(Integer.valueOf(10)).getItem();
        slots[8] = (new ItemCreator(Material.BANNER)).setName("§a+5").setDurability(Integer.valueOf(2)).getItem();
        slots[13] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case BANNER:
                switch (clickedItem.getDurability()) {
                    case 1:
                        this.gameConfig.setDayNightDuration(this.gameConfig.getDayNightDuration() - 300L);
                        break;
                    case 14:
                        this.gameConfig.setDayNightDuration(this.gameConfig.getDayNightDuration() - 120L);
                        break;
                    case 11:
                        this.gameConfig.setDayNightDuration(this.gameConfig.getDayNightDuration() - 60L);
                        break;
                    case 12:
                        this.gameConfig.setDayNightDuration(this.gameConfig.getDayNightDuration() + 60L);
                        break;
                    case 10:
                        this.gameConfig.setDayNightDuration(this.gameConfig.getDayNightDuration() + 120L);
                        break;
                    case 2:
                        this.gameConfig.setDayNightDuration(this.gameConfig.getDayNightDuration() + 300L);
                        break;
                }
                break;
            case ARROW:
                this.gameManager.getApi().openInventory(player, ConfigOptionsGUI.class);
                return;
        }
        if (this.gameConfig.getDayNightDuration() < 120L) {
            this.gameConfig.setDayNightDuration(120L);
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
        } else if (this.gameConfig.getDayNightDuration() > 4800L) {
            this.gameConfig.setDayNightDuration(4800L);
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
        }
        this.gameManager.getApi().openInventory(player, getClass());
    }

    public int getRows() {
        return 2;
    }
}
