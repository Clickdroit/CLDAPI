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
import fr.clickdroit.api.utils.UHCConstants;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Gestionnaire principal de la partie UHC.
 * <p>
 * Cette classe centralise la gestion de tous les aspects d'une partie UHC :
 * <ul>
 * <li>État de la partie (attente, démarrage, jeu, fin)</li>
 * <li>Gestion des joueurs en jeu et hors ligne</li>
 * <li>Configuration de la partie</li>
 * <li>Gestion des équipes</li>
 * <li>Bordure et téléportation</li>
 * <li>Scénarios activés</li>
 * </ul>
 * 
 * @author Clickdroit
 * @version 1.0
 * @see GameState
 * @see GameConfig
 * @see TeamManager
 */
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

    // Collections thread-safe pour éviter les problèmes de concurrence
    private final List<UUID> inGamePlayers = new CopyOnWriteArrayList<>();
    private final List<Teams> aliveTeams = new CopyOnWriteArrayList<>();
    private final List<UUID> offlinePlayers = new CopyOnWriteArrayList<>();
    private final List<Scenario> enabledScenarios = new CopyOnWriteArrayList<>();
    private final List<UUID> playedPlayers = new CopyOnWriteArrayList<>();
    private final List<String> playedDomeiPlayers = new CopyOnWriteArrayList<>();
    private final List<String> whitelistedPlayers = new CopyOnWriteArrayList<>();
    private final List<UUID> hosts = new CopyOnWriteArrayList<>();
    private final List<String> banList = new CopyOnWriteArrayList<>();
    private final List<UUID> vanishList = new CopyOnWriteArrayList<>();

    // Cache pour les opérations fréquentes
    private final Map<UUID, GamePlayer> gamePlayerCache = new ConcurrentHashMap<>();
    private final Set<UUID> hostAccessCache = ConcurrentHashMap.newKeySet();
    private long hostCacheLastUpdate = 0;

    // Messages pré-compilés pour éviter les concaténations répétées
    private static final String[] START_MESSAGES = {
            "       §f(§a!§f) §fBien le bonjour §f(§a!§f) ",
            "",
            " §8> §fVoici certaines §crègles§f à respecter.",
            " §8| §fLe respect des §cgroupes§f.",
            " §8| §fNe pas §cSoundBoard.§f ",
            " §8| §fNe pas §ctuer§f sans raison.",
            " §8| §fNe pas §cdévoiler§f son rôle",
            "§4",
            " §8 > §fVoici les §ccommandes§f à connaitre.",
            " §8| §f/§cdoc §8• §fPermet de voir le §cdocument explicatif§f du mode.",
            " §8| §f/mumble §8• §fPermet de voir le mumble de la §c§1game ",
            " §8| §f/§crules §8• §fPermet de voir les §crègles§f.",
            " §8| §f/helpop §8• §fPermet de §cdemander§f de l'aide.",
            "§4",
            " §8 > §f Bonne §achance§f à tous !",
            "§4"
    };

    private volatile GameState gameState;
    private final boolean announcedOnHub;
    private volatile boolean preload;
    private volatile boolean preloadFinished;
    private volatile int groupe;
    private BeforeStartTask beforeStartTask;
    private GlobalTask globalTask;
    private StartGameCountDown startGameCountDown;
    private volatile UUID gameHost;

    public GameManager(API api) {
        this.api = api;
        this.moduleManager = new ModuleManager(this);
        this.gameState = GameState.WAITING;
        this.gameConfig = new GameConfig(this);
        this.episodeManager = new EpisodeManager(this);
        this.teamManager = new TeamManager(this);
        this.worldPopulator = new WorldPopulator(this);
        this.uhcStandard = new UHCStandard(this);
        this.uhcFinisherGame = new UHCFinisherGame(this.api);
        this.border = new SimpleBorder(this.worldPopulator.getGameWorld().getWorldBorder());
        this.cycleManager = new CycleManager(api, this.worldPopulator.getGameWorld());
        this.combatLogManager = new CombatLogManager(this);
        this.groupe = UHCConstants.DEFAULT_GROUPES;
        this.beforeStartTask = new BeforeStartTask(this);
        this.beforeStartTask.runTaskTimer((Plugin) api, 60L, 40L);
        this.announcedOnHub = false;
        this.preload = false;
        this.preloadFinished = false;

        // Activation optimisée des scénarios par défaut
        activateDefaultScenarios();
    }

    /**
     * Active les scénarios par défaut de manière optimisée
     */
    private void activateDefaultScenarios() {
        Scenario[] defaultScenarios = {
                Scenario.TIMBER, Scenario.CUTCLEAN, Scenario.HASTEYBOYS,
                Scenario.SAFEMINER, Scenario.CAT_EYES, Scenario.BETAZOMBIE
        };

        for (Scenario scenario : defaultScenarios) {
            scenario.getScenarioManager().activeScenario();
        }
    }

    public void startWithTimer() {
        setGameState(GameState.STARTING);
        this.startGameCountDown = new StartGameCountDown(this);
        this.startGameCountDown.runTaskTimer((Plugin) this.api, 0L, 20L);
    }

    public void startGame() {
        setGameState(GameState.PLAYING);
        this.api.getModules().onStart(this.api);
        this.globalTask = new GlobalTask(this);
        this.globalTask.runTaskTimer((Plugin) this.api, 0L, 20L);
        this.cycleManager.startDayCycle(this.gameConfig.getDayNightDuration());

        // Diffusion optimisée des messages
        broadcastStartMessages();

        // Traitement optimisé des joueurs
        processGameStartForPlayers();

        // Activation des scénarios de manière optimisée
        activateEnabledScenarios();

        this.worldPopulator.getGameWorld().setGameRuleValue("randomTickSpeed", "3");

        // Gestion optimisée des spectateurs OP
        processSpectatorOPs();
    }

    /**
     * Diffuse les messages de début de manière optimisée
     */
    private void broadcastStartMessages() {
        for (String message : START_MESSAGES) {
            Bukkit.broadcastMessage(message);
        }
    }

    /**
     * Traite le début de jeu pour tous les joueurs de manière optimisée
     */
    private void processGameStartForPlayers() {
        List<UUID> inGamePlayersCopy = new ArrayList<>(getInGamePlayers());

        for (UUID uuid : inGamePlayersCopy) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null)
                continue;

            // Réinitialisation optimisée du joueur
            resetPlayer(player);
        }
    }

    /**
     * Réinitialise un joueur de manière optimisée
     */
    private void resetPlayer(Player player) {
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

    /**
     * Active les scénarios activés de manière optimisée
     */
    private void activateEnabledScenarios() {
        Arrays.stream(Scenario.values())
                .filter(Scenario::isEnabled)
                .map(Scenario::getScenarioManager)
                .forEach(ScenarioManager::onStart);
    }

    /**
     * Traite les OPs spectateurs de manière optimisée
     */
    private void processSpectatorOPs() {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> !this.inGamePlayers.contains(player.getUniqueId()) && player.isOp())
                .map(OfflinePlayer::getUniqueId)
                .map(this::getCachedGamePlayer)
                .filter(Objects::nonNull)
                .forEach(gamePlayer -> gamePlayer.setAlerts(true));
    }

    public void tryStartGame() {
        if (!this.gameState.equals(GameState.STARTING))
            return;

        System.out.println("[UHC] Starting game..");
        setGameState(GameState.TELEPORTATION);

        // Nettoyage optimisé des listes
        clearGameLists();

        // Initialisation de la bordure
        this.border.init((this.gameConfig.getBorderStartSize() * 2), 0, 0);

        // Collecte optimisée des joueurs
        List<SoloPlayerPlate> playerPlatesList = collectActivePlayers();

        // Traitement optimisé des joueurs en jeu
        processInGamePlayers();

        // Gestion des équipes si nécessaire
        if (this.gameConfig.getPlayerPerTeam() > 1) {
            processTeams();
        }

        // Collecte des scénarios activés
        collectEnabledScenarios();

        // Téléportation optimisée
        handleTeleportation(playerPlatesList);
    }

    /**
     * Nettoie les listes de jeu de manière optimisée
     */
    private void clearGameLists() {
        this.inGamePlayers.clear();
        this.playedPlayers.clear();
        this.offlinePlayers.clear();
        this.aliveTeams.clear();
        this.enabledScenarios.clear();
    }

    /**
     * Collecte les joueurs actifs de manière optimisée
     */
    private List<SoloPlayerPlate> collectActivePlayers() {
        List<SoloPlayerPlate> playerPlatesList = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.getGameMode().equals(GameMode.SPECTATOR)) {
                UUID uuid = player.getUniqueId();
                this.inGamePlayers.add(uuid);
                this.playedPlayers.add(uuid);
                this.playedDomeiPlayers.add(uuid.toString());
                playerPlatesList.add(new SoloPlayerPlate(player));
            }
        }

        return playerPlatesList;
    }

    /**
     * Traite les joueurs en jeu de manière optimisée
     */
    private void processInGamePlayers() {
        Iterator<UUID> iterator = this.inGamePlayers.iterator();

        while (iterator.hasNext()) {
            UUID uuid = iterator.next();
            Player player = Bukkit.getPlayer(uuid);
            GamePlayer gamePlayer = getCachedGamePlayer(uuid);

            if (player == null || gamePlayer == null) {
                iterator.remove();
                continue;
            }

            if (this.gameConfig.getPlayerPerTeam() > 1 && gamePlayer.getTeams() == null) {
                assignPlayerToTeam(player);
            }

            GameUtils.startPlayer(player, GameMode.ADVENTURE);
        }
    }

    /**
     * Assigne un joueur à une équipe disponible
     */
    private void assignPlayerToTeam(Player player) {
        for (Teams team : Teams.values()) {
            if (this.teamManager.getPlayerAmountInTeam(team) < this.gameConfig.getPlayerPerTeam()) {
                this.teamManager.addPlayerToTeam(player, team);
                break;
            }
        }
    }

    /**
     * Traite les équipes de manière optimisée
     */
    private void processTeams() {
        for (Teams team : Teams.values()) {
            if (this.teamManager.getPlayerAmountInTeam(team) >= 1) {
                getAliveTeams().add(team);
            }
        }
    }

    /**
     * Collecte les scénarios activés
     */
    private void collectEnabledScenarios() {
        for (Scenario scenario : Scenario.values()) {
            if (scenario.isEnabled()) {
                this.enabledScenarios.add(scenario);
            }
        }
    }

    /**
     * Gère la téléportation de manière optimisée
     */
    private void handleTeleportation(List<SoloPlayerPlate> playerPlatesList) {
        World gameWorld = this.worldPopulator.getGameWorld();
        CircleForm circleForm = new CircleForm(
                (this.gameConfig.getBorderStartSize() - 10),
                150,
                gameWorld,
                gameWorld.getWorldBorder().getCenter());

        TeleportationManager teleportationManager;

        if (GameUtils.isSoloMode()) {
            PlayerPlate[] playerPlates = playerPlatesList.toArray(new SoloPlayerPlate[0]);
            teleportationManager = new TeleportationManager(playerPlates, 2L, circleForm);
        } else {
            TeamPlayerPlate[] teamPlates = getTeamPlayerPlateOptimized(this.teamManager);
            teleportationManager = new TeleportationManager(teamPlates, 2L, circleForm);
        }

        teleportationManager.teleportAllAndStart(this.api);
    }

    /**
     * Version optimisée de getTeamPlayerPlate
     */
    private TeamPlayerPlate[] getTeamPlayerPlateOptimized(TeamManager teamManager) {
        return Arrays.stream(Teams.values())
                .limit(100)
                .filter(team -> teamManager.getPlayerAmountInTeam(team) > 0)
                .map(team -> {
                    List<UUID> players = teamManager.getPlayersInTeam(team).stream()
                            .map(Player::getUniqueId)
                            .collect(Collectors.toList());
                    return new TeamPlayerPlate(players, team.getColor() + "équipe " + team.getColor() + team.getName());
                })
                .toArray(TeamPlayerPlate[]::new);
    }

    /**
     * Obtient un GamePlayer avec mise en cache
     */
    private GamePlayer getCachedGamePlayer(UUID uuid) {
        return gamePlayerCache.computeIfAbsent(uuid, GamePlayer::getPlayer);
    }

    /**
     * Invalide le cache d'un joueur
     */
    public void invalidateGamePlayerCache(UUID uuid) {
        gamePlayerCache.remove(uuid);
    }

    /**
     * Nettoie le cache des GamePlayer
     */
    public void cleanGamePlayerCache() {
        Set<UUID> onlinePlayerIds = Bukkit.getOnlinePlayers().stream()
                .map(Player::getUniqueId)
                .collect(Collectors.toSet());

        gamePlayerCache.entrySet().removeIf(entry -> !onlinePlayerIds.contains(entry.getKey()));
    }

    public boolean isPreloadFinished() {
        return this.preloadFinished;
    }

    public void setPreloadFinished(boolean preloadFinished) {
        this.preloadFinished = preloadFinished;

        if (preloadFinished && !this.preload) {
            getLogger().info("Map pregeneration completed successfully!");

            // Notification optimisée aux hosts
            notifyHostsPreloadComplete();

            this.preload = false;
        }
    }

    /**
     * Notifie les hosts de manière optimisée
     */
    private void notifyHostsPreloadComplete() {
        String[] messages = {
                "",
                "§a§l✓ PRÉGÉNÉRATION TERMINÉE",
                "§fLa map est maintenant prête pour la partie !",
                "§fVous pouvez configurer les scénarios et lancer la partie.",
                ""
        };

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (hasHostAccess(player)) {
                for (String message : messages) {
                    player.sendMessage(message);
                }
            }
        }
    }

    private Logger getLogger() {
        return this.api.getLogger();
    }

    public boolean isPreloadRequired() {
        return !isPreloadFinished();
    }

    public boolean isPreload() {
        return this.preload;
    }

    public String getPreloadStatus() {
        if (isPreloadFinished()) {
            return "§aTerminée";
        } else if (isPreload()) {
            return "§eEn cours...";
        } else {
            return "§cNon démarrée";
        }
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
        // Invalider le cache des hosts
        hostCacheLastUpdate = 0;
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

    /**
     * Version optimisée de hasHostAccess avec cache
     */
    public boolean hasHostAccess(Player player) {
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Vérifier le cache
        if ((currentTime - hostCacheLastUpdate) < UHCConstants.HOST_CACHE_DURATION_MS) {
            if (hostAccessCache.contains(playerId)) {
                return true;
            }
        } else {
            // Rafraîchir le cache
            hostAccessCache.clear();
            hostCacheLastUpdate = currentTime;
        }

        // Vérifier les permissions
        boolean hasAccess = (this.gameHost != null && this.gameHost.equals(playerId)) ||
                player.isOp() ||
                getHosts().contains(playerId);

        if (hasAccess) {
            hostAccessCache.add(playerId);
        }

        return hasAccess;
    }

    public void broadcast(String message) {
        Bukkit.broadcastMessage(message);
    }

    public void broadcastToAlivePlayers(String message) {
        for (UUID uuid : this.inGamePlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) {
                GamePlayer gamePlayer = getCachedGamePlayer(uuid);
                if (gamePlayer != null && gamePlayer.isAlive()) {
                    player.sendMessage(message);
                }
            }
        }
    }

    public void broadcastToAll(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public void broadcastToSpectators(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            GamePlayer gamePlayer = getCachedGamePlayer(player.getUniqueId());
            if (gamePlayer != null && !gamePlayer.isAlive()) {
                player.sendMessage(message);
            }
        }
    }

    public void broadcastWithPrefix(String message) {
        broadcast("§8[§6UHC§8] §f" + message);
    }
}