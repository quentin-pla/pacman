package engines;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Moteur entrée/sortie
 */
public class InputOutputEngine {
    /**
     * Instance
     */
    private static InputOutputEngine instance;

    /**
     * Touches clavier
     */
    private boolean[] keyboard_keys = new boolean[GLFW_KEY_LAST];

    /**
     * Boutons souris
     */
    private boolean[] mouse_buttons = new boolean[GLFW_MOUSE_BUTTON_LAST];

    /**
     * Constructeur
     */
    private InputOutputEngine() {
        Arrays.fill(keyboard_keys, false);
        Arrays.fill(mouse_buttons, false);
    }

    /**
     * Obtenir l'instance
     * @return instance
     */
    public static InputOutputEngine get() {
        if (instance == null) instance = new InputOutputEngine();
        return instance;
    }

    /**
     * Fonction de rappel touches clavier
     * @param window fenêtre
     * @param key touche
     * @param scancode scanner
     * @param action action
     * @param mods touche combinée
     */
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) get().keyboard_keys[key] = true;
        else if (action == GLFW_RELEASE) get().keyboard_keys[key] = false;
    }

    /**
     * Fonction de rappel boutons souris
     * @param window fenêtre
     * @param button bouton
     * @param action action
     * @param mods touche combinée
     */
    public static void mouseCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) get().mouse_buttons[button] = true;
        else if (action == GLFW_RELEASE) get().mouse_buttons[button] = false;
    }

    /**
     * Savoir si une touche clavier est pressée
     * @param code code de la touche
     * @return booléen
     */
    public static boolean isKeyPressed(int code) {
        return get().keyboard_keys[code];
    }

    /**
     * Savoir si un bouton souris est pressée
     * @param code code du bouton
     * @return booléen
     */
    public static boolean isMouseButtonPressed(int code) {
        return get().mouse_buttons[code];
    }
}
