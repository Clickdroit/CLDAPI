package fr.clickdroit.api.module.games;

import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.common.scoreboard.ScoreboardContents;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.utils.Chrono;
import fr.clickdroit.api.utils.DaMath;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UHCScoreboard implements ScoreboardContents {
    private final GameManager gameManager;

    private final GameConfig gameConfig;

    private long seconds;

    private boolean isPvp;

    private boolean isBorder;

    private String gameTime;

    private String pvpTime;

    private String borderTime;

    private int episode;

    private int kills;

    private int groupe;

    private int borderSize;

    private int locCenterPlayer;

    private int playersSize;

    private String arrow;

    public UHCScoreboard(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameConfig = gameManager.getGameConfig();
    }

    public void reloadData(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        this.seconds = this.gameManager.getGlobalTask().getGlobalTime();
        this.isPvp = Rules.pvp.isActive();
        this.isBorder = this.gameManager.getBorder().isStart();
        if (player.getLocation().getWorld() != null)
            if (player.getLocation().getWorld().getName().equalsIgnoreCase("world_nether")) {
                this.locCenterPlayer = (int)Bukkit.getWorld("world_nether").getWorldBorder().getCenter().distance(player.getLocation());
                this.arrow = DaMath.getArrow(player.getLocation().clone(), Bukkit.getWorld("world_nether").getWorldBorder().getCenter().clone());
            } else {
                this.locCenterPlayer = (int)this.gameManager.getApi().getLobbyPopulator().getCenter().distance(player.getLocation());
                this.arrow = DaMath.getArrow(player.getLocation().clone(), this.gameManager.getApi().getLobbyPopulator().getCenter().clone());
            }
        this.borderSize = (int)this.gameManager.getWorldPopulator().getGameWorld().getWorldBorder().getSize() / 2;
        this.gameTime = Chrono.timeToDigitalString(this.seconds);
        this.pvpTime = Chrono.timeToDigitalString(this.gameConfig.getPvpTime() - this.seconds);
        this.borderTime = Chrono.timeToDigitalString(this.gameConfig.getBorderTime() - this.seconds);
        this.episode = this.gameManager.getEpisodeManager().getEpisode();
        this.kills = GamePlayer.getPlayer(uuid).getKills();
        this.playersSize = this.gameManager.getInGamePlayers().size();
        this.groupe = this.gameManager.getGroupe();
    }

    public void setLines(BPlayerBoard board, UUID uuid, String ip) {
        int line = 14;
        board.setName("");
        board.set("", Integer.valueOf(line--));
        board.set(" "+ this.episode, Integer.valueOf(line--));
        board.set(" "+ Chrono.timeToDigitalString(this.seconds), Integer.valueOf(line--));
        board.set(((this.playersSize == 1) ? "§aActive": "§cDesactive" ) + "" + this.playersSize, Integer.valueOf(line--));
        board.set("", Integer.valueOf(line--));
        board.set("" + (!this.isPvp ? this.pvpTime : ""), Integer.valueOf(line--));
        board.set(" ", Integer.valueOf(line--));
        board.set("   "+ (!this.isBorder ? this.borderTime : ""), Integer.valueOf(line--));
        board.set("  " + this.borderSize + " "+ this.borderSize, Integer.valueOf(line--));
        board.set("", Integer.valueOf(line--));
        board.set(" "+ this.locCenterPlayer + "m " + this.arrow, Integer.valueOf(line--));
        if (this.kills > 0)
            board.set(((this.kills == 1) ? "§aActive": "§cDesactive" ) + "" + this.kills, Integer.valueOf(line--));
        board.set("", Integer.valueOf(line--));
        board.set(ChatColor.RED + ip, Integer.valueOf(line--));
    }
}
