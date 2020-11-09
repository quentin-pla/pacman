package engines.network;

/**
 * Entité réseau
 */
public class NetworkEntity implements NetworkEngine {
    /**
     * Constructeur
     */
    protected NetworkEntity() {}

    /**
     * Cloner l'entité
     * @return clone
     */
    public NetworkEntity clone() {
        return new NetworkEntity();
    }
}
