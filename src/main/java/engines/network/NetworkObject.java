package engines.network;

/**
 * Objet réseau
 */
public class NetworkObject implements NetworkEntity {
    /**
     * Constructeur
     */
    protected NetworkObject() {}

    /**
     * Cloner l'entité
     * @return clone
     */
    public NetworkObject clone() {
        return new NetworkObject();
    }
}
