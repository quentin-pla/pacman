package engines.kernel;

import engines.AI.AIEngine;
import engines.AI.AIEntity;

/**
 * Décorateur entité intelligence artificielle
 */
public interface AIDecorator extends AIEngine {
    /**
     * Obtenir l'entité intelligence artificielle
     * @return instance
     */
    AIEntity getAI();
}
