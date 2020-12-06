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
    private int lastPressedButton = -1;

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
    public void mousePressed(MouseEvent e) {
        int key = e.getButton();
        if (!pressedButtons.contains(key))
            pressedButtons.add(key);
        updateAttributes(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int key = e.getButton();
        if (pressedButtons.contains(key))
            pressedButtons.remove((Integer) key);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * Mettre à jour les attributs
     * @param e évènement
     */
    public void updateAttributes(MouseEvent e) {
        Point point = e.getPoint();
        point.y = point.y - SwingWindow.getInstance().getUselessTopGap();
        clickCoords = point;
        lastPressedButton = e.getButton();
    }

    // GETTERS //

    public ArrayList<Integer> getPressedButtons() {
        return pressedButtons;
    }

    public int getLastPressedButton() {
        return lastPressedButton;
    }

    public void setLastPressedButton(int lastPressedButton) {
        this.lastPressedButton = lastPressedButton;
    }

    public Point getClickCoords() { return clickCoords; }

    public void setClickCoords(Point clickCoords) { this.clickCoords = clickCoords; }
}
