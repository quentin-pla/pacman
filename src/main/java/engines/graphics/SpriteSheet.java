package engines.graphics;

/**
 * Fichier de textures
 */
public class SpriteSheet {
    /**
     * Lien du fichier
     */
    private String link;

    /**
     * Liste des sous-textures
     */
    private Sprite[][] sprites;

    /**
     * Constructeur
     * @param link lien vers le fichier
     */
    protected SpriteSheet(String link, int height, int width) {
        this.link = link;
        this.sprites = new Sprite[height][width];
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                sprites[row][col] = new Sprite(this, row, col);
    }

    /**
     * Obtenir une partie de la texture
     * @param row ligne
     * @param col colonne
     * @return texture
     */
    protected Sprite getSprite(int row, int col) {
        return sprites[row-1][col-1];
    }

    // GETTERS //

    protected String getLink() { return link; }
}
