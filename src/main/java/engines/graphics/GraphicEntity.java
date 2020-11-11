package engines.graphics;

import engines.kernel.Entity;

/**
 * Entité graphique
 */
public class GraphicEntity extends Entity {
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
    protected GraphicEntity() {
        this.height = 0;
        this.width = 0;
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructeur par clonage
     * @param clone clone
     */
    private GraphicEntity(GraphicEntity clone) {
        this.height = clone.height;
        this.width = clone.width;
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
    public GraphicEntity clone() {
        return new GraphicEntity(this);
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

    public Color getColor() {
        return color;
    }

    protected void setColor(Color color) {
        this.color = color;
    }

    public Cover getTexture() {
        return texture;
    }

    protected void setTexture(Cover texture) {
        this.texture = texture;
    }
}
