package fr.clickdroit.api.config;

import fr.clickdroit.api.config.common.GameAccess;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.UHCConstants;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Date;

public class GameConfig {
    private final GameManager gameManager;

    private int gameSlot;

    private int playerPerTeam;

    private int pvpTime;

    private int borderTime;

    private int borderStartSize;

    private int borderEndSize;

    private int borderBlocksPerSecond;

    private boolean showAllTeams;

    private boolean friendlyfire;

    private boolean spectators;

    private boolean nether;

    private long dayNightDuration;

    private int diamondMax;

    private int goldMax;

    private GameAccess gameAccess;

    private boolean chat;

    private boolean gameDev;

    private int roleTime;

    private int enderpearlDamage;

    private Date openDate;

    private boolean serverHourSetup;

    private int episodeTime;

    private int disconnectSeconds;

    private WaitingTeleportationState teleportationState;

    public GameConfig(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameSlot = UHCConstants.DEFAULT_SLOTS;
        this.playerPerTeam = 1;
        this.pvpTime = UHCConstants.DEFAULT_PVP_TIME;
        this.roleTime = 0;
        this.borderTime = UHCConstants.DEFAULT_BORDER_TIME;
        this.borderStartSize = UHCConstants.DEFAULT_BORDER_START_SIZE;
        this.borderBlocksPerSecond = UHCConstants.DEFAULT_BORDER_SPEED;
        this.borderEndSize = UHCConstants.DEFAULT_BORDER_END_SIZE;
        this.showAllTeams = true;
        this.friendlyfire = true;
        this.spectators = true;
        this.nether = true;
        this.dayNightDuration = UHCConstants.DEFAULT_DAY_NIGHT_DURATION;
        this.diamondMax = UHCConstants.DEFAULT_DIAMOND_MAX;
        this.goldMax = UHCConstants.DEFAULT_GOLD_MAX;
        this.enderpearlDamage = UHCConstants.DEFAULT_ENDERPEARL_DAMAGE;
        this.gameAccess = GameAccess.OPEN;
        this.chat = false;
        this.gameDev = false;
        this.openDate = Date.from(Instant.now());
        this.serverHourSetup = true;
        this.episodeTime = UHCConstants.DEFAULT_EPISODE_TIME;
        this.disconnectSeconds = UHCConstants.DEFAULT_DISCONNECT_TIME;
        this.teleportationState = WaitingTeleportationState.IN_LOBBY;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public int getEnderpearlDamage() {
        return this.enderpearlDamage;
    }

    public void setEnderpearlDamage(int enderpearlDamage) {
        this.enderpearlDamage = enderpearlDamage;
    }

    public int getEpisodeTime() {
        return this.episodeTime;
    }

    public void setEpisodeTime(int episodeTime) {
        this.episodeTime = episodeTime;
    }

    public int getDiamondMax() {
        return this.diamondMax;
    }

    public void setDiamondMax(int diamondMax) {
        this.diamondMax = diamondMax;
    }

    public int getGoldMax() {
        return this.goldMax;
    }

    public void setGoldMax(int goldMax) {
        this.goldMax = goldMax;
    }

    public long getDayNightDuration() {
        return this.dayNightDuration;
    }

    public void setDayNightDuration(long dayNightDuration) {
        this.dayNightDuration = dayNightDuration;
    }

    public boolean isSpectators() {
        return this.spectators;
    }

    public void setSpectators(boolean spectators) {
        this.spectators = spectators;
    }

    public boolean isNether() {
        return this.nether;
    }

    public void setNether(boolean nether) {
        this.nether = nether;
    }

    public boolean isShowAllTeams() {
        return this.showAllTeams;
    }

    public void setShowAllTeams(boolean showAllTeams) {
        this.showAllTeams = showAllTeams;
    }

    public boolean isFriendlyfire() {
        return this.friendlyfire;
    }

    public void setFriendlyfire(boolean friendlyfire) {
        this.friendlyfire = friendlyfire;
    }

    public int getBorderBlocksPerSecond() {
        return this.borderBlocksPerSecond;
    }

    public void setBorderBlocksPerSecond(int borderBlocksPerSecond) {
        this.borderBlocksPerSecond = borderBlocksPerSecond;
    }

    public int getBorderStartSize() {
        return this.borderStartSize;
    }

    public void setBorderStartSize(int borderStartSize) {
        this.borderStartSize = borderStartSize;
    }

    public int getBorderEndSize() {
        return this.borderEndSize;
    }

    public void setBorderEndSize(int borderEndSize) {
        this.borderEndSize = borderEndSize;
    }

    public int getPvpTime() {
        return this.pvpTime;
    }

    public void setPvpTime(int pvpTime) {
        this.pvpTime = pvpTime;
    }

    public int getBorderTime() {
        return this.borderTime;
    }

    public void setBorderTime(int borderTime) {
        this.borderTime = borderTime;
    }

    public int getPlayerPerTeam() {
        return this.playerPerTeam;
    }

    public void setPlayerPerTeam(int playerPerTeam) {
        this.playerPerTeam = playerPerTeam;
        if (this.playerPerTeam == 0) {
            this.playerPerTeam = 10;
        } else if (this.playerPerTeam == 11) {
            this.playerPerTeam = 1;
        }
    }

    public int getGameSlot() {
        return this.gameSlot;
    }

    public void setGameSlot(int gameSlot) {
        this.gameSlot = gameSlot;
    }

    public int getRoleTime() {
        return this.roleTime;
    }

    public void setRoleTime(int roleTime) {
        this.roleTime = roleTime;
    }

    public GameAccess getGameAccess() {
        return this.gameAccess;
    }

    public void setGameAccess(GameAccess gameAccess) {
        this.gameAccess = gameAccess;
        this.gameManager.getApi().getLogger().info("Game access changed to: " + gameAccess.getMessage());
    }
    public void toggleGameAccess() {
        if (this.gameAccess == GameAccess.OPEN) {
            setGameAccess(GameAccess.CLOSE);
        } else {
            setGameAccess(GameAccess.OPEN);
        }
    }
    public boolean isGameOpen() {
        return this.gameAccess == GameAccess.OPEN;
    }

    public boolean isChat() {
        return this.chat;
    }

    public void setChat(boolean chat) {
        this.chat = chat;
    }

    public boolean isGameDev() {
        return this.gameDev;
    }

    public void setGameDev(boolean gameDev) {
        this.gameDev = gameDev;
    }

    public boolean isServerHourSetup() {
        return this.serverHourSetup;
    }

    public void setServerHourSetup(boolean serverHourSetup) {
        this.serverHourSetup = serverHourSetup;
    }

    public Date getOpenDate() {
        return this.openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public int getDisconnectSeconds() {
        return this.disconnectSeconds;
    }

    public void setDisconnectSeconds(int disconnectSeconds) {
        this.disconnectSeconds = disconnectSeconds;
    }

    public WaitingTeleportationState getTeleportationState() {
        return this.teleportationState;
    }

    public void setTeleportationState(WaitingTeleportationState teleportationState) {
        this.teleportationState = teleportationState;
        if (teleportationState.equals(WaitingTeleportationState.IN_LOBBY)) {
            Location location = this.gameManager.getApi().getLobbyPopulator().getLobbyLocation();
            for (Player players : Bukkit.getOnlinePlayers())
                players.teleport(location);
        } else {
            Location location = this.gameManager.getApi().getLobbyPopulator().getLobbyRulesRoom();
            for (Player players : Bukkit.getOnlinePlayers())
                players.teleport(location);
        }
    }

    public enum WaitingTeleportationState {
        IN_LOBBY, IN_ROOM;
    }
}
