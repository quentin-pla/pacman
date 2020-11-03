package engines.input_output;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class MouseInputOutput implements MouseListener {
    /**
     * Boutons pressés
     */
    private static ArrayList<Integer> pressed_buttons = new ArrayList<>();

    /**
     * Dernier bouton pressé
     */
    private static int last_pressed_button;

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

    public static ArrayList<Integer> getPressedButtons() {
        return pressed_buttons;
    }

    public static int getLastPressedButton() {
        return last_pressed_button;
    }
}
