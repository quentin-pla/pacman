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
     * Ligne
     */
    private int row;

    /**
     * Colonne
     */
    private int col;

    /**
     * Constructeur
     * @param sprite_sheet fichier de textures
     * @param row ligne
     * @param col colonne
     */
    public Sprite(SpriteSheet sprite_sheet, int row, int col) {
        this.sprite_sheet = sprite_sheet;
        this.row = row - 1;
        this.col = col - 1;
    }

    @Override
    public void cover(Entity entity) {
        renderSpriteQUAD(entity.height, entity.width, entity.x, entity.y, getSpriteSheet().getTexture().getId(),
                getSpriteSheet().getSize(), getCoords());
    }

    @Override
    public void update() {}

    @Override
    public EntityTexture clone() {
        return this;
    }

    // GETTERS //

    public int[] getCoords() { return new int[]{row,col}; }

    public SpriteSheet getSpriteSheet() { return sprite_sheet; }
}
