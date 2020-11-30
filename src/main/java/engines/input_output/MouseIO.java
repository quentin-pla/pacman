package engines.input_output;

import api.SwingWindow;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Écouteur entrées / sorties souris
 */
public class MouseIO implements MouseListener {
    /**
     * Moteur entrées / sorties
     */
    private final IOEngine ioEngine;

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
    private Point clickCoords;

    /**
     * Constructeur privé
     */
    protected MouseIO(IOEngine ioEngine) {
        this.ioEngine = ioEngine;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Point point = e.getPoint();
        point.y = point.y - SwingWindow.getInstance().getUselessTopGap();
        clickCoords = point;
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

    public void setClickCoords(Point clickCoords) { this.clickCoords = clickCoords; }
}
