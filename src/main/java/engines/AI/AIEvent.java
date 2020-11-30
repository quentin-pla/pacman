package engines.AI;

import engines.kernel.EventListener;

/**
 * Évènements de collision
 */
public interface AIEvent {
    /**
     * Notifier un évènement
     */
    void notifyEvent(String event);

    /**
     * S'abonner aux évènements
     */
    void subscribeEvents(EventListener listener);
}
