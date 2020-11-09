package engines.AI;

/**
 * Objet intelligence artificielle
 */
public class AIObject implements AIEntity {
    /**
     * Constructeur
     */
    protected AIObject() {}

    /**
     * Cloner l'entité
     * @return clone
     */
    public AIObject clone() {
        return new AIObject();
    }
}
