package engines.kernel;

/**
 * Entité liée à un moteur
 */
public abstract class EngineEntity {
    /**
     * Parent
     */
    protected Entity parent;

    /**
     * Constructeur vide
     */
    public EngineEntity() {}

    /**
     * Constructeur
     * @param parent parent
     */
    public EngineEntity(Entity parent) {
        this.parent = parent;
    }

    // GETTERS & SETTERS //

    public Entity getParent() { return parent; }

    public void setParent(Entity parent) { this.parent = parent; }

    public int getId() { return parent.getId(); }

    public void setId(int id) { parent.setId(id); }

    public int getX() { return parent.getX(); }

    public void setX(int x) { parent.setX(x); }

    public int getY() { return parent.getY(); }

    public void setY(int y) { parent.setY(y); }

    public int getHeight() { return parent.getHeight(); }

    public void setHeight(int height) { parent.setHeight(height); }

    public int getWidth() { return parent.getWidth(); }

    public void setWidth(int width) { parent.setWidth(width); }
}
