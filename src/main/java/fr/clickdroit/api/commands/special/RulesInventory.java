package fr.clickdroit.api.commands.special;

import fr.clickdroit.api.common.rules.items.DropItemRate;
import fr.clickdroit.api.common.rules.items.GeneralRules;
import fr.clickdroit.api.common.rules.items.UseItems;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.common.potion.PotionManagerGUI;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.Chrono;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.item.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class RulesInventory implements CustomInventory {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    public RulesInventory(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    public String getName() {
        return "Règles";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        int result = Chrono.getCycleDurationTime(this.gameConfig.getDayNightDuration());
        ItemCreator infoItem = (new ItemCreator(Material.ITEM_FRAME)).setName("§6Informations").addLore("§fSlots: §6"+ this.gameConfig.getGameSlot()).addLore("§fJoueur(s) par équipe: §6"+ this.gameConfig.getPlayerPerTeam()).addLore("§fLimite de diamants: §6"+ this.gameConfig.getDiamondMax()).addLore("§fLimite d'ors: §6"+ this.gameConfig.getGoldMax()).addLore("§fCylce jour/nuit: §6"+ result + "min").addLore("§fNether: " + (this.gameConfig.isNether() ? "§aActivé": "§cDesactivé")).addLore("§fSpectateurs: " + (this.gameConfig.isSpectators() ? "§aActivé": "§cDésactivé"));
        if (this.gameConfig.getPlayerPerTeam() > 1)
            infoItem.addLore("§6Friendly Fire: " + (this.gameConfig.isFriendlyfire() ? "§aActivé": "§cDesactivé"));
        slots[4] = infoItem.getItem();
        slots[10] = (new ItemCreator(Material.WATCH)).setName("§6Temps")
                .addLore("§fPvP: §a"+ Chrono.timeToDigitalString(this.gameConfig.getPvpTime()))
                .addLore("§fBordure: §a"+ Chrono.timeToDigitalString(this.gameConfig.getBorderTime()))
                .getItem();

        // Section potions corrigée avec le nouveau système de catégories
        ItemCreator potion = (new ItemCreator(Material.POTION)).setName("§6Potions activées");
        for (PotionManagerGUI.Potions potions : PotionManagerGUI.Potions.values()) {
            String status = potions.isEnabled() ? "§aActivée" : "§cDésactivée";

            // Ajouter les détails des catégories si la potion est activée
            if (potions.isEnabled()) {
                StringBuilder categoryDetails = new StringBuilder();

                // Vérifier quelles catégories sont disponibles et activées
                for (PotionManagerGUI.PotionCategory category : potions.getAvailableCategories()) {
                    if (category.isEnabled(potions)) {
                        switch (category) {
                            case SPLASH:
                                if (categoryDetails.length() > 0) categoryDetails.append("§8, ");
                                categoryDetails.append("§aSplash");
                                break;
                            case NIVEAU_II:
                                if (categoryDetails.length() > 0) categoryDetails.append("§8, ");
                                categoryDetails.append("§aNv2");
                                break;
                            case LONGUE_DUREE:
                                if (categoryDetails.length() > 0) categoryDetails.append("§8, ");
                                categoryDetails.append("§aLongue");
                                break;
                        }
                    }
                }

                if (categoryDetails.length() > 0) {
                    status = "§aActivée §8(" + categoryDetails.toString() + "§8)";
                }
            }

            potion.addLore("§f" + potions.getName() + ": " + status);
        }
        slots[16] = potion.getItem();

        ItemCreator useItemItem = (new ItemCreator(Material.IRON_SWORD)).setName("");
        for (UseItems useItems : UseItems.values())
            useItemItem.addLore("§f"+ useItems.getName() + ": " + (useItems.isEnabled() ? "§aActivée": "§cDésactivée"));
        slots[20] = useItemItem.getItem();
        slots[22] = (new ItemCreator(Material.PRISMARINE_SHARD)).setName("§6Modde de jeu: §f"+ this.gameManager.getModuleManager().getCurrentModule().getName()).getItem();
        ItemCreator dropItemRateItem = (new ItemCreator(Material.FLINT)).setName("§6Taux de drop");
        for (DropItemRate dropItemRate : DropItemRate.values())
            dropItemRateItem.addLore("§f"+ dropItemRate.getName() + ": §a+" + dropItemRate.getAmount() + "%");
        slots[24] = dropItemRateItem.getItem();
        slots[28] = (new ItemCreator(Material.STAINED_GLASS)).setName("§6Bordure").setDurability(Integer.valueOf(9))
                .addLore("§fTaille initiale: §b"+ this.gameConfig.getBorderStartSize() + " / -" + this.gameConfig.getBorderStartSize())
                .addLore("§fTaille finale: §b"+ this.gameConfig.getBorderEndSize() + " / -" + this.gameConfig.getBorderEndSize())
                .addLore("§fBloc(s) par seconde: §b"+ this.gameConfig.getBorderBlocksPerSecond() + " bloc" + ((this.gameConfig.getBorderBlocksPerSecond() > 1) ? "s" : ""))
                .getItem();
        ItemCreator ruleItem = (new ItemCreator(Material.PAPER)).setName("§6Règles");
        for (GeneralRules rules : GeneralRules.values())
            ruleItem.addLore("§f"+ rules.getName() + ": " + (rules.isEnabled() ? "§aActivée": "§cDésactivée"));
        slots[34] = ruleItem.getItem();
        ItemCreator scenarioItem = (new ItemCreator(Material.BOOK)).setName("§6Scénarios");
        for (Scenario scenarios : Scenario.values()) {
            if (scenarios.isEnabled())
                scenarioItem.addLore("§f"+ scenarios.getName());
        }
        slots[40] = scenarioItem.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {}

    public int getRows() {
        return 5;
    }
}
