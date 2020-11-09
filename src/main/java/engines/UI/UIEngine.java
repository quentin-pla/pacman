package engines.UI;

/**
 * Moteur interface utilisateur
 */
public interface UIEngine {
    /**
     * Générer une nouvelle entité
     * @return entité interface utilisateur
     */
    static UIEntity generateEntity() { return new UIEntity(); }
}
