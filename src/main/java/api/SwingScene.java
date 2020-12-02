package api;

import javax.swing.*;
import java.awt.*;

/**
 * Scène (JPanel) utilisant la librairie Swing
 */
public class SwingScene extends JPanel {
    /**
     * Hauteur
     */
    protected int height;

    /**
     * Largeur
     */
    protected int width;

    /**
     * Graphismes 2D
     */
    protected Graphics2D graphics;

    /**
     * Couleur de fond
     */
    protected Color backgroundColor;

    /**
     * Constructeur
     * @param height hauteur
     * @param width largeur
     */
    protected SwingScene(int height, int width) {
        this.height = height;
        this.width = width;
        this.backgroundColor = Color.black;
    }

    /**
     * Définir la couleur de fond
     * @param red rouge
     * @param green vert
     * @param blue bleu
     */
    public void setBackgroundColor(int red, int green, int blue) {
        backgroundColor = new Color(red, green, blue);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public Graphics2D get2DGraphics() { return graphics; }
}
