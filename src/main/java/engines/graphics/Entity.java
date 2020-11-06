package engines.graphics;

import api.SwingRenderer;

/**
 * Entité
 */
public class Entity extends SwingRenderer {
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
     * Constructeur avec dimensions
     * @param height hauteur
     * @param width largeur
     */
    public Entity(int height, int width) {
        this.height = height;
        this.width = width;
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructeur par clonage
     * @param clone clone
     */
    private Entity(Entity clone) {
        this(clone.height, clone.width);
        this.x = 0;
        this.y = 0;
        this.scene = null;
        this.color = clone.color;
        this.texture = clone.texture.clone();
    }

    /**
     * Cloner une entité
     * @return clone
     */
    public Entity clone() {
        return new Entity(this);
    }

    /**
     * Dessiner l'entité
     */
    public void draw() {
        if (color != null) color.cover(this);
        if (texture != null) texture.cover(this);
    }

    /**
     * Mettre à jour l'entité
     */
    public void update() {
        if (texture != null) texture.update();
    }

    /**
     * Effacer l'entité
     */
    public void erase() {
        scene.removeEntity(this);
    }

    /**
     * Translater l'entité
     * @param x nombre à additionner à la position horizontale
     * @param y nombre à additionner à la position verticale
     */
    public void translate(int x, int y) {
        this.x = this.x + x;
        this.y = this.y + y;
    }

    /**
     * Déplacer l'entité
     * @param x nouvelle position horizontale
     * @param y nouvelle position verticale
     */
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Redimensionner l'entité
     * @param height hauteur
     * @param width largeur
     */
    public void resize(int height, int width) {
        this.height = height;
        this.width = width;
    }

    /**
     * Attacher une couleur
     * @param red intensité rouge
     * @param green intensité vert
     * @param blue intensité bleu
     */
    public void bindColor(int red, int green, int blue) {
        color = new Color(red, green, blue);
    }

    /**
     * Détacher la couleur
     */
    public void unbindColor() { color = null; }

    /**
     * Attacher une texture
     * @param texture texture
     */
    public void bindTexture(EntityTexture texture) { this.texture = texture; }

    /**
     * Détacher la texture
     */
    public void unbindTexture() { texture = null; }

    // GETTERS & SETTERS //

    public Scene getScene() { return scene; }

    protected void setScene(Scene scene) { this.scene = scene; }

    public int getHeight() { return height; }

    public void setHeight(int height) { this.height = height; }

    public int getWidth() { return width; }

    public void setWidth(int width) { this.width = width; }

    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    public int getY() { return y; }

    public void setY(int y) { this.y = y; }

    public Color getColor() { return color; }

    public EntityTexture getTexture() { return texture; }
}
