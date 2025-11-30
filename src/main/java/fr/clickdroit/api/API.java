package fr.clickdroit.api;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;

import fr.clickdroit.api.common.Common;
import fr.clickdroit.api.game.GameManager;
import fr.clickdroit.api.listener.ReconnectListener;
import fr.clickdroit.api.module.Modules;
import fr.clickdroit.api.module.games.UHCModule;
import fr.clickdroit.api.utils.CustomInventory;
import fr.clickdroit.api.utils.HologramCreate;
import fr.clickdroit.api.utils.TabHandler;
import fr.clickdroit.api.utils.UHCConstants;
import fr.clickdroit.api.worlds.BiomeChanger;
import fr.clickdroit.api.worlds.Generator;
import fr.clickdroit.api.worlds.LobbyPopulator;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Classe principale du plugin CLDAPI UHC.
 * <p>
 * Cette classe est le point d'entrée du plugin Minecraft pour les serveurs
 * Spigot/Paper 1.8.8. Elle initialise tous les composants nécessaires au
 * fonctionnement de l'API UHC :
 * <ul>
 *   <li>Gestionnaire de partie ({@link GameManager})</li>
 *   <li>Modules de jeu ({@link Modules})</li>
 *   <li>Systèmes communs ({@link Common})</li>
 *   <li>Génération de monde et lobby</li>
 * </ul>
 * 
 * <p>Exemple d'utilisation :</p>
 * <pre>{@code
 * API api = API.getAPI();
 * GameManager gameManager = api.getGameManager();
 * }</pre>
 * 
 * @author Clickdroit
 * @version 1.0
 * @see GameManager
 * @see Modules
 */
public class API extends JavaPlugin {
    private static API api;

    private GameManager gameManager;

    private Common common;

    private Map<Class<? extends CustomInventory>, CustomInventory> registeredInventories = new HashMap<>();

    private LobbyPopulator lobbyPopulator;

    private Modules modules;

    private Scoreboard scoreboard;

    private TabHandler tabHandler;

    public static API getAPI() {
        return api;
    }

    public void onLoad() {
        BiomeChanger.init();
    }

    public void onEnable() {
        api = this;
        ((World)getServer().getWorlds().get(0)).getPopulators().add(new Generator());
        this.gameManager = new GameManager(this);
        this.lobbyPopulator = new LobbyPopulator(this);
        this.common = new Common(this);
        this.common.load();
        setModules((Modules)new UHCModule(this));
        this.scoreboard = getServer().getScoreboardManager().getMainScoreboard();
        this.tabHandler = new TabHandler();
        for (World world : Bukkit.getWorlds()) {
            world.setDifficulty(Difficulty.NORMAL);
            world.setGameRuleValue("naturalRegeneration", "false");
        }
        Bukkit.getWorld(UHCConstants.LOBBY_WORLD_NAME).setDifficulty(Difficulty.PEACEFUL);
        (new HologramCreate()).create();
        Bukkit.getPluginManager().registerEvents((Listener)new ReconnectListener(this.gameManager), (Plugin)this);
    }

    public void onDisable() {
        this.common.onDisable();
    }

    public Common getCommon() {
        return this.common;
    }

    public Modules getModules() {
        return this.modules;
    }

    public void setModules(Modules modules) {
        (this.modules = modules).onLoad();
    }

    public LobbyPopulator getLobbyPopulator() {
        return this.lobbyPopulator;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public CustomInventory getInventory(Class<? extends CustomInventory> inventoryClass) {
        Preconditions.checkState(this.registeredInventories.containsKey(inventoryClass));
        return this.registeredInventories.get(inventoryClass);
    }

    public CustomInventory getInventory(String inventoryName) {
        return this.registeredInventories.values().stream().filter(inventory -> inventory.getName().equals(inventoryName)).findFirst().orElse(null);
    }

    public Map<Class<? extends CustomInventory>, CustomInventory> getRegisteredInventories() {
        return this.registeredInventories;
    }

    public void openInventory(Player player, Class<? extends CustomInventory> inventoryClass) {
        CustomInventory customInventory = getInventory(inventoryClass);
        Inventory inventory = Bukkit.createInventory(null, customInventory.getSlots(), customInventory.getName());
        inventory.setContents(customInventory.getContents(player).get());
        player.openInventory(inventory);
    }

    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    public TabHandler getTabHandler() {
        return this.tabHandler;
    }
}
