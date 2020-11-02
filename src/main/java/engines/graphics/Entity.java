package engines.graphics;

import engines.graphics.api.Renderer;

/**
 * Entité
 */
public class Entity extends Renderer {
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
    protected float[] color;

    /**
     * Texture
     */
    protected EntityTexture texture;

    /**
     * Constructeur avec dimensions
     * @param height hauteur
     * @param width largeur
     */
    protected Entity(int height, int width) {
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
    protected Entity clone() {
        return new Entity(this);
    }

    /**
     * Dessiner l'entité
     */
    protected void draw() {
        if (color != null) renderQUAD(height, width, x, y, color);
        if (texture != null) texture.cover(this);
    }

    /**
     * Mettre à jour l'entité
     */
    protected void update() {
        if (texture != null) texture.update();
    }

    /**
     * Effacer l'entité
     */
    protected void erase() {
        scene.removeEntity(this);
    }

    /**
     * Translater l'entité
     * @param x nombre à additionner à la position horizontale
     * @param y nombre à additionner à la position verticale
     */
    protected void translate(int x, int y) {
        this.x = this.x + x;
        this.y = this.y + y;
    }

    /**
     * Déplacer l'entité
     * @param x nouvelle position horizontale
     * @param y nouvelle position verticale
     */
    protected void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Redimensionner l'entité
     * @param height hauteur
     * @param width largeur
     */
    protected void resize(int height, int width) {
        this.height = height;
        this.width = width;
    }

    /**
     * Attacher une couleur
     * @param red intensité rouge
     * @param green intensité vert
     * @param blue intensité bleu
     * @param alpha opacité
     */
    protected void bindColor(float red, float green, float blue, float alpha) {
        color = new float[]{red, green, blue, alpha};
    }

    /**
     * Détacher la couleur
     */
    protected void unbindColor() { color = null; }

    /**
     * Attacher une texture
     * @param texture texture
     */
    protected void bindTexture(EntityTexture texture) { this.texture = texture; }

    /**
     * Détacher la texture
     */
    protected void unbindTexture() { texture = null; }

    // GETTERS & SETTERS //

    public Scene getScene() { return scene; }

    protected void setScene(Scene scene) { this.scene = scene; }

    public int getHeight() { return height; }

    protected void setHeight(int height) { this.height = height; }

    public int getWidth() { return width; }

    protected void setWidth(int width) { this.width = width; }

    public int getX() { return x; }

    protected void setX(int x) { this.x = x; }

    public int getY() { return y; }

    protected void setY(int y) { this.y = y; }

    public float[] getColor() { return color; }

    public EntityTexture getTexture() { return texture; }
}
