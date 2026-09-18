package fr.clickdroit.api.config.scenario;

import fr.clickdroit.api.config.scenario.special.*;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Représente un scénario de jeu UHC.
 * <p>
 * Cette classe remplace l'ancien enum Scenario pour permettre l'enregistrement dynamique
 * de nouveaux scénarios par des plugins tiers à l'aide d'un registre extensible.
 * </p>
 *
 * @author Clickdroit
 * @version 2.0
 */
public class Scenario {

    private static final Map<String, Scenario> REGISTRY = new LinkedHashMap<>();
    private static int ordinalCounter = 0;

    // ========================================
    // SCÉNARIOS INTÉGRÉS (BUILT-IN)
    // ========================================

    public static final Scenario CUTCLEAN = register("CUTCLEAN", ScenarioCategory.WORLD, "Cut Clean", new String[] {
                    "§7Tous les minerais et nourritures",
                    "§7sont directement cuits !"
    }, Material.IRON_INGOT, 0, (ScenarioManager) new CutClean(), false, false);

    public static final Scenario NOFALL = register("NOFALL", ScenarioCategory.SURVIVAL, "No Fall", new String[] {
                    "§7Plus de dégâts de chutes !"
    }, Material.IRON_BOOTS, 0, (ScenarioManager) new NoFall(), false, false);

    public static final Scenario HASTEYBOYS = register("HASTEYBOYS", ScenarioCategory.WORLD, "Hastey Boys", new String[] {
                    "§7Tous les outils sont enchantés",
                    "§7avec Efficiency !"
    }, Material.GOLD_PICKAXE, 0, (ScenarioManager) new HasteyBoys(), false, false);

    public static final Scenario NINE_SLOTS = register("NINE_SLOTS", ScenarioCategory.SURVIVAL, "Nine Slots", new String[] {
                    "§7Seuls vos 9 premiers slots",
                    "§7sont utilisables !"
    }, Material.STAINED_GLASS_PANE, 14, (ScenarioManager) new NineSlots(), false, false);

    public static final Scenario CAT_EYES = register("CAT_EYES", ScenarioCategory.MISC, "Cat Eyes", new String[] {
                    "§7Vision nocturne permanente,",
                    "§7voir clair même la nuit !"
    }, Material.SEA_LANTERN, 0, (ScenarioManager) new CatEyes(), false, false);

    public static final Scenario DIAMOND_LESS = register("DIAMOND_LESS", ScenarioCategory.WORLD, "Diamond Less", new String[] {
                    "§7Attention ! Plus de diamants",
                    "§7disponibles dans le monde !"
    }, Material.DIAMOND, 0, (ScenarioManager) new DiamondLess(), false, false);

    public static final Scenario NOFIRE = register("NOFIRE", ScenarioCategory.SURVIVAL, "No Fire", new String[] {
                    "§7Tous les dégâts du feu",
                    "§7sont annulés !"
    }, Material.FLINT_AND_STEEL, 0, (ScenarioManager) new NoFire(), false, false);

    public static final Scenario HORSELESS = register("HORSELESS", ScenarioCategory.MISC, "Horseless", new String[] {
                    "§7Les chevaux sont",
                    "§7indomptables !"
    }, Material.SADDLE, 0, (ScenarioManager) new Horseless(), false, false);

    public static final Scenario TIMBER = register("TIMBER", ScenarioCategory.WORLD, "Timber", new String[] {
                    "§7Cassez un arbre entier",
                    "§7en un seul coup !"
    }, Material.LOG, 0, (ScenarioManager) new Timber(), false, false);

    public static final Scenario TIMEBOMB = register("TIMEBOMB", ScenarioCategory.PVP, "Time Bomb", new String[] {
                    "§7À la mort d'un joueur, son stuff",
                    "§7est placé dans un coffre qui",
                    "§7explosera au bout de X secondes !"
    }, Material.TNT, 0, (ScenarioManager) new TimeBomb(), false, true, ScenarioValueType.TIME, 15, 5, 30);

