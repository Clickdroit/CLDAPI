package fr.clickdroit.api.config.teamvalue;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.team.Teams;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinTeamsGUI implements Listener {
    private final API api;

    private final GameManager gameManager;

    public static Map<Player, Integer> player_page = new HashMap<>();

    public JoinTeamsGUI(API api) {
        this.api = api;
        this.gameManager = api.getGameManager();
    }

    public Inventory openInventory(Player player, int page) {
        player_page.put(player, Integer.valueOf(page));
        Inventory inventory = Bukkit.createInventory(null, 54, "Équipes (" + page + "/2)");
        int[] glass = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                17, 18, 26, 27, 35, 36, 44, 45, 46, 47,
                48, 49, 50, 51, 52, 53 };
        ItemStack glassItem = (new ItemCreator(Material.STAINED_GLASS_PANE)).setDurability(Integer.valueOf(15)).getItem();
        for (int i : glass)
            inventory.setItem(i, glassItem);
        GameConfig gameConfig = this.api.getGameManager().getGameConfig();
        if (!Scenario.RANDOMTEAM.isEnabled()) {
            if (gameConfig.isShowAllTeams()) {
                for (Teams teams : Teams.values()) {
                    if (teams.getPage() == page)
                        inventory.setItem(teams.getSlot(), getItem(teams));
                }
            } else {
                int slot = gameConfig.getGameSlot();
                int player_per_team = gameConfig.getPlayerPerTeam();
                List<Teams> available_teams = new ArrayList<>();
                for (Teams teams : Teams.values())
                    available_teams.add(teams);
                double result = (slot / player_per_team);
                result++;
                for (int i = 0; i < result; i++) {
                    Teams teams = available_teams.remove(0);
                    inventory.setItem(teams.getSlot(), getItem(teams));
                }
            }
        } else {
            inventory.setItem(22, (new ItemCreator(Material.BARRIER)).setName("§cLe scénario RandomTeam est activé").getItem());
        }
        if (page == 1 && inventory
                .getItem(43) != null && inventory.getItem(43).hasItemMeta() && inventory
                .getItem(43).getType() == Material.BANNER)
            inventory.setItem(51, (new ItemCreator(Material.PAPER)).setName("§fPage suivante").getItem());
        if (page > 1)
            inventory.setItem(47, (new ItemCreator(Material.PAPER)).setName("§fPage précédente").getItem());
        inventory.setItem(49, (new ItemCreator(Material.ARROW)).setName("§fRevenir en arriere").getItem());
        player.openInventory(inventory);
        return inventory;
    }

    @EventHandler
    private void onClick(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        Inventory inventory = event.getInventory();
        if (player == null || inventory == null || itemStack == null || itemStack.getType().equals(Material.AIR))
            return;
        if (inventory.getName().contains("Équipes")) {
            event.setCancelled(true);
            int page = ((Integer)player_page.get(player)).intValue();
            int slot = event.getSlot();
            switch (itemStack.getType()) {
                case ARROW:
                    player.closeInventory();
                    break;
                case BANNER:
                    player.closeInventory();
                    for (Teams teams : Teams.values()) {
                        if (teams.getPage() == page && teams
                                .getSlot() == slot) {
                            this.gameManager.getTeamManager().addPlayerToTeam(player, teams);
                            break;
                        }
                    }
                    break;
                case PAPER:
                    if (itemStack.hasItemMeta()) {
                        if (itemStack.getItemMeta().getDisplayName().contains("précédente")) {
                            if (page > 1)
                                page--;
                        } else {
                            page++;
                        }
                        openInventory(player, page);
                    }
                    break;
            }
        }
    }

    public ItemStack getItem(Teams teams) {
        ItemCreator item = teams.getItem();
        item.addLore("");
        item.addLore("§8Membre"+ ((this.gameManager.getTeamManager().getPlayerAmountInTeam(teams) > 1) ? "s" : "") + ":");
        for (Player pl : this.gameManager.getTeamManager().getPlayersInTeam(teams))
            item.addLore("  §f- " + teams.getColor() + pl.getName());
        for (int j = this.gameManager.getTeamManager().getPlayersInTeam(teams).size(); j < this.gameManager.getGameConfig().getPlayerPerTeam(); j++)
            item.addLore("  §f- §8[Emplacement vide]");
        item.addLore("");
        item.addLore("§f> §eCliquez ici pour rejoindre.");
        return item.getItem();
    }
}

