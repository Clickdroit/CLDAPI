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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScenariosGUI implements Listener {
    private static Map<Player, Integer> player_page = new HashMap<>();
    private static Map<Player, ScenarioCategory> player_category = new HashMap<>();
    private Map<Player, Map<Integer, Scenario>> playerScenarios = new HashMap<>();

    private final API main;

    public ScenariosGUI(API main) {
        this.main = main;
    }

    public Inventory openInventory(Player player) {
        return openInventory(player, player_page.getOrDefault(player, 1),
                player_category.getOrDefault(player, ScenarioCategory.ALL));
    }

    public Inventory openInventory(Player player, int page, ScenarioCategory category) {
        player_page.put(player, page);
        player_category.put(player, category);

        Inventory inventory = Bukkit.createInventory(null, 54, "§f(§c!§f) §cScéna' - Page " + page);

        int[] glass = { 36, 37, 38, 39, 40, 41, 42, 43, 44 };
        for (int i : glass) {
            inventory.setItem(i, new ItemCreator(Material.STAINED_GLASS_PANE).setDurability(7).getItem());
        }

        List<Scenario> filtered = new ArrayList<>();
        for (Scenario scenario : Scenario.values()) {
            if (category == ScenarioCategory.ALL || scenario.getCategory() == category) {
                filtered.add(scenario);
            }
        }

        int totalPage = (int) Math.ceil((double) filtered.size() / 36.0);
        if (totalPage == 0)
            totalPage = 1;
        if (page > totalPage)
            page = totalPage;
        if (page < 1)
            page = 1;

        Map<Integer, Scenario> currentScenarios = new HashMap<>();

        int startIndex = (page - 1) * 36;
        int endIndex = Math.min(startIndex + 36, filtered.size());

        int currentSlot = 0;
        for (int i = startIndex; i < endIndex; i++) {
            Scenario scenario = filtered.get(i);
            inventory.setItem(currentSlot, scenario.getItem());
            currentScenarios.put(currentSlot, scenario);
            currentSlot++;
        }

        playerScenarios.put(player, currentScenarios);

        if (page < totalPage) {
            inventory.setItem(43, new ItemCreator(Material.ITEM_FRAME)
                    .setName("§8| §fPage §asuivante §f(" + (page + 1) + "§a/§f" + totalPage + ")")
                    .getItem());
        }
        if (page > 1) {
            inventory.setItem(37, new ItemCreator(Material.ITEM_FRAME)
                    .setName("§8| §fPage §cprécédente §f(" + (page - 1) + "§c/§f" + totalPage + ")")
                    .getItem());
        }

        inventory.setItem(49, CommonItems.GUI_BACK_ITEM.getItem());

        ItemCreator bookFilter = new ItemCreator(Material.ENCHANTED_BOOK)
                .setName("§8» §6§lFiltre de Scénarios");

        for (ScenarioCategory cat : ScenarioCategory.values()) {
            if (cat == category) {
                bookFilter.addLore(" §8▪ §a" + cat.getDisplayName() + " §7(Actif)");
            } else {
                bookFilter.addLore(" §8▪ §7" + cat.getDisplayName());
            }
        }
        bookFilter.addLore("");
        bookFilter.addLore(" §8> §eClic Gauche §fpour précédent");
        bookFilter.addLore(" §8> §eClic Droit §fpour suivant");

        inventory.setItem(45, bookFilter.getItem());

        player.openInventory(inventory);
        return inventory;
    }

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        ClickType clickType = event.getClick();

        if (itemStack == null || !itemStack.hasItemMeta())
            return;

        if (inventory.getName().contains("§f(§c!§f) §cScéna' - Page")) {
            event.setCancelled(true);
            int slot = event.getSlot();
            int page = player_page.getOrDefault(player, 1);
            ScenarioCategory currentCat = player_category.getOrDefault(player, ScenarioCategory.ALL);

            Map<Integer, Scenario> sessionScenarios = playerScenarios.get(player);

            if (sessionScenarios != null && sessionScenarios.containsKey(slot)) {
                Scenario scenario = sessionScenarios.get(slot);
                if (scenario.isConfigurable()) {
                    if (clickType.isRightClick()) {
                        new ScenarioTimeGUI(this.main, player, scenario, scenario.getValue());
                        return;
                    }
                    if (clickType.isLeftClick()) {
                        scenario.getScenarioManager().activeScenario();
                    }
                } else {
                    scenario.getScenarioManager().activeScenario();
                }
                openInventory(player, page, currentCat);
            } else {
                switch (slot) {
                    case 43:
                        openInventory(player, page + 1, currentCat);
                        break;
                    case 37:
                        if (page > 1) {
                            openInventory(player, page - 1, currentCat);
                        }
                        break;
                    case 45:
                        ScenarioCategory newCat;
                        if (clickType.isRightClick()) {
                            newCat = ScenarioCategory.getNext(currentCat);
                        } else {
                            newCat = ScenarioCategory.getPrevious(currentCat);
                        }
                        openInventory(player, 1, newCat);
                        break;
                    case 49:
                        this.main.openInventory(player, ConfigMainGUI.class);
                        break;
                }
            }
        }
    }
}
