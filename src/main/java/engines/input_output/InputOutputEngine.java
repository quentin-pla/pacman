package engines.input_output;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Moteur entrée/sortie
 */
public class InputOutputEngine {
    /**
     * Touches préssées
     */
    private static ArrayList<Integer> pressed_keys = new ArrayList<>();

    /**
     * Boutons pressés
     */
    private static ArrayList<Integer> pressed_buttons = new ArrayList<>();

    /**
     * Fonction de rappel touches clavier
     * @param window fenêtre
     * @param key touche
     * @param scancode scanner
     * @param action action
     * @param mods touche combinée
     */
    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS && !pressed_keys.contains(key))
            pressed_keys.add(key);
        else if (action == GLFW_RELEASE && pressed_keys.contains(key))
            pressed_keys.remove((Integer) key);
    }

    /**
     * Fonction de rappel boutons souris
     * @param window fenêtre
     * @param button bouton
     * @param action action
     * @param mods touche combinée
     */
    public static void mouseCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS && !pressed_buttons.contains(button))
            pressed_buttons.add(button);
        else if (action == GLFW_RELEASE && pressed_buttons.contains(button))
            pressed_buttons.remove((Integer) button);
    }

    /**
     * Savoir si une touche clavier est pressée
     * @param code code de la touche
     * @return booléen
     */
    public static boolean isKeyPressed(int code) {
        return pressed_keys.contains(code);
    }

    /**
     * Savoir si le clavier n'est pas utilisé
     * @return booléen
     */
    public static boolean isKeyboardFree() {
        return pressed_keys.isEmpty();
    }

    /**
     * Savoir si un bouton souris est pressée
     * @param code code du bouton
     * @return booléen
     */
    public static boolean isMouseButtonPressed(int code) {
        return pressed_buttons.contains(code);
    }

    /**
     * Savoir si la souris n'est pas utilisée
     * @return booléen
     */
    public static boolean isMouseFree() {
        return pressed_buttons.isEmpty();
    }

    // GETTERS //

    public static ArrayList<Integer> getPressedKeys() {
        return pressed_keys;
    }

    public static ArrayList<Integer> getPressedButtons() {
        return pressed_buttons;
    }
}
