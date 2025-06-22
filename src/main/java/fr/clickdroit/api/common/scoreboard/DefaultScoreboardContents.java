package fr.clickdroit.api.common.scoreboard;

import fr.clickdroit.api.API;
import fr.clickdroit.api.UHCInfos;
import fr.clickdroit.api.game.GameUtils;
import fr.clickdroit.api.module.ModuleType;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;

import java.util.UUID;

public class DefaultScoreboardContents implements ScoreboardContents {
    private final API api;

    private int online;

    private int maxplayer;

    private String moduleName;

    private String hostName;

    private boolean teams;

    private ModuleType moduleType;

    public DefaultScoreboardContents(API api) {
        this.api = api;
    }

    public void reloadData(UUID player) {
        this.maxplayer = this.api.getGameManager().getGameConfig().getGameSlot();
        this.online = GameUtils.getPlayerAmount();
        this.moduleName = this.api.getGameManager().getModuleManager().getCurrentModule().getName();
        this.teams = !GameUtils.isSoloMode();
        this.moduleType = this.api.getGameManager().getModuleManager().getCurrentModule();
        this.hostName = (UHCInfos.hostName == null) ? "Aucun" : UHCInfos.hostName;
    }

    public void setLines(BPlayerBoard board, UUID player, String ip) {
        int line = 14;
        board.setName("");
        board.set("", Integer.valueOf(line--));
        board.set("" + ((this.online == 1) ? "" : "s") + "+ this.online + "+ this.maxplayer, Integer.valueOf(line--));
        board.set(" "+ this.hostName, Integer.valueOf(line--));
        board.set(" "+ this.moduleName, Integer.valueOf(line--));
        if (this.teams) {
            int am = this.api.getGameManager().getGameConfig().getPlayerPerTeam();
            board.set(" + am + "+ am, Integer.valueOf(line--));
        }
        board.set("", Integer.valueOf(line--));
    }
}
