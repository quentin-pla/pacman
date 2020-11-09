package engines.AI;

/**
 * Moteur intelligence artificelle
 */
public interface AIEngine {
    /**
     * Générer une nouvelle entité
     * @return entité intelligence artificielle
     */
    static AIEntity generateEntity() {
        return new AIEntity();
    }
}
