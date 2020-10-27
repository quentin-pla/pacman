package api.tiles;

import api.textures.Texture;

import static org.lwjgl.opengl.GL11.*;

/**
 * Carreau affiché avec une texture
 */
public class TextureTile extends Tile {
    /**
     * Texture
     */
    private Texture texture;

    /**
     * Constructeur
     * @param size dimensions
     * @param texture texture
     */
    public TextureTile(int size, Texture texture) {
        super(size);
        this.texture = texture;
    }

    @Override
    public void draw() {
        texture.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0,0);  glVertex2i(x, y);
        glTexCoord2f(1,0);  glVertex2i(x + size, y);
        glTexCoord2f(1,1);  glVertex2i(x + size, y + size);
        glTexCoord2f(0,1);  glVertex2i(x, y + size);
        glEnd();
        texture.unbind();
    }

    // GETTERS & SETTERS //

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