    public static final Scenario SUPERHEROES = register("SUPERHEROES", ScenarioCategory.MISC, "Super Heroes", new String[] {
                    "§7Parmi plusieurs effets, chaque",
                    "§7joueur va en recevoir un !"
    }, Material.POTION, 0, (ScenarioManager) new SuperHeroes(), false, false);

    public static final Scenario ORESMULTIPLICATOR = register("ORESMULTIPLICATOR", ScenarioCategory.WORLD, "Ores Multiplicator", new String[] {
                    "§7Tous les minerais sont",
                    "§7multipliés par X !"
    }, Material.IRON_INGOT, 0, (ScenarioManager) new OresMultiplicator(), false, true,
                    ScenarioValueType.MULTIPLICATOR, 2, 2, 10);

    public static final Scenario SANGSUE = register("SANGSUE", ScenarioCategory.PVP, "Sangsue", new String[] {
                    "§7Quand vous tuez un joueur,",
                    "§7vous récupérez 5 cœurs ainsi",
                    "§7que 64 blocs de bois et pierre !"
    }, Material.REDSTONE, 0, (ScenarioManager) new Sangsue(), false, false);

    public static final Scenario BOOKCEPTION = register("BOOKCEPTION", ScenarioCategory.PVP, "Bookception", new String[] {
                    "§7Chaque joueur tué vous",
                    "§7donnera un livre enchanté !"
    }, Material.ENCHANTED_BOOK, 0, (ScenarioManager) new Bookception(), false, false);

    public static final Scenario BOWSWAP = register("BOWSWAP", ScenarioCategory.PVP, "Bow Swap", new String[] {
                    "§7Vous avez X% de chances",
                    "§7d'échanger votre place avec",
                    "§7celle d'un joueur touché !"
    }, Material.BOW, 0, (ScenarioManager) new BowSwap(), false, true, ScenarioValueType.PERCENT, 10, 5, 100);

    public static final Scenario SKYHIGH = register("SKYHIGH", ScenarioCategory.SURVIVAL, "SkyHigh", new String[] {
                    "§7Après l'activation du PvP,",
                    "§7tous les joueurs situés sous",
                    "§7la couche X perdent 1♥",
                    "§7toutes les 30 secondes !"
    }, Material.VINE, 0, (ScenarioManager) new SkyHigh(), false, true, ScenarioValueType.YCOORD, 150, 100, 256);

    public static final Scenario FINALHEAL = register("FINALHEAL", ScenarioCategory.SURVIVAL, "Final Heal", new String[] {
                    "§7Après X minutes, tous les",
                    "§7joueurs seront soignés !"
    }, Material.GOLDEN_APPLE, 0, (ScenarioManager) new FinalHeal(), false, true, ScenarioValueType.TIMEMIN, 20, 2,
                    60);

    public static final Scenario STOCKUP = register("STOCKUP", ScenarioCategory.PVP, "Stock Up", new String[] {
                    "§7Chaque joueur tué offre",
                    "§7un slot de stockage",
                    "§7supplémentaire à tous !"
    }, Material.CHEST, 0, (ScenarioManager) new StockUp(), false, false);

    public static final Scenario GOLDENHEAD = register("GOLDENHEAD", ScenarioCategory.PVP, "Golden Head", new String[] {
                    "§7À sa mort, le joueur tué",
                    "§7laissera derrière lui sa tête.",
                    "§7En l'entourant d'or, cela vous",
                    "§7permettra de crafter une Golden",
                    "§7Head permettant de régénérer 4♥ !"
    }, Material.SKULL_ITEM, 3, (ScenarioManager) new GoldenHead(), false, false);

    public static final Scenario ENCHANTEDDEATH = register("ENCHANTEDDEATH", ScenarioCategory.PVP, "Enchanted Death", new String[] {
                    "§7Le seul moyen d'obtenir une",
                    "§7table d'enchantement est",
                    "§7de tuer un joueur !"
    }, Material.ENCHANTMENT_TABLE, 0, (ScenarioManager) new EnchantedDeath(), false, false);

