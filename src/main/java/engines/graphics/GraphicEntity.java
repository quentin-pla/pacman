package engines.graphics;

import engines.kernel.EngineEntity;
import engines.kernel.Entity;

/**
 * Entité graphique
 */
public class GraphicEntity extends EngineEntity {
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

    public int getX() { return parent.getPhysicEntity().getX(); }

    public int getY() { return parent.getPhysicEntity().getY(); }

    public int getHeight() { return parent.getPhysicEntity().getHeight(); }

    public int getWidth() { return parent.getPhysicEntity().getWidth(); }

    public Scene getScene() {
        return scene;
    }

    protected void setScene(Scene scene) {
        this.scene = scene;
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
