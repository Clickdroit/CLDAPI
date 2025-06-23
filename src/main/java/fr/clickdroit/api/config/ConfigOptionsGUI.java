package fr.clickdroit.api.config;

import fr.clickdroit.api.config.common.CycleManagerGUI;
import fr.clickdroit.api.config.common.DefaultDeathInvGUI;
import fr.clickdroit.api.config.common.DefaultInvGUI;
import fr.clickdroit.api.config.common.EnchantMaxGUI;
import fr.clickdroit.api.config.common.ores.OresLimitGUI;
import fr.clickdroit.api.config.common.potion.PotionManagerGUI;
import fr.clickdroit.api.config.common.rules.DropItemRateGUI;
import fr.clickdroit.api.config.common.rules.GeneralRulesGUI;
import fr.clickdroit.api.config.intValue.DamageGUI;
import fr.clickdroit.api.config.intValue.DeconnexionTimeGUI;
import fr.clickdroit.api.config.timevalue.BorderTimeGUI;
import fr.clickdroit.api.config.timevalue.PvPTimeGUI;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.config.value.OpenVar;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class ConfigOptionsGUI implements CustomInventory {
    private GameManager gameManager;

    public ConfigOptionsGUI(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public String getName() {
        return "§f(§c!§f) §cOptions";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        Integer[] glass = {
                Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(9), Integer.valueOf(17), Integer.valueOf(27), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(37),
                Integer.valueOf(43), Integer.valueOf(44) };
        Integer[] arrayOfInteger1;
        int i;
        byte b;
        for (arrayOfInteger1 = glass, i = arrayOfInteger1.length, b = 0; b < i; ) {
            int j = arrayOfInteger1[b].intValue();
            slots[j] = (new ItemCreator(Material.STAINED_GLASS_PANE)).setDurability(Integer.valueOf(11)).setName("").getItem();
            b = (byte)(b + 1);
        }
        slots[10] = OpenVar.PVP_TIME.getItem();
        slots[11] = OpenVar.BORDER_TIME.getItem();
        slots[15] = (new ItemCreator(Material.DIAMOND)).setName("§8| §fLimite de §eminerais" )
                .addLore("")
                .addLore("  §8| §fVous permet de limiter le nombre")
                .addLore("  §8| §fde diamants et d'ors minables.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage())
                .addLore("")
                .getItem();
        slots[16] = (new ItemCreator(Material.ENCHANTED_BOOK)).setName("§8| §fLimite d'§benchantements" )
                .addLore("")
                .addLore("  §8| §fVous permet de définir")
                .addLore("  §8| §fla limite des tous")
                .addLore("  §8| §fles enchantements.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
        slots[18] = (new ItemCreator(Material.COMPASS)).setName("§8| §fTemps avant mort de §6déconnexion")
                .addLore("")
                .addLore("  §8| §fVous permet de configurer")
                .addLore("  §8| §fle temps necéssaire cpour")
                .addLore("  §8| §fmourir de déconnexion.")
                .addLore("")
                .addLore(" §8> §fConfiguration: §c"+ this.gameManager.getGameConfig().getDisconnectMinute() + " minute(s)")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
        slots[22] = (new ItemCreator(Material.CHEST)).setName("§8| §fInventaire par §cdéfault" ).addLore("").addLore("  §8| §fVous permet de dpar ")
                .addLore("")
                .addLore("  §8| §fVous permet de configurer")
                .addLore("  §8| §fle temps necéssaire pour")
                .addLore("  §8| §fdonné en début de partie.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
        slots[26] = (new ItemCreator(Material.CHEST)).setName("§8| §fInventaire de §cmort" )
                .addLore("")
                .addLore("  §8| §fVous permet de définir")
                .addLore("  §8| §fl'inventaire de §cmort")
                .addLore("  §8| §fdonné lors d'une §cmort§f.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
        slots[28] = OpenVar.CYCLE_DURATION.getItem();
        slots[29] = (new ItemCreator(Material.POTION)).setName("§8| §fLimite de §9potions")
                .addLore("")
                .addLore("  §8| §fVous permet de limiter la")
                .addLore("  §8| §ffabrication de certaines potions")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage())
                .addLore("")
                .getItem();
        slots[33] = (new ItemCreator(Material.PAPER)).setName("§8| §7Règles")
                .addLore("")
                .addLore("  §8| §fVous permet de définir")
                .addLore("  §8| §fles règles rpour la partie.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();
        slots[34] = (new ItemCreator(Material.APPLE)).setName("§8| §fTaux de §7drop" )
                .addLore("")
                .addLore("  §8| §fVous permet de modifier les")
                .addLore("  §8| §ftaux de drop de certains objets.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage())
                .addLore("")
                .getItem();
        slots[40] = CommonItems.GUI_BACK_ITEM.getItem();
        return () -> slots;
    }

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case DIAMOND_SWORD:
                this.gameManager.getApi().openInventory(player, PvPTimeGUI.class);
                break;
            case STAINED_GLASS:
                this.gameManager.getApi().openInventory(player, BorderTimeGUI.class);
                break;
            case CHEST:
                if (slot == 22)
                    this.gameManager.getApi().openInventory(player, DefaultInvGUI.class);
                if (slot == 26)
                    this.gameManager.getApi().openInventory(player, DefaultDeathInvGUI.class);
                break;
            case WATCH:
                this.gameManager.getApi().openInventory(player, CycleManagerGUI.class);
                break;
            case PAPER:
                this.gameManager.getApi().openInventory(player, GeneralRulesGUI.class);
                break;
            case APPLE:
                this.gameManager.getApi().openInventory(player, DropItemRateGUI.class);
                break;
            case DIAMOND:
                this.gameManager.getApi().openInventory(player, OresLimitGUI.class);
                break;
            case POTION:
                this.gameManager.getApi().openInventory(player, PotionManagerGUI.class);
                break;
            case ENCHANTED_BOOK:
                this.gameManager.getApi().openInventory(player, EnchantMaxGUI.class);
                break;
            case ENDER_PEARL:
                this.gameManager.getApi().openInventory(player, DamageGUI.class);
                break;
            case COMPASS:
                this.gameManager.getApi().openInventory(player, DeconnexionTimeGUI.class);
                break;
            case ARROW:
                this.gameManager.getApi().openInventory(player, ConfigMainGUI.class);
                break;
        }
    }

    public int getRows() {
        return 5;
    }
}

