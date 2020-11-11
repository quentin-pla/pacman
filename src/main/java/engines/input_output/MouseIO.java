package engines.input_output;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Écouteur entrées / sorties souris
 */
public class MouseIO implements MouseListener {
    /**
     * Boutons pressés
     */
    private ArrayList<Integer> pressed_buttons = new ArrayList<>();

    /**
     * Dernier bouton pressé
     */
    private int last_pressed_button;

    /**
     * Constructeur privé
     */
    protected MouseIO() {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        int key = e.getButton();
        if (!pressed_buttons.contains(key)) {
            pressed_buttons.add(key);
            last_pressed_button = key;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int key = e.getButton();
        if (pressed_buttons.contains(key))
            pressed_buttons.remove((Integer) key);
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    // GETTERS //

    public ArrayList<Integer> getPressedButtons() {
        return pressed_buttons;
    }

    public int getLastPressedButton() {
        return last_pressed_button;
    }
}
