package fr.clickdroit.api.commands;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ScenarioCommand implements CommandExecutor, CustomInventory {
    public String getName() {
        return "Scénarios activés";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        int[] glass = {
                0, 1, 7, 8, 9, 17, 27, 35, 36, 37,
                43, 44 };
        for (int i : glass)
            slots[i] = (new ItemCreator(Material.STAINED_GLASS_PANE)).setDurability(Integer.valueOf(5)).getItem();
        int size = 0;
        for (Scenario scenario : Scenario.values()) {
            if (scenario.isEnabled())
                size++;
        }
        if (size == 0) {
            slots[22] = (new ItemCreator(Material.BARRIER)).setName("§cAucun scénario activé :(").getItem();
        } else {
            int i = 10;
            for (Scenario scenarios : Scenario.values()) {
                if (scenarios.isEnabled()) {
                    slots[i] = scenarios.getItem();
                    i++;
                    switch (i) {
                        case 17:
                            i = 19;
                            break;
                        case 26:
                            i = 28;
                            break;
                    }
                    if (i > 34)
                        break;
                }
            }
        }
        slots[40] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (slot) {
            case 40:
                player.closeInventory();
                break;
        }
    }

    public int getRows() {
        return 5;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player)sender;
            API.getAPI().openInventory(player, getClass());
        }
        return false;
    }
}
