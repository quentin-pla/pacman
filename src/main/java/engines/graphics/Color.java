package engines.graphics;

import api.SwingRenderer;

/**
 * Couleur
 */
public class Color extends Cover {
    /**
     * Rouge
     */
    private int red;

    /**
     * Vert
     */
    private int green;

    /**
     * Bleu
     */
    private int blue;

    /**
     * Constructeur
     * @param red rouge
     * @param green vert
     * @param blue bleu
     */
    public Color(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue= blue;
    }

    @Override
    public void cover(Entity entity) {
        SwingRenderer.renderRect(entity.getHeight(), entity.getWidth(),
                entity.getX(), entity.getY(), getSwingColor());
    }

    @Override
    public void update() {}

    @Override
    public Cover clone() {
        return this;
    }

    // GETTERS //

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public java.awt.Color getSwingColor() { return SwingRenderer.getSwingColor(red, green, blue); }
}
