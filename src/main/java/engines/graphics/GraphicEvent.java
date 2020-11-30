package engines.graphics;

import engines.kernel.EventListener;

/**
 * Évènements de graphismes
 */
public interface GraphicEvent {
    /**
     * Notifier une collision
     */
    void notifyEvent(String event);

    void notifyEntityUpdate(GraphicEntity entity);

    /**
     * S'abonner aux évènements
     */
    void subscribeEvents(EventListener listener);
}