    public static final Scenario POISONFOOD = register("POISONFOOD", ScenarioCategory.SURVIVAL, "Poison Food", new String[] {
                    "§7Vous avez X% de chances",
                    "§7d'être empoisonné en",
                    "§7mangeant de la nourriture !"
    }, Material.POISONOUS_POTATO, 0, (ScenarioManager) new PoisonFood(), false, true, ScenarioValueType.PERCENT, 5,
                    1, 100);

    public static final Scenario NETHERIBUS = register("NETHERIBUS", ScenarioCategory.WORLD, "Netheribus", new String[] {
                    "§7Au bout de X minutes vous",
                    "§7devez vous rendre dans le",
                    "§7Nether sous peine de",
                    "§7recevoir des dégâts !"
    }, Material.NETHERRACK, 0, (ScenarioManager) new Netheribus(), false, true, ScenarioValueType.TIMEMIN, 60, 1,
                    180);

    public static final Scenario MASTERLEVEL = register("MASTERLEVEL", ScenarioCategory.MISC, "Master Level", new String[] {
                    "§7Commencez la partie",
                    "§7avec X niveaux !"
    }, Material.EXP_BOTTLE, 0, (ScenarioManager) new MasterLevel(), false, true, ScenarioValueType.INT, 10000, 1,
                    10000);

    public static final Scenario SHAREDHEALTH = register("SHAREDHEALTH", ScenarioCategory.TEAM, "Shared Health", new String[] {
                    "§7La vie des joueurs de l'équipe",
                    "§7est partagée pour n'en",
                    "§7former qu'une seule !"
    }, Material.RED_ROSE, 0, (ScenarioManager) new SharedHealth(), true, false);

    public static final Scenario GONEFISHING = register("GONEFISHING", ScenarioCategory.SURVIVAL, "Gone Fishing", new String[] {
                    "§7Commencez la partie avec",
                    "§7une canne à pêche vous",
                    "§7permettant d'attraper de",
                    "§7nombreux objets rares !"
    }, Material.FISHING_ROD, 0, (ScenarioManager) new GoneFishing(), false, false);

    public static final Scenario NOCLEANUP = register("NOCLEANUP", ScenarioCategory.PVP, "NoCleanUp", new String[] {
                    "§7Après avoir tué un joueur",
                    "§7vous serez soigné de X♥ !"
    }, Material.GHAST_TEAR, 0, (ScenarioManager) new NoCleanUp(), false, true, ScenarioValueType.DAMAGE, 4, 1, 20);

    public static final Scenario RANDOMTEAM = register("RANDOMTEAM", ScenarioCategory.TEAM, "Random Team", new String[] {
                    "§7Chaque joueur sera assigné",
                    "§7à une équipe aléatoire !"
    }, Material.BANNER, 0, (ScenarioManager) new RandomTeam(), true, false);

    public static final Scenario OVERCOOKED = register("OVERCOOKED", ScenarioCategory.SURVIVAL, "OverCooked", new String[] {
                    "§7Les fours explosent après",
                    "§7sa première cuisson mais",
                    "§7cuisent tous les items !"
    }, Material.FURNACE, 0, (ScenarioManager) new OverCooked(), false, false);

    public static final Scenario BESTPVE = register("BESTPVE", ScenarioCategory.PVP, "BestPVE", new String[] {
                    "§7Au début de la partie vous",
                    "§7êtes ajouté à une liste, tant",
                    "§7que vous y restez vous gagnez",
                    "§7un cœur chaque X minute(s).",
                    "§7Si vous prenez des dégâts vous",
                    "§7ne ferez plus partie de la liste",
                    "§7et devrez tuer un joueur pour",
                    "§7retourner sur cette dernière !"
    }, Material.PAPER, 0, (ScenarioManager) new BestPVE(), false, true, ScenarioValueType.TIMEMIN, 1, 1, 60);

    public static final Scenario KILLSWITCH = register("KILLSWITCH", ScenarioCategory.PVP, "Kill Switch", new String[] {
                    "§7Votre inventaire sera",
                    "§7remplacé par celui du",
                    "§7joueur que vous tuerez !"
    }, Material.FEATHER, 0, (ScenarioManager) new KillSwitch(), false, false);

