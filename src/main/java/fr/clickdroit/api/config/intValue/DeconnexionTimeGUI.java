package fr.clickdroit.api.config.intValue;

import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DeconnexionTimeGUI implements CustomInventory {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    public DeconnexionTimeGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    public String getName() {
        return "Temps avant mort de déconnexion";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        slots[0] = (new ItemCreator(Material.BANNER)).setName("§c-10").setDurability(Integer.valueOf(1)).getItem();
        slots[1] = (new ItemCreator(Material.BANNER)).setName("§c-5").setDurability(Integer.valueOf(14)).getItem();
        slots[2] = (new ItemCreator(Material.BANNER)).setName("§c-1").setDurability(Integer.valueOf(11)).getItem();
        slots[4] = (new ItemCreator(Material.COMPASS)).setName("§8| §fTemps avant mort de §6déconnexion" )
                .addLore("")
                .addLore("  §8| §fVous permet de configurer")
                .addLore("  §8| §fle temps necéssaire pour")
                .addLore("  §8| §fmourir de déconnexion.")
                .addLore("")
                .addLore(" §8> §fConfiguration: §c" + this.gameManager.getGameConfig().getDisconnectMinute() + " minute(s)")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
        slots[6] = (new ItemCreator(Material.BANNER)).setName("§a+1").setDurability(Integer.valueOf(12)).getItem();
        slots[7] = (new ItemCreator(Material.BANNER)).setName("§a+5").setDurability(Integer.valueOf(10)).getItem();
        slots[8] = (new ItemCreator(Material.BANNER)).setName("§a+10").setDurability(Integer.valueOf(2)).getItem();
        slots[13] = (new ItemCreator(Material.ARROW)).setName("§fRevenir en arriere").getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case BANNER:
                if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§c-10")) {
                    this.gameConfig.setDisconnectMinute(this.gameConfig.getDisconnectMinute() - 10);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§c-5")) {
                    this.gameConfig.setDisconnectMinute(this.gameConfig.getDisconnectMinute() - 5);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§c-1")) {
                    this.gameConfig.setDisconnectMinute(this.gameConfig.getDisconnectMinute() - 1);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§a+10")) {
                    this.gameConfig.setDisconnectMinute(this.gameConfig.getDisconnectMinute() + 10);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§a+5")) {
                    this.gameConfig.setDisconnectMinute(this.gameConfig.getDisconnectMinute() + 5);
                } else if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§a+1")) {
                    this.gameConfig.setDisconnectMinute(this.gameConfig.getDisconnectMinute() + 1);
                }
                if (this.gameConfig.getDisconnectMinute() < 1) {
                    this.gameConfig.setDisconnectMinute(1);
                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                } else if (this.gameConfig.getDisconnectMinute() > 60) {
                    this.gameConfig.setDisconnectMinute(60);
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

