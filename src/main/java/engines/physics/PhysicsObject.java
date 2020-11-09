package engines.physics;

/**
 * Objet physique
 */
public class PhysicsObject implements PhysicsEntity {
    /**
     * Constructeur
     */
    protected PhysicsObject() {}

    /**
     * Cloner l'entité
     * @return
     */
    public PhysicsObject clone() {
        return new PhysicsObject();
    }
}
