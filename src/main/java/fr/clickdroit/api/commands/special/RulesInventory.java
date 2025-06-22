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
import fr.clickdroit.api.utils.ItemCreator;
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
        return "R";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        int result = Chrono.getCycleDurationTime(this.gameConfig.getDayNightDuration());
        ItemCreator infoItem = (new ItemCreator(Material.ITEM_FRAME)).setName(").addLore("+ this.gameConfig.getGameSlot()).addLore("par "+ this.gameConfig.getPlayerPerTeam()).addLore("de diamants: "+ this.gameConfig.getDiamondMax()).addLore("d'ors: "+ this.gameConfig.getGoldMax()).addLore("jour/nuit: "+ result + "min").addLore("" + (this.gameConfig.isNether() ? "§aActive": "§cDesactive")).addLore("" + (this.gameConfig.isSpectators() ? "§aActive": "§cDesactive"));
        if (this.gameConfig.getPlayerPerTeam() > 1)
            infoItem.addLore("Fire: " + (this.gameConfig.isFriendlyfire() ? "§aActive": "§cDesactive"));
        slots[4] = infoItem.getItem();
        slots[10] = (new ItemCreator(Material.WATCH)).setName("")
                .addLore(""+ Chrono.timeToDigitalString(this.gameConfig.getPvpTime()))
                .addLore(""+ Chrono.timeToDigitalString(this.gameConfig.getBorderTime()))
                .getItem();
        ItemCreator potion = (new ItemCreator(Material.POTION)).setName("activ");
        for (PotionManagerGUI.Potions potions : PotionManagerGUI.Potions.values())
            potion.addLore(""+ potions.getName() + ": " + (potions.isEnabled() ? "§aActive": "§cDesactive"));
        slots[16] = potion.getItem();
        ItemCreator useItemItem = (new ItemCreator(Material.IRON_SWORD)).setName("");
        for (UseItems useItems : UseItems.values())
            useItemItem.addLore(""+ useItems.getName() + ": " + (useItems.isEnabled() ? "§aActive": "§cDesactive"));
        slots[20] = useItemItem.getItem();
        slots[22] = (new ItemCreator(Material.PRISMARINE_SHARD)).setName("de jeu: "+ this.gameManager.getModuleManager().getCurrentModule().getName()).getItem();
        ItemCreator dropItemRateItem = (new ItemCreator(Material.FLINT)).setName("de drop");
        for (DropItemRate dropItemRate : DropItemRate.values())
            dropItemRateItem.addLore(""+ dropItemRate.getName() + ":" + dropItemRate.getAmount() + "%");
        slots[24] = dropItemRateItem.getItem();
        slots[28] = (new ItemCreator(Material.STAINED_GLASS)).setName("").setDurability(Integer.valueOf(9))
                .addLore("initiale: "+ this.gameConfig.getBorderStartSize() + " / -" + this.gameConfig.getBorderStartSize())
                .addLore("finale: "+ this.gameConfig.getBorderEndSize() + " / -" + this.gameConfig.getBorderEndSize())
                .addLore("par seconde: "+ this.gameConfig.getBorderBlocksPerSecond() + " bloc" + ((this.gameConfig.getBorderBlocksPerSecond() > 1) ? "s" : ""))
                .getItem();
        ItemCreator ruleItem = (new ItemCreator(Material.PAPER)).setName("");
        for (GeneralRules rules : GeneralRules.values())
            ruleItem.addLore(""+ rules.getName() + ": " + (rules.isEnabled() ? "§aActive": "§cDesactive"));
        slots[34] = ruleItem.getItem();
        ItemCreator scenarioItem = (new ItemCreator(Material.BOOK)).setName("");
        for (Scenario scenarios : Scenario.values()) {
            if (scenarios.isEnabled())
                scenarioItem.addLore(""+ scenarios.getName());
        }
        slots[40] = scenarioItem.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {}

    public int getRows() {
        return 5;
    }
}
