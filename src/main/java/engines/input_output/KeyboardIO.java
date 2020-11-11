package engines.input_output;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Écouteur entrées / sorties clavier
 */
public class KeyboardIO implements KeyListener {
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
    protected KeyboardIO() {}

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

    // GETTERS //

    public ArrayList<Integer> getPressedKeys() {
        return pressed_keys;
    }

    public int getLastPressedKey() { return last_pressed_key; }
}
