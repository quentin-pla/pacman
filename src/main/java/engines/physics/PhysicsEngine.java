package engines.physics;

/**
 * Moteur physique
 */
public interface PhysicsEngine {
    /**
     * Générer une nouvelle entité
     * @return entité physique
     */
    static PhysicsEntity generateEntity() {
        return new PhysicsEntity();
    }
}
