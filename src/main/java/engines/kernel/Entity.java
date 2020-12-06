package engines.kernel;

import engines.AI.AIEntity;
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
     * Entité intelligence artificielle
     */
    protected AIEntity aiEntity;

    /**
     * Constructeur
     */
    protected Entity(KernelEngine kernelEngine) {
        this.kernelEngine = kernelEngine;
        this.id = kernelEngine.generateNewID();
        this.graphicEntity = kernelEngine.getGraphicsEngine().createEntity(this);
        this.physicEntity = kernelEngine.getPhysicsEngine().createEntity(this);
        this.aiEntity = kernelEngine.getAiEngine().createEntity(this);
        this.kernelEngine.getEntities().add(this);
    }

    /**
     * Constructeur par clonage
     * @param entity clone
     */
    private Entity(Entity entity) {
        this.kernelEngine = entity.kernelEngine;
        this.id = kernelEngine.generateNewID();
        this.graphicEntity = kernelEngine.getGraphicsEngine().createEntity(this);
        this.graphicEntity.clone(entity.getGraphicEntity());
        this.physicEntity = kernelEngine.getPhysicsEngine().createEntity(this);
        this.physicEntity.clone(entity.getPhysicEntity());
        this.aiEntity = kernelEngine.getAiEngine().createEntity(this);
        this.aiEntity.clone(entity.getAiEntity());
        this.kernelEngine.getEntities().add(this);
    }

    /**
     * Cloner une entité
     * @return clone
     */
    public Entity clone() {
        return new Entity(this);
    }

    // GETTERS & SETTERS //

    public int getId() { return id; }

    protected void setId(int id) { this.id = id; }

    public GraphicEntity getGraphicEntity() { return graphicEntity; }

    public PhysicEntity getPhysicEntity() { return physicEntity; }

    public void setGraphicEntity(GraphicEntity graphicEntity) { this.graphicEntity = graphicEntity; }

    public void setPhysicEntity(PhysicEntity physicEntity) { this.physicEntity = physicEntity; }

    public AIEntity getAiEntity() { return aiEntity; }

    public void setAiEntity(AIEntity aiEntity) { this.aiEntity = aiEntity; }
}
