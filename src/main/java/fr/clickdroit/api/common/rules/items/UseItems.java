package fr.clickdroit.api.common.rules.items;


import fr.clickdroit.api.utils.item.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum UseItems {
    FISHINGROD("Cannes pêche", Material.FISHING_ROD, 0),
    LAVA("Seaux de lave", Material.LAVA_BUCKET, 0),
    BOW("Arcs", Material.BOW, 0),
    FLINT_AND_STEEL("Briquets", Material.FLINT_AND_STEEL, 0),
    SNOWBALL("Boules de neige", Material.SNOW_BALL, 0),
    SHEAR("Cisailles", Material.SHEARS, 0);

    private boolean enabled = true;

    private final String name;

    private final Material material;

    private final int data;

    UseItems(String name, Material material, int data) {
        this.name = name;
        this.material = material;
        this.data = data;
    }

    public ItemStack getItem() {
        return (new ItemCreator(this.material)).setDurability(Integer.valueOf(this.data)).setName("§8| §c"+ this.name + " " + (isEnabled() ? "§8(§aOui§8)": "§8(§cNon§8)")).getItem();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void toggleEnabled() {
        this.enabled = !this.enabled;
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
