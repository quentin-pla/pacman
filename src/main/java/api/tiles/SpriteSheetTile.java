package api.tiles;

import api.textures.SpriteSheet;

import static org.lwjgl.opengl.GL11.*;

/**
 * Carreau affiché avec un fichier de textures
 */
public class SpriteSheetTile extends Tile {
    /**
     * Fichier de textures
     */
    private SpriteSheet spriteSheet;

    /**
     * Coordonnée horizontale de la texture
     */
    private int x_coord;

    /**
     * Coordonnée verticale de la texture
     */
    private int y_coord;

    /**
     * Constructeur
     * @param size dimensions
     * @param spriteSheet fichier de textures
     */
    public SpriteSheetTile(int size, SpriteSheet spriteSheet, int x_coord, int y_coord) {
        super(size);
        this.spriteSheet = spriteSheet;
        this.x_coord = x_coord - 1;
        this.y_coord = y_coord - 1;
    }

    @Override
    public void render() {
        spriteSheet.getTexture().bind();
        glBegin(GL_QUADS);
        glTexCoord2f((x_coord + 0f) / spriteSheet.getWidth(),(y_coord + 0f) / spriteSheet.getHeight());
        glVertex2i(x, y);
        glTexCoord2f((x_coord + 1f) / spriteSheet.getWidth(),(y_coord + 0f) / spriteSheet.getHeight());
        glVertex2i(x + size, y);
        glTexCoord2f((x_coord + 1f) / spriteSheet.getWidth(),(y_coord + 1f) / spriteSheet.getHeight());
        glVertex2i(x + size, y + size);
        glTexCoord2f((x_coord + 0f) / spriteSheet.getWidth(),(y_coord + 1f) / spriteSheet.getHeight());
        glVertex2i(x, y + size);
        glEnd();
        spriteSheet.getTexture().unbind();
    }

    // GETTERS & SETTERS //

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    public void setSpriteSheet(SpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public int getX_coord() {
        return x_coord;
    }

    public void setX_coord(int x_coord) {
        this.x_coord = x_coord;
    }

    public int getY_coord() {
        return y_coord;
    }

    public void setY_coord(int y_coord) {
        this.y_coord = y_coord;
    }
}
