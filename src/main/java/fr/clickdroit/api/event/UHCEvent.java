package fr.clickdroit.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Classe de base pour tous les événements UHC.
 * <p>
 * Cette classe abstraite fournit une base commune pour tous les événements
 * personnalisés du plugin UHC. Elle étend {@link Event} de Bukkit et
 * fournit une implémentation par défaut des méthodes requises.
 * </p>
 * 
 * <p>Exemple d'utilisation :</p>
 * <pre>{@code
 * public class MyUHCEvent extends UHCEvent {
 *     private static final HandlerList HANDLERS = new HandlerList();
 *     
 *     @Override
 *     public HandlerList getHandlers() {
 *         return HANDLERS;
 *     }
 *     
 *     public static HandlerList getHandlerList() {
 *         return HANDLERS;
 *     }
 * }
 * }</pre>
 * 
 * @author Clickdroit
 * @version 1.0
 * @see Event
 */
public abstract class UHCEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    /**
     * Constructeur par défaut pour les événements synchrones.
     */
    public UHCEvent() {
        super();
    }

    /**
     * Constructeur pour les événements qui peuvent être asynchrones.
     *
     * @param isAsync true si l'événement est asynchrone
     */
    public UHCEvent(boolean isAsync) {
        super(isAsync);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
