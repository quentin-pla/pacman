package api;

import static org.lwjgl.opengl.GL11.*;

/**
 * Carreau
 */
public class Tile {
    /**
     * Position horizontale
     */
    private int x;

    /**
     * Position verticale
     */
    private int y;

    /**
     * Dimensions
     */
    private int size;

    /**
     * Couleur
     */
    private float[] color;

    /**
     * Texture
     */
    private Texture texture;

    /**
     * Constructeur avec couleur
     * @param size dimensions
     * @param color couleur
     */
    public Tile(int size, float[] color) {
        this.size = size;
        this.color = color;
        this.texture = null;
    }

    /**
     * Constructeur avec texture
     * @param size dimensions
     * @param texture texture
     */
    public Tile(int size, Texture texture) {
        this.size = size;
        this.texture = texture;
        this.color = null;
    }

    /**
     * Générer le carreau
     * @param x position horizontale
     * @param y position verticale
     */
    public void render(int x, int y) {
        this.x = x;
        this.y = y;
        if (color != null) {
            glColor4f(color[0], color[1], color[2], color[3]);
            glBegin(GL_QUADS);
            glVertex2i(x, y);
            glVertex2i(x + size, y);
            glVertex2i(x + size, y + size);
            glVertex2i(x, y + size);
            glEnd();
        }
        else if (texture != null) {
            texture.bind();
            glBegin(GL_QUADS);
            glTexCoord2f(0,0);  glVertex2i(x, y);
            glTexCoord2f(1,0);  glVertex2i(x + size, y);
            glTexCoord2f(1,1);  glVertex2i(x + size, y + size);
            glTexCoord2f(0,1);  glVertex2i(x, y + size);
            glEnd();
            texture.unbind();
        }

    }

    // GETTERS & SETTERS //

    public int getSize() {
        return size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float[] getColor() {
        return color;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
