package engines;

/**
 * Moteur noyau
 */
public class KernelEngine {
    /**
     * Initialiser le moteur noyau
     */
    public static void init() {
        GraphicsEngine.init();
        InputOutputEngine.init();
        PhysicsEngine.init();
    }

    /**
     * Nouvelle saisie clavier détectée
     */
    public static void newInput(int key) {
        System.out.println(key);
    }
}
