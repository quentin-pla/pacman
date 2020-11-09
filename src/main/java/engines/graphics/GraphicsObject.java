package engines.graphics;

/**
 * Objet graphique
 */
public class GraphicsObject implements GraphicsEntity {
    /**
     * Scène
     */
    protected Scene scene;

    /**
     * Hateur en pixels
     */
    protected int height;

    /**
     * Largeur en pixels
     */
    protected int width;

    /**
     * Position horizontale
     */
    protected int x;

    /**
     * Position verticale
     */
    protected int y;

    /**
     * Couleur
     */
    protected Color color;

    /**
     * Texture
     */
    protected Cover texture;

    /**
     * Constructeur par défaut
     */
    protected GraphicsObject() {
        this.height = 0;
        this.width = 0;
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructeur avec dimensions
     * @param height hauteur
     * @param width largeur
     */
    protected GraphicsObject(int height, int width) {
        this.height = height;
        this.width = width;
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructeur par clonage
     * @param clone clone
     */
    private GraphicsObject(GraphicsObject clone) {
        this(clone.height, clone.width);
        this.x = 0;
        this.y = 0;
        this.scene = null;
        this.color = clone.color;
        if (clone.texture != null)
            this.texture = clone.texture.clone();
    }

    /**
     * Cloner l'entité
     * @return clone
     */
    public GraphicsObject clone() {
        return new GraphicsObject(this);
    }

    @Override
    public void draw() {
        if (color != null) color.cover(this);
        if (texture != null) texture.cover(this);
    }

    @Override
    public void update() {
        if (texture != null) texture.update();
    }

    @Override
    public void erase() {
        scene.removeEntity(this);
    }

    @Override
    public void translate(int x, int y) {
        this.x = this.x + x;
        this.y = this.y + y;
    }

    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void resize(int height, int width) {
        this.height = height;
        this.width = width;
    }

    @Override
    public void bindColor(int red, int green, int blue) {
        color = new Color(red, green, blue);
    }

    @Override
    public void unbindColor() { color = null; }

    @Override
    public void bindTexture(Cover texture) { this.texture = texture; }

    @Override
    public void unbindTexture() { texture = null; }

    // GETTERS & SETTERS //

    @Override
    public Scene getScene() { return scene; }

    @Override
    public void setScene(Scene scene) { this.scene = scene; }

    @Override
    public int getHeight() { return height; }

    @Override
    public int getWidth() { return width; }

    @Override
    public int getX() { return x; }

    @Override
    public int getY() { return y; }

    @Override
    public Color getColor() { return color; }

    @Override
    public Cover getTexture() { return texture; }
}
