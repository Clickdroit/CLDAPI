package fr.clickdroit.api.module.external;

import fr.clickdroit.api.API;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Registry thread-safe pour les modules de jeu externes.
 * Permet l'enregistrement et la récupération des modules depuis des plugins
 * tiers.
 * 
 * <p>
 * Exemple d'utilisation :
 * </p>
 * 
 * <pre>{@code
 * API api = API.getAPI();
 * api.getModuleRegistry().registerModule(new MyGameModule(this));
 * }</pre>
 * 
 * @author Clickdroit
 * @version 1.0
 */
public class GameModuleRegistry {

    private final ConcurrentHashMap<String, GameModule> modules;
    private final Logger logger;

    public GameModuleRegistry() {
        this.modules = new ConcurrentHashMap<>();
        this.logger = API.getAPI().getLogger();
    }

    /**
     * Enregistre un module de jeu externe.
     * 
     * @param module le module à enregistrer
     * @throws IllegalArgumentException si un module avec le même ID existe déjà
     */
    public void registerModule(GameModule module) {
        if (module == null) {
            throw new IllegalArgumentException("Le module ne peut pas être null");
        }

        String id = module.getId();
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("L'ID du module ne peut pas être null ou vide");
        }

        GameModule existing = modules.putIfAbsent(id.toUpperCase(), module);
        if (existing != null) {
            throw new IllegalArgumentException("Un module avec l'ID '" + id + "' est déjà enregistré");
        }

        logger.info("§aModule externe enregistré: §e" + module.getName() + " §7(ID: " + id + ")");

        // Appeler onLoad après l'enregistrement
        module.onLoad();
    }

    /**
     * Désenregistre un module par son ID.
     * 
     * @param id l'ID du module à désenregistrer
     * @return le module désenregistré, ou null s'il n'existait pas
     */
    public GameModule unregisterModule(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        GameModule removed = modules.remove(id.toUpperCase());
        if (removed != null) {
            logger.info("§cModule externe désenregistré: §e" + removed.getName());
        }
        return removed;
    }

    /**
     * Récupère un module par son ID.
     * 
     * @param id l'ID du module
     * @return le module, ou null s'il n'existe pas
     */
    public GameModule getModule(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return modules.get(id.toUpperCase());
    }

    /**
     * Vérifie si un module est enregistré.
     * 
     * @param id l'ID du module
     * @return true si le module est enregistré
     */
    public boolean isRegistered(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        return modules.containsKey(id.toUpperCase());
    }

    /**
     * Récupère tous les modules enregistrés.
     * 
     * @return une collection non-modifiable de tous les modules
     */
    public Collection<GameModule> getAllModules() {
        return Collections.unmodifiableCollection(modules.values());
    }

    /**
     * Récupère le nombre de modules enregistrés.
     * 
     * @return le nombre de modules
     */
    public int getModuleCount() {
        return modules.size();
    }

    /**
     * Vide le registry (utilisé lors du rechargement).
     */
    public void clear() {
        modules.clear();
        logger.info("§cRegistry des modules externes vidé");
    }
}
