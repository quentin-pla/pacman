package api.textures;

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
    private int height;

    /**
     * Nombre de textures verticales
     */
    private int width;

    /**
     * Constructeur
     * @param link lien vers la texture
     * @param height nombre de textures horizontales
     * @param width nombre de textures verticales
     */
    public SpriteSheet(String link, int height, int width) {
        this.texture = new Texture(link);
        this.height = height;
        this.width = width;
    }

    // GETTERS //

    public Texture getTexture() {
        return texture;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
