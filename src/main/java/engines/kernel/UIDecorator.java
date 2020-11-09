package engines.kernel;

import engines.UI.UIEntity;
import engines.UI.UIObject;

/**
 * Décorateur entité interface utilisateur
 */
public interface UIDecorator extends UIEntity {
    /**
     * Obtenir l'entité interface utilisateur
     * @return instance
     */
    UIObject getUI();
}
