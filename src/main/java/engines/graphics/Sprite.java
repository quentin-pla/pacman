package engines.graphics;

import api.SwingRenderer;

/**
 * Texture issue d'un fichier de textures
 */
public class Sprite extends Cover {
    /**
     * Nom du fichier de textures
     */
    private SpriteSheet spriteSheet;

    /**
     * Lien vers la partie de la texture
     */
    private String link;

    /**
     * Constructeur
     * @param spriteSheet fichier de textures
     * @param row ligne
     * @param col colonne
     */
    protected Sprite(SpriteSheet spriteSheet, int row, int col) {
        this.spriteSheet = spriteSheet;
        this.link = spriteSheet.getLink() + row + col;
    }

    @Override
    protected void cover(GraphicsObject graphicsObject) {
        SwingRenderer.renderTexturedRect(graphicsObject.height, graphicsObject.width,
                graphicsObject.x, graphicsObject.y, link);
    }

    @Override
    protected void update() {}

    @Override
    public Cover clone() {
        return this;
    }

    // GETTERS //

    public SpriteSheet getSpriteSheet() { return spriteSheet; }

    public String getLink() { return link; }
}
