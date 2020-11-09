package engines.physics;

/**
 * Entité physique
 */
public interface PhysicsEntity {
    /**
     * Générer une nouvelle entité
     * @return entité physique
     */

    static PhysicsObject generateEntity() {
        return new PhysicsObject(0,0,0);
    }

}
