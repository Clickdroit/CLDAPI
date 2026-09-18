package fr.clickdroit.api.config;

import fr.clickdroit.api.API;
import fr.clickdroit.api.config.common.GameAccess;
import fr.clickdroit.api.config.scenario.Scenario;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

/**
 * Service de persistance pour la configuration de partie UHC.
 * Permet de sauvegarder et charger la configuration de partie dans un fichier YAML.
 * 
 * <p>Exemple d'utilisation :</p>
 * <pre>{@code
 * GameConfigPersistence persistence = new GameConfigPersistence(api);
 * persistence.saveConfig(gameConfig, "ma-config");
 * persistence.loadConfig(gameConfig, "ma-config");
 * }</pre>
 * 
 * @author Clickdroit
 * @version 1.0
 */
public class GameConfigPersistence {
    
    private static final String CONFIG_FOLDER = "game-configs";
    private static final String DEFAULT_CONFIG_NAME = "default";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private final API api;
    private final File configFolder;
    
    /**
     * Crée une nouvelle instance du service de persistance.
     *
     * @param api l'instance de l'API principale
     */
    public GameConfigPersistence(API api) {
        this.api = api;
        this.configFolder = new File(api.getDataFolder(), CONFIG_FOLDER);
        
        // Créer le dossier s'il n'existe pas
        if (!configFolder.exists()) {
            configFolder.mkdirs();
        }
    }
    
    /**
     * Sauvegarde la configuration actuelle dans un fichier YAML.
     *
     * @param config la configuration à sauvegarder
     * @param configName le nom du fichier de configuration (sans extension)
     * @return true si la sauvegarde a réussi, false sinon
     */
    public boolean saveConfig(GameConfig config, String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        FileConfiguration yaml = new YamlConfiguration();
        
        try {
            // Paramètres généraux
            yaml.set("general.slots", config.getGameSlot());
            yaml.set("general.player-per-team", config.getPlayerPerTeam());
            yaml.set("general.day-night-duration", config.getDayNightDuration());
            yaml.set("general.episode-time", config.getEpisodeTime());
            yaml.set("general.disconnect-seconds", config.getDisconnectSeconds());
            
            // Options de jeu
            yaml.set("options.show-all-teams", config.isShowAllTeams());
            yaml.set("options.friendly-fire", config.isFriendlyfire());
            yaml.set("options.spectators", config.isSpectators());
            yaml.set("options.nether", config.isNether());
            yaml.set("options.chat", config.isChat());
            yaml.set("options.game-dev", config.isGameDev());
            yaml.set("options.game-access", config.getGameAccess().name());
            
            // Temps
            yaml.set("timers.pvp-time", config.getPvpTime());
            yaml.set("timers.border-time", config.getBorderTime());
            yaml.set("timers.role-time", config.getRoleTime());
            
            // Bordure
            yaml.set("border.start-size", config.getBorderStartSize());
            yaml.set("border.end-size", config.getBorderEndSize());
            yaml.set("border.speed", config.getBorderBlocksPerSecond());
            
            // Limites
            yaml.set("limits.diamond-max", config.getDiamondMax());
            yaml.set("limits.gold-max", config.getGoldMax());
            yaml.set("limits.enderpearl-damage", config.getEnderpearlDamage());
            
            // Scénarios activés
            List<String> enabledScenarios = new ArrayList<>();
            for (Scenario scenario : Scenario.values()) {
                if (scenario.isEnabled()) {
                    enabledScenarios.add(scenario.name());
                }
            }
            yaml.set("scenarios.enabled", enabledScenarios);
            
            // Métadonnées
            long currentTime = System.currentTimeMillis();
            yaml.set("metadata.saved-at", currentTime);
            yaml.set("metadata.saved-at-readable", DATE_FORMAT.format(new Date(currentTime)));
            yaml.set("metadata.version", api.getDescription().getVersion());
            
            yaml.save(configFile);
            api.getLogger().info("Configuration sauvegardée: " + configName);
            return true;
            
        } catch (IOException e) {
            api.getLogger().log(Level.SEVERE, "Erreur lors de la sauvegarde de la configuration: " + configName, e);
            return false;
        }
    }
    
