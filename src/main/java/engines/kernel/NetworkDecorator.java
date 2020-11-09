package engines.kernel;

import engines.network.NetworkEngine;
import engines.network.NetworkEntity;

/**
 * Décorateur entité réseau
 */
public interface NetworkDecorator extends NetworkEngine {
    /**
     * Obtenir l'entité réseau
     * @return instance
     */
    NetworkEntity getNetwork();
}
