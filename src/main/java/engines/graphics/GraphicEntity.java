package engines.graphics;

import engines.kernel.EngineEntity;
import engines.kernel.Entity;

/**
 * Entité graphique
 */
public class GraphicEntity extends EngineEntity {
    /**
     * Position x
     */
    private int x;

    /**
     * Position y
     */
    private int y;

    /**
     * Hauteur
     */
    private int height;

    /**
     * Largeur
     */
    private int width;

    /**
     * Scène
     */
    protected Scene scene;

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
     * @param parent parent
     */
    protected GraphicEntity(Entity parent) {
        super(parent);
    }

    /**
     * Constructeur par clonage
     * @param clone clone
     */
    private GraphicEntity(GraphicEntity clone) {
        this.scene = null;
        this.color = clone.color;
        if (clone.texture != null)
            this.texture = clone.texture.clone();
    }

    /**
     * Cloner l'entité
     * @return clone
     */
    protected GraphicEntity clone() {
        return new GraphicEntity(this);
    }

    // GETTERS & SETTERS //

    public int getX() { return x; }

    protected void setX(int x) { this.x = x; }

    public int getY() { return y; }

    protected void setY(int y) { this.y = y; }

    public int getHeight() { return height; }

    protected void setHeight(int height) { this.height = height; }

    public int getWidth() { return width; }

    protected void setWidth(int width) { this.width = width; }

    public Scene getScene() { return scene; }

    protected void setScene(Scene scene) { this.scene = scene; }

    public Color getColor() { return color; }

    protected void setColor(Color color) { this.color = color; }

    public Cover getTexture() { return texture; }

    protected void setTexture(Cover texture) { this.texture = texture; }
}
