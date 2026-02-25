package fr.clickdroit.api.common;

import fr.clickdroit.api.API;
import fr.clickdroit.api.commands.*;
import fr.clickdroit.api.commands.special.RulesInventory;
import fr.clickdroit.api.commands.special.TPHereCommand;
import fr.clickdroit.api.common.rules.Rules;
import fr.clickdroit.api.common.scoreboard.ScoreboardManager;
import fr.clickdroit.api.config.AdminPanelGUI;
import fr.clickdroit.api.config.ConfigMainGUI;
import fr.clickdroit.api.config.ConfigOptionsGUI;
import fr.clickdroit.api.config.GameModeSelectionGUI;
import fr.clickdroit.api.config.borderValue.BorderEndSizeGUI;
import fr.clickdroit.api.config.borderValue.BorderManagerGUI;
import fr.clickdroit.api.config.borderValue.BorderSpeedGUI;
import fr.clickdroit.api.config.borderValue.BorderStartSizeGUI;
import fr.clickdroit.api.config.common.CycleManagerGUI;
import fr.clickdroit.api.config.common.rules.ForbiddenItemsGUI;
import fr.clickdroit.api.config.common.rules.GameRulesManagerGUI;
import fr.clickdroit.api.listener.GameAccessListener;
import fr.clickdroit.api.config.common.DefaultDeathInvGUI;
import fr.clickdroit.api.config.common.DefaultInvGUI;
import fr.clickdroit.api.config.common.ores.DiamondMaxGUI;
import fr.clickdroit.api.config.common.potion.PotionManagerGUI;
import fr.clickdroit.api.config.common.rules.DropItemRateGUI;
import fr.clickdroit.api.config.common.rules.GeneralRulesGUI;
import fr.clickdroit.api.config.intValue.DeconnexionTimeGUI;
import fr.clickdroit.api.config.intValue.SlotsGUI;
import fr.clickdroit.api.config.scenario.ScenarioTimeGUI;
import fr.clickdroit.api.config.scenario.ScenariosGUI;
import fr.clickdroit.api.config.teamvalue.JoinTeamsGUI;
import fr.clickdroit.api.config.teamvalue.TeamManagerGUI;
import fr.clickdroit.api.config.timevalue.BorderTimeGUI;
import fr.clickdroit.api.config.timevalue.EpisodeTimeGUI;
import fr.clickdroit.api.config.timevalue.PvPTimeGUI;
import fr.clickdroit.api.game.GameListener;
import fr.clickdroit.api.game.listeners.BlockListener;
import fr.clickdroit.api.game.listeners.InventoryListener;
import fr.clickdroit.api.game.listeners.PlayerEnvironmentListener;
import fr.clickdroit.api.game.listeners.PvPListener;
import fr.clickdroit.api.game.listeners.WorldListener;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.game.team.Teams;
import fr.clickdroit.api.listener.*;
import fr.clickdroit.api.listener.world.ChunkUnloadListener;
import fr.clickdroit.api.module.standard.listener.UHCStandardListener;
import fr.clickdroit.api.utils.InventoryAPI;
import fr.clickdroit.api.utils.InventoryConvetor;
import fr.clickdroit.api.utils.nms.NMSPatcher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Common {
    private final API main;

    private final GameManager gameManager;

    private final ScoreboardManager scoreboardManager;

    private final Scoreboard scoreboard;

    private JoinTeamsGUI joinTeamsGUI;

    private ScenariosGUI scenariosGUI;

    private EnchantCommand enchantCommand;

    private ScenarioCommand scenarioCommand;

    private InvCommand invCommand;

    public Common(API main) {
        this.main = main;
        this.gameManager = main.getGameManager();
        this.scoreboardManager = new ScoreboardManager(main);
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        new NMSPatcher(main);
    }

    public void load() {
        Rules.load(this.main);
        this.scoreboardManager.load();
        this.scenariosGUI = new ScenariosGUI(this.main);
        this.joinTeamsGUI = new JoinTeamsGUI(this.main);
        this.enchantCommand = new EnchantCommand(this.gameManager);
        this.scenarioCommand = new ScenarioCommand();
        this.invCommand = new InvCommand(this.gameManager);
        registerCommands();
        registerListeners();
        registerInventories();
        registerNameTag();
        this.gameManager.getWorldPopulator().getGameWorld().setGameRuleValue("doFireTick", "false");
        InventoryAPI.items = InventoryConvetor.inventoryFromBase64(
                "rO0ABXcEAAAAKHBwcHBwcHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmplY3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAJ0AAI9PXQABHR5cGV1cQB+AAYAAAACdAAeb3JnLmJ1a2tpdC5pbnZlbnRvcnkuSXRlbVN0YWNrdAAMV0FURVJfQlVDS0VUc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAl0AAZhbW91bnR1cQB+AAYAAAADcQB+AAt0AAtDT09LRURfQkVFRnNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAABAc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AEHVxAH4ABgAAAANxAH4AC3QABEJPT0tzcQB+ABMAAAAHcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==");
        InventoryAPI.inventoryContents = "rO0ABXcEAAAAKHBwcHBwcHNyABpvcmcuYnVra2l0LnV0aWwuaW8uV3JhcHBlcvJQR+zxEm8FAgABTAADbWFwdAAPTGphdmEvdXRpbC9NYXA7eHBzcgA1Y29tLmdvb2dsZS5jb21tb24uY29sbGVjdC5JbW11dGFibGVNYXAkU2VyaWFsaXplZEZvcm0AAAAAAAAAAAIAAlsABGtleXN0ABNbTGphdmEvbGFuZy9PYmplY3Q7WwAGdmFsdWVzcQB+AAR4cHVyABNbTGphdmEubGFuZy5PYmplY3Q7kM5YnxBzKWwCAAB4cAAAAAJ0AAI9PXQABHR5cGV1cQB+AAYAAAACdAAeb3JnLmJ1a2tpdC5pbnZlbnRvcnkuSXRlbVN0YWNrdAAMV0FURVJfQlVDS0VUc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAl0AAZhbW91bnR1cQB+AAYAAAADcQB+AAt0AAtDT09LRURfQkVFRnNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAABAc3EAfgAAc3EAfgADdXEAfgAGAAAAA3EAfgAIcQB+AAlxAH4AEHVxAH4ABgAAAANxAH4AC3QABEJPT0tzcQB+ABMAAAAHcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcHBwcA==";
    }

    private void registerCommands() {
        this.main.getCommand("host").setExecutor((CommandExecutor) new HostCommand(this.gameManager));
        this.main.getCommand("finish").setExecutor((CommandExecutor) new FinishCommand(this.gameManager));
        this.main.getCommand("enchant").setExecutor((CommandExecutor) this.enchantCommand);
        this.main.getCommand("scenario").setExecutor((CommandExecutor) this.scenarioCommand);
        this.main.getCommand("helpop").setExecutor((CommandExecutor) new HelpopCommand());
        this.main.getCommand("revive").setExecutor((CommandExecutor) new ReviveCommand(this.main));
        this.main.getCommand("inv").setExecutor((CommandExecutor) this.invCommand);
        this.main.getCommand("view").setExecutor((CommandExecutor) new ViewCommand(this.gameManager));
        this.main.getCommand("rules").setExecutor((CommandExecutor) new RulesCommand(this.main));
        this.main.getCommand("tc").setExecutor((CommandExecutor) new TCCommand(this.gameManager));
        this.main.getCommand("vote").setExecutor((CommandExecutor) new VoteCommand(this.gameManager));
        this.main.getCommand("preload").setExecutor((CommandExecutor) new PreLoadCommand(this.gameManager));
        this.main.getCommand("tprandom").setExecutor((CommandExecutor) new TPRandomCommand());
        this.main.getCommand("whitelist").setExecutor((CommandExecutor) new WhitelistCommand(this.gameManager));
        this.main.getCommand("alerts").setExecutor((CommandExecutor) new AlertsCommand());
        this.main.getCommand("tphere").setExecutor((CommandExecutor) new TPHereCommand());
        this.main.getCommand("health").setExecutor((CommandExecutor) new HealthCommand(this.gameManager));
        this.main.getCommand("viewoffline").setExecutor((CommandExecutor) new ViewOfflineCommand(this.gameManager));
        this.main.getCommand("disperse").setExecutor((CommandExecutor) new DisperseCommand(this.gameManager));
        this.main.getCommand("groupe").setExecutor((CommandExecutor) new GroupeCommand(this.gameManager));
        this.main.getCommand("near").setExecutor((CommandExecutor) new NearCommand(this.gameManager));
        this.main.getCommand("vanish").setExecutor((CommandExecutor) new VanishCommand(this.gameManager));

        // Nouvelles commandes
        GameConfigCommand gameConfigCommand = new GameConfigCommand(this.main);
        this.main.getCommand("gameconfig").setExecutor((CommandExecutor) gameConfigCommand);
        this.main.getCommand("gameconfig").setTabCompleter(gameConfigCommand);

        SpectateCommand spectateCommand = new SpectateCommand(this.main);
        this.main.getCommand("spectate").setExecutor((CommandExecutor) spectateCommand);
        this.main.getCommand("spectate").setTabCompleter(spectateCommand);

        HealCommand healCommand = new HealCommand(this.main);
        this.main.getCommand("heal").setExecutor((CommandExecutor) healCommand);
        this.main.getCommand("heal").setTabCompleter(healCommand);
    }

    private void registerListeners() {
        PluginManager pluginManager = this.main.getServer().getPluginManager();
        pluginManager.registerEvents((Listener) new GameAccessListener(this.gameManager), this.main);
        pluginManager.registerEvents((Listener) new PlayerJoinListener(this.main), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new CommonListener(this), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new GameListener(this.gameManager), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new BlockListener(this.gameManager), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new InventoryListener(this.gameManager), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new PlayerEnvironmentListener(this.gameManager), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new PvPListener(this.gameManager), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new WorldListener(this.gameManager), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new PlayerChatListener(this.gameManager), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new PlayerInteractListener(this.gameManager), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new UHCStandardListener(this.gameManager.getUhcStandard()),
                (Plugin) this.main);
        pluginManager.registerEvents((Listener) this.joinTeamsGUI, (Plugin) this.main);
        pluginManager.registerEvents((Listener) this.scenariosGUI, (Plugin) this.main);
        pluginManager.registerEvents((Listener) new ScenarioTimeGUI(), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new ChunkUnloadListener(), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new PlayerBrewItemListener(), (Plugin) this.main);
        pluginManager.registerEvents((Listener) new PlayerEnchantListener(), (Plugin) this.main);
    }

    private void registerInventories() {
        this.main.getRegisteredInventories().put(ConfigMainGUI.class, new ConfigMainGUI(this.main));
        this.main.getRegisteredInventories().put(TeamManagerGUI.class, new TeamManagerGUI(this.main));
        this.main.getRegisteredInventories().put(ConfigOptionsGUI.class, new ConfigOptionsGUI(this.gameManager));
        this.main.getRegisteredInventories().put(SlotsGUI.class, new SlotsGUI(this.gameManager));
        this.main.getRegisteredInventories().put(BorderStartSizeGUI.class, new BorderStartSizeGUI(this.gameManager));
        this.main.getRegisteredInventories().put(BorderEndSizeGUI.class, new BorderEndSizeGUI(this.gameManager));
        this.main.getRegisteredInventories().put(BorderManagerGUI.class, new BorderManagerGUI(this.gameManager));
        this.main.getRegisteredInventories().put(BorderSpeedGUI.class, new BorderSpeedGUI(this.gameManager));
        this.main.getRegisteredInventories().put(BorderTimeGUI.class, new BorderTimeGUI(this.gameManager));
        this.main.getRegisteredInventories().put(PvPTimeGUI.class, new PvPTimeGUI(this.gameManager));
        this.main.getRegisteredInventories().put(EpisodeTimeGUI.class, new EpisodeTimeGUI(this.gameManager));
        this.main.getRegisteredInventories().put(DefaultInvGUI.class, new DefaultInvGUI());
        this.main.getRegisteredInventories().put(DefaultDeathInvGUI.class, new DefaultDeathInvGUI());
        this.main.getRegisteredInventories().put(CycleManagerGUI.class, new CycleManagerGUI(this.gameManager));
        this.main.getRegisteredInventories().put(EnchantCommand.class, this.enchantCommand);
        this.main.getRegisteredInventories().put(this.scenarioCommand.getClass(), this.scenarioCommand);
        this.main.getRegisteredInventories().put(GeneralRulesGUI.class, new GeneralRulesGUI(this.gameManager));
        this.main.getRegisteredInventories().put(DropItemRateGUI.class, new DropItemRateGUI(this.gameManager));
        this.main.getRegisteredInventories().put(DiamondMaxGUI.class, new DiamondMaxGUI(this.gameManager));
        this.main.getRegisteredInventories().put(InvCommand.class, this.invCommand);
        this.main.getRegisteredInventories().put(RulesInventory.class, new RulesInventory(this.gameManager));
        this.main.getRegisteredInventories().put(PotionManagerGUI.class, new PotionManagerGUI(this.gameManager));
        this.main.getRegisteredInventories().put(AdminPanelGUI.class, new AdminPanelGUI());
        this.main.getRegisteredInventories().put(GameRulesManagerGUI.class, new GameRulesManagerGUI(this.gameManager));
        this.main.getRegisteredInventories().put(ForbiddenItemsGUI.class, new ForbiddenItemsGUI(this.gameManager));
        this.main.getRegisteredInventories().put(DeconnexionTimeGUI.class, new DeconnexionTimeGUI(this.gameManager));
        this.main.getRegisteredInventories().put(GameModeSelectionGUI.class, new GameModeSelectionGUI());
    }

    private void registerNameTag() {
        for (Teams teams : Teams.values()) {
            if (this.scoreboard.getTeam(teams.getName()) != null)
                this.scoreboard.getTeam(teams.getName()).unregister();
            Team t = this.scoreboard.registerNewTeam(teams.getName());
            t.setPrefix(teams.getColor() + teams.getName() + " ");
            t.setCanSeeFriendlyInvisibles(false);
        }
    }

    public void onDisable() {
        this.scoreboardManager.onDisable();
    }

    public ScenariosGUI getScenariosGUI() {
        return this.scenariosGUI;
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public JoinTeamsGUI getJoinTeamsGUI() {
        return this.joinTeamsGUI;
    }

    public API getMain() {
        return this.main;
    }

    public ScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }
}
