package engines.graphics;

import api.SwingRenderer;

/**
 * Fichier de textures
 */
public class SpriteSheet extends SwingRenderer {
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
    private SpriteSheet(String link, int height, int width) {
        this.link = link;
        this.sprites = new Sprite[height][width];
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                sprites[row][col] = new Sprite(this, row, col);
    }

    /**
     * Transférer une texture
     * @param link lien du fichier
     */
    public static SpriteSheet load(String link, int height, int width) {
        if (!SwingRenderer.isTextureLoaded(link)) {
            SwingRenderer.loadSpriteSheet(link, height, width);
        } else {
            try {
                throw new Exception("Fichier de texture déjà transféré");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new SpriteSheet(link, height, width);
    }

    /**
     * Obtenir une partie de la texture
     * @param row ligne
     * @param col colonne
     */
    public Sprite getSprite(int row, int col) {
        return sprites[row-1][col-1];
    }

    // GETTERS //

    public String getLink() { return link; }
}