    public static final Scenario NOENCAHNT = register("NOENCAHNT", ScenarioCategory.SURVIVAL, "NoEnchant", new String[] {
                    "§7Tous les enchantements",
                    "§7sont désactivés !"
    }, Material.ENCHANTMENT_TABLE, 0, (ScenarioManager) new NoEnchant(), false, false);

    public static final Scenario TOXICFOOD = register("TOXICFOOD", ScenarioCategory.SURVIVAL, "Toxic Food", new String[] {
                    "§7Manger un aliment vous",
                    "§7infligera X♥ de dégâts !"
    }, Material.POISONOUS_POTATO, 0, (ScenarioManager) new ToxicFood(), false, true, ScenarioValueType.DAMAGE, 1, 1,
                    20);

    public static final Scenario NOFOOD = register("NOFOOD", ScenarioCategory.SURVIVAL, "No Food", new String[] {
                    "§7Vous ne perdez plus de",
                    "§7points de nourriture !"
    }, Material.COOKED_BEEF, 0, (ScenarioManager) new NoFood(), false, false);

    public static final Scenario WEBCAGE = register("WEBCAGE", ScenarioCategory.PVP, "WebCage", new String[] {
                    "§7Une sphère de toiles",
                    "§7d'araignée apparaît autour",
                    "§7du joueur à sa mort !"
    }, Material.WEB, 0, (ScenarioManager) new WebCage(), false, false);

    public static final Scenario FASTSMELTING = register("FASTSMELTING", ScenarioCategory.WORLD, "Fast Smelting", new String[] {
                    "§7Les fours cuisent",
                    "§7plus rapidement !"
    }, Material.FURNACE, 0, (ScenarioManager) new FastSmelting(), false, false);

    public static final Scenario BLOODDIAMOND = register("BLOODDIAMOND", ScenarioCategory.WORLD, "Blood Diamond", new String[] {
                    "§7Miner un diamant vous",
                    "§7retire X♥ de vie !"
    }, Material.DIAMOND_ORE, 0, (ScenarioManager) new BloodDiamond(), false, true, ScenarioValueType.DAMAGE, 1, 1,
                    20);

    public static final Scenario MASTERAPPLE = register("MASTERAPPLE", ScenarioCategory.SURVIVAL, "Master Apple", new String[] {
                    "§7Toutes les X minutes chaque",
                    "§7joueur reçoit une pomme",
                    "§7d'or enchantée !"
    }, Material.GOLDEN_APPLE, 1, (ScenarioManager) new MasterApple(), false, true, ScenarioValueType.TIMEMIN, 1, 1,
                    60);

    public static final Scenario VEINMINER = register("VEINMINER", ScenarioCategory.WORLD, "VeinMiner", new String[] {
                    "§7Cassez tous les filons",
                    "§7de minerais en un seul coup !"
    }, Material.IRON_PICKAXE, 0, (ScenarioManager) new VeinMiner(), false, false);

    public static final Scenario BETAZOMBIE = register("BETAZOMBIE", ScenarioCategory.SURVIVAL, "Beta Zombie", new String[] {
                    "§7Les zombies lâchent des",
                    "§7plumes à leur mort !"
    }, Material.SKULL_ITEM, 2, (ScenarioManager) new BetaZombie(), false, false);

    public static final Scenario PARANOIA = register("PARANOIA", ScenarioCategory.WORLD, "Parano", new String[] {
                    "§7Miner du diamant ou de l'or,",
                    "§7manger une pomme d'or ou",
                    "§7crafter une table d'enchantement",
                    "§7révèle vos coordonnées !"
    }, Material.EMERALD, 0, (ScenarioManager) new Paranoia(), false, false);

    public static final Scenario HASTEYBABIES = register("HASTEYBABIES", ScenarioCategory.WORLD, "Hastey Babies", new String[] {
                    "§7Tous les outils sont",
                    "§7enchantés avec Haste !"
    }, Material.GOLD_PICKAXE, 0, (ScenarioManager) new HasteyBabies(), false, false);

