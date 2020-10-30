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
     * Position horizontale
     */
    protected int x;

    /**
     * Position verticale
     */
    protected int y;

    /**
     * Constructeur
     * @param x position horizontale
     * @param y position verticale
     */
    protected Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Dessiner l'entité
     */
    protected abstract void draw();

    /**
     * Effacer l'entité
     */
    protected void erase() {
        scene.removeEntity(this);
    }

    /**
     * Translater l'entité
     * @param x position horizontale
     * @param y position verticale
     */
    protected void translate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // GETTERS & SETTERS //

    public Scene getScene() {
        return scene;
    }

    protected void setScene(Scene scene) {
        this.scene = scene;
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
