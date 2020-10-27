package api.tiles;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glEnd;

/**
 * Carreau coloré
 */
public class ColorTile extends Tile {
    /**
     * Couleur
     */
    private float[] color;

    /**
     * Constructeur
     * @param size dimensions
     * @param color couleur
     */
    public ColorTile(int size, float[] color) {
        super(size);
        this.color = color;
    }

    @Override
    public void draw() {
        glColor4f(color[0], color[1], color[2], color[3]);
        glBegin(GL_QUADS);
        glVertex2i(x, y);
        glVertex2i(x + size, y);
        glVertex2i(x + size, y + size);
        glVertex2i(x, y + size);
        glEnd();
    }

    // GETTERS & SETTERS //

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }
}
