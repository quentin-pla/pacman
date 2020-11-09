package engines.UI;

/**
 * Entité interface utilisateur
 */
public interface UIEntity {
    /**
     * Générer une nouvelle entité
     * @return entité interface utilisateur
     */
    static UIObject generateEntity() { return new UIObject(); }
}
