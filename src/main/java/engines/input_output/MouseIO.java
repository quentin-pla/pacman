package engines.input_output;

import java.awt.*;
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
    private ArrayList<Integer> pressedButtons = new ArrayList<>();

    /**
     * Dernier bouton pressé
     */
    private int lastPressedButton;

    /**
     * Coordonnées du click
     */
    private Point clickCoords = new Point();

    /**
     * Constructeur privé
     */
    protected MouseIO() {}

    @Override
    public void mouseClicked(MouseEvent e) {
        clickCoords = e.getPoint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int key = e.getButton();
        if (!pressedButtons.contains(key)) {
            pressedButtons.add(key);
            lastPressedButton = key;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int key = e.getButton();
        if (pressedButtons.contains(key))
            pressedButtons.remove((Integer) key);
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    // GETTERS //

    public ArrayList<Integer> getPressedButtons() {
        return pressedButtons;
    }

    public int getLastPressedButton() {
        return lastPressedButton;
    }

    public Point getClickCoords() { return clickCoords; }
}
