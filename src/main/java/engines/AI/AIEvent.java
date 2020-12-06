package engines.AI;

import engines.kernel.EventListener;

/**
 * Évènements de collision
 */
public interface AIEvent {
    /**
     * Notifier un évènement
     * @param event évènement
     */
    void notifyEvent(String event);

    /**
     * S'abonner aux évènements
     * @param listener écouteur
     */
    void subscribeEvents(EventListener listener);
}
