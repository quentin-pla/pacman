package engines.input_output;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyboardInputOutput implements KeyListener {
    /**
     * Touches préssées
     */
    private ArrayList<Integer> pressed_keys = new ArrayList<>();

    /**
     * Dernière touche pressée
     */
    private static int last_pressed_key;

    /**
     * Constructeur
     */
    protected KeyboardInputOutput() {}

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
    public boolean isKeyPressed(int code) {
        return pressed_keys.contains(code);
    }

    /**
     * Savoir si le clavier n'est pas utilisé
     * @return booléen
     */
    public boolean isKeyboardFree() {
        return pressed_keys.isEmpty();
    }

    // GETTERS //

    public ArrayList<Integer> getPressedKeys() {
        return pressed_keys;
    }

    public int getLastPressedKey() { return last_pressed_key; }
}
