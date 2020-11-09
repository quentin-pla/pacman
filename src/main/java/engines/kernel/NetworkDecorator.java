package engines.kernel;

import engines.network.NetworkEntity;
import engines.network.NetworkObject;

/**
 * Décorateur entité réseau
 */
public interface NetworkDecorator extends NetworkEntity {
    /**
     * Obtenir l'entité réseau
     * @return instance
     */
    NetworkObject getNetwork();
}
