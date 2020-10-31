package engines.graphics;

/**
 * Carreau
 */
public class Tile extends Entity {
    /**
     * Couleur
     */
    private float[] color;

    /**
     * Texture
     */
    private TileTexture texture;

    /**
     * Constructeur par défaut
     * @param height hauteur
     * @param width largeur
     */
    public Tile(int height, int width) {
        super(height, width,0,0);
    }

    /**
     * Constructeur avec taille
     * @param size taille
     */
    public Tile(int size) {
        super(size,0,0);
    }

    /**
     * Constructeur par clonage
     * @param clone clone
     */
    public Tile(Tile clone) {
        super(clone.height, clone.width, clone.x, clone.y);
        this.color = clone.color;
        this.texture = clone.texture;
    }

    /**
     * Attacher une couleur
     * @param red intensité rouge
     * @param green intensité vert
     * @param blue intensité bleu
     * @param alpha opacité
     */
    public void bindColor(float red, float green, float blue, float alpha) {
        color = new float[]{red, green, blue, alpha};
    }

    /**
     * Détacher la couleur
     */
    public void unbindColor() { color = null; }

    /**
     * Attacher une texture
     * @param texture texture
     */
    public void bindTexture(TileTexture texture) { this.texture = texture; }

    /**
     * Détacher la texture
     */
    public void unbindTexture() { texture = null; }

    @Override
    public void draw() {
        if (color != null) renderQUAD(height, width, x, y, color);
        if (texture != null) texture.cover(this);
    }

    @Override
    public void update() {
        if (texture != null) texture.update();
    }

    // GETTERS & SETTERS //

    public float[] getColor() { return color; }

    public TileTexture getTexture() { return texture; }
}
