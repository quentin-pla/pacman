package engines.network;

/**
 * Entité réseau
 */
public interface NetworkEntity {
    /**
     * Générer une nouvelle entité
     * @return entité réseau
     */
    static NetworkObject generateEntity() {
        return new NetworkObject();
    }
}
