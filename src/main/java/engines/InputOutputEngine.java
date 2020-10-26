package engines;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Moteur entrée/sortie
 */
public class InputOutputEngine {
    /**
     * Instance
     */
    private static InputOutputEngine instance;

    /**
     * Touche pressée
     */
    private boolean[] key_pressed = new boolean[350];

    /**
     * Constructeur
     */
    private InputOutputEngine() {}

    /**
     * Obtenir l'instance
     * @return instance
     */
    public static InputOutputEngine get() {
        if (instance == null) instance = new InputOutputEngine();
        return instance;
    }

    /**
     * Fonction de rappel touche clavier
     * @param window fenêtre
     * @param key touche
     * @param scancode ?
     * @param action action
     * @param mods ?
     */
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) get().key_pressed[key] = true;
        else if (action == GLFW_RELEASE) get().key_pressed[key] = false;
    }

    /**
     * Savoir si une touche est pressée
     * @param keyCode code ASCII décimal
     * @return booléen
     */
    public static boolean isKeyPressed(int keyCode) {
        return get().key_pressed[keyCode];
    }
}
