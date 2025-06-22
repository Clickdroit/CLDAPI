package fr.clickdroit.api.config.teamvalue;

import fr.clickdroit.api.API;
import fr.clickdroit.api.common.player.PlayerUtils;
import fr.clickdroit.api.config.ConfigMainGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class TeamManagerGUI implements CustomInventory {
    private final API api;

    private final GameManager gameManager;

    public TeamManagerGUI(API api) {
        this.api = api;
        this.gameManager = api.getGameManager();
    }

    public String getName() {
        return "Gestion des ";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        slots[3] = (new ItemCreator(Material.DIAMOND)).setName("des" )
                .addLore("de modifier l'affichage des")
                .addLore("dans dans le menu correspondant")
                .addLore("fonction du nombre de slots")
                .addLore("en affichant toute les disponibles.")
                .addLore("")
                .addLore(""+ (this.gameManager.getGameConfig().isShowAllTeams() ? "Toutes les" : "Nombre de slots"))
      .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
        slots[4] = (new ItemCreator(Material.BLAZE_POWDER)).setName("Fire")
                .addLore("ici pour autoriser")
                .addLore("non le friendly fire.")
                .addLore("")
                .addLore(""+ (this.gameManager.getGameConfig().isFriendlyfire() ? "": ""))
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
        slots[5] = (new ItemCreator(Material.BANNER)).setDurability(Integer.valueOf(15)).setName("").setAmount(Integer.valueOf(this.gameManager.getGameConfig().getPlayerPerTeam()))
                .addLore("ici pour modifier")
                .addLore("nombre de joueurs")
                .addLore("dans chaque ")
                        .addLore("")
                        .addLore(""+ this.gameManager.getGameConfig().getPlayerPerTeam() + "vs" + this.gameManager.getGameConfig().getPlayerPerTeam())
                                .addLore("")
                                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                                .addLore("")
                                .getItem();
        slots[13] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case DIAMOND:
                this.gameManager.getGameConfig().setShowAllTeams(!this.gameManager.getGameConfig().isShowAllTeams());
                this.gameManager.getTeamManager().resetTeams();
                break;
            case BANNER:
                changePlayerPerTeam(clickType);
                break;
            case BLAZE_POWDER:
                this.gameManager.getGameConfig().setFriendlyfire(!this.gameManager.getGameConfig().isFriendlyfire());
                break;
            case ARROW:
                this.api.openInventory(player, ConfigMainGUI.class);
                return;
        }
        this.api.openInventory(player, getClass());
    }

    private void changePlayerPerTeam(ClickType clickType) {
        this.gameManager.getTeamManager().resetTeams();
        GameConfig gameConfig = this.gameManager.getGameConfig();
        gameConfig.setPlayerPerTeam(clickType.isRightClick() ? (gameConfig.getPlayerPerTeam() - 1) : (gameConfig.getPlayerPerTeam() + 1));
        Bukkit.getOnlinePlayers().forEach(players -> PlayerUtils.giveDefaultItems(players));
    }

    public int getRows() {
        return 2;
    }
}

