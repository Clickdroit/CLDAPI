package fr.clickdroit.api.service.impl;

import fr.clickdroit.api.API;
import fr.clickdroit.api.service.GameService;
import fr.clickdroit.api.utils.UHCConstants;

/**
 * Service de gestion de la configuration de la partie.
 */
public class ConfigService implements GameService {

    private API api;
    private boolean initialized = false;

    // Configuration générale
    private int gameSlots = UHCConstants.DEFAULT_SLOTS;
    private int playerPerTeam = 1;
    private boolean friendlyFire = true;
    private boolean spectators = true;
    private boolean nether = true;
    private boolean chat = false;

    // Temps
    private int pvpTime = UHCConstants.DEFAULT_PVP_TIME;
    private int borderTime = UHCConstants.DEFAULT_BORDER_TIME;
    private int episodeTime = UHCConstants.DEFAULT_EPISODE_TIME;
    private int disconnectTime = UHCConstants.DEFAULT_DISCONNECT_TIME;
    private long dayNightDuration = UHCConstants.DEFAULT_DAY_NIGHT_DURATION;

    // Bordure
    private int borderStartSize = UHCConstants.DEFAULT_BORDER_START_SIZE;
    private int borderEndSize = UHCConstants.DEFAULT_BORDER_END_SIZE;
    private int borderSpeed = UHCConstants.DEFAULT_BORDER_SPEED;

    // Limites
    private int diamondMax = UHCConstants.DEFAULT_DIAMOND_MAX;
    private int goldMax = UHCConstants.DEFAULT_GOLD_MAX;
    private int enderpearlDamage = UHCConstants.DEFAULT_ENDERPEARL_DAMAGE;

    @Override
    public void initialize(API api) {
        this.api = api;
        this.initialized = true;
    }

    @Override
    public void shutdown() {
        initialized = false;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public int getPriority() {
        return 95; // Haute priorité
    }

    // ===== Getters et Setters - Configuration générale =====

    public int getGameSlots() { return gameSlots; }
    public void setGameSlots(int slots) { this.gameSlots = slots; }

    public int getPlayerPerTeam() { return playerPerTeam; }
    public void setPlayerPerTeam(int count) { this.playerPerTeam = count; }

    public boolean isFriendlyFire() { return friendlyFire; }
    public void setFriendlyFire(boolean enabled) { this.friendlyFire = enabled; }

    public boolean isSpectators() { return spectators; }
    public void setSpectators(boolean enabled) { this.spectators = enabled; }

    public boolean isNether() { return nether; }
    public void setNether(boolean enabled) { this.nether = enabled; }

    public boolean isChat() { return chat; }
    public void setChat(boolean enabled) { this.chat = enabled; }

    public boolean isSoloMode() { return playerPerTeam == 1; }

    // ===== Getters et Setters - Temps =====

    public int getPvpTime() { return pvpTime; }
    public void setPvpTime(int seconds) { this.pvpTime = seconds; }

    public int getBorderTime() { return borderTime; }
    public void setBorderTime(int seconds) { this.borderTime = seconds; }

    public int getEpisodeTime() { return episodeTime; }
    public void setEpisodeTime(int seconds) { this.episodeTime = seconds; }

    public int getDisconnectTime() { return disconnectTime; }
    public void setDisconnectTime(int seconds) { this.disconnectTime = seconds; }

    public long getDayNightDuration() { return dayNightDuration; }
    public void setDayNightDuration(long seconds) { this.dayNightDuration = seconds; }

    // ===== Getters et Setters - Bordure =====

    public int getBorderStartSize() { return borderStartSize; }
    public void setBorderStartSize(int size) { this.borderStartSize = size; }

    public int getBorderEndSize() { return borderEndSize; }
    public void setBorderEndSize(int size) { this.borderEndSize = size; }

    public int getBorderSpeed() { return borderSpeed; }
    public void setBorderSpeed(int blocksPerSecond) { this.borderSpeed = blocksPerSecond; }

    // ===== Getters et Setters - Limites =====

    public int getDiamondMax() { return diamondMax; }
    public void setDiamondMax(int max) { this.diamondMax = max; }
    public boolean hasDiamondLimit() { return diamondMax > 0; }

    public int getGoldMax() { return goldMax; }
    public void setGoldMax(int max) { this.goldMax = max; }
    public boolean hasGoldLimit() { return goldMax > 0; }

    public int getEnderpearlDamage() { return enderpearlDamage; }
    public void setEnderpearlDamage(int damage) { this.enderpearlDamage = damage; }

    // ===== Utilitaires =====

    public void resetToDefaults() {
        this.gameSlots = UHCConstants.DEFAULT_SLOTS;
        this.playerPerTeam = 1;
        this.friendlyFire = true;
        this.spectators = true;
        this.nether = true;
        this.chat = false;
        this.pvpTime = UHCConstants.DEFAULT_PVP_TIME;
        this.borderTime = UHCConstants.DEFAULT_BORDER_TIME;
        this.episodeTime = UHCConstants.DEFAULT_EPISODE_TIME;
        this.disconnectTime = UHCConstants.DEFAULT_DISCONNECT_TIME;
        this.dayNightDuration = UHCConstants.DEFAULT_DAY_NIGHT_DURATION;
        this.borderStartSize = UHCConstants.DEFAULT_BORDER_START_SIZE;
        this.borderEndSize = UHCConstants.DEFAULT_BORDER_END_SIZE;
        this.borderSpeed = UHCConstants.DEFAULT_BORDER_SPEED;
        this.diamondMax = UHCConstants.DEFAULT_DIAMOND_MAX;
        this.goldMax = UHCConstants.DEFAULT_GOLD_MAX;
        this.enderpearlDamage = UHCConstants.DEFAULT_ENDERPEARL_DAMAGE;
    }
}

