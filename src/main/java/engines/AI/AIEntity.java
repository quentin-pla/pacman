package engines.AI;

/**
 * Entité intelligence artificielle
 */
public class AIEntity implements AIEngine {
    /**
     * Constructeur
     */
    protected AIEntity() {}

    /**
     * Cloner l'entité
     * @return clone
     */
    public AIEntity clone() {
        return new AIEntity();
    }
}
