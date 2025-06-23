package fr.clickdroit.api.config.value;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.utils.Chrono;
import fr.clickdroit.api.utils.CommonString;
import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public enum OpenVar {
    // SLOTS maintenant utilise une BOUSSOLE au lieu d'une tête
    SLOTS("gameSlot", Material.COMPASS, 0, "§8| §fSlots",
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

    // CYCLE_DURATION maintenant utilise une MONTRE au lieu d'une tête
    CYCLE_DURATION("dayNightDuration", Material.WATCH, 0, "§8| §fDurée du cycle jour/nuit",
            new String[] { "", "  §8| §fVous permet de §cmodifier", "  §8| §fla durée du cycle", "  §8| §fjour/nuit de la §cpartie.", "  §8| §fLe temps du §bjour§f ou de la ", "  §8| §cnuit§f sera égal à la §cmoitié§f de", "  §8| §fla valeur choisie.", "" },
            false, true),

    DIAMOND_MAX("diamondMax", Material.DIAMOND, 0, "§8| §fDiamants",
            new String[] { "", "  §8| §fVous permet de §climiter", "  §8| §fle minage des diamants.", "" },
            false, true),

    GOLD_MAX("goldMax", Material.GOLD_INGOT, 0, "§8| §fOrs",
            new String[] { "", "  §8| §fVous permet de §climiter", "  §8| §fle minage de l'or.", "" },
            false, true);

    private String fieldName;
    private Material material;
    private String textureURL; // Pour les têtes personnalisées si besoin plus tard
    private int durability;
    private String name;
    private String[] lore;
    private boolean isTime;
    private boolean hasLimit;

    // Constructeur principal pour les matériaux normaux
    OpenVar(String fieldName, Material material, int durability, String name, String[] lore, boolean isTime, boolean hasLimit) {
        this.fieldName = fieldName;
        this.material = material;
        this.textureURL = null;
        this.durability = durability;
        this.name = name;
        this.lore = lore;
        this.isTime = isTime;
        this.hasLimit = hasLimit;
    }

    // Constructeur alternatif pour les têtes personnalisées (au cas où vous en auriez besoin plus tard)
    OpenVar(String fieldName, String textureURL, int durability, String name, String[] lore, boolean isTime, boolean hasLimit) {
        this.fieldName = fieldName;
        this.material = Material.SKULL_ITEM;
        this.textureURL = textureURL;
        this.durability = durability;
        this.name = name;
        this.lore = lore;
        this.isTime = isTime;
        this.hasLimit = hasLimit;
    }

    public ItemStack getItem() {
        GameConfig gameConfig = API.getAPI().getGameManager().getGameConfig();

        ItemCreator itemCreator;

        // Si c'est une tête personnalisée
        if (this.textureURL != null && this.material == Material.SKULL_ITEM) {
            itemCreator = new ItemCreator(Material.SKULL_ITEM)
                    .setDurability((short) 3) // Tête de joueur
                    .setSkullURL(this.textureURL);
        } else {
            // Matériau normal
            itemCreator = new ItemCreator(this.material);
            if (this.durability > 0) {
                itemCreator.setDurability((short) this.durability);
            }
        }

        itemCreator.setName(this.name);

        // Ajouter la lore de base
        for (String loreLine : this.lore) {
            itemCreator.addLore(loreLine);
        }

        try {
            Field field = GameConfig.class.getDeclaredField(this.fieldName);
            field.setAccessible(true);
            Object value = field.get(gameConfig);

            String displayValue;
            if (this.isTime && value instanceof Long) {
                displayValue = Chrono.timeToDigitalString((Long) value);
            } else if (this.fieldName.equals("dayNightDuration")) {
                displayValue = Chrono.getCycleDurationTime((Long) value) + " minute(s)";
            } else {
                displayValue = String.valueOf(value);
            }

            itemCreator.addLore(" §8> §fConfiguration: §c" + displayValue);
            itemCreator.addLore("");
            itemCreator.addLore(CommonString.CLICK_HERE_TO_MODIFY.getMessage());
            itemCreator.addLore("");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemCreator.getItem();
    }

    // Getters
    public String getFieldName() {
        return fieldName;
    }

    public Material getMaterial() {
        return material;
    }

    public String getTextureURL() {
        return textureURL;
    }

    public int getDurability() {
        return durability;
    }

    public String getName() {
        return name;
    }

    public String[] getLore() {
        return lore;
    }

    public boolean isTime() {
        return isTime;
    }

    public boolean hasLimit() {
        return hasLimit;
    }
}