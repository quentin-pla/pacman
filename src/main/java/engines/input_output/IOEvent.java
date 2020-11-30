package engines.input_output;

import engines.kernel.Entity;
import engines.kernel.EventListener;

/**
 * Évènements d'entrées / sorties
 */
public interface IOEvent {
    /**
     * Notifier une collision
     */
    void notifyInput(String event);

    /**
     * Notifier un click
     * @param entity entité
     * @param eventName nom de l'évènement
     */
    void notifyClick(Entity entity, String eventName);

    /**
     * S'abonner aux évènements
     */
    void subscribeEvents(EventListener listener);
}
