package engines.input_output;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyboardInputOutput implements KeyListener {
    /**
     * Touches préssées
     */
    private static ArrayList<Integer> pressed_keys = new ArrayList<>();

    /**
     * Instance
     */
    private static KeyboardInputOutput instance;

    /**
     * Obtenir l'instance
     */
    public static KeyboardInputOutput getInstance() {
        if (instance == null) instance = new KeyboardInputOutput();
        return instance;
    }

    /**
     * Dernière touche pressée
     */
    private static int last_pressed_key;

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (!pressed_keys.contains(key)) {
            pressed_keys.add(key);
            last_pressed_key = key;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (pressed_keys.contains(key))
            pressed_keys.remove((Integer) key);
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

    // GETTERS //

    public static ArrayList<Integer> getPressedKeys() {
        return pressed_keys;
    }

    public static int getLastPressedKey() {
        return last_pressed_key;
    }
}
