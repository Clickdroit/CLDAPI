package fr.clickdroit.api.game;

import fr.clickdroit.api.API;
import fr.clickdroit.api.GamePlayer;
import fr.clickdroit.api.common.border.SimpleBorder;
import fr.clickdroit.api.common.world.WorldPopulator;
import fr.clickdroit.api.config.GameConfig;
import fr.clickdroit.api.config.scenario.Scenario;
import fr.clickdroit.api.config.scenario.ScenarioManager;
import fr.clickdroit.api.game.combatlog.CombatLogManager;
import fr.clickdroit.api.game.cycle.CycleManager;
import fr.clickdroit.api.game.episode.EpisodeManager;
import fr.clickdroit.api.game.task.BeforeStartTask;
import fr.clickdroit.api.game.task.GlobalTask;
import fr.clickdroit.api.game.task.StartGameCountDown;
import fr.clickdroit.api.game.team.TeamManager;
import fr.clickdroit.api.game.team.Teams;
import fr.clickdroit.api.game.teleportation.TeleportationManager;
import fr.clickdroit.api.game.teleportation.form.CircleForm;
import fr.clickdroit.api.game.teleportation.form.Form;
import fr.clickdroit.api.game.teleportation.player.PlayerPlate;
import fr.clickdroit.api.game.teleportation.player.SoloPlayerPlate;
import fr.clickdroit.api.game.teleportation.player.TeamPlayerPlate;
import fr.clickdroit.api.module.ModuleManager;
import fr.clickdroit.api.module.standard.UHCFinisherGame;
import fr.clickdroit.api.module.standard.UHCStandard;
import fr.clickdroit.api.utils.InventoryAPI;
import fr.clickdroit.api.utils.TabHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GameManager {
    private final API api;

    private final GameConfig gameConfig;

    private final ModuleManager moduleManager;

    private final EpisodeManager episodeManager;

    private final TeamManager teamManager;

    private final WorldPopulator worldPopulator;

    private final CycleManager cycleManager;

    private final CombatLogManager combatLogManager;

    private final UHCStandard uhcStandard;

    private final UHCFinisherGame uhcFinisherGame;

    private final SimpleBorder border;

    private final List<UUID> inGamePlayers;

    private final List<Teams> aliveTeams;

    private final List<UUID> offlinePlayers;

    private final List<Scenario> enabledScenarios;

    private final List<UUID> playedPlayers;

    private final List<String> playedDomeiPlayers;

    private final List<String> whitelistedPlayers;

    private GameState gameState;

    private boolean announcedOnHub;

    private boolean preload;

    private boolean preloadFinished;

    private int groupe;

    private BeforeStartTask beforeStartTask;

    private GlobalTask globalTask;

    private StartGameCountDown startGameCountDown;

    private UUID gameHost;

    private final List<UUID> hosts;

    private final List<String> banList;

    private final List<UUID> vanishList;

    public GameManager(API api) {
        this.api = api;
        this.moduleManager = new ModuleManager(this);
        this.gameState = GameState.WAITING;
        this.inGamePlayers = new ArrayList<>();
        this.playedPlayers = new ArrayList<>();
        this.hosts = new ArrayList<>();
        this.banList = new ArrayList<>();
        this.vanishList = new ArrayList<>();
        this.playedDomeiPlayers = new ArrayList<>();
        this.aliveTeams = new ArrayList<>();
        this.offlinePlayers = new ArrayList<>();
        this.enabledScenarios = new ArrayList<>();
        this.whitelistedPlayers = new ArrayList<>();
        this.gameConfig = new GameConfig(this);
        this.episodeManager = new EpisodeManager(this);
        this.teamManager = new TeamManager(this);
        this.worldPopulator = new WorldPopulator(this);
        this.uhcStandard = new UHCStandard(this);
        this.uhcFinisherGame = new UHCFinisherGame(this.api);
        this.border = new SimpleBorder(this.worldPopulator.getGameWorld().getWorldBorder());
        this.cycleManager = new CycleManager(api, this.worldPopulator.getGameWorld());
        this.combatLogManager = new CombatLogManager(this);
        this.groupe = 6;
        this.beforeStartTask = new BeforeStartTask(this);
        this.beforeStartTask.runTaskTimer((Plugin)api, 60L, 40L);
        this.announcedOnHub = false;
        this.preload = false;
        this.preloadFinished = false;
        Scenario.TIMBER.getScenarioManager().activeScenario();
        Scenario.CUTCLEAN.getScenarioManager().activeScenario();
        Scenario.HASTEYBOYS.getScenarioManager().activeScenario();
        Scenario.SAFEMINER.getScenarioManager().activeScenario();
        Scenario.CAT_EYES.getScenarioManager().activeScenario();
        Scenario.BETAZOMBIE.getScenarioManager().activeScenario();
    }

    public void startWithTimer() {
        setGameState(GameState.STARTING);
        this.startGameCountDown = new StartGameCountDown(this);
        this.startGameCountDown.runTaskTimer((Plugin)this.api, 0L, 20L);
    }

    public void startGame() {
        setGameState(GameState.PLAYING);
        this.api.getModules().onStart(this.api);
        this.globalTask = new GlobalTask(this);
        this.globalTask.runTaskTimer((Plugin)this.api, 0L, 20L);
        this.cycleManager.startDayCycle(this.gameConfig.getDayNightDuration());
        Bukkit.broadcastMessage("       le bonjour ");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(" certaines respecter.");
        Bukkit.broadcastMessage("   respect des ");
        Bukkit.broadcastMessage("   pas ");
        Bukkit.broadcastMessage("   pas sans raison. ");
        Bukkit.broadcastMessage("   pas son r");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(" les connaitre.");
        Bukkit.broadcastMessage("   de voir le explicatifdu mode.");
        Bukkit.broadcastMessage("   de voir le mumble de ");
        Bukkit.broadcastMessage("   de voir les ");
        Bukkit.broadcastMessage("   de de l'aide.");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(" tous !");
        Bukkit.broadcastMessage("");
        for (UUID uuid : getInGamePlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null)
                continue;
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            player.setFoodLevel(20);
            player.setHealth(player.getMaxHealth());
            player.setExp(0.0F);
            player.setLevel(0);
            TabHandler.removePrefixFor(player);
            GameUtils.clearPlayerEffect(player);
            InventoryAPI.giveInvent(player);
        }
        Arrays.<Scenario>stream(Scenario.values())
                .filter(Scenario::isEnabled)
                .map(Scenario::getScenarioManager)
                .forEach(ScenarioManager::onStart);
        this.worldPopulator.getGameWorld().setGameRuleValue("randomTickSpeed", "3");
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> (!this.inGamePlayers.contains(player.getUniqueId()) && player.isOp()))
                .map(OfflinePlayer::getUniqueId)
                .map(GamePlayer::getPlayer)
                .forEach(gamePlayer -> gamePlayer.setAlerts(true));
    }

    public void tryStartGame() {
        if (!this.gameState.equals(GameState.STARTING))
            return;
        System.out.println("[UHC] Starting game..");
        setGameState(GameState.TELEPORTATION);
        this.inGamePlayers.clear();
        this.playedPlayers.clear();
        this.offlinePlayers.clear();
        this.aliveTeams.clear();
        this.enabledScenarios.clear();
        this.border.init((this.gameConfig.getBorderStartSize() * 2), 0, 0);
        List<SoloPlayerPlate> playerPlatesList = new ArrayList<>();
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (!players.getGameMode().equals(GameMode.SPECTATOR)) {
                UUID uuid = players.getUniqueId();
                this.inGamePlayers.add(uuid);
                this.playedPlayers.add(uuid);
                this.playedDomeiPlayers.add(uuid.toString());
                playerPlatesList.add(new SoloPlayerPlate(players));
            }
        }
        for (UUID uuid : this.inGamePlayers) {
            Player player = Bukkit.getPlayer(uuid);
            GamePlayer gamePlayer = GamePlayer.getPlayer(uuid);
            if (player == null || gamePlayer == null) {
                this.inGamePlayers.remove(uuid);
                continue;
            }
            if (this.gameConfig.getPlayerPerTeam() > 1 && gamePlayer
                    .getTeams() == null)
                for (Teams teams : Teams.values()) {
                    if (this.teamManager.getPlayerAmountInTeam(teams) < this.gameConfig.getPlayerPerTeam()) {
                        this.teamManager.addPlayerToTeam(player, teams);
                        break;
                    }
                }
            GameUtils.startPlayer(player, GameMode.ADVENTURE);
        }
        if (this.gameConfig.getPlayerPerTeam() > 1)
            for (Teams teams : Teams.values()) {
                if (this.teamManager.getPlayerAmountInTeam(teams) >= 1)
                    getAliveTeams().add(teams);
            }
        for (Scenario scenario : Scenario.values()) {
            if (scenario.isEnabled())
                this.enabledScenarios.add(scenario);
        }
        World gameWorld = this.worldPopulator.getGameWorld();
        CircleForm circleForm = new CircleForm((this.gameConfig.getBorderStartSize() - 10), 150, gameWorld, gameWorld.getWorldBorder().getCenter());
        if (GameUtils.isSoloMode()) {
            PlayerPlate[] playerPlates = (PlayerPlate[])playerPlatesList.<Object>toArray((Object[])new SoloPlayerPlate[0]);
            TeleportationManager teleportationManager = new TeleportationManager(playerPlates, 2L, (Form)circleForm);
            teleportationManager.teleportAllAndStart(this.api);
        } else {
            TeamPlayerPlate[] arrayOfTeamPlayerPlate = getTeamPlayerPlate(this.teamManager);
            TeleportationManager teleportationManager = new TeleportationManager((PlayerPlate[])arrayOfTeamPlayerPlate, 2L, (Form)circleForm);
            teleportationManager.teleportAllAndStart(this.api);
        }
    }

    private TeamPlayerPlate[] getTeamPlayerPlate(TeamManager teamManager) {
        return (TeamPlayerPlate[])((List)((List)Arrays.<Teams>stream(Teams.values()).limit(100L).filter(teams -> (teamManager.getPlayerAmountInTeam(teams) > 0)).collect(Collectors.toList()))
                .stream().map(team -> {
                    List<UUID> players = new ArrayList<>();
                    teamManager.getPlayersInTeam((Teams)team);
                    return new TeamPlayerPlate(players, ((Teams)team).getColor() + "" + ((Teams)team).getColor() + ((Teams)team).getName());
                }).collect(Collectors.toList())).toArray((Object[])new TeamPlayerPlate[0]);
    }

    public boolean isPreloadFinished() {
        return this.preloadFinished;
    }

    public void setPreloadFinished(boolean preloadFinished) {}

    public boolean isPreload() {
        return this.preload;
    }

    public void setPreload(boolean preload) {
        this.preload = preload;
    }

    public boolean isAnnouncedOnHub() {
        return this.announcedOnHub;
    }

    public CombatLogManager getCombatLogManager() {
        return this.combatLogManager;
    }

    public List<String> getWhitelistedPlayers() {
        return this.whitelistedPlayers;
    }

    public int getGroupe() {
        return this.groupe;
    }

    public void setGroupe(int groupe) {
        this.groupe = groupe;
    }

    public StartGameCountDown getStartGameCountDown() {
        return this.startGameCountDown;
    }

    public GlobalTask getGlobalTask() {
        return this.globalTask;
    }

    public UUID getGameHost() {
        return this.gameHost;
    }

    public void setGameHost(UUID gameHost) {
        this.gameHost = gameHost;
        this.whitelistedPlayers.add(Bukkit.getOfflinePlayer(gameHost).getName());
    }

    public SimpleBorder getBorder() {
        return this.border;
    }

    public UHCFinisherGame getUhcFinisherGame() {
        return this.uhcFinisherGame;
    }

    public UHCStandard getUhcStandard() {
        return this.uhcStandard;
    }

    public WorldPopulator getWorldPopulator() {
        return this.worldPopulator;
    }

    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }

    public TeamManager getTeamManager() {
        return this.teamManager;
    }

    public CycleManager getCycleManager() {
        return this.cycleManager;
    }

    public List<Scenario> getEnabledScenarios() {
        return this.enabledScenarios;
    }

    public List<UUID> getOfflinePlayers() {
        return this.offlinePlayers;
    }

    public List<Teams> getAliveTeams() {
        return this.aliveTeams;
    }

    public List<String> getPlayedDomeiPlayers() {
        return this.playedDomeiPlayers;
    }

    public List<UUID> getPlayedPlayers() {
        return this.playedPlayers;
    }

    public List<UUID> getInGamePlayers() {
        return this.inGamePlayers;
    }

    public GameConfig getGameConfig() {
        return this.gameConfig;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public EpisodeManager getEpisodeManager() {
        return this.episodeManager;
    }

    public List<UUID> getHosts() {
        return this.hosts;
    }

    public List<String> getBanList() {
        return this.banList;
    }

    public List<UUID> getVanishList() {
        return this.vanishList;
    }

    public API getApi() {
        return this.api;
    }

    public boolean hasHostAccess(Player player) {
        return ((this.gameHost != null && this.gameHost.equals(player.getUniqueId())) || player.isOp() || getHosts().contains(player.getUniqueId()));
    }
}

