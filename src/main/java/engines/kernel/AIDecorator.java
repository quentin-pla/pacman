package engines.kernel;

import engines.AI.AIEntity;
import engines.AI.AIObject;

/**
 * Décorateur entité intelligence artificielle
 */
public interface AIDecorator extends AIEntity {
    /**
     * Obtenir l'entité intelligence artificielle
     * @return instance
     */
    AIObject getAI();
}