    /**
     * Charge une configuration depuis un fichier YAML.
     *
     * @param config la configuration à mettre à jour
     * @param configName le nom du fichier de configuration (sans extension)
     * @return true si le chargement a réussi, false sinon
     */
    public boolean loadConfig(GameConfig config, String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        
        if (!configFile.exists()) {
            api.getLogger().warning("Configuration non trouvée: " + configName);
            return false;
        }
        
        try {
            FileConfiguration yaml = YamlConfiguration.loadConfiguration(configFile);
            
            // Paramètres généraux
            if (yaml.contains("general.slots")) {
                config.setGameSlot(yaml.getInt("general.slots"));
            }
            if (yaml.contains("general.player-per-team")) {
                config.setPlayerPerTeam(yaml.getInt("general.player-per-team"));
            }
            if (yaml.contains("general.day-night-duration")) {
                config.setDayNightDuration(yaml.getLong("general.day-night-duration"));
            }
            if (yaml.contains("general.episode-time")) {
                config.setEpisodeTime(yaml.getInt("general.episode-time"));
            }
            if (yaml.contains("general.disconnect-seconds")) {
                config.setDisconnectSeconds(yaml.getInt("general.disconnect-seconds"));
            }
            
            // Options de jeu
            if (yaml.contains("options.show-all-teams")) {
                config.setShowAllTeams(yaml.getBoolean("options.show-all-teams"));
            }
            if (yaml.contains("options.friendly-fire")) {
                config.setFriendlyfire(yaml.getBoolean("options.friendly-fire"));
            }
            if (yaml.contains("options.spectators")) {
                config.setSpectators(yaml.getBoolean("options.spectators"));
            }
            if (yaml.contains("options.nether")) {
                config.setNether(yaml.getBoolean("options.nether"));
            }
            if (yaml.contains("options.chat")) {
                config.setChat(yaml.getBoolean("options.chat"));
            }
            if (yaml.contains("options.game-dev")) {
                config.setGameDev(yaml.getBoolean("options.game-dev"));
            }
            if (yaml.contains("options.game-access")) {
                String gameAccessValue = yaml.getString("options.game-access");
                try {
                    config.setGameAccess(GameAccess.valueOf(gameAccessValue));
                } catch (IllegalArgumentException e) {
                    api.getLogger().warning("Valeur game-access invalide: " + gameAccessValue + ", conservation de la valeur par défaut");
                }
            }
            
            // Temps
            if (yaml.contains("timers.pvp-time")) {
                config.setPvpTime(yaml.getInt("timers.pvp-time"));
            }
            if (yaml.contains("timers.border-time")) {
                config.setBorderTime(yaml.getInt("timers.border-time"));
            }
            if (yaml.contains("timers.role-time")) {
                config.setRoleTime(yaml.getInt("timers.role-time"));
            }
            
            // Bordure
            if (yaml.contains("border.start-size")) {
                config.setBorderStartSize(yaml.getInt("border.start-size"));
            }
            if (yaml.contains("border.end-size")) {
                config.setBorderEndSize(yaml.getInt("border.end-size"));
            }
            if (yaml.contains("border.speed")) {
                config.setBorderBlocksPerSecond(yaml.getInt("border.speed"));
            }
            
            // Limites
            if (yaml.contains("limits.diamond-max")) {
                config.setDiamondMax(yaml.getInt("limits.diamond-max"));
            }
            if (yaml.contains("limits.gold-max")) {
                config.setGoldMax(yaml.getInt("limits.gold-max"));
            }
            if (yaml.contains("limits.enderpearl-damage")) {
                config.setEnderpearlDamage(yaml.getInt("limits.enderpearl-damage"));
            }
            
            // Scénarios
            if (yaml.contains("scenarios.enabled")) {
                List<String> enabledScenarios = yaml.getStringList("scenarios.enabled");
                
                // D'abord, désactiver tous les scénarios qui ne sont pas dans la liste
                for (Scenario scenario : Scenario.values()) {
                    boolean shouldBeEnabled = enabledScenarios.contains(scenario.name());
                    boolean isCurrentlyEnabled = scenario.isEnabled();
                    
                    // Si l'état actuel est différent de l'état souhaité, basculer
                    if (shouldBeEnabled != isCurrentlyEnabled) {
                        scenario.getScenarioManager().activeScenario();
                    }
                }
            }
            
            api.getLogger().info("Configuration chargée: " + configName);
            return true;
            
        } catch (Exception e) {
            api.getLogger().log(Level.SEVERE, "Erreur lors du chargement de la configuration: " + configName, e);
            return false;
        }
    }
    
    /**
     * Sauvegarde la configuration par défaut.
     *
     * @param config la configuration à sauvegarder
     * @return true si la sauvegarde a réussi
     */
    public boolean saveDefaultConfig(GameConfig config) {
        return saveConfig(config, DEFAULT_CONFIG_NAME);
    }
    
    /**
     * Charge la configuration par défaut.
     *
     * @param config la configuration à mettre à jour
     * @return true si le chargement a réussi
     */
    public boolean loadDefaultConfig(GameConfig config) {
        return loadConfig(config, DEFAULT_CONFIG_NAME);
    }
    
    /**
     * Liste tous les fichiers de configuration disponibles.
     *
     * @return une liste des noms de configurations (sans extension)
     */
    public List<String> listConfigs() {
        List<String> configs = new ArrayList<>();
        File[] files = configFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                configs.add(name.substring(0, name.length() - 4)); // Enlever .yml
            }
        }
        
        return configs;
    }
    
    /**
     * Supprime un fichier de configuration.
     *
     * @param configName le nom de la configuration à supprimer
     * @return true si la suppression a réussi
     */
    public boolean deleteConfig(String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        if (configFile.exists()) {
            boolean deleted = configFile.delete();
            if (deleted) {
                api.getLogger().info("Configuration supprimée: " + configName);
            }
            return deleted;
        }
        return false;
    }
    
    /**
     * Vérifie si une configuration existe.
     *
     * @param configName le nom de la configuration
     * @return true si la configuration existe
     */
    public boolean configExists(String configName) {
        File configFile = new File(configFolder, configName + ".yml");
        return configFile.exists();
    }
    
    /**
     * Retourne le dossier de configurations.
     *
     * @return le dossier de configurations
     */
    public File getConfigFolder() {
        return configFolder;
    }
}
