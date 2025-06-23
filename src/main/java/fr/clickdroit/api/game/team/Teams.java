package fr.clickdroit.api.game.team;

import org.bukkit.Material;
import fr.clickdroit.api.utils.ItemCreator;


public enum Teams {
    TEAM_1(1, 10, "Bleu", "", 12, 3),
    TEAM_2(1, 11, "Rouge", "", 1, 14),
    TEAM_3(1, 12, "Orange", "", 14, 1),
    TEAM_4(1, 13, "Jaune", "", 11, 4),
    TEAM_5(1, 14, "Vert", "", 10, 5),
    TEAM_6(1, 15, "Gris", "", 7, 7),
    TEAM_7(1, 16, "Rose", "", 9, 6),
    TEAM_8(1, 19, "Bleu", "", 12, 3),
    TEAM_9(1, 20, "Rouge", "", 1, 14),
    TEAM_10(1, 21, "Orange", "", 14, 1),
    TEAM_11(1, 22, "Jaune", "", 11, 4),
    TEAM_12(1, 23, "Vert", "", 10, 5),
    TEAM_13(1, 24, "Gris", "", 7, 7),
    TEAM_14(1, 25, "Rose", "", 9, 6),
    TEAM_15(1, 28, "Bleu", "", 12, 3),
    TEAM_16(1, 29, "Rouge", "", 1, 14),
    TEAM_17(1, 30, "Orange", "", 14, 1),
    TEAM_18(1, 31, "Jaune", "", 11, 4),
    TEAM_19(1, 32, "Vert", "", 10, 5),
    TEAM_20(1, 33, "Gris", "", 7, 7),
    TEAM_21(1, 34, "Rose", "", 9, 6),
    TEAM_22(1, 37, "Bleu", "", 12, 3),
    TEAM_23(1, 38, "Rouge", "", 1, 14),
    TEAM_24(1, 39, "Orange", "", 14, 1),
    TEAM_25(1, 40, "Jaune", "", 11, 4),
    TEAM_26(1, 41, "Vert", "", 10, 5),
    TEAM_27(1, 42, "Gris", "", 7, 7),
    TEAM_28(1, 43, "Rose", "", 9, 6),
    TEAM_29(2, 10, "Bleu", "", 12, 3),
    TEAM_30(2, 11, "Rouge", "", 1, 14),
    TEAM_31(2, 12, "Orange", "", 14, 1),
    TEAM_32(2, 13, "Jaune", "", 11, 4),
    TEAM_33(2, 14, "Vert", "", 10, 5),
    TEAM_34(2, 15, "Gris", "", 7, 7),
    TEAM_35(2, 16, "Rose", "", 9, 6),
    TEAM_36(2, 19, "Bleu", "", 12, 3),
    TEAM_37(2, 20, "Rouge", "", 1, 14),
    TEAM_38(2, 21, "Orange", "", 14, 1),
    TEAM_39(2, 22, "Jaune", "", 11, 4),
    TEAM_40(2, 23, "Vert", "", 10, 5),
    TEAM_41(2, 24, "Gris", "", 7, 7),
    TEAM_42(2, 25, "Rose", "", 9, 6),
    TEAM_43(2, 28, "Bleu", "", 12, 3),
    TEAM_44(2, 29, "Rouge", "", 1, 14),
    TEAM_45(2, 30, "Orange", "", 14, 1),
    TEAM_46(2, 31, "Jaune", "", 11, 4),
    TEAM_47(2, 32, "Vert", "", 10, 5),
    TEAM_48(2, 33, "Gris", "", 7, 7),
    TEAM_49(2, 34, "Rose", "", 9, 6),
    TEAM_50(2, 37, "Bleu", "", 12, 3),
    TEAM_51(2, 38, "Rouge", "", 1, 14),
    TEAM_52(2, 39, "Orange", "", 14, 1),
    TEAM_53(2, 40, "Jaune", "", 11, 4),
    TEAM_54(2, 41, "Vert", "", 10, 5),
    TEAM_55(2, 42, "Gris", "", 7, 7),
    TEAM_56(2, 43, "Rose", "", 9, 6);

    private final int page;

    private final int slot;

    private final String name;

    private final String color;

    private final int dataitem;

    private final int dataGlass;

    Teams(int page, int slot, String name, String color, int dataitem, int dataGlass) {
        this.page = page;
        this.slot = slot;
        this.name = name;
        this.color = color;
        this.dataitem = dataitem;
        this.dataGlass = dataGlass;
    }

    public ItemCreator getItem() {
        return (new ItemCreator(Material.BANNER)).setName("§fÉquipe" + this.color + this.name).setDurability(Integer.valueOf(this.dataitem));
    }

    public int getPage() {
        return this.page;
    }

    public int getSlot() {
        return this.slot;
    }

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
    }

    public int getDataitem() {
        return this.dataitem;
    }

    public int getDataGlass() {
        return this.dataGlass;
    }
}

