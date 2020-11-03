package engines.graphics;

/**
 * Texture issue d'un fichier de textures
 */
public class Sprite extends EntityTexture {
    /**
     * Nom du fichier de textures
     */
    private SpriteSheet sprite_sheet;

    /**
     * Lien vers la partie de la texture
     */
    private String link;

    /**
     * Constructeur
     * @param sprite_sheet fichier de textures
     * @param row ligne
     * @param col colonne
     */
    protected Sprite(SpriteSheet sprite_sheet, int row, int col) {
        this.sprite_sheet = sprite_sheet;
        this.link = sprite_sheet.getLink() + row + col;
    }

    @Override
    protected void cover(Entity entity) {
        renderTexturedRect(entity.height, entity.width, entity.x, entity.y, link);
    }

    @Override
    protected void update() {}

    @Override
    public EntityTexture clone() {
        return this;
    }

    // GETTERS //

    public SpriteSheet getSpriteSheet() { return sprite_sheet; }

    public String getLink() { return link; }
}
