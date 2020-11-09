package engines.AI;

/**
 * Entité intelligence artificelle
 */
public interface AIEntity {
    /**
     * Générer une nouvelle entité
     * @return entité intelligence artificielle
     */
    static AIObject generateEntity() {
        return new AIObject();
    }
}
