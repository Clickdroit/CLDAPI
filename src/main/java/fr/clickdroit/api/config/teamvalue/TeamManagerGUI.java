package fr.clickdroit.api.config.teamvalue;

import fr.clickdroit.api.API;
import fr.clickdroit.api.common.player.PlayerUtils;
import fr.clickdroit.api.config.ConfigMainGUI;
import fr.clickdroit.api.config.GameConfig;
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
    private final GameConfig gameConfig;

    // URL de la texture pour la tête de retour
    private static final String LEFT_ARROW_URL = "http://textures.minecraft.net/texture/37aee9a75bf0df7897183015cca0b2a7d755c63388ff01752d5f4419fc645";

    public TeamManagerGUI(API api) {
        this.api = api;
        this.gameManager = api.getGameManager();
        this.gameConfig = gameManager.getGameConfig();
    }

    /**
     * Crée une tête flèche pour le retour
     */
    private ItemStack createBackArrowHead(String name, String lore) {
        return new ItemCreator(Material.SKULL_ITEM)
                .setAmount(1)
                .setDurability((short) 3)
                .setName(name)
                .addLore("")
                .addLore("  " + lore)
                .addLore("")
                .addLore("§e▶ Cliquez pour retourner")
                .setSkullURL(LEFT_ARROW_URL)
                .getItem();
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        // Gestion des clics sur les items principaux
        if (clickedItem == null || clickedItem.getType() == Material.STAINED_GLASS_PANE) {
            return; // Ignorer les clics sur le verre
        }

        switch (slot) {
            case 11: // Affichage des équipes (DIAMOND)
                this.gameConfig.setShowAllTeams(!this.gameConfig.isShowAllTeams());
                this.gameManager.getTeamManager().resetTeams();
                break;

            case 13: // Friendly Fire (BLAZE_POWDER)
                this.gameConfig.setFriendlyfire(!this.gameConfig.isFriendlyfire());
                break;

            case 15: // Joueurs par équipe (BANNER) - Gestion clic gauche/droit
                changePlayerPerTeam(clickType);
                break;

            case 22: // Retour
                this.api.openInventory(player, ConfigMainGUI.class);
                return;
        }

        // Rafraîchir l'inventaire
        this.api.openInventory(player, getClass());
    }

     public String getName() {
     return "Gestion des équipes";
     }

     @Override
     public int getSlots() {
     return 27; // 3 rangées
     }

     @Override
     public Supplier<ItemStack[]> getContents(Player player) {
     ItemStack[] slots = new ItemStack[27];

     // Remplir d'abord avec du verre gris pour la bordure
     fillWithGlass(slots);

     // Item central gauche - Affichage des équipes (slot 11)
     slots[11] = createTeamDisplayItem();

     // Item central centre - Friendly Fire (slot 13)
     slots[13] = createFriendlyFireItem();

     // Item central droite - Joueurs par équipe (slot 15)
     slots[15] = createPlayersPerTeamItem();

     // Flèche de retour avec une tête personnalisée (ligne du bas, centre - slot 22)
     slots[22] = createBackArrowHead("§fRevenir en arrière", "§7Retourner au menu principal");

     return () -> slots;
     }

     /**
      * Remplit les slots vides avec du verre coloré pour la bordure
     */
    private void fillWithGlass(ItemStack[] slots) {
        ItemStack glassPane = new ItemCreator(Material.STAINED_GLASS_PANE)
                .setAmount(1)
                .setDurability((short) 7) // Gris
                .setName("§0") // Nom invisible
                .getItem();

        // Remplir tous les slots d'abord
        for (int i = 0; i < slots.length; i++) {
            slots[i] = glassPane;
        }
    }

    /**
     * Crée l'item pour l'affichage des équipes
     */
    private ItemStack createTeamDisplayItem() {
        return new ItemCreator(Material.DIAMOND)
                .setName("§8| §eAffichage des équipes")
                .addLore("")
                .addLore("  §8| §fPermet de modifier l'affichage des")
                .addLore("  §8| §féquipes dans le menu correspondant")
                .addLore("  §8| §fen fonction du nombre de slots")
                .addLore("  §8| §fou en affichant toutes les disponibles.")
                .addLore("")
                .addLore(" §8> §fÉtat: §c" + (this.gameConfig.isShowAllTeams() ? "Toutes les équipes" : "Nombre de slots"))
                .addLore("")
                .addLore("§e▶ " + CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
    }

    /**
     * Crée l'item pour le friendly fire
     */
    private ItemStack createFriendlyFireItem() {
        return new ItemCreator(Material.BLAZE_POWDER)
                .setName("§8| §6Friendly Fire")
                .addLore("")
                .addLore("  §8| §fCliquez ici pour autoriser")
                .addLore("  §8| §fou non le friendly fire.")
                .addLore("")
                .addLore(" §8> §fConfiguration: §c" + (this.gameConfig.isFriendlyfire() ? "§aOui" : "§cNon"))
                .addLore("")
                .addLore("§e▶ " + CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
    }

    /**
     * Crée l'item pour les joueurs par équipe
     */
    private ItemStack createPlayersPerTeamItem() {
        int playersPerTeam = this.gameConfig.getPlayerPerTeam();
        return new ItemCreator(Material.BANNER)
                .setDurability((short) 15)
                .setAmount(playersPerTeam)
                .setName("§8| §aJoueurs par équipe")
                .addLore("")
                .addLore("  §8| §fGestion du nombre de joueurs")
                .addLore("  §8| §fprésent dans chaque équipe.")
                .addLore("")
                .addLore(" §8> §fConfiguration: §c" + playersPerTeam + "vs" + playersPerTeam)
                .addLore("")
                .addLore("§e▶ §fClic gauche: §a+1 joueur")
                .addLore("§e▶ §fClic droit: §c-1 joueur")
                .addLore("")
                .getItem();
    }

    /**
     * Change le nombre de joueurs par équipe selon le type de clic
     */
    private void changePlayerPerTeam(ClickType clickType) {
        this.gameManager.getTeamManager().resetTeams();
        GameConfig gameConfig = this.gameManager.getGameConfig();
        int newValue = clickType.isRightClick() ?
                (gameConfig.getPlayerPerTeam() - 1) :
                (gameConfig.getPlayerPerTeam() + 1);

        // Limiter les valeurs entre 1 et 10
        newValue = Math.max(1, Math.min(10, newValue));
        gameConfig.setPlayerPerTeam(newValue);

        Bukkit.getOnlinePlayers().forEach(players -> PlayerUtils.giveDefaultItems(players));
    }

    @Override
    public int getRows() {
        return 3; // 3 rangées pour 27 slots
    }
}