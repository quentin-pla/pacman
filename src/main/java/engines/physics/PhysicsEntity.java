package engines.physics;

/**
 * Entité physique
 */
public class PhysicsEntity implements PhysicsEngine {
    /**
     * Constructeur
     */
    protected PhysicsEntity() {}

    /**
     * Cloner l'entité
     * @return
     */
    public PhysicsEntity clone() {
        return new PhysicsEntity();
    }
}
