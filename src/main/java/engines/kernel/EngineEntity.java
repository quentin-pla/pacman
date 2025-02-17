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
    protected EngineEntity() {}

    /**
     * Constructeur
     * @param parent parent
     */
    protected EngineEntity(Entity parent) {
        this.parent = parent;
    }

    // GETTERS //

    public Entity getParent() { return parent; }

    protected void setParent(Entity parent) { this.parent = parent; }
}
