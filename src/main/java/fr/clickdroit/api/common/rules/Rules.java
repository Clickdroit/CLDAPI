package fr.clickdroit.api.common.rules;

import fr.clickdroit.api.API;

public class Rules {
    public static final PvP pvp = new PvP();

    public static final NoDamage noDamage = new NoDamage();

    public static void load(API main) {
        pvp.onLoad(main);
        noDamage.onLoad(main);
    }
}
