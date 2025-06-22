package fr.clickdroit.api.utils.role.effect;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class InfinityEffect {
    private final PotionEffectType type;

    private final int level;

    public InfinityEffect(PotionEffectType type, int level) {
        this.type = type;
        this.level = level;
    }

    public void apply(Player player) {
        if (player != null) {
            player.removePotionEffect(this.type);
            player.addPotionEffect(getInfinitePotion());
        }
    }

    public void remove(Player player) {
        if (player != null)
            for (PotionEffect potionEffect : player.getActivePotionEffects()) {
                if (potionEffect.getType() == this.type && potionEffect.getAmplifier() == this.level)
                    player.removePotionEffect(potionEffect.getType());
            }
    }

    public PotionEffect getInfinitePotion() {
        return new PotionEffect(this.type, 2147483647, this.level, false, false);
    }

    public PotionEffect getUnstablePotion() {
        return new PotionEffect(this.type, 100, this.level, false, false);
    }

    public PotionEffectType getType() {
        return this.type;
    }

    public int getLevel() {
        return this.level;
    }
}
