package fr.clickdroit.api.config.common.rules;

import fr.clickdroit.api.common.rules.items.DropItemRate;
import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class DropItemRateGUI implements CustomInventory {
    private final GameManager gameManager;

    public DropItemRateGUI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public String getName() {
        return "Taux de drop";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        int slot = 2;
        for (DropItemRate dropItemRate : DropItemRate.values()) {
            slots[slot] = dropItemRate.getItem();
            slot++;
        }
        slots[13] = (new ItemCreator(Material.ARROW)).setName("§fRevenir en arriere").getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        for (DropItemRate dropItemRate : DropItemRate.values()) {
            if (dropItemRate.getMaterial() == clickedItem.getType() && !clickedItem.getItemMeta().getDisplayName().equals("§fRevenir en arriere")) {
                dropItemRate.toggleAmount(clickType);
                this.gameManager.getApi().openInventory(player, getClass());
            }
        }
        switch (slot) {
            case 13:
                if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName() && clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§fRevenir en arriere"))
                this.gameManager.getApi().openInventory(player, ConfigOptionsGUI.class);
                break;
        }
    }

    public int getRows() {
        return 2;
    }
}

