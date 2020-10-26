package engines;

/**
 * Moteur graphique
 */
public class GraphicsEngine {
    /**
     * Instance
     */
    private static GraphicsEngine instance;

    /**
     * Constructeur
     */
    private GraphicsEngine() {}

    /**
     * Obtenir l'instance
     * @return instance
     */
    public static GraphicsEngine get() {
        if (instance == null) instance = new GraphicsEngine();
        return instance;
    }
}
