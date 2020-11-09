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
        return new PhysicsObject();
    }
}
