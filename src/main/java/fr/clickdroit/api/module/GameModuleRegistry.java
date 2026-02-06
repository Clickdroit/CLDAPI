package fr.clickdroit.api.module;

import fr.clickdroit.api.API;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Registre des modules de jeu.
 * <p>
 * Cette classe gère l'enregistrement et la récupération des modules de jeu
 * personnalisés. Les plugins externes peuvent enregistrer leurs modules
 * via cette classe pour les intégrer dans l'API UHC.
 * </p>
 * 
 * <h3>Utilisation dans un plugin externe :</h3>
 * <pre>{@code
 * public class MonPlugin extends JavaPlugin {
 *     @Override
 *     public void onEnable() {
 *         // S'assurer que CLDAPI est chargé
 *         Plugin cldapi = Bukkit.getPluginManager().getPlugin("UHCAPI");
 *         if (cldapi != null && cldapi.isEnabled()) {
 *             API api = API.getAPI();
 *             api.getModuleRegistry().registerModule(new MonModule(this));
 *         }
 *     }
 * }
 * }</pre>
 * 
 * @author Clickdroit
 * @version 1.0
 * @see GameModule
 */
public class GameModuleRegistry {
    private final API api;
    private final Map<String, GameModule> registeredModules;
    private final Map<String, Plugin> moduleOwners;
    private final Logger logger;

    /**
     * Crée un nouveau registre de modules.
     * 
     * @param api l'instance de l'API
     */
    public GameModuleRegistry(API api) {
        this.api = api;
        this.registeredModules = new ConcurrentHashMap<>();
        this.moduleOwners = new ConcurrentHashMap<>();
        this.logger = api.getLogger();
    }

    /**
     * Enregistre un nouveau module de jeu.
     * 
     * @param module le module à enregistrer
     * @return true si l'enregistrement a réussi, false si un module avec le même ID existe déjà
     */
    public boolean registerModule(GameModule module) {
        String moduleId = module.getId().toUpperCase();
        
        if (registeredModules.containsKey(moduleId)) {
            logger.warning("Impossible d'enregistrer le module '" + moduleId + 
                    "': un module avec cet ID existe déjà.");
            return false;
        }

        // Vérifier que le plugin propriétaire est valide
        if (module.getOwnerPlugin() == null) {
            logger.warning("Impossible d'enregistrer le module '" + moduleId + 
                    "': plugin propriétaire null.");
            return false;
        }

        registeredModules.put(moduleId, module);
        moduleOwners.put(moduleId, module.getOwnerPlugin());
        
        // Appeler onLoad du module
        try {
            module.onLoad();
            logger.info("Module '" + module.getDisplayName() + "' (" + moduleId + 
                    ") enregistré par " + module.getOwnerPlugin().getName());
        } catch (Exception e) {
            logger.severe("Erreur lors du chargement du module '" + moduleId + "': " + e.getMessage());
            e.printStackTrace();
            registeredModules.remove(moduleId);
            moduleOwners.remove(moduleId);
            return false;
        }

        return true;
    }

    /**
     * Désenregistre un module de jeu.
     * 
     * @param moduleId l'ID du module à désenregistrer
     * @return true si le module a été désenregistré, false s'il n'existait pas
     */
    public boolean unregisterModule(String moduleId) {
        moduleId = moduleId.toUpperCase();
        
        GameModule module = registeredModules.remove(moduleId);
        if (module != null) {
            moduleOwners.remove(moduleId);
            
            // Si le module actif est celui qu'on désenregistre, revenir au module UHC par défaut
            GameModule activeModule = api.getActiveGameModule();
            if (activeModule != null && activeModule.getId().equals(moduleId)) {
                api.getGameManager().getModuleManager().setCurrentModule(ModuleType.UHC);
            }
            
            try {
                module.onDisable(api);
            } catch (Exception e) {
                logger.warning("Erreur lors de la désactivation du module '" + moduleId + "': " + e.getMessage());
            }
            
            logger.info("Module '" + moduleId + "' désenregistré.");
            return true;
        }
        return false;
    }

    /**
     * Désenregistre tous les modules d'un plugin.
     * 
     * @param plugin le plugin dont les modules doivent être désenregistrés
     */
    public void unregisterModules(Plugin plugin) {
        List<String> toRemove = new ArrayList<>();
        
        for (Map.Entry<String, Plugin> entry : moduleOwners.entrySet()) {
            if (entry.getValue().equals(plugin)) {
                toRemove.add(entry.getKey());
            }
        }
        
        for (String moduleId : toRemove) {
            unregisterModule(moduleId);
        }
    }

    /**
     * Récupère un module par son ID.
     * 
     * @param moduleId l'ID du module
     * @return le module, ou null s'il n'existe pas
     */
    public GameModule getModule(String moduleId) {
        return registeredModules.get(moduleId.toUpperCase());
    }

    /**
     * Vérifie si un module est enregistré.
     * 
     * @param moduleId l'ID du module
     * @return true si le module est enregistré
     */
    public boolean isModuleRegistered(String moduleId) {
        return registeredModules.containsKey(moduleId.toUpperCase());
    }

    /**
     * Récupère tous les modules enregistrés.
     * 
     * @return une collection non modifiable des modules enregistrés
     */
    public Collection<GameModule> getRegisteredModules() {
        return Collections.unmodifiableCollection(registeredModules.values());
    }

    /**
     * Récupère tous les IDs des modules enregistrés.
     * 
     * @return un ensemble non modifiable des IDs de modules
     */
    public Set<String> getRegisteredModuleIds() {
        return Collections.unmodifiableSet(registeredModules.keySet());
    }

    /**
     * Récupère le nombre de modules enregistrés.
     * 
     * @return le nombre de modules
     */
    public int getModuleCount() {
        return registeredModules.size();
    }

    /**
     * Récupère le plugin propriétaire d'un module.
     * 
     * @param moduleId l'ID du module
     * @return le plugin propriétaire, ou null si le module n'existe pas
     */
    public Plugin getModuleOwner(String moduleId) {
        return moduleOwners.get(moduleId.toUpperCase());
    }
}
