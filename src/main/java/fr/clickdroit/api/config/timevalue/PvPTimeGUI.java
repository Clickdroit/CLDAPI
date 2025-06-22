package fr.clickdroit.api.config.timevalue;

import fr.clickdroit.api.config.ConfigOptionsGUI;
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

public class PvPTimeGUI implements CustomInventory {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    public PvPTimeGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    public String getName() {
        return "PvP - Temps d'activation";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        slots[0] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(1)).getItem();
        slots[1] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(14)).getItem();
        slots[2] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(11)).getItem();
        slots[4] = OpenVar.PVP_TIME.getItem();
        slots[6] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(12)).getItem();
        slots[7] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(10)).getItem();
        slots[8] = (new ItemCreator(Material.BANNER)).setName("").setDurability(Integer.valueOf(2)).getItem();
        slots[13] = (new ItemCreator(Material.ARROW)).setName("en arri").getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case BANNER:
                if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("")) {
                    this.gameConfig.setPvpTime(this.gameConfig.getPvpTime() - 10);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("")) {
                    this.gameConfig.setPvpTime(this.gameConfig.getPvpTime() - 30);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("")) {
                    this.gameConfig.setPvpTime(this.gameConfig.getPvpTime() - 60);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("")) {
                    this.gameConfig.setPvpTime(this.gameConfig.getPvpTime() + 10);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("")) {
                    this.gameConfig.setPvpTime(this.gameConfig.getPvpTime() + 30);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("")) {
                    this.gameConfig.setPvpTime(this.gameConfig.getPvpTime() + 60);
                }
                if (this.gameConfig.getPvpTime() < 60) {
                    this.gameConfig.setPvpTime(60);
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                } else if (this.gameConfig.getPvpTime() > 2400) {
                    this.gameConfig.setPvpTime(2400);
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                }
                this.gameManager.getApi().openInventory(player, getClass());
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

