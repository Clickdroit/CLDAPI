package fr.clickdroit.api.config.scenario;

import org.bukkit.inventory.ItemStack;

/**
 * Interface définissant le contrat pour tous les scénarios UHC.
 * Cette interface permet de standardiser le comportement des scénarios
 * et facilite leur gestion, test et extension.
 */
public interface IScenario {

    /**
     * Appelé lorsque le scénario est activé.
     * Initialise les ressources nécessaires au scénario.
     */
    void onEnable();

    /**
     * Appelé lorsque le scénario est désactivé.
     * Nettoie les ressources utilisées par le scénario.
     */
    void onDisable();

    /**
     * Appelé au démarrage de la partie.
     * Configure le scénario pour la partie en cours.
     */
    void onStart();

    /**
     * Appelé à la fin de la partie.
     * Effectue le nettoyage final du scénario.
     */
    void onEnd();

    /**
     * Vérifie si le scénario est actuellement activé.
     *
     * @return true si le scénario est activé, false sinon
     */
    boolean isEnabled();

    /**
     * Active ou désactive le scénario.
     *
     * @param enabled true pour activer, false pour désactiver
     */
    void setEnabled(boolean enabled);

    /**
     * Retourne le nom d'affichage du scénario.
     *
     * @return le nom du scénario
     */
    String getName();

    /**
     * Retourne la description du scénario.
     *
     * @return un tableau de lignes décrivant le scénario
     */
    String[] getDescription();

    /**
     * Retourne l'icône représentant le scénario dans les menus.
     *
     * @return l'ItemStack servant d'icône
     */
    ItemStack getIcon();

    /**
     * Vérifie si le scénario nécessite le mode équipe.
     *
     * @return true si le scénario requiert des équipes, false sinon
     */
    boolean requiresTeams();

    /**
     * Vérifie si le scénario est configurable (possède des valeurs ajustables).
     *
     * @return true si le scénario est configurable, false sinon
     */
    boolean isConfigurable();

    /**
     * Retourne la valeur actuelle du paramètre configurable.
     * Cette méthode n'est pertinente que si isConfigurable() retourne true.
     *
     * @return la valeur du paramètre, ou 0 si non configurable
     */
    int getValue();

    /**
     * Définit la valeur du paramètre configurable.
     * Cette méthode n'est pertinente que si isConfigurable() retourne true.
     *
     * @param value la nouvelle valeur à définir
     */
    void setValue(int value);

    /**
     * Retourne la valeur minimale autorisée pour le paramètre configurable.
     *
     * @return la valeur minimale, ou 0 si non configurable
     */
    int getMinValue();

    /**
     * Retourne la valeur maximale autorisée pour le paramètre configurable.
     *
     * @return la valeur maximale, ou 0 si non configurable
     */
    int getMaxValue();

    /**
     * Retourne le type de valeur pour la configuration.
     *
     * @return le type de valeur du scénario, ou null si non configurable
     */
    ScenarioValueType getValueType();
}
