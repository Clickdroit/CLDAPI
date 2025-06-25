package fr.clickdroit.api.config.common.rules;

import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.value.CommonItems;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class GameRulesManagerGUI implements CustomInventory {
    private final GameManager gameManager;
    private final GameConfig gameConfig;

    public GameRulesManagerGUI(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    @Override
    public String getName() {
        return "§f(§c!§f) §cGestion des règles & limites";
    }

    @Override
    public int getSlots() {
        return 45; // 5 rangées au lieu de 4
    }

    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[45]; // 5 rangées

        // Remplir avec du verre jaune pour les bordures
        fillWithGlass(slots);

        // === LIGNE 1: ARMURES ===
        // Armure en fer (slot 10)
        slots[11] = createIronArmorItem();

        // Diamant - Limite (slot 11)
        slots[13] = createDiamondLimitItem();

        // Armure en diamant (slot 12)
        slots[15] = createDiamondArmorItem();

        // === LIGNE 2: ITEMS DIVERS ===
        // Crafting table - pour les règles générales (slot 19)
        slots[19] = createGeneralRulesItem();

        // Enclume - pour les objets interdits (slot 25)
        slots[25] = createForbiddenItemsItem();

        // === LIGNE 3: ÉPÉES ET ARC ===
        // Épée en fer (slot 28)
        slots[29] = createIronSwordItem();

        // Arc (slot 29)
        slots[31] = createBowItem();

        // Épée en diamant (slot 30)
        slots[33] = createDiamondSwordItem();

        // === LIGNE 4: ESPACE LIBRE POUR D'AUTRES ITEMS ===
        // Vous pouvez ajouter d'autres items ici (slots 37, 38, 39, etc.)

        // Flèche de retour (slot 40) - utiliser CommonItems comme les autres GUI
        slots[40] = CommonItems.GUI_BACK_ITEM.getItem();

        return () -> slots;
    }

    /**
     * Remplit les slots vides avec du verre coloré pour la bordure
     */
    private void fillWithGlass(ItemStack[] slots) {
        // Verre gris pour les bordures
        ItemStack grayGlassPane = new ItemCreator(Material.STAINED_GLASS_PANE)
                .setAmount(1)
                .setDurability((short) 7) // Gris
                .setName("§0") // Nom invisible
                .getItem();

        // Ne rien mettre au centre - laisser null (vide)
        // Seulement placer le verre gris sur les bordures extérieures

        // Positions des bordures extérieures seulement
        Integer[] borderPositions = {
                // Première ligne (0-8)
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                // Côtés gauche et droit des lignes du milieu
                9, 17, 18, 26, 27, 35, 36, 44,
                // Dernière ligne (37-44) - en excluant le slot 40 qui aura la flèche
                37, 38, 39, 41, 42, 43
        };

        // Appliquer le verre gris seulement aux bordures
        for (int pos : borderPositions) {
            if (pos < slots.length) {
                slots[pos] = grayGlassPane;
            }
        }

        // Le reste des slots restent null (vides)
    }

    /**
     * Crée l'item pour accéder aux règles générales
     */
    private ItemStack createGeneralRulesItem() {
        return new ItemCreator(Material.WORKBENCH)
                .setName("§8| §6Règles générales")
                .addLore("")
                .addLore("  §8| §fGestion des règles de jeu:")
                .addLore("  §8| §7StripMining, iPvP, CrossTeam,")
                .addLore("  §8| §7Tower, DigDown, etc...")
                .addLore("")
                .addLore("§e▶ §fCliquez pour configurer")
                .addLore("  §7les règles de la partie")
                .addLore("")
                .getItem();
    }

    /**
     * Crée l'item pour accéder aux objets interdits
     */
    private ItemStack createForbiddenItemsItem() {
        return new ItemCreator(Material.ANVIL)
                .setName("§8| §cObjets interdits")
                .addLore("")
                .addLore("  §8| §fGestion des objets utilisables:")
                .addLore("  §8| §7Cannes à pêche, Seaux de lave,")
                .addLore("  §8| §7Arcs, Briquets, etc...")
                .addLore("")
                .addLore("§e▶ §fCliquez pour configurer")
                .addLore("  §7les objets autorisés/interdits")
                .addLore("")
                .getItem();
    }

    /**
     * Crée l'item de limite de diamants (avec gestion directe par clics)
     */
    private ItemStack createDiamondLimitItem() {
        int limit = this.gameConfig.getDiamondMax();
        String limitText = limit == 0 ? "Illimitée" : String.valueOf(limit);

        return new ItemCreator(Material.DIAMOND)
                .setName("§8| §bLimite de diamants")
                .addLore("")
                .addLore("  §8| §fLimite actuelle: §b" + limitText)
                .addLore("")
                .addLore("§e▶ §fClic gauche: §a+1 diamant")
                .addLore("§e▶ §fClic droit: §c-1 diamant")
                .addLore("§e▶ §fShift+clic: §a+5 §7ou §c-5")
                .addLore("")
                .addLore("  §7(0 = illimité)")
                .addLore("")
                .setAmount(Math.max(1, limit))
                .getItem();
    }

    /**
     * Crée l'item d'armure en fer (avec gestion par catégories)
     */
    private ItemStack createIronArmorItem() {
        EquipmentEnchants currentCategory = getCurrentArmorCategory(true);

        return new ItemCreator(Material.IRON_CHESTPLATE)
                .setName("§8| §7Armure en fer")
                .addLore("")
                .addLore("  §8| §fCatégorie actuelle: §e" + currentCategory.getDisplayName())
                .addLore("  §8| §fValeur: " + getStatusText(currentCategory.getLevel(), currentCategory.getMaxLevel()))
                .addLore("")
                .addLore("§e▶ §fClic gauche: §aChanger niveau")
                .addLore("§e▶ §fClic droit: §bChanger catégorie")
                .addLore("")
                .getItem();
    }

    /**
     * Crée l'item d'armure en diamant (avec gestion par catégories)
     */
    private ItemStack createDiamondArmorItem() {
        EquipmentEnchants currentCategory = getCurrentArmorCategory(false);

        return new ItemCreator(Material.DIAMOND_CHESTPLATE)
                .setName("§8| §bArmure en diamant")
                .addLore("")
                .addLore("  §8| §fCatégorie actuelle: §e" + currentCategory.getDisplayName())
                .addLore("  §8| §fValeur: " + getStatusText(currentCategory.getLevel(), currentCategory.getMaxLevel()))
                .addLore("")
                .addLore("§e▶ §fClic gauche: §aChanger niveau")
                .addLore("§e▶ §fClic droit: §bChanger catégorie")
                .addLore("")
                .getItem();
    }

    /**
     * Crée l'item d'épée en fer (avec gestion par catégories)
     */
    private ItemStack createIronSwordItem() {
        EquipmentEnchants currentCategory = getCurrentSwordCategory(true);

        return new ItemCreator(Material.IRON_SWORD)
                .setName("§8| §7Épée en fer")
                .addLore("")
                .addLore("  §8| §fCatégorie actuelle: §e" + currentCategory.getDisplayName())
                .addLore("  §8| §fValeur: " + getStatusText(currentCategory.getLevel(), currentCategory.getMaxLevel()))
                .addLore("")
                .addLore("§e▶ §fClic gauche: §aChanger niveau")
                .addLore("§e▶ §fClic droit: §bChanger catégorie")
                .addLore("")
                .getItem();
    }

    /**
     * Crée l'item d'épée en diamant (avec gestion par catégories)
     */
    private ItemStack createDiamondSwordItem() {
        EquipmentEnchants currentCategory = getCurrentSwordCategory(false);

        return new ItemCreator(Material.DIAMOND_SWORD)
                .setName("§8| §bÉpée en diamant")
                .addLore("")
                .addLore("  §8| §fCatégorie actuelle: §e" + currentCategory.getDisplayName())
                .addLore("  §8| §fValeur: " + getStatusText(currentCategory.getLevel(), currentCategory.getMaxLevel()))
                .addLore("")
                .addLore("§e▶ §fClic gauche: §aChanger niveau")
                .addLore("§e▶ §fClic droit: §bChanger catégorie")
                .addLore("")
                .getItem();
    }

    /**
     * Crée l'item d'arc (avec gestion par catégories)
     */
    private ItemStack createBowItem() {
        EquipmentEnchants currentCategory = getCurrentBowCategory();

        return new ItemCreator(Material.BOW)
                .setName("§8| §6Arc")
                .addLore("")
                .addLore("  §8| §fCatégorie actuelle: §e" + currentCategory.getDisplayName())
                .addLore("  §8| §fValeur: " + getStatusText(currentCategory.getLevel(), currentCategory.getMaxLevel()))
                .addLore("")
                .addLore("§e▶ §fClic gauche: §aChanger niveau")
                .addLore("§e▶ §fClic droit: §bChanger catégorie")
                .addLore("")
                .getItem();
    }

    /**
     * Retourne le texte de statut pour un enchantement
     */
    private String getStatusText(int level, int maxLevel) {
        if (level == 0) {
            return "§cDésactivé";
        } else {
            return "§a" + level + "§7/§f" + maxLevel;
        }
    }

    // Variables pour suivre les catégories actuelles
    private static int currentIronArmorCategory = 0;
    private static int currentDiamondArmorCategory = 0;
    private static int currentIronSwordCategory = 0;
    private static int currentDiamondSwordCategory = 0;
    private static int currentBowCategory = 0;

    /**
     * Récupère la catégorie actuelle pour les armures
     */
    private EquipmentEnchants getCurrentArmorCategory(boolean isIron) {
        if (isIron) {
            EquipmentEnchants[] categories = {
                    EquipmentEnchants.IRON_ARMOR_PIECES,
                    EquipmentEnchants.IRON_PROTECTION,
                    EquipmentEnchants.IRON_THORNS
            };
            return categories[currentIronArmorCategory % categories.length];
        } else {
            EquipmentEnchants[] categories = {
                    EquipmentEnchants.DIAMOND_ARMOR_PIECES,
                    EquipmentEnchants.DIAMOND_PROTECTION,
                    EquipmentEnchants.DIAMOND_THORNS
            };
            return categories[currentDiamondArmorCategory % categories.length];
        }
    }

    /**
     * Récupère la catégorie actuelle pour les épées
     */
    private EquipmentEnchants getCurrentSwordCategory(boolean isIron) {
        if (isIron) {
            EquipmentEnchants[] categories = {
                    EquipmentEnchants.IRON_SHARPNESS,
                    EquipmentEnchants.IRON_FIRE_ASPECT,
                    EquipmentEnchants.IRON_KNOCKBACK
            };
            return categories[currentIronSwordCategory % categories.length];
        } else {
            EquipmentEnchants[] categories = {
                    EquipmentEnchants.DIAMOND_SHARPNESS,
                    EquipmentEnchants.DIAMOND_FIRE_ASPECT,
                    EquipmentEnchants.DIAMOND_KNOCKBACK
            };
            return categories[currentDiamondSwordCategory % categories.length];
        }
    }

    /**
     * Récupère la catégorie actuelle pour l'arc
     */
    private EquipmentEnchants getCurrentBowCategory() {
        EquipmentEnchants[] categories = {
                EquipmentEnchants.BOW_INFINITY,
                EquipmentEnchants.BOW_FLAME,
                EquipmentEnchants.BOW_POWER
        };
        return categories[currentBowCategory % categories.length];
    }

    /**
     * Crée une flèche pour le retour (version 1.8)
     */
    private ItemStack createBackArrowHead(String name, String lore) {
        return new ItemCreator(Material.ARROW) // En 1.8, pas de têtes custom facilement
                .setName(name)
                .addLore("")
                .addLore("  " + lore)
                .addLore("")
                .addLore("§e▶ Cliquez pour retourner")
                .getItem();
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot, ClickType clickType) {
        // === GESTION DIRECTE DES ENCHANTEMENTS ===

        // Limite de diamants (slot 11)
        if (clickedItem.getType() == Material.DIAMOND && slot == 13) {
            handleDiamondLimitClick(player, clickType);
            return;
        }

        // Armure en fer (slot 11)
        if (clickedItem.getType() == Material.IRON_CHESTPLATE && slot == 11) {
            if (clickType == ClickType.RIGHT) {
                // Changer de catégorie
                currentIronArmorCategory = (currentIronArmorCategory + 1) % 3;
                player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 2.0F);
                this.gameManager.getApi().openInventory(player, getClass());
            } else {
                // Changer le niveau
                handleArmorEnchantClick(player, clickType, true);
            }
            return;
        }

        // Armure en diamant (slot 15)
        if (clickedItem.getType() == Material.DIAMOND_CHESTPLATE && slot == 15) {
            if (clickType == ClickType.RIGHT) {
                // Changer de catégorie
                currentDiamondArmorCategory = (currentDiamondArmorCategory + 1) % 3;
                player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 2.0F);
                this.gameManager.getApi().openInventory(player, getClass());
            } else {
                // Changer le niveau
                handleArmorEnchantClick(player, clickType, false);
            }
            return;
        }

        // Épée en fer (slot 28)
        if (clickedItem.getType() == Material.IRON_SWORD && slot == 29) {
            if (clickType == ClickType.RIGHT) {
                // Changer de catégorie
                currentIronSwordCategory = (currentIronSwordCategory + 1) % 3;
                player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 2.0F);
                this.gameManager.getApi().openInventory(player, getClass());
            } else {
                // Changer le niveau
                handleSwordEnchantClick(player, clickType, true);
            }
            return;
        }

        // Épée en diamant (slot 30)
        if (clickedItem.getType() == Material.DIAMOND_SWORD && slot == 33) {
            if (clickType == ClickType.RIGHT) {
                // Changer de catégorie
                currentDiamondSwordCategory = (currentDiamondSwordCategory + 1) % 3;
                player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 2.0F);
                this.gameManager.getApi().openInventory(player, getClass());
            } else {
                // Changer le niveau
                handleSwordEnchantClick(player, clickType, false);
            }
            return;
        }

        // Arc (slot 29)
        if (clickedItem.getType() == Material.BOW && slot == 31) {
            if (clickType == ClickType.RIGHT) {
                // Changer de catégorie
                currentBowCategory = (currentBowCategory + 1) % 3;
                player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 2.0F);
                this.gameManager.getApi().openInventory(player, getClass());
            } else {
                // Changer le niveau
                handleBowEnchantClick(player, clickType);
            }
            return;
        }

        // Règles générales (slot 19)
        if (clickedItem.getType() == Material.WORKBENCH && slot == 19) {
            // Ouvrir le GUI des règles générales séparé
            this.gameManager.getApi().openInventory(player, GeneralRulesGUI.class);
            return;
        }

        // Objets interdits (slot 25)
        if (clickedItem.getType() == Material.ANVIL && slot == 25) {
            // Ouvrir le GUI des objets interdits séparé
            this.gameManager.getApi().openInventory(player, ForbiddenItemsGUI.class);
            return;
        }

        // Retour (slot 40)
        if (clickedItem.getType() == Material.ARROW || slot == 40) {
            this.gameManager.getApi().openInventory(player, ConfigOptionsGUI.class);
        }
    }

    private void handleDiamondLimitClick(Player player, ClickType clickType) {
        int oldValue = this.gameConfig.getDiamondMax();
        int change = 0;

        if (clickType == ClickType.LEFT) {
            change = 1;
        } else if (clickType == ClickType.RIGHT) {
            change = -1;
        } else if (clickType == ClickType.SHIFT_LEFT) {
            change = 5;
        } else if (clickType == ClickType.SHIFT_RIGHT) {
            change = -5;
        }

        int newValue = Math.max(0, Math.min(100, oldValue + change));
        this.gameConfig.setDiamondMax(newValue);

        if (newValue != oldValue) {
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.5F);
        } else {
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0F, 1.0F);
        }

        this.gameManager.getApi().openInventory(player, getClass());
    }

    private void handleArmorEnchantClick(Player player, ClickType clickType, boolean isIron) {
        // Récupérer seulement l'enchantement de la catégorie actuelle
        EquipmentEnchants currentEnchant = getCurrentArmorCategory(isIron);
        cycleSingleEnchant(player, clickType, currentEnchant, isIron ? "armure fer" : "armure diamant");
    }

    private void handleSwordEnchantClick(Player player, ClickType clickType, boolean isIron) {
        // Récupérer seulement l'enchantement de la catégorie actuelle
        EquipmentEnchants currentEnchant = getCurrentSwordCategory(isIron);
        cycleSingleEnchant(player, clickType, currentEnchant, isIron ? "épée fer" : "épée diamant");
    }

    private void handleBowEnchantClick(Player player, ClickType clickType) {
        // Récupérer seulement l'enchantement de la catégorie actuelle
        EquipmentEnchants currentEnchant = getCurrentBowCategory();
        cycleSingleEnchant(player, clickType, currentEnchant, "arc");
    }

    /**
     * Modifie seulement l'enchantement spécifié
     */
    private void cycleSingleEnchant(Player player, ClickType clickType, EquipmentEnchants enchant, String equipmentName) {
        boolean isIncrease = clickType == ClickType.LEFT;

        int oldLevel = enchant.getLevel();
        int newLevel;

        if (isIncrease) {
            newLevel = oldLevel + 1;
            if (newLevel > enchant.getMaxLevel()) {
                newLevel = 0; // Cycle back to disabled
            }
        } else {
            newLevel = oldLevel - 1;
            if (newLevel < 0) {
                newLevel = enchant.getMaxLevel(); // Cycle to max
            }
        }

        enchant.setLevel(newLevel);

        if (newLevel != oldLevel) {
            player.playSound(player.getLocation(), Sound.CLICK, 1.0F, 1.5F);
        }

        this.gameManager.getApi().openInventory(player, getClass());
    }

    @Override
    public int getRows() {
        return 5; // 45 slots (5 rangées de 9)
    }

    // Enum pour gérer tous les enchantements d'équipement
    public enum EquipmentEnchants {
        // Armure en fer
        IRON_ARMOR_PIECES("Limite pièces fer", 0, 4),
        IRON_PROTECTION("Protection fer", 0, 4),
        IRON_THORNS("Thorns fer", 0, 3),

        // Armure en diamant
        DIAMOND_ARMOR_PIECES("Limite pièces diamant", 0, 4),
        DIAMOND_PROTECTION("Protection diamant", 0, 4),
        DIAMOND_THORNS("Thorns diamant", 0, 3),

        // Épée en fer
        IRON_SHARPNESS("Sharpness fer", 0, 5),
        IRON_FIRE_ASPECT("Fire Aspect fer", 0, 2),
        IRON_KNOCKBACK("Knockback fer", 0, 2),

        // Épée en diamant
        DIAMOND_SHARPNESS("Sharpness diamant", 0, 5),
        DIAMOND_FIRE_ASPECT("Fire Aspect diamant", 0, 2),
        DIAMOND_KNOCKBACK("Knockback diamant", 0, 2),

        // Arc
        BOW_INFINITY("Infinity", 0, 1),
        BOW_FLAME("Flame", 0, 1),
        BOW_POWER("Power", 0, 5);

        private final String displayName;
        private int level;
        private final int maxLevel;

        EquipmentEnchants(String displayName, int defaultLevel, int maxLevel) {
            this.displayName = displayName;
            this.level = defaultLevel;
            this.maxLevel = maxLevel;
        }

        public String getDisplayName() {
            return displayName;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getMaxLevel() {
            return maxLevel;
        }
    }
}