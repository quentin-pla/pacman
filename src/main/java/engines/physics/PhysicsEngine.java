package engines.physics;

/**
 * Moteur Physique
 */
public class PhysicsEngine {
    /**
     * Instance
     */
    private static PhysicsEngine instance;

    /**
     * Constructeur
     */
    private PhysicsEngine() {}

    /**
     * Obtenir l'instance
     * @return instance
     */
    public static PhysicsEngine get() {
        if (instance == null) instance = new PhysicsEngine();
        return instance;
    }
}
