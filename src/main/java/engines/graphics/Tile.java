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
    private SpriteSheet spriteSheet;

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
        this.spriteSheet = spriteSheet;
        this.sprite_coords = new int[]{row-1, col-1};
    }

    @Override
    protected void draw() {
        if (color != null)
            renderQUAD(size, x, y, color);
        else if (texture != null)
            renderTexturedQUAD(size, x, y, texture.getId());
        else if (spriteSheet != null)
            renderSpriteSheetQUAD(size, x, y, spriteSheet.getTexture().getId(),
                    spriteSheet.getHeight(), spriteSheet.getWidth(), sprite_coords);
    }

    // GETTERS & SETTERS //

    public int getSize() { return size; }

    public void setSize(int size) { this.size = size; }

    public float[] getColor() { return color; }

    public Texture getTexture() { return texture; }

    public SpriteSheet getSpriteSheet() { return spriteSheet; }
}
