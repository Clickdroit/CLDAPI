package fr.clickdroit.api.common.rules.items;

import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum GeneralRules {
    STRIPMINING("Strip Mining", Material.IRON_PICKAXE, 0, true),
    IPVP("iPvP", Material.IRON_SWORD, 0, true),
    CROSSTEAM("Cross Team", Material.GOLD_SWORD, 0, true),
    TOWER("Towers", Material.NETHER_BRICK, 0, true),
    DIGDOWN("Dig Down", Material.WOOD_SPADE, 0, true),
    ROLLERCOASTER("Roller Coaster", Material.LADDER, 0, true),
    HEALTH("Vie des joueurs", Material.RED_ROSE, 0, false),
    MUMBLE("Mumble obligatoire", Material.IRON_HELMET, 0, true),
    PRIVATEMSG("Messages privés", Material.SIGN, 0, true);

    private final String name;

    private final Material material;

    private final int data;

    private boolean enabled;

    GeneralRules(String name, Material material, int data, boolean enabled) {
        this.name = name;
        this.material = material;
        this.data = data;
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void toggleEnabled() {
        this.enabled = !this.enabled;
    }

    public ItemStack getItem() {
        return (new ItemCreator(this.material)).setDurability(Integer.valueOf(this.data)).setName("§8| §c"+ this.name + " §8(" + (isEnabled() ? "§aActivé§8": "§cDésactivé§8)")).getItem();
    }

    public String getName() {
        return this.name;
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getData() {
        return this.data;
    }
    }

