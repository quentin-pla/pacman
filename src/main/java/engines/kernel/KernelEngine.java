package engines.kernel;

/**
 * Moteur noyau
 */
public class KernelEngine {
    /**
     * Instance
     */
    private static KernelEngine instance;

    /**
     * Constructeur
     */
    private KernelEngine() {}

    /**
     * Obtenir l'instance
     * @return instance
     */
    public static KernelEngine get() {
        if (instance == null) instance = new KernelEngine();
        return instance;
    }
}