    public static final Scenario SAFEMINER = register("SAFEMINER", ScenarioCategory.WORLD, "Safe Miner", new String[] {
                    "§7Les dégâts de feu/lave sont",
                    "§7désactivés, les dégâts de",
                    "§7chutes et mobs sont",
                    "§7réduits de moitié sous Y=X !"
    }, Material.LAVA_BUCKET, 0, (ScenarioManager) new SafeMiner(), false, true, ScenarioValueType.YCOORD, 40, 0,
                    60);

    public static final Scenario SWITCH = register("SWITCH", ScenarioCategory.PVP, "Switch", new String[] {
                    "§7La position de tous les joueurs",
                    "§7actifs est échangée",
                    "§7toutes les 10 minutes !"
    }, Material.ENDER_PEARL, 0, (ScenarioManager) new Switch(), false, false);

    public static final Scenario ANONYMOUS = register("ANONYMOUS", ScenarioCategory.MISC, "Anonymous", new String[] {
                    "§7L'identité des joueurs",
                    "§7est anonyme (Dans le tab",
                    "§7et au dessus de la tête) !"
    }, Material.NAME_TAG, 0, (ScenarioManager) new Anonymous(), false, false);

    public static final Scenario SPLIT_SPAWN = register("SPLIT_SPAWN", ScenarioCategory.TEAM, "Split Spawn", new String[] {
                    "§7Les membres d'une équipe",
                    "§7ne sont plus téléportés",
                    "§7aux mêmes coordonnées !"
    }, Material.BED, 0, (ScenarioManager) new SplitSpawn(), true, false);


    // ========================================
    // MÉTHODES DE REGISTRE ET COMPATIBILITÉ ENUM
    // ========================================

    private final String enumName;
    private final int ordinalVal;

    private ScenarioCategory category;
    private String name;
    private String[] lore;
    private Material material;
    private int data;
    private ScenarioManager scenarioManager;
    private boolean needTeams;
    private boolean enabled;
    private boolean configurable;
    private ScenarioValueType scenarioValueType;
    private int value;
    private int min;
    private int max;

    public static Scenario register(Scenario scenario) {
        REGISTRY.put(scenario.name().toUpperCase(), scenario);
        return scenario;
    }

    public static Scenario register(String enumName, ScenarioCategory category, String name, String[] lore, Material material, int data,
                    ScenarioManager scenarioManager, boolean needTeams, boolean configurable) {
        Scenario s = new Scenario(enumName, category, name, lore, material, data, scenarioManager, needTeams, configurable);
        return register(s);
    }

    public static Scenario register(String enumName, ScenarioCategory category, String name, String[] lore, Material material, int data,
                    ScenarioManager scenarioManager, boolean needTeams, boolean configurable,
                    ScenarioValueType scenarioValueType, int value, int min, int max) {
        Scenario s = new Scenario(enumName, category, name, lore, material, data, scenarioManager, needTeams, configurable, scenarioValueType, value, min, max);
        return register(s);
    }

    public Scenario(String enumName, ScenarioCategory category, String name, String[] lore, Material material, int data,
                    ScenarioManager scenarioManager, boolean needTeams, boolean configurable) {
        this.enumName = enumName;
        this.ordinalVal = ordinalCounter++;
        this.category = category;
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.data = data;
        this.scenarioManager = scenarioManager;
        this.needTeams = needTeams;
        this.configurable = configurable;
    }

    public Scenario(String enumName, ScenarioCategory category, String name, String[] lore, Material material, int data,
                    ScenarioManager scenarioManager, boolean needTeams, boolean configurable,
                    ScenarioValueType scenarioValueType, int value, int min, int max) {
        this.enumName = enumName;
        this.ordinalVal = ordinalCounter++;
        this.scenarioValueType = scenarioValueType;
        this.value = value;
        this.min = min;
        this.max = max;
        this.category = category;
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.data = data;
        this.scenarioManager = scenarioManager;
        this.needTeams = needTeams;
        this.configurable = configurable;
    }

    public String name() {
        return this.enumName;
    }

    public int ordinal() {
        return this.ordinalVal;
    }

