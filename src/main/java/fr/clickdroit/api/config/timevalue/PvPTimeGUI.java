package fr.clickdroit.api.config.timevalue;

import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.item.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PvPTimeGUI implements CustomInventory {
    private final GameManager gameManager;
    private final GameConfig gameConfig;

    // URLs des textures pour les têtes personnalisées
    private static final String RED_MINUS_URL = "http://textures.minecraft.net/texture/935e4e26eafc11b52c11668e1d6634e7d1d0d21c411cb085f9394268eb4cdfba";
    private static final String GREEN_PLUS_URL = "http://textures.minecraft.net/texture/9a2d891c6ae9f6baa040d736ab84d48344bb6b70d7f1a280dd12cbac4d777";
    private static final String LEFT_ARROW_URL = "http://textures.minecraft.net/texture/37aee9a75bf0df7897183015cca0b2a7d755c63388ff01752d5f4419fc645";

    public PvPTimeGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    @Override
    public String getName() {
        return "PvP - Temps d'activation";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[27];

        // Remplir d'abord avec du verre gris pour la bordure
        fillWithGlass(slots);

        // Ligne du haut - Têtes pour diminuer (-60, -30, -10)
        slots[10] = createRedMinusHead("-60", "§7Diminuer de 60 secondes");
        slots[11] = createRedMinusHead("-30", "§7Diminuer de 30 secondes");
        slots[12] = createRedMinusHead("-10", "§7Diminuer de 10 secondes");

        // Item central - ÉPÉE EN DIAMANT (PvP)
        slots[13] = new ItemCreator(Material.DIAMOND_SWORD)
                .setName("§8| §fTemps d'activation du §cPvP")
                .addLore("")
                .addLore("  §8| §fVous permet de §cmodifier")
                .addLore("  §8| §fle temps avant l'§cactivation")
                .addLore("  §8| §fdu §cPvP §fdans la partie.")
                .addLore("")
                .addLore(" §8> §fConfiguration: §c" + formatTime(this.gameConfig.getPvpTime()))
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .getItem();

        // Ligne du haut - Têtes pour augmenter (+10, +30, +60)
        slots[14] = createGreenPlusHead("+10", "§7Augmenter de 10 secondes");
        slots[15] = createGreenPlusHead("+30", "§7Augmenter de 30 secondes");
        slots[16] = createGreenPlusHead("+60", "§7Augmenter de 60 secondes");

        // Flèche de retour avec une tête personnalisée (ligne du bas, centre)
        slots[22] = createBackArrowHead("§fRevenir en arrière", "§7Retourner au menu des options");

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
     * Crée une tête rouge avec un moins pour diminuer
     */
    private ItemStack createRedMinusHead(String name, String lore) {
        return new ItemCreator(Material.SKULL_ITEM)
                .setAmount(1)
                .setDurability((short) 3)
                .setName("§c§l" + name)
                .addLore("")
                .addLore("  " + lore)
                .addLore("")
                .addLore("§e▶ Cliquez pour diminuer")
                .setSkullURL(RED_MINUS_URL)
                .getItem();
    }

    /**
     * Crée une tête verte avec un plus pour augmenter
     */
    private ItemStack createGreenPlusHead(String name, String lore) {
        return new ItemCreator(Material.SKULL_ITEM)
                .setAmount(1)
                .setDurability((short) 3)
                .setName("§a§l" + name)
                .addLore("")
                .addLore("  " + lore)
                .addLore("")
                .addLore("§e▶ Cliquez pour augmenter")
                .setSkullURL(GREEN_PLUS_URL)
                .getItem();
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

    /**
     * Formate le temps en secondes vers un format lisible (ex: "5m 30s")
     */
    private String formatTime(int seconds) {
        if (seconds < 60) {
            return seconds + "s";
        } else if (seconds % 60 == 0) {
            return (seconds / 60) + "m";
        } else {
            return (seconds / 60) + "m " + (seconds % 60) + "s";
        }
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        // Gestion des têtes personnalisées
        if (clickedItem.getType() == Material.SKULL_ITEM) {
            String itemName = clickedItem.getItemMeta().getDisplayName();

            // Gestion des têtes de diminution (rouges)
            if (itemName.contains("§c§l-10")) {
                this.gameConfig.setPvpTime(this.gameConfig.getPvpTime() - 10);
                player.playSound(player.getLocation(), Sound.NOTE_PLING,1.0F,0.8F);
            } else if (itemName.contains("§c§l-30")) {
                this.gameConfig.setPvpTime(this.gameConfig.getPvpTime() - 30);
                player.playSound(player.getLocation(), Sound.NOTE_PLING,1.0F,0.8F);
            } else if (itemName.contains("§c§l-60")) {
                this.gameConfig.setPvpTime(this.gameConfig.getPvpTime() - 60);
                player.playSound(player.getLocation(), Sound.NOTE_PLING,1.0F,0.8F);
            }
            // Gestion des têtes d'augmentation (vertes)
            else if (itemName.contains("§a§l+10")) {
                this.gameConfig.setPvpTime(this.gameConfig.getPvpTime() + 10);
                player.playSound(player.getLocation(), Sound.NOTE_PLING,1.0F,1.2F);
            } else if (itemName.contains("§a§l+30")) {
                this.gameConfig.setPvpTime(this.gameConfig.getPvpTime() + 30);
                player.playSound(player.getLocation(), Sound.NOTE_PLING,1.0F,1.2F);
            } else if (itemName.contains("§a§l+60")) {
                this.gameConfig.setPvpTime(this.gameConfig.getPvpTime() + 60);
                player.playSound(player.getLocation(), Sound.NOTE_PLING,1.0F,1.2F);
            }
            else if (itemName.contains("§fRevenir en arrière")) {
                this.gameManager.getApi().openInventory(player, ConfigOptionsGUI.class);
                player.playSound(player.getLocation(), Sound.NOTE_STICKS,1.0F,0.8F);
                return;
            }

            if (this.gameConfig.getPvpTime() > 2400) {
                this.gameConfig.setPvpTime(2400);
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                player.sendMessage("§c§l✗ §fLe temps maximum d'activation du PvP est de §c40m§f!");
            }
            if (this.gameConfig.getPvpTime() < 60) {
                this.gameConfig.setPvpTime(60);
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                player.sendMessage("§c§l✗ §fLe temps minimum d'activation du PvP est de §c1m§f!");
            }

            // Actualiser l'inventaire
            this.gameManager.getApi().openInventory(player, getClass());
        }
    }

    @Override
    public int getRows() {
        return 3; // 27 slots (3 rangées de 9)
    }
}
