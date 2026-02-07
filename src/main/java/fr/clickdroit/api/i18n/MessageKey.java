package fr.clickdroit.api.i18n;

/**
 * Énumération des clés de messages pour l'internationalisation.
 */
public enum MessageKey {
    // Préfixes
    PREFIX("prefix"),
    ERROR_PREFIX("error_prefix"),
    SUCCESS_PREFIX("success_prefix"),

    // Messages généraux
    NO_PERMISSION("no_permission"),
    PLAYER_ONLY("player_only"),
    PLAYER_NOT_FOUND("player_not_found"),
    INVALID_ARGUMENTS("invalid_arguments"),

    // Messages de jeu
    GAME_STARTING("game.starting"),
    GAME_STARTED("game.started"),
    GAME_ENDED("game.ended"),
    GAME_ALREADY_STARTED("game.already_started"),
    GAME_NOT_STARTED("game.not_started"),

    // Messages PvP
    PVP_ENABLED("pvp.enabled"),
    PVP_DISABLED("pvp.disabled"),
    PVP_TIME_REMAINING("pvp.time_remaining"),

    // Messages de bordure
    BORDER_SHRINKING("border.shrinking"),
    BORDER_STOPPED("border.stopped"),
    BORDER_SIZE("border.size"),

    // Messages d'épisode
    EPISODE_CHANGE("episode.change"),
    EPISODE_CURRENT("episode.current"),

    // Messages de mort
    DEATH_KILLED_BY_PLAYER("death.killed_by_player"),
    DEATH_KILLED_BY_MOB("death.killed_by_mob"),
    DEATH_ENVIRONMENT("death.environment"),
    DEATH_DISCONNECT("death.disconnect"),

    // Messages d'équipe
    TEAM_JOINED("team.joined"),
    TEAM_LEFT("team.left"),
    TEAM_FULL("team.full"),
    TEAM_ELIMINATED("team.eliminated"),

    // Messages de scénarios
    SCENARIO_ENABLED("scenario.enabled"),
    SCENARIO_DISABLED("scenario.disabled"),

    // Messages de configuration
    CONFIG_SAVED("config.saved"),
    CONFIG_LOADED("config.loaded"),
    CONFIG_INVALID("config.invalid"),

    // Messages d'administration
    HOST_REQUIRED("admin.host_required"),
    HOST_ADDED("admin.host_added"),
    HOST_REMOVED("admin.host_removed"),

    // Messages de cycle jour/nuit
    DAY_START("cycle.day_start"),
    NIGHT_START("cycle.night_start");

    private final String key;

    MessageKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }
}

