package engines.input_output;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Écouteur entrées / sorties clavier
 */
public class KeyboardIO implements KeyListener {
    /**
     * Moteur entrées / sorties
     */
    private final IOEngine ioEngine;

    /**
     * Touches préssées
     */
    private final ArrayList<Integer> pressedKeys = new ArrayList<>();

    /**
     * Dernière touche pressée
     */
    private int lastPressedKey;

    /**
     * Constructeur
     */
    protected KeyboardIO(IOEngine ioEngine) {
        this.ioEngine = ioEngine;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (!pressedKeys.contains(key) && ioEngine.isKeyBindedToEvent(key)) {
            pressedKeys.add(key);
            lastPressedKey = key;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (pressedKeys.contains(key))
            pressedKeys.remove((Integer) key);
    }

    // GETTERS //

    public ArrayList<Integer> getPressedKeys() {
        return pressedKeys;
    }

    public int getLastPressedKey() { return lastPressedKey; }

    public void setLastPressedKey(int lastPressedKey) {
        this.lastPressedKey = lastPressedKey;
    }
}
