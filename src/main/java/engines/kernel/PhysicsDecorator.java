package engines.kernel;

import engines.physics.PhysicsEngine;
import engines.physics.PhysicsEntity;

/**
 * Décorateur entité physique
 */
public interface PhysicsDecorator extends PhysicsEngine {
    /**
     * Obtenir l'entité physique
     * @return instance
     */
    PhysicsEntity getPhysics();
}
