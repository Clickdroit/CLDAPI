package fr.clickdroit.api.config.scenario;

import fr.clickdroit.api.config.scenario.special.*;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public enum Scenario {
    CUTCLEAN(1, 0, "Cut Clean", new String[] { "", " les minerais et nourritues", " directements cuits !" }, Material.IRON_INGOT, 0, (ScenarioManager)new CutClean(), false, false),
    NOFALL(1, 1, "No Fall", new String[] { "", " dde chutes", " plus." }, Material.IRON_BOOTS, 0, (ScenarioManager)new NoFall(), false, false),
    HASTEYBOYS(1, 2, "Hastey Boys", new String[] { "", " les outils sont enchant!" }, Material.GOLD_PICKAXE, 0, (ScenarioManager)new HasteyBoys(), false, false),
    NINE_SLOTS(1, 3, "Nine Slots", new String[] { "", " vos 9 premiers", " sont utilisables !" }, Material.STAINED_GLASS_PANE, 14, (ScenarioManager)new NineSlots(), false, false),
    CAT_EYES(1, 4, "Cat Eyes", new String[] { "", " clair, mla nuit.." }, Material.SEA_LANTERN, 0, (ScenarioManager)new CatEyes(), false, false),
    DIAMOND_LESS(1, 5, "Diamond Less", new String[] { "", " ! Plus de diamants.." }, Material.DIAMOND, 0, (ScenarioManager)new DiamondLess(), false, false),
    NOFIRE(1, 6, "No Fire", new String[] { "", " ddu feu sont annul"}, Material.FLINT_AND_STEEL, 0, (ScenarioManager)new NoFire(), false, false),
    HORSELESS(1, 7, "Horseless", new String[] { "", " chevaux sont indomptables." }, Material.SADDLE, 0, (ScenarioManager)new Horseless(), false, false),
    TIMBER(1, 8, "Timber", new String[] { "", " un arbre en un seul coup !" }, Material.LOG, 0, (ScenarioManager)new Timber(), false, false),
    TIMEBOMB(1, 9, "Time Bomb", new String[] { "", " la mort d'un joueur, son stuff est", " dans un coffre qui explosera", " bout de X secondes." }, Material.TNT, 0, (ScenarioManager)new TimeBomb(), false, true, ScenarioValueType.TIME, 15, 5, 30),
    SUPERHEROES(1, 10, "Super Heroes", new String[] { "", " plusieurs effets, chaques joueurs", " voit en recevoir un !" }, Material.POTION, 0, (ScenarioManager)new SuperHeroes(), false, false),
    ORESMULTIPLICATOR(1, 11, "Ores Multiplicator", new String[] { "", " minerais sont multiplipar X." }, Material.IRON_INGOT, 0, (ScenarioManager)new OresMultiplicator(), false, true, ScenarioValueType.MULTIPLICATOR, 2, 2, 10),
    SANGSUE(1, 12, "Sangsue", new String[] { "", " vous tuez un joueur, vous", " 5 que", " blocs de bois et de pierre." }, Material.REDSTONE, 0, (ScenarioManager)new Sangsue(), false, false),
    BOOKCEPTION(1, 13, "Bookception", new String[] { "", " joueur tudonnera un livre enchant"}, Material.ENCHANTED_BOOK, 0, (ScenarioManager)new Bookception(), false, false),
    BOWSWAP(1, 14, "Bow Swap", new String[] { "", " avez X% de chances d'votre place", " celle d'un joueur touchavec votre fl"}, Material.BOW, 0, (ScenarioManager)new BowSwap(), false, true, ScenarioValueType.PERCENT, 10, 5, 100),
    SKYHIGH(1, 15, "SkyHigh", new String[] { "", " l'activation du PvP, tous les", " situsous la couche X,", " 1 les 30 secondes." }, Material.VINE, 0, (ScenarioManager)new SkyHigh(), false, true, ScenarioValueType.YCOORD, 150, 100, 256),
    FINALHEAL(1, 16, "Final Heal", new String[] { "", " X minutes, tous les joueurs seront soign"}, Material.GOLDEN_APPLE, 0, (ScenarioManager)new FinalHeal(), false, true, ScenarioValueType.TIMEMIN, 20, 2, 60),
    STOCKUP(1, 17, "Stock Up", new String[] { "", " joueur tuoffre un slot de", " suppltous les joueurs vivants." }, Material.CHEST, 0, (ScenarioManager)new StockUp(), false, false),
    GOLDENHEAD(1, 18, "Golden Head", new String[] { "", " sa mort, le joueur tulaissera derri", " en supplsa tEn l'entourant d'or,", " vous permettra de crafter une Golden Head", " permettant de r4"}, Material.SKULL_ITEM, 3, (ScenarioManager)new GoldenHead(), false, false),
    ENCHANTEDDEATH(1, 19, "Enchanted Death", new String[] { "", " seul moyen d'obtenir une table", " est de tuer un joueur." }, Material.ENCHANTMENT_TABLE, 0, (ScenarioManager)new EnchantedDeath(), false, false),
    POISONFOOD(1, 20, "Poison Food", new String[] { "", " avez X% de chances", " empoisonnen mangeant." }, Material.POISONOUS_POTATO, 0, (ScenarioManager)new PoisonFood(), false, true, ScenarioValueType.PERCENT, 5, 1, 100),
    NETHERIBUS(1, 21, "Netheribus", new String[] { "", " bout de X minutes vous devez vous rendre", " le Nether sous peine de recevoir des d"}, Material.NETHERRACK, 0, (ScenarioManager)new Netheribus(), false, true, ScenarioValueType.TIMEMIN, 60, 1, 180),
    MASTERLEVEL(1, 22, "Master Level", new String[] { "", " la partie avec X niveaux." }, Material.EXP_BOTTLE, 0, (ScenarioManager)new MasterLevel(), false, true, ScenarioValueType.INT, 10000, 1, 10000),
    SHAREDHEALTH(1, 23, "Shared Health", new String[] { "", " vie des joueurs de l'est", " pour n'en former qu'une seule." }, Material.RED_ROSE, 0, (ScenarioManager)new SharedHealth(), true, false),
    GONEFISHING(1, 24, "Gone Fishing", new String[] { "", " la partie avec une canne", " pvous permettant d'attraper", " nombreux objets rares." }, Material.FISHING_ROD, 0, (ScenarioManager)new GoneFishing(), false, false),
    NOCLEANUP(1, 25, "NoCleanUp", new String[] { "", " avoir tuun joueur", " serez soignde X"}, Material.GHAST_TEAR, 0, (ScenarioManager)new NoCleanUp(), false, true, ScenarioValueType.DAMAGE, 4, 1, 20),
    RANDOMTEAM(1, 26, "Random Team", new String[] { "", " joueur sera assign", " une al"}, Material.BANNER, 0, (ScenarioManager)new RandomTeam(), true, false),
    OVERCOOKED(1, 27, "OverCooked", new String[] { "", " four explose aprsa premicuisson", " cuits tout les !" }, Material.FURNACE, 0, (ScenarioManager)new OverCooked(), false, false),
    BESTPVE(1, 28, "BestPVE", new String[] { "", " dde la partie vous ajout", " une liste, tant que vous y restez", " gagnez un coeur chaque X minute(s)", " vous prenez des dvous ne ferez plus", " de la liste et vous devrez tuer", " joueur pour retourner sur cette derni"}, Material.PAPER, 0, (ScenarioManager)new BestPVE(), false, true, ScenarioValueType.TIMEMIN, 1, 1, 60),
    KILLSWITCH(1, 29, "Kill Switch", new String[] { "", " inventaire sera remplacpar", " du joueur que vous tuerez." }, Material.FEATHER, 0, (ScenarioManager)new KillSwitch(), false, false),
    NOENCAHNT(1, 30, "NoEnchant", new String[] { "", " enchantements sont d"}, Material.ENCHANTMENT_TABLE, 0, (ScenarioManager)new NoEnchant(), false, false),
    TOXICFOOD(1, 31, "Toxic Food", new String[] { "", " un aliment vous infligera X d"}, Material.POISONOUS_POTATO, 0, (ScenarioManager)new ToxicFood(), false, true, ScenarioValueType.DAMAGE, 1, 1, 20),
    NOFOOD(1, 32, "No Food", new String[] { "", " ne perdez plus de points de nourriture." }, Material.COOKED_BEEF, 0, (ScenarioManager)new NoFood(), false, false),
    WEBCAGE(1, 33, "WebCage", new String[] { "", "  sphde toiles d'araignse", " autour du joueur sa mort." }, Material.WEB, 0, (ScenarioManager)new WebCage(), false, false),
    FASTSMELTING(1, 34, "Fast Smelting", new String[] { "", " fours cuisent plus rapidement." }, Material.FURNACE, 0, (ScenarioManager)new FastSmelting(), false, false),
    BLOODDIAMOND(1, 35, "Blood Diamond", new String[] { "", " un diamant vous retire X"}, Material.DIAMOND_ORE, 0, (ScenarioManager)new BloodDiamond(), false, true, ScenarioValueType.DAMAGE, 1, 1, 20),
    MASTERAPPLE(2, 0, "Master Apple", new String[] { "", " les X minutes chaque joueur", " une pomme d'or cheat." }, Material.GOLDEN_APPLE, 1, (ScenarioManager)new MasterApple(), false, true, ScenarioValueType.TIMEMIN, 1, 1, 60),
    VEINMINER(2, 1, "VeinMiner", new String[] { "", " les filons de minerais", " un seul coup !" }, Material.IRON_PICKAXE, 0, (ScenarioManager)new VeinMiner(), false, false),
    BETAZOMBIE(2, 2, "Beta Zombie", new String[] { "", " zombies ldes", " leur mort." }, Material.SKULL_ITEM, 2, (ScenarioManager)new BetaZombie(), false, false),
    PARANOIA(2, 3, "Parano", new String[] { "", " du diamant ou de l'or,", " une pomme d'or ou crafter", " table d'enchantement", " vos coordon"}, Material.EMERALD, 0, (ScenarioManager)new Paranoia(), false, false),
    HASTEYBABIES(2, 4, "Hastey Babies", new String[] { "", " les outils sont enchant!" }, Material.GOLD_PICKAXE, 0, (ScenarioManager)new HasteyBabies(), false, false),
    SAFEMINER(2, 6, "Safe Miner", new String[] { "", " dde feu/lave sont d", " dde chutes / mobs", " rde moiti"}, Material.LAVA_BUCKET, 0, (ScenarioManager)new SafeMiner(), false, true, ScenarioValueType.YCOORD, 40, 0, 60);

    private int page;

    private int slot;

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

    Scenario(int page, int slot, String name, String[] lore, Material material, int data, ScenarioManager scenarioManager, boolean needTeams, boolean configurable) {
        this.page = page;
        this.slot = slot;
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.data = data;
        this.scenarioManager = scenarioManager;
        this.needTeams = needTeams;
        this.configurable = configurable;
    }

    Scenario(int page, int slot, String name, String[] lore, Material material, int data, ScenarioManager scenarioManager, boolean needTeams, boolean configurable, ScenarioValueType scenarioValueType, int value, int min, int max) {
        this.scenarioValueType = scenarioValueType;
        this.value = value;
        this.min = min;
        this.max = max;
        this.page = page;
        this.slot = slot;
        this.name = name;
        this.lore = lore;
        this.material = material;
        this.data = data;
        this.scenarioManager = scenarioManager;
        this.needTeams = needTeams;
        this.configurable = configurable;
    }

    public ItemStack getItem() {
        ItemCreator item = (new ItemCreator(this.material)).setDurability(Integer.valueOf(this.data)).setTableauLores(this.lore).setName("+ this.name + " + (isEnabled() ? "": "")).addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if (isEnabled()) {
            item.addEnchantment(Enchantment.DURABILITY, Integer.valueOf(1));
            item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (isConfigurable()) {
            item.addLore("");
            switch (this.scenarioValueType) {
                case TIME:
                    item.addLore(" §8> §fConfiguration §f: §6§1"+ getValue() + " seconde" + ((getValue() > 1) ? "s" : ""));
                    break;
                case TIMEMIN:
                    item.addLore(" §8> §fConfiguration §f: §6§1"+ getValue() + " minute" + ((getValue() > 1) ? "s" : ""));
                    break;
                case MULTIPLICATOR:
                    item.addLore(" §8> §fConfiguration §f: §c§1x"+ getValue());
                    break;
                case DAMAGE:
                    item.addLore(" §8> §fConfiguration §f: §c§1"+ getValue() + "❤");
                    break;
                case PERCENT:
                    item.addLore(" §8> §fConfiguration §f: §b§1"+ getValue() + "%");
                    break;
                case YCOORD:
                    item.addLore(" §8> §fConfiguration §f: §b§1Y" + getValue());
                    break;
                case INT:
                    item.addLore(" §8> §fConfiguration §f: §e§1"+ getValue());
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
        item.addLore(" §8> §fCLiquez pour " + (!isEnabled() ? "§aactiver": "§cdesactiver") + "§f.");
        item.addLore("");
        return item.getItem();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.enabled = isEnabled;
    }

    public void toggleEnabled() {
        if (isEnabled()) {
            this.enabled = false;
        } else if (isNeedTeams()) {
            if (!GameUtils.isSoloMode())
                this.enabled = true;
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

    public int getPage() {
        return this.page;
    }

    public int getSlot() {
        return this.slot;
    }
}

