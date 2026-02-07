package fr.clickdroit.api.service;

import fr.clickdroit.api.API;

/**
 * Interface de base pour tous les services de l'API UHC.
 * <p>
 * Les services sont des composants réutilisables qui encapsulent
 * une logique métier spécifique. Ils sont gérés par le {@link ServiceManager}
 * et peuvent être injectés dans d'autres composants.
 * </p>
 *
 * <h3>Cycle de vie d'un service :</h3>
 * <ol>
 *   <li>{@link #initialize(API)} - Appelé au démarrage du plugin</li>
 *   <li>Service actif et utilisable</li>
 *   <li>{@link #shutdown()} - Appelé à l'arrêt du plugin</li>
 * </ol>
 *
 * <h3>Exemple d'implémentation :</h3>
 * <pre>{@code
 * public class PlayerService implements GameService {
 *     private API api;
 *     private boolean initialized = false;
 *
 *     @Override
 *     public void initialize(API api) {
 *         this.api = api;
 *         this.initialized = true;
 *     }
 *
 *     @Override
 *     public void shutdown() {
 *         this.initialized = false;
 *     }
 *
 *     @Override
 *     public boolean isInitialized() {
 *         return initialized;
 *     }
 * }
 * }</pre>
 *
 * @author Clickdroit
 * @version 1.1
 * @see ServiceManager
 */
public interface GameService {

    /**
     * Initialise le service avec l'instance de l'API.
     * <p>
     * Cette méthode est appelée automatiquement par le {@link ServiceManager}
     * lors de l'enregistrement du service. Elle doit configurer toutes les
     * ressources nécessaires au fonctionnement du service.
     * </p>
     *
     * @param api l'instance principale de l'API UHC
     * @throws IllegalStateException si le service est déjà initialisé
     */
    void initialize(API api);

    /**
     * Arrête proprement le service et libère ses ressources.
     * <p>
     * Cette méthode est appelée automatiquement lors de la désactivation
     * du plugin. Elle doit libérer toutes les ressources utilisées par le service.
     * </p>
     */
    void shutdown();

    /**
     * Vérifie si le service est initialisé et prêt à être utilisé.
     *
     * @return {@code true} si le service est initialisé, {@code false} sinon
     */
    boolean isInitialized();

    /**
     * Retourne le nom du service pour l'identification dans les logs.
     * <p>
     * Par défaut, retourne le nom simple de la classe.
     * </p>
     *
     * @return le nom du service
     */
    default String getServiceName() {
        return getClass().getSimpleName();
    }

    /**
     * Retourne la priorité de chargement du service.
     * <p>
     * Les services avec une priorité plus élevée sont chargés en premier.
     * La priorité par défaut est 0.
     * </p>
     *
     * @return la priorité de chargement (plus élevé = chargé en premier)
     */
    default int getPriority() {
        return 0;
    }
}

