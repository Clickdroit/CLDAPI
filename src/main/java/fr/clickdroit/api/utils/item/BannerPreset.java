package fr.clickdroit.api.utils.item;

import org.bukkit.DyeColor;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;

public enum BannerPreset {
    barre,
    precedent,
    suivant,
    coeur,
    croix;

    public Pattern[] getPatterns(DyeColor color) {
        switch (this) {
            case barre:
                return new Pattern[] {
                        new Pattern(color, PatternType.STRIPE_TOP),
                        new Pattern(color, PatternType.STRIPE_BOTTOM)
                };
            case precedent:
                return new Pattern[] {
                        new Pattern(color, PatternType.TRIANGLE_TOP),
                        new Pattern(color, PatternType.TRIANGLE_BOTTOM)
                };
            case suivant:
                return new Pattern[] {
                        new Pattern(color, PatternType.TRIANGLES_TOP),
                        new Pattern(color, PatternType.TRIANGLES_BOTTOM)
                };
            case coeur:
                return new Pattern[] {
                        new Pattern(color, PatternType.RHOMBUS_MIDDLE),
                        new Pattern(color, PatternType.HALF_HORIZONTAL)
                };
            case croix:
                return new Pattern[] {
                        new Pattern(color, PatternType.CROSS),
                        new Pattern(color, PatternType.STRAIGHT_CROSS)
                };
            default:
                return new Pattern[0];
        }
    }
}