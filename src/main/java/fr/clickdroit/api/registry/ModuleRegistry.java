package fr.clickdroit.api.registry;

import fr.clickdroit.api.API;
import fr.clickdroit.api.exception.ModuleNotFoundException;
import fr.clickdroit.api.module.GameModule;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Registre unifié des modules de jeu.
 */
public class ModuleRegistry {

    private final API api;
    private final Logger logger;
    private final Map<String, GameModule> modules = new ConcurrentHashMap<>();
    private final Map<String, Plugin> moduleOwners = new ConcurrentHashMap<>();
    private GameModule activeModule;

    public ModuleRegistry(API api) {
        this.api = api;
        this.logger = api.getLogger();
    }

    public boolean register(GameModule module) {
        String id = module.getId().toUpperCase();
        if (modules.containsKey(id)) return false;

        modules.put(id, module);
        if (module.getOwnerPlugin() != null) {
            moduleOwners.put(id, module.getOwnerPlugin());
        }

        try {
            module.onLoad();
            logger.info("Module enregistré: " + module.getDisplayName());
            return true;
        } catch (Exception e) {
            modules.remove(id);
            return false;
        }
    }

    public boolean unregister(String moduleId) {
        moduleId = moduleId.toUpperCase();
        GameModule module = modules.remove(moduleId);
        if (module != null) {
            moduleOwners.remove(moduleId);
            if (activeModule != null && activeModule.getId().equalsIgnoreCase(moduleId)) {
                deactivate();
            }
            return true;
        }
        return false;
    }

    public boolean activate(String moduleId) {
        GameModule module = getModule(moduleId);
        if (activeModule != null) deactivate();
        activeModule = module;
        module.onEnable(api);
        return true;
    }

    public void deactivate() {
        if (activeModule != null) {
            activeModule.onDisable(api);
            activeModule = null;
        }
    }

    public GameModule getModule(String moduleId) {
        GameModule module = modules.get(moduleId.toUpperCase());
        if (module == null) throw new ModuleNotFoundException(moduleId);
        return module;
    }

    public GameModule getModuleOrNull(String moduleId) {
        return modules.get(moduleId.toUpperCase());
    }

    public boolean isRegistered(String moduleId) {
        return modules.containsKey(moduleId.toUpperCase());
    }

    public GameModule getActiveModule() {
        return activeModule;
    }

    public Collection<GameModule> getAllModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    public int getModuleCount() {
        return modules.size();
    }
}

