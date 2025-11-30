package fr.clickdroit.api.utils;

/**
 * Classe contenant toutes les constantes utilisées dans l'API UHC.
 * Cette classe centralise les valeurs pour faciliter la maintenance et la modification.
 */
public final class UHCConstants {

    private UHCConstants() {
        // Classe utilitaire, pas d'instanciation
    }

    // ===== PRÉFIXES DE MESSAGES =====
    
    /** Préfixe standard pour les messages UHC */
    public static final String PREFIX = "§8[§6UHC§8] §f";
    
    /** Préfixe pour les alertes de diamant */
    public static final String DIAMOND_ALERT_PREFIX = "§f[FD] §b";
    
    /** Préfixe pour les messages d'erreur */
    public static final String ERROR_PREFIX = "§c";
    
    /** Préfixe pour les messages de succès */
    public static final String SUCCESS_PREFIX = "§a";
    
    /** Préfixe pour les messages d'information */
    public static final String INFO_PREFIX = "§e";
    
    /** Préfixe pour les messages host */
    public static final String HOST_PREFIX = "§c§lHOST ";

    // ===== TEMPS EN SECONDES =====
    
    /** Durée du combat log en secondes */
    public static final long COMBAT_TIME_SECONDS = 60L;
    
    /** Durée par défaut du PvP en secondes */
    public static final int DEFAULT_PVP_TIME = 1200;
    
    /** Durée par défaut de la bordure en secondes */
    public static final int DEFAULT_BORDER_TIME = 6000;
    
    /** Durée par défaut d'un épisode en secondes */
    public static final int DEFAULT_EPISODE_TIME = 1200;
    
    /** Temps de déconnexion par défaut en secondes */
    public static final int DEFAULT_DISCONNECT_TIME = 900;
    
    /** Durée par défaut du cycle jour/nuit en secondes */
    public static final long DEFAULT_DAY_NIGHT_DURATION = 600L;

    // ===== TICKS =====
    
    /** Nombre de ticks par seconde */
    public static final int TICKS_PER_SECOND = 20;
    
    /** Nombre de ticks par minute */
    public static final int TICKS_PER_MINUTE = 1200;

    // ===== VALEURS PAR DÉFAUT =====
    
    /** Nombre de slots par défaut */
    public static final int DEFAULT_SLOTS = 60;
    
    /** Taille de départ de la bordure */
    public static final int DEFAULT_BORDER_START_SIZE = 1250;
    
    /** Taille finale de la bordure */
    public static final int DEFAULT_BORDER_END_SIZE = 250;
    
    /** Vitesse de réduction de la bordure (blocs par seconde) */
    public static final int DEFAULT_BORDER_SPEED = 1;
    
    /** Dégâts de perle de l'Ender par défaut */
    public static final int DEFAULT_ENDERPEARL_DAMAGE = 2;
    
    /** Nombre de groupes par défaut */
    public static final int DEFAULT_GROUPES = 6;
    
    /** Durée du cache des hosts en millisecondes */
    public static final long HOST_CACHE_DURATION_MS = 5000;

    // ===== LIMITES =====
    
    /** Limite maximale de diamants (0 = pas de limite) */
    public static final int DEFAULT_DIAMOND_MAX = 0;
    
    /** Limite maximale d'or (0 = pas de limite) */
    public static final int DEFAULT_GOLD_MAX = 0;
    
    /** Longueur maximale du nom de l'host */
    public static final int MAX_HOST_NAME_LENGTH = 32;

    // ===== MESSAGES DE MORT =====
    
    /** Format de message de mort en solo */
    public static final String DEATH_MESSAGE_SOLO = "§8| §c%player% §fest mort.";
    
    /** Format de message de kill en solo */
    public static final String KILL_MESSAGE_SOLO = "§8| §c%player% §fa été tué par §c%killer%§f.";
    
    /** Format de message de mort en équipe */
    public static final String DEATH_MESSAGE_TEAM = "§8| §c%teamColor%%teamName% %player% est mort.";
    
    /** Format de message de kill en équipe */
    public static final String KILL_MESSAGE_TEAM = "§8| §c%teamColor%%teamName% %player% a été tué par %killerColor%%killerTeam% %killer%.";

    // ===== SYMBOLES =====
    
    /** Symbole de cœur */
    public static final String HEART_SYMBOL = "❤";
    
    /** Symbole de coche */
    public static final String CHECK_SYMBOL = "✓";
    
    /** Symbole de croix */
    public static final String CROSS_SYMBOL = "✗";

    // ===== COULEURS =====
    
    /** Couleur primaire */
    public static final String COLOR_PRIMARY = "§6";
    
    /** Couleur secondaire */
    public static final String COLOR_SECONDARY = "§f";
    
    /** Couleur d'erreur */
    public static final String COLOR_ERROR = "§c";
    
    /** Couleur de succès */
    public static final String COLOR_SUCCESS = "§a";
    
    /** Couleur d'avertissement */
    public static final String COLOR_WARNING = "§e";
    
    /** Couleur d'information */
    public static final String COLOR_INFO = "§b";

    // ===== NOMS DE MONDES =====
    
    /** Nom du monde Lobby */
    public static final String LOBBY_WORLD_NAME = "Lobby";
}
