package fr.clickdroit.api.game.team;

import org.bukkit.Material;
import fr.clickdroit.api.utils.item.ItemCreator;


public enum Teams {
        // PAGE 1 - avec symboles pour différencier les doublons
        TEAM_1(1, 10, "Bleu", "", 12, 3),         // Bleu sans symbole (premier)
        TEAM_2(1, 11, "Rouge", "", 1, 14),        // Rouge sans symbole (premier)
        TEAM_3(1, 12, "Orange", "", 14, 1),       // Orange sans symbole (premier)
        TEAM_4(1, 13, "Jaune", "", 11, 4),        // Jaune sans symbole (premier)
        TEAM_5(1, 14, "Vert", "", 10, 5),         // Vert sans symbole (premier)
        TEAM_6(1, 15, "Gris", "", 7, 7),          // Gris sans symbole (premier)
        TEAM_7(1, 16, "Rose", "", 9, 6),          // Rose sans symbole (premier)

        TEAM_8(1, 19, "Bleu", "✿", 12, 3),        // Bleu Point
        TEAM_9(1, 20, "Rouge", "✿", 1, 14),       // Rouge Point
        TEAM_10(1, 21, "Orange", "✿", 14, 1),     // Orange Point
        TEAM_11(1, 22, "Jaune", "✿", 11, 4),      // Jaune Point
        TEAM_12(1, 23, "Vert", "✿", 10, 5),       // Vert Point
        TEAM_13(1, 24, "Gris", "✿", 7, 7),        // Gris Point
        TEAM_14(1, 25, "Rose", "✿", 9, 6),        // Rose Point

        TEAM_15(1, 28, "Bleu", "✚", 12, 3),       // Bleu Triangle
        TEAM_16(1, 29, "Rouge", "✚", 1, 14),      // Rouge Triangle
        TEAM_17(1, 30, "Orange", "✚", 14, 1),     // Orange Triangle
        TEAM_18(1, 31, "Jaune", "✚", 11, 4),      // Jaune Triangle
        TEAM_19(1, 32, "Vert", "✚", 10, 5),       // Vert Triangle
        TEAM_20(1, 33, "Gris", "✚", 7, 7),        // Gris Triangle
        TEAM_21(1, 34, "Rose", "✚", 9, 6),        // Rose Triangle

        TEAM_22(1, 37, "Bleu", "■", 12, 3),       // Bleu Carré
        TEAM_23(1, 38, "Rouge", "■", 1, 14),      // Rouge Carré
        TEAM_24(1, 39, "Orange", "■", 14, 1),     // Orange Carré
        TEAM_25(1, 40, "Jaune", "■", 11, 4),      // Jaune Carré
        TEAM_26(1, 41, "Vert", "■", 10, 5),       // Vert Carré
        TEAM_27(1, 42, "Gris", "■", 7, 7),        // Gris Carré
        TEAM_28(1, 43, "Rose", "■", 9, 6),        // Rose Carré

        // PAGE 2 - avec symboles pour différencier les doublons
        TEAM_29(2, 10, "Bleu", "✦", 12, 3),        // Bleu sans symbole (premier)
        TEAM_30(2, 11, "Rouge", "✦", 1, 14),       // Rouge sans symbole (premier)
        TEAM_31(2, 12, "Orange", "✦", 14, 1),      // Orange sans symbole (premier)
        TEAM_32(2, 13, "Jaune", "✦", 11, 4),       // Jaune sans symbole (premier)
        TEAM_33(2, 14, "Vert", "✦", 10, 5),        // Vert sans symbole (premier)
        TEAM_34(2, 15, "Gris", "✦", 7, 7),         // Gris sans symbole (premier)
        TEAM_35(2, 16, "Rose", "✦", 9, 6),         // Rose sans symbole (premier)

        TEAM_36(2, 19, "Bleu", "●", 12, 3),       // Bleu Point
        TEAM_37(2, 20, "Rouge", "●", 1, 14),      // Rouge Point
        TEAM_38(2, 21, "Orange", "●", 14, 1),     // Orange Point
        TEAM_39(2, 22, "Jaune", "●", 11, 4),      // Jaune Point
        TEAM_40(2, 23, "Vert", "●", 10, 5),       // Vert Point
        TEAM_41(2, 24, "Gris", "●", 7, 7),        // Gris Point
        TEAM_42(2, 25, "Rose", "●", 9, 6),        // Rose Point

        TEAM_43(2, 28, "Bleu", "▲", 12, 3),       // Bleu Triangle
        TEAM_44(2, 29, "Rouge", "▲", 1, 14),      // Rouge Triangle
        TEAM_45(2, 30, "Orange", "▲", 14, 1),     // Orange Triangle
        TEAM_46(2, 31, "Jaune", "▲", 11, 4),      // Jaune Triangle
        TEAM_47(2, 32, "Vert", "▲", 10, 5),       // Vert Triangle
        TEAM_48(2, 33, "Gris", "▲", 7, 7),        // Gris Triangle
        TEAM_49(2, 34, "Rose", "▲", 9, 6),        // Rose Triangle

        TEAM_50(2, 37, "Bleu", "❤", 12, 3),       // Bleu Carré
        TEAM_51(2, 38, "Rouge", "❤", 1, 14),      // Rouge Carré
        TEAM_52(2, 39, "Orange", "❤", 14, 1),     // Orange Carré
        TEAM_53(2, 40, "Jaune", "❤", 11, 4),      // Jaune Carré
        TEAM_54(2, 41, "Vert", "❤", 10, 5),       // Vert Carré
        TEAM_55(2, 42, "Gris", "❤", 7, 7),        // Gris Carré
        TEAM_56(2, 43, "Rose", "❤", 9, 6);        // Rose Carré

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

