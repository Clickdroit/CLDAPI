package fr.clickdroit.api.config.intValue;

import fr.clickdroit.api.config.ConfigMainGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.OpenVar;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SlotsGUI implements CustomInventory {
    private final GameManager gameManager;
    private final GameConfig gameConfig;

    // URLs des textures pour les têtes personnalisées
    private static final String RED_MINUS_URL = "http://textures.minecraft.net/texture/935e4e26eafc11b52c11668e1d6634e7d1d0d21c411cb085f9394268eb4cdfba";
    private static final String GREEN_PLUS_URL = "http://textures.minecraft.net/texture/9a2d891c6ae9f6baa040d736ab84d48344bb6b70d7f1a280dd12cbac4d777";
    private static final String LEFT_ARROW_URL = "http://textures.minecraft.net/texture/37aee9a75bf0df7897183015cca0b2a7d755c63388ff01752d5f4419fc645";

    public SlotsGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    public String getName() {
        return "Slots";
    }

    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];

        // Remplir avec du verre gris pour faire le fond
        fillWithGlass(slots);

        // Ligne du haut - Têtes pour diminuer (-10, -5, -1)
        slots[10] = createRedMinusHead("-10", "§7Diminuer de 10 slots");
        slots[11] = createRedMinusHead("-5", "§7Diminuer de 5 slots");
        slots[12] = createRedMinusHead("-1", "§7Diminuer de 1 slot");

        // Item central (configuration des slots)
        slots[13] = OpenVar.SLOTS.getItem();

        // Ligne du haut - Têtes pour augmenter (+1, +5, +10)
        slots[14] = createGreenPlusHead("+1", "§7Augmenter de 1 slot");
        slots[15] = createGreenPlusHead("+5", "§7Augmenter de 5 slots");
        slots[16] = createGreenPlusHead("+10", "§7Augmenter de 10 slots");

        // Flèche de retour avec une tête personnalisée (ligne du bas, centre)
        slots[22] = createBackArrowHead("§fRevenir en arrière", "§7Retourner au menu principal");

        return () -> slots;
    }

    /**
     * Remplit les slots vides avec du verre coloré
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

        // Les slots 10-16 (ligne du milieu) et 22 (retour) seront remplacés par les vrais items
        // Tous les autres resteront en verre pour faire une belle bordure complète
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

    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        // Gérer les clics sur les têtes personnalisées
        if (clickedItem.getType() == Material.SKULL_ITEM) {
            String itemName = clickedItem.getItemMeta().getDisplayName();

            // Gestion des têtes de diminution (rouges)
            if (itemName.contains("§c§l-1")) {
                this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() - 1);
            } else if (itemName.contains("§c§l-5")) {
                this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() - 5);
            } else if (itemName.contains("§c§l-10")) {
                this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() - 10);
            }
            // Gestion des têtes d'augmentation (vertes)
            else if (itemName.contains("§a§l+1")) {
                this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() + 1);
            } else if (itemName.contains("§a§l+5")) {
                this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() + 5);
            } else if (itemName.contains("§a§l+10")) {
                this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() + 10);
            }
            // Gestion du retour
            else if (itemName.contains("§fRevenir en arrière")) {
                this.gameManager.getApi().openInventory(player, ConfigMainGUI.class);
                return;
            }

            // Vérifier les limites
            if (this.gameConfig.getGameSlot() > 1000) {
                this.gameConfig.setGameSlot(1000);
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                player.sendMessage("§c§l✗ §fLe nombre maximum de slots est de §c1000§f!");
            }
            if (this.gameConfig.getGameSlot() < 1) {
                this.gameConfig.setGameSlot(1);
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
                player.sendMessage("§c§l✗ §fLe nombre minimum de slots est de §c1§f!");
            }

            // Jouer un son de confirmation
            player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.0F);

            // Actualiser l'inventaire
            this.gameManager.getApi().openInventory(player, getClass());
        }

        // Gestion de l'ancien système avec les bannières (au cas où)
        else if (clickedItem.getType() == Material.BANNER) {
            if (clickedItem.getDurability() == 10) {
                this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() - 1);
            } else if (clickedItem.getDurability() == 11) {
                this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() - 5);
            } else if (clickedItem.getDurability() == 12) {
                this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() - 10);
            } else if (clickedItem.getDurability() == 14) {
                this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() + 1);
            } else if (clickedItem.getDurability() == 15) {
                this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() + 5);
            } else if (clickedItem.getDurability() == 16) {
                this.gameConfig.setGameSlot(this.gameConfig.getGameSlot() + 10);

            }
            if (this.gameConfig.getGameSlot() > 1000) {
                this.gameConfig.setGameSlot(1000);
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
            }
            if (this.gameConfig.getGameSlot() < 1) {
                this.gameConfig.setGameSlot(1);
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 10.0F, 1.0F);
            }
            this.gameManager.getApi().openInventory(player, getClass());
        }
        // Gestion de la flèche normale (ancien système)
        else if (clickedItem.getType() == Material.ARROW) {
            this.gameManager.getApi().openInventory(player, ConfigMainGUI.class);
        }
    }

    public int getRows() {
        return 3; // 27 slots (3 rangées de 9)
    }
}