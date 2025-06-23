package fr.clickdroit.api.config.value;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.utils.Chrono;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.ItemCreator; // Utiliser cette version qui a setSkullURL
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public enum OpenVar {
    SLOTS("gameSlot", "http://textures.minecraft.net/texture/3b2c7b4f8a0c6d6e6a2f2b1a1c1b1a1b1a1b1a1b1a1b1a1b1a1b1a1b1a1b1a1b", 3, "§8| §fSlots",
            new String[] { "", "  §8| §fVous permet de §cmodifier", "  §8| §fle nombre de §cjoueurs§f autorisés", "  §8| §fà se §aconnecter§f à la §cpartie§f.", "" },
            false, true),

    PVP_TIME("pvpTime", Material.DIAMOND_SWORD, 0, "§8| §cP§fv§cP",
            new String[] { "", "  §8| §fVous permet de §cmodifier", "  §8| §fle temps avant l'activation", "  §8| §fdu §6PvP§f durant la §cpartie§f.", "" },
            true, false),

    EPISODE_TIME("episodeTime", Material.WATCH, 0, "§8| §fDurée §f: §cEpisode",
            new String[] { "", "  §8| §fVous permet de §cmodifier", "  §8| §fle temps entre chaque épisode.", "" },
            true, false),

    BORDER_TIME("borderTime", Material.STAINED_GLASS, 4, "§8| §fBordure",
            new String[] { "", "  §8| §fVous permet de §cmodifier", "  §8| §fle temps avant l'activation", "  §8| §fde la réduction de la", "  §8| §fbordure durant la partie.", "" },
            true, false),

    CYCLE_DURATION("dayNightDuration", "http://textures.minecraft.net/texture/b89042082bb7a7618b784ee7605a134c58834e21e374c888937161057f6c7", 0, "§8| §fDurée du cycle jour/nuit",
            new String[] { "", "  §8| §fVous permet de §cmodifier", "  §8| §fla durée du cycle", "  §8| §fjour/nuit de la §cpartie.", "  §8| §fLe temps du §bjour§f ou de la ", "  §8| §cnuit§f sera égal à la §cmoitié§f de", "  §8| §fla valeur choisie.", "" },
            false, true),

    DIAMOND_MAX("diamondMax", Material.DIAMOND, 0, "§8| §fDiamants",
            new String[] { "", "  §8| §fVous permet de §climiter", "  §8| §fle minage des diamants.", "" },
            false, true),

    GOLD_MAX("goldMax", Material.GOLD_INGOT, 0, "§8| §fOrs",
            new String[] { "", "  §8| §fVous permet de §climiter", "  §8| §fle minage de l'or.", "" },
            false, true);

    private final String var;
    private final Material material;
    private final String textureUrl; // Nouvelle propriété pour les URLs
    private final int data;
    private final String itemName;
    private final String[] itemDescription;
    private final boolean toDigital;
    private final boolean stack;

    // Constructeur pour les items avec texture personnalisée (têtes)
    OpenVar(String var, String textureUrl, int data, String itemName, String[] itemDescription, boolean toDigital, boolean stack) {
        this.var = var;
        this.material = Material.SKULL_ITEM; // Force le matériel à être une tête
        this.textureUrl = textureUrl;
        this.data = data;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.toDigital = toDigital;
        this.stack = stack;
    }

    // Constructeur pour les items normaux (sans texture)
    OpenVar(String var, Material material, int data, String itemName, String[] itemDescription, boolean toDigital, boolean stack) {
        this.var = var;
        this.material = material;
        this.textureUrl = null; // Pas de texture personnalisée
        this.data = data;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.toDigital = toDigital;
        this.stack = stack;
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getData() {
        return this.data;
    }

    public String getTextureUrl() {
        return this.textureUrl;
    }

    public boolean hasCustomTexture() {
        return this.textureUrl != null && !this.textureUrl.isEmpty();
    }

    public ItemStack getItem() {
        ItemCreator itemCreator = new ItemCreator(this.material)
                .setDurability(this.data)
                .setName(this.itemName)
                .addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        // Ajouter les lores de description
        for (String lore : this.itemDescription) {
            itemCreator.addLore(lore);
        }

        // Si l'item a une texture personnalisée, l'appliquer
        if (hasCustomTexture()) {
            itemCreator.setSkullURL(this.textureUrl);
        }

        try {
            Field field = GameConfig.class.getDeclaredField(this.var);
            field.setAccessible(true);

            // CORRECTION ICI : Gérer les différents types de champs
            Object fieldValue = field.get(API.getAPI().getGameManager().getGameConfig());
            int amount;

            if (fieldValue instanceof Integer) {
                amount = (Integer) fieldValue;
            } else if (fieldValue instanceof Long) {
                amount = ((Long) fieldValue).intValue(); // Conversion sécurisée de long vers int
            } else {
                amount = 1; // Valeur par défaut
            }

            itemCreator.addLore(" §8> §fAccès §f: §6§lHost");
            itemCreator.addLore(" §8> §fConfiguration: §c" + (this.toDigital ?
                    Chrono.timeToDigitalString(amount) : String.valueOf(amount)));
            itemCreator.addLore("");
            itemCreator.addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage());

            if (this.stack) {
                itemCreator.setAmount(Math.max(1, Math.min(64, amount)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // En cas d'erreur, utiliser des valeurs par défaut
            itemCreator.addLore(" §8> §fAccès §f: §6§lHost");
            itemCreator.addLore(" §8> §fConfiguration: §cErreur");
            itemCreator.addLore("");
            itemCreator.addLore(CommonString.CLICK_HERE_TO_ACCESS.getMessage());
        }

        return itemCreator.getItem();
    }
}