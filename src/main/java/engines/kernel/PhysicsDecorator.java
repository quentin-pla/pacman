package engines.kernel;

import engines.physics.PhysicsEntity;
import engines.physics.PhysicsObject;

/**
 * Décorateur entité physique
 */
public interface PhysicsDecorator extends PhysicsEntity {
    /**
     * Obtenir l'entité physique
     * @return instance
     */
    PhysicsObject getPhysics();
}
