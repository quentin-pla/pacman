package engines.kernel;

import engines.graphics.GraphicEntity;
import engines.physics.PhysicEntity;

/**
 * Entité
 */
public class Entity {
    /**
     * Identifiant de l'entité
     */
    protected int id;

    /**
     * Moteur noyau
     */
    protected KernelEngine kernelEngine;

    /**
     * Entité graphique
     */
    protected GraphicEntity graphicEntity;

    /**
     * Entité physique
     */
    protected PhysicEntity physicEntity;

    /**
     * Constructeur
     */
    protected Entity(KernelEngine kernelEngine) {
        this.kernelEngine = kernelEngine;
        this.id = kernelEngine.generateNewID();
        this.graphicEntity = kernelEngine.getGraphicsEngine().createEntity(this);
        this.physicEntity = kernelEngine.getPhysicsEngine().createEntity(this);
    }

    // GETTERS & SETTERS //

    public int getId() { return id; }

    protected void setId(int id) { this.id = id; }

    public GraphicEntity getGraphicEntity() { return graphicEntity; }

    public PhysicEntity getPhysicEntity() { return physicEntity; }

    public void setGraphicEntity(GraphicEntity graphicEntity) { this.graphicEntity = graphicEntity; }

    public void setPhysicEntity(PhysicEntity physicEntity) { this.physicEntity = physicEntity; }
}
