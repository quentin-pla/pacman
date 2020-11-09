package engines.network;

/**
 * Moteur réseau
 */
public interface NetworkEngine {
    /**
     * Générer une nouvelle entité
     * @return entité réseau
     */
    static NetworkEntity generateEntity() {
        return new NetworkEntity();
    }
}
