package fr.clickdroit.api.config.borderValue;

import fr.clickdroit.api.config.ConfigMainGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class BorderManagerGUI implements CustomInventory {
    private final GameManager gameManager;
    private final GameConfig gameConfig;

    // URLs des textures pour les têtes personnalisées
    private static final String LEFT_ARROW_URL = "http://textures.minecraft.net/texture/37aee9a75bf0df7897183015cca0b2a7d755c63388ff01752d5f4419fc645";
    private static final String BLUE_GLASS_URL = "http://textures.minecraft.net/texture/b5652ec33bb8abc6315093d5dfe0f34d474c277da9b0f2a726d504864e310093";
    private static final String RED_GLASS_URL = "http://textures.minecraft.net/texture/b254e2426d2acc84c5c658869a86d7a1cc9a4f604866aae467b5b49665404f5d";
    private static final String YELLOW_WATCH_URL = "http://textures.minecraft.net/texture/e4d39b99ffdbd6b5a205dd69b095b5d463c66c1d537e3744eb82a9d5fecc15b9";

    public BorderManagerGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    @Override
    public String getName() {
        return "§f(§c!§f) §cGestion des bordures";
    }

    @Override
    public int getSlots() {
        return 45; // 5 rangées
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[45];

        // Remplir d'abord avec du verre gris pour la bordure
        fillWithGlass(slots);

        // Item central haut - Bordure initiale (avec tête personnalisée)
        slots[12] = createBorderStartHead();

        // Item central milieu - Vitesse bordure (avec tête personnalisée)
        slots[13] = createBorderSpeedHead();

        // Item central bas - Bordure finale (avec tête personnalisée)
        slots[14] = createBorderEndHead();

        // Flèche de retour avec une tête personnalisée (ligne du bas, centre)
        slots[40] = createBackArrowHead("§fRevenir en arrière", "§7Retourner au menu principal");

        return () -> slots;
    }

    /**
     * Remplit les slots vides avec du verre coloré pour la bordure
     */
    private void fillWithGlass(ItemStack[] slots) {
        ItemStack glassPane = new ItemCreator(Material.STAINED_GLASS_PANE)
                .setAmount(1)
                .setDurability((short) 7) // Gris clair
                .setName("§0") // Nom invisible
                .getItem();

        // Positions spécifiques pour la bordure
        Integer[] glassPositions = {
                0, 1, 7, 8, 9, 17, 18, 19, 25, 26, 27, 35, 36, 37, 43, 44
        };

        // Remplir d'abord tous les slots
        for (int i = 0; i < slots.length; i++) {
            slots[i] = glassPane;
        }
    }

    /**
     * Crée la tête pour la bordure initiale
     */
    private ItemStack createBorderStartHead() {
        return new ItemCreator(Material.SKULL_ITEM)
                .setAmount(1)
                .setDurability((short) 3)
                .setName("§8| §fBordure initiale §8(§c" + this.gameConfig.getBorderStartSize() + "§8)")
                .addLore("")
                .addLore("  §8| §fCliquez ici pour définir la taille")
                .addLore("  §8| §fde la bordure initiale de la partie.")
                .addLore("")
                .addLore("  §8| §fTaille actuelle: §b" + this.gameConfig.getBorderStartSize() + "x" + this.gameConfig.getBorderStartSize() + " blocs")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .setSkullURL("http://textures.minecraft.net/texture/b5652ec33bb8abc6315093d5dfe0f34d474c277da9b0f2a726d504864e310093")
                .getItem();
    }

    /**
     * Crée la tête pour la vitesse de bordure
     */
    private ItemStack createBorderSpeedHead() {
        return new ItemCreator(Material.SKULL_ITEM)
                .setAmount(1)
                .setDurability((short) 3)
                .setName("§8| §fVitesse de la bordure §8(§c" + this.gameConfig.getBorderBlocksPerSecond() + " bloc(s)/s§8)")
                .addLore("")
                .addLore("  §8| §fCliquez ici pour définir la vitesse")
                .addLore("  §8| §fde réduction de la bordure.")
                .addLore("")
                .addLore("  §8| §fVitesse actuelle: §e" + this.gameConfig.getBorderBlocksPerSecond() + " bloc(s) par seconde")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .setSkullURL("http://textures.minecraft.net/texture/e4d39b99ffdbd6b5a205dd69b095b5d463c66c1d537e3744eb82a9d5fecc15b9")
                .getItem();
    }

    /**
     * Crée la tête pour la bordure finale
     */
    private ItemStack createBorderEndHead() {
        return new ItemCreator(Material.SKULL_ITEM)
                .setAmount(1)
                .setDurability((short) 3)
                .setName("§8| §fBordure finale §8(§c" + this.gameConfig.getBorderEndSize() + "§8)")
                .addLore("")
                .addLore("  §8| §fCliquez ici pour définir la taille")
                .addLore("  §8| §fde la bordure finale de la partie.")
                .addLore("")
                .addLore("  §8| §fTaille actuelle: §c" + this.gameConfig.getBorderEndSize() + "x" + this.gameConfig.getBorderEndSize() + " blocs")
                .addLore("")
                .addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage())
                .addLore("")
                .setSkullURL("http://textures.minecraft.net/texture/b254e2426d2acc84c5c658869a86d7a1cc9a4f604866aae467b5b49665404f5d")
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

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        // Gestion des têtes personnalisées
        if (clickedItem.getType() == Material.SKULL_ITEM) {
            String itemName = clickedItem.getItemMeta().getDisplayName();

            // Gestion de la bordure initiale
            if (itemName.contains("§fBordure initiale")) {
                this.gameManager.getApi().openInventory(player, BorderStartSizeGUI.class);
            }
            // Gestion de la vitesse de bordure
            else if (itemName.contains("§fVitesse de la bordure")) {
                this.gameManager.getApi().openInventory(player, BorderSpeedGUI.class);
            }
            // Gestion de la bordure finale
            else if (itemName.contains("§fBordure finale")) {
                this.gameManager.getApi().openInventory(player, BorderEndSizeGUI.class);
            }
            // Gestion du retour
            else if (itemName.contains("§fRevenir en arrière")) {
                this.gameManager.getApi().openInventory(player, ConfigMainGUI.class);
            }
        }
    }

    @Override
    public int getRows() {
        return 5; // 45 slots (5 rangées de 9)
    }
}
