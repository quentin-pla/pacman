package engines.physics;

import engines.kernel.EventListener;

/**
 * Évènements de collision
 */
public interface CollisionEvent {
    /**
     * Notifier une collision
     */
    void notifyCollision(String event);

    /**
     * Notifier une mise à jour d'entité
     * @param entity entité
     */
    void notifyEntityUpdate(PhysicEntity entity);

    /**
     * S'abonner aux évènements
     */
    void subscribeEvents(EventListener listener);
}
