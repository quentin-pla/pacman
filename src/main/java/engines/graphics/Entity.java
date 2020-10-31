package engines.graphics;

import engines.graphics.api.Renderer;

/**
 * Entité
 */
public abstract class Entity extends Renderer {
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
     * Constructeur avec dimensions
     * @param height hauteur
     * @param width largeur
     * @param x position horizontale
     * @param y position verticale
     */
    protected Entity(int height, int width, int x, int y) {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
    }

    /**
     * Constructeur avec taille
     * @param size taille
     * @param x position horizontale
     * @param y position verticale
     */
    protected Entity(int size, int x, int y) {
        this(size, size, x, y);
    }

    /**
     * Dessiner l'entité
     */
    protected abstract void draw();

    /**
     * Mettre à jour l'entité
     */
    protected abstract void update();

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
     * Redimensionner l'entité
     * @param size taille
     */
    protected void resize(int size) {
        resize(size, size);
    }

    // GETTERS & SETTERS //

    public Scene getScene() {
        return scene;
    }

    protected void setScene(Scene scene) {
        this.scene = scene;
    }

    public int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    protected void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    protected void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    protected void setY(int y) {
        this.y = y;
    }
}
