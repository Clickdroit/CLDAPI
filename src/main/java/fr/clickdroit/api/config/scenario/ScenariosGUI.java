package fr.clickdroit.api.config.scenario;


import fr.clickdroit.api.API;
import fr.clickdroit.api.config.ConfigMainGUI;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ScenariosGUI implements Listener {
    private static Map<Player, Integer> player_page = new HashMap<>();

    private Map<Integer, Scenario> scenarioHash = new HashMap<>();

    private final API main;

    public ScenariosGUI(API main) {
        this.main = main;
    }

    public Inventory openInventory(Player player, int page) {
        player_page.put(player, Integer.valueOf(page));
        Inventory inventory = Bukkit.createInventory(null, 54, "- Page " + page);
        int[] glass = { 36, 37, 38, 39, 40, 41, 42, 43, 44 };
        for (int i : glass)
            inventory.setItem(i, (new ItemCreator(Material.STAINED_GLASS_PANE)).setDurability(Integer.valueOf(7)).getItem());
        int totalPage = 1;
        int size = (Scenario.values()).length;
        if (size > 36 && size <= 72)
            totalPage = 2;
        this.scenarioHash.clear();
        for (Scenario scenario : Scenario.values()) {
            if (scenario.getPage() == page)
                this.scenarioHash.put(Integer.valueOf(scenario.getSlot()), scenario);
        }
        for (Map.Entry<Integer, Scenario> entry : this.scenarioHash.entrySet())
            inventory.setItem(((Integer)entry.getKey()).intValue(), ((Scenario)entry.getValue()).getItem());
        ItemStack itemStack = inventory.getItem(35);
        if (itemStack != null && itemStack.getType() != Material.AIR)
            inventory.setItem(43, (new ItemCreator(Material.ITEM_FRAME)).setName("+ (page + 1) + "+ totalPage + ")").getItem());
        if (page > 1)
            inventory.setItem(37, (new ItemCreator(Material.ITEM_FRAME)).setName("+ (page - 1) + "+ totalPage + ")").getItem());
        inventory.setItem(49, CommonItems.GUI_BACK_ITEM.getItem());
        player.openInventory(inventory);
        return inventory;
    }

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        ClickType clickType = event.getClick();
        int totalPage = 1;
        int size = (Scenario.values()).length;
        if (size > 36 && size <= 72)
            totalPage = 2;
        if (itemStack == null || !itemStack.hasItemMeta())
            return;
        if (inventory.getName().contains("- Page")) {
            event.setCancelled(true);
            int slot = event.getSlot();
            int page = ((Integer)player_page.get(player)).intValue();
            if (this.scenarioHash.containsKey(Integer.valueOf(slot))) {
                Scenario scenario = this.scenarioHash.get(Integer.valueOf(slot));
                if (scenario.isConfigurable()) {
                    if (clickType.isRightClick()) {
                        new ScenarioTimeGUI(this.main, player, scenario, scenario.getValue());
                        return;
                    }
                    if (clickType.isLeftClick())
                        scenario.getScenarioManager().activeScenario();
                } else {
                    scenario.getScenarioManager().activeScenario();
                }
                openInventory(player, page);
            } else {
                switch (slot) {
                    case 43:
                        if (page < totalPage)
                            page++;
                        openInventory(player, page);
                        break;
                    case 37:
                        if (page > 1)
                            page--;
                        openInventory(player, page);
                        break;
                    case 49:
                        this.main.openInventory(player, ConfigMainGUI.class);
                        break;
                }
            }
        }
    }
}
