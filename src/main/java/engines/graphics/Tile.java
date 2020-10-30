package engines.graphics;

/**
 * Carreau
 */
public class Tile extends Entity {
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
     * Fichier de texture
     */
    private SpriteSheet sprite_sheet;

    /**
     * Coordonnées de la texture dans le fichier de texture
     */
    private int[] sprite_coords;

    /**
     * Constructeur par défaut
     * @param size dimensions
     */
    public Tile(int size) {
        super(0,0);
        this.size = size;
    }

    /**
     * Constructeur par clonage
     * @param clone clone
     */
    public Tile(Tile clone) {
        super(clone.x, clone.y);
        this.size = clone.size;
        this.color = clone.color;
        this.texture = clone.texture;
        this.sprite_sheet = clone.sprite_sheet;
        this.sprite_coords = clone.sprite_coords;
    }

    /**
     * Attacher une couleur
     * @param red intensité rouge
     * @param green intensité vert
     * @param blue intensité bleu
     * @param alpha opacité
     */
    public void bindColor(float red, float green, float blue, float alpha) {
        this.color = new float[]{red, green, blue, alpha};
    }

    /**
     * Attacher une texture
     * @param texture texture
     */
    public void bindTexture(Texture texture) {
        this.texture = texture;
    }

    /**
     * Attacher une texture depuis un fichier de texture
     * @param spriteSheet fichier de texture
     * @param row coordonnée horizontal de la texture
     * @param col coordonnée verticale de la texture
     */
    public void bindSpriteSheet(SpriteSheet spriteSheet, int row, int col) {
        this.sprite_sheet = spriteSheet;
        this.sprite_coords = new int[]{row-1, col-1};
    }

    @Override
    protected void draw() {
        if (color != null)
            renderQUAD(size, x, y, color);
        if (texture != null)
            renderTexturedQUAD(size, x, y, texture.getId());
        if (sprite_sheet != null)
            renderSpriteSheetQUAD(size, x, y, sprite_sheet.getTexture().getId(),
                    sprite_sheet.getHeight(), sprite_sheet.getWidth(), sprite_coords);
    }

    // GETTERS & SETTERS //

    public int getSize() { return size; }

    public void setSize(int size) { this.size = size; }

    public float[] getColor() { return color; }

    public Texture getTexture() { return texture; }

    public SpriteSheet getSprite_sheet() { return sprite_sheet; }
}
