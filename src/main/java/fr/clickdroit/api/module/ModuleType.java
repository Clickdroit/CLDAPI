package fr.clickdroit.api.module;

import fr.clickdroit.api.utils.ItemCreator;
import org.bukkit.Material;

public enum ModuleType {
    UHC("UHC", "§e", Material.GOLDEN_APPLE, 0, false, true, true),
    DEMONSLAYER("Demon Slayer", "§6", Material.BLAZE_POWDER, 0, true, false, false),
    CODEGEASS("Code Geass", "", Material.COMPASS, 0, false, false, false),
    HUNTERXHUNTER("Hunter X Hunter", "", Material.VINE, 0, true, false, false),
    LG("Loup-Garou", "§c", Material.PUMPKIN, 0, true, false, false),
    JJK("Jujutsu", "§5", Material.BLAZE_POWDER, 0, true, false, false),
    NARUTO("Naruto", "§e", Material.GLOWSTONE_DUST, 0, true, false, false);

    private String name;

    private String color;

    private Material material;

    private int data;

    private boolean hasRole;

    private boolean hasTeam;

    private boolean deleteSpawn;

    ModuleType(String name, String color, Material material, int data, boolean hasRole, boolean hasTeam,
            boolean deleteSpawn) {
        this.name = name;
        this.color = color;
        this.material = material;
        this.data = data;
        this.hasRole = hasRole;
        this.hasTeam = hasTeam;
        this.deleteSpawn = deleteSpawn;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getData() {
        return this.data;
    }

    public boolean isHasRole() {
        return this.hasRole;
    }

    public boolean hasTeam() {
        return this.hasTeam;
    }

    public boolean isDeleteSpawn() {
        return this.deleteSpawn;
    }

    public ItemCreator getItem() {
        return (new ItemCreator(this.material)).setDurability(Integer.valueOf(this.data)).setName(this.name);
    }
}
