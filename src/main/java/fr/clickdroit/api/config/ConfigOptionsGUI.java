package fr.clickdroit.api.config;

import fr.clickdroit.api.config.common.CycleManagerGUI;
import fr.clickdroit.api.config.common.DefaultDeathInvGUI;
import fr.clickdroit.api.config.common.DefaultInvGUI;
import fr.clickdroit.api.config.common.potion.PotionManagerGUI;
import fr.clickdroit.api.config.common.rules.DropItemRateGUI;
import fr.clickdroit.api.config.common.rules.GameRulesManagerGUI;
import fr.clickdroit.api.config.intValue.DeconnexionTimeGUI;
import fr.clickdroit.api.config.timevalue.BorderTimeGUI;
import fr.clickdroit.api.config.timevalue.PvPTimeGUI;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.config.value.OpenVar;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.Sound;
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

    @Override
    public String getName() {
        return "§f(§c!§f) §cOptions";
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];

        // Bordures en verre bleu
        Integer[] glass = {
                0, 1,2, 3, 4, 5, 6, 7, 8, 9, 17,18, 26,27, 35, 36, 37,38,39,41,42, 43, 44
        };

        for (int glassSlot : glass) {
            slots[glassSlot] = new ItemCreator(Material.STAINED_GLASS_PANE)
                    .setDurability(11) // Bleu
                    .setName("")
                    .getItem();
        }

        // === LIGNE 1: TEMPS ===
        slots[11] = OpenVar.PVP_TIME.getItem();
        slots[12] = OpenVar.BORDER_TIME.getItem();

        // === LIGNE 2: RÈGLES ET LIMITES (NOUVEAU) ===
        slots[15] = new ItemCreator(Material.PAPER)
                .setName("§8| §6Règles & Limites")
                .addLore("")
                .addLore("  §8| §fGestion complète des règles,")
                .addLore("  §8| §flimites et enchantements.")
                .addLore("")
                .addLore("  §7§oArmures, épées, arcs, diamants,")
                .addLore("  §7§orègles générales et objets interdits.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage())
                .addLore("")
                .getItem();

        // === LIGNE 2: INVENTAIRES ===
        slots[30] = new ItemCreator(Material.CHEST)
                .setName("§8| §fInventaire par §cdéfaut")
                .addLore("")
                .addLore("  §8| §fVous permet de définir")
                .addLore("  §8| §fl'inventaire de §cdépart§f des joueurs.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();

        slots[32] = new ItemCreator(Material.CHEST)
                .setName("§8| §fInventaire de §cmort")
                .addLore("")
                .addLore("  §8| §fVous permet de définir")
                .addLore("  §8| §fl'inventaire §cspectateur§f des joueurs.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();

        // === LIGNE 3: AUTRES OPTIONS ===
        slots[14] = new ItemCreator(Material.WATCH)
                .setName("§8| §fCycle §ejour§f/§8nuit")
                .addLore("")
                .addLore("  §8| §fVous permet de configurer")
                .addLore("  §8| §fla durée du cycle jour/nuit.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();

        slots[33] = new ItemCreator(Material.APPLE)
                .setName("§8| §fTaux de §7drop")
                .addLore("")
                .addLore("  §8| §fVous permet de modifier les")
                .addLore("  §8| §ftaux de drop de certains objets.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage())
                .addLore("")
                .getItem();

        // === LIGNE 4: POTIONS ET AUTRES ===
        slots[29] = new ItemCreator(Material.POTION)
                .setName("§8| §fConfiguration des §dpotions")
                .addLore("")
                .addLore("  §8| §fVous permet d'activer ou")
                .addLore("  §8| §fde désactiver certaines potions.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage())
                .addLore("")
                .getItem();

        slots[13] = new ItemCreator(Material.COMPASS)
                .setName("§8| §fTemps de §cdeconnexion")
                .addLore("")
                .addLore("  §8| §fVous permet de configurer")
                .addLore("  §8| §fle temps avant kick pour déco.")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();

        slots[40] = CommonItems.GUI_BACK_ITEM.getItem();

        return () -> slots;
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        switch (clickedItem.getType()) {
            case DIAMOND_SWORD:
                this.gameManager.getApi().openInventory(player, PvPTimeGUI.class);
                player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,1.0F);
                break;
            case STAINED_GLASS:
                this.gameManager.getApi().openInventory(player, BorderTimeGUI.class);
                player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,1.0F);
                break;
            case CHEST:
                if (slot == 30)
                    this.gameManager.getApi().openInventory(player, DefaultInvGUI.class);
                player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,1.0F);
                if (slot == 32)
                    this.gameManager.getApi().openInventory(player, DefaultDeathInvGUI.class);
                player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,1.0F);
                break;
            case WATCH:
                this.gameManager.getApi().openInventory(player, CycleManagerGUI.class);
                player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,1.0F);
                break;
            case PAPER:
                this.gameManager.getApi().openInventory(player, GameRulesManagerGUI.class);
                player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,1.0F);
                break;
            case APPLE:
                this.gameManager.getApi().openInventory(player, DropItemRateGUI.class);
                player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,1.0F);
                break;
            case POTION:
                this.gameManager.getApi().openInventory(player, PotionManagerGUI.class);
                player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,1.0F);
                break;
            case COMPASS:
                this.gameManager.getApi().openInventory(player, DeconnexionTimeGUI.class);
                player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,1.0F);
                break;
            default:
                break;
        }
        if (clickedItem.getType() == Material.ARROW || slot == 40) {
            this.gameManager.getApi().openInventory(player, ConfigMainGUI.class);
            player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,0.8F);
        }
    }

    @Override
    public int getRows() {
        return 5;
    }
}
