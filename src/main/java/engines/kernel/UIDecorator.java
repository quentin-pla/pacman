package engines.kernel;

import engines.UI.UIEngine;
import engines.UI.UIEntity;

/**
 * Décorateur entité interface utilisateur
 */
public interface UIDecorator extends UIEngine {
    /**
     * Obtenir l'entité interface utilisateur
     * @return instance
     */
    UIEntity getUI();
}