    public static Scenario[] values() {
        return REGISTRY.values().toArray(new Scenario[0]);
    }

    public static Scenario valueOf(String name) {
        Scenario s = REGISTRY.get(name.toUpperCase());
        if (s == null) {
            throw new IllegalArgumentException("No scenario constant " + name);
        }
        return s;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return name();
    }

    // ========================================
    // MÉTHODES DE LOGIQUE DU SCÉNARIO
    // ========================================

    /**
     * Méthode pour créer l'ItemStack représentant le scénario
     */
    public ItemStack getItem() {
            ItemCreator item = (new ItemCreator(this.material))
                            .setDurability(Integer.valueOf(this.data))
                            .setTableauLores(this.lore)
                            .setName("§6" + this.name + " " + (isEnabled() ? "§a✓" : "§c✗"))
                            .addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            if (isEnabled()) {
                    item.addEnchantment(Enchantment.DURABILITY, Integer.valueOf(1));
                    item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            if (isConfigurable()) {
                    item.addLore("");
                    switch (this.scenarioValueType) {
                            case TIME:
                                    item.addLore(" §8> §fConfiguration: §6" + getValue() + " seconde"
                                                    + ((getValue() > 1) ? "s" : ""));
                                    break;
                            case TIMEMIN:
                                    item.addLore(" §8> §fConfiguration: §6" + getValue() + " minute"
                                                    + ((getValue() > 1) ? "s" : ""));
                                    break;
                            case MULTIPLICATOR:
                                    item.addLore(" §8> §fConfiguration: §c§lx" + getValue());
                                    break;
                            case DAMAGE:
                                    item.addLore(" §8> §fConfiguration: §c" + getValue() + "❤");
                                    break;
                            case PERCENT:
                                    item.addLore(" §8> §fConfiguration: §b" + getValue() + "%");
                                    break;
                            case YCOORD:
                                    item.addLore(" §8> §fConfiguration: §bY" + getValue());
                                    break;
                            case INT:
                                    item.addLore(" §8> §fConfiguration: §e" + getValue());
                                    break;
                    }
            }

            if (getValue() > 1 && getValue() <= 64) {
                    item.setAmount(Integer.valueOf(getValue()));
            } else if (getValue() > 64) {
                    item.setAmount(Integer.valueOf(64));
            } else {
                    item.setAmount(Integer.valueOf(1));
            }

            if (isNeedTeams()) {
                    item.addLore("");
                    item.addLore(" §8| §fLes §céquipes§f sont obligatoires");
                    item.addLore(" §8| §fpour utiliser ce §cscénario§f.");
            }

            item.addLore("");
            item.addLore(" §8> §fCliquez pour " + (!isEnabled() ? "§aactiver" : "§cdésactiver") + "§f.");
            item.addLore("");

            return item.getItem();
    }

    // Getters et Setters
    public boolean isEnabled() {
            return this.enabled;
    }

    public void setEnabled(boolean enabled) {
            this.enabled = enabled;
    }

    public void toggleEnabled() {
            if (isEnabled()) {
                    this.enabled = false;
            } else if (isNeedTeams()) {
                    if (!GameUtils.isSoloMode()) {
                            this.enabled = true;
                    }
            } else {
                    this.enabled = true;
            }
    }

    public String getName() {
            return this.name;
    }

    public String[] getLore() {
            return this.lore;
    }

    public Material getMaterial() {
            return this.material;
    }

    public int getData() {
            return this.data;
    }

    public ScenarioManager getScenarioManager() {
            return this.scenarioManager;
    }

    public boolean isNeedTeams() {
            return this.needTeams;
    }

    public boolean isConfigurable() {
            return this.configurable;
    }

    public ScenarioValueType getScenarioValueType() {
            return this.scenarioValueType;
    }

    public ScenarioValueType getValueType() {
            return this.scenarioValueType;
    }

    public void setValue(int value) {
            this.value = value;
    }

    public int getValue() {
            return this.value;
    }

    public int getMin() {
            return this.min;
    }

    public int getMax() {
            return this.max;
    }

    public ScenarioCategory getCategory() {
            return this.category;
    }
}
