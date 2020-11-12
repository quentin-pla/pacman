package engines.kernel;

/**
 * Entité
 */
public abstract class Entity {
    /**
     * Identifiant de l'entité
     */
    protected int id;

    /**
     * Constructeur
     */
    protected Entity() {}

    /**
     * Constructeur surchargé
     * @param id identifiant
     */
    protected Entity(int id) {
        this.id = id;
    }

    // GETTERS & SETTERS //

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }
}
