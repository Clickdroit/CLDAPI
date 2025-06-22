package fr.clickdroit.api.utils;

import org.bukkit.potion.PotionEffectType;

public class TranslateEffect {
    public static String translate(PotionEffectType potionEffectType) {
        if (potionEffectType.equals(PotionEffectType.SPEED))
            return "Vitesse";
        if (potionEffectType.equals(PotionEffectType.SLOW))
            return "Lenteur";
        if (potionEffectType.equals(PotionEffectType.FAST_DIGGING))
            return "Minage accéléré";
        if (potionEffectType.equals(PotionEffectType.SLOW_DIGGING))
            return "Minage ralenti";
        if (potionEffectType.equals(PotionEffectType.INCREASE_DAMAGE))
            return "Force";
        if (potionEffectType.equals(PotionEffectType.HEAL))
            return "Soin";
        if (potionEffectType.equals(PotionEffectType.HARM))
            return "Dégats";
        if (potionEffectType.equals(PotionEffectType.JUMP))
            return "Saut amélioré";
        if (potionEffectType.equals(PotionEffectType.CONFUSION))
            return "Nausée";
        if (potionEffectType.equals(PotionEffectType.REGENERATION))
            return "Régeneration";
        if (potionEffectType.equals(PotionEffectType.DAMAGE_RESISTANCE))
            return "Résistance aux dégats";
        if (potionEffectType.equals(PotionEffectType.FIRE_RESISTANCE))
            return "Résistance aux feu";
        if (potionEffectType.equals(PotionEffectType.WATER_BREATHING))
            return "Respiration";
        if (potionEffectType.equals(PotionEffectType.INVISIBILITY))
            return "Invisibilité";
        if (potionEffectType.equals(PotionEffectType.BLINDNESS))
            return "Aveuglement";
        if (potionEffectType.equals(PotionEffectType.NIGHT_VISION))
            return "Vision nocturne";
        if (potionEffectType.equals(PotionEffectType.HUNGER))
            return "Faim";
        if (potionEffectType.equals(PotionEffectType.WEAKNESS))
            return "Faiblesse";
        if (potionEffectType.equals(PotionEffectType.POISON))
            return "Poison";
        if (potionEffectType.equals(PotionEffectType.WITHER))
            return "Wither";
        if (potionEffectType.equals(PotionEffectType.HEALTH_BOOST))
            return "Boost de coeur";
        if (potionEffectType.equals(PotionEffectType.ABSORPTION))
            return "Absorption";
        if (potionEffectType.equals(PotionEffectType.SATURATION))
            return "Saturation";
        return null;
    }
}
