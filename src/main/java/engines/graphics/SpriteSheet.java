package engines.graphics;

import static engines.graphics.GraphicsEngine.*;

/**
 * Fichier de textures
 */
public class SpriteSheet {
    /**
     * Texture liée
     */
    private Texture texture;

    /**
     * Nombre de textures horizontales
     */
    private int[] size;

    /**
     * Constructeur
     * @param link lien vers la texture
     * @param height nombre de textures horizontales
     * @param width nombre de textures verticales
     */
    protected SpriteSheet(String link, int height, int width) {
        this.texture = new Texture(link);
        this.size = new int[]{height,width};
    }

    /**
     * Transférer un fichier de texture
     * @param link lien du fichier
     * @param name nom du fichier
     */
    public static void upload(String link, int height, int width, String name) {
        uploadSpriteSheet(link, height, width, name);
    }

    /**
     * Obtenir un fichier de texture
     * @param name nom
     * @return texture
     */
    public static SpriteSheet get(String name) {
        return getSpriteSheet(name);
    }

    // GETTERS //

    protected Texture getTexture() { return texture; }

    protected int[] getSize() { return size; }
}
