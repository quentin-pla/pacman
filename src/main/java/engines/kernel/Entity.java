package engines.kernel;

import engines.graphics.GraphicEntity;
import engines.graphics.GraphicsEngine;
import engines.input_output.IOEngine;
import engines.input_output.IOEntity;
import engines.physics.PhysicEntity;
import engines.physics.PhysicsEngine;

/**
 * Entité
 */
public class Entity {
    /**
     * Identifiant de l'entité
     */
    protected int id;

    /**
     * Entité graphique
     */
    protected GraphicEntity graphicEntity;

    /**
     * Entité physique
     */
    protected PhysicEntity physicEntity;

    /**
     * Entité entrées / sorties
     */
    protected IOEntity ioEntity;

    /**
     * Constructeur
     */
    protected Entity() {
        id = KernelEngine.generateNewID();
        graphicEntity = GraphicsEngine.getInstance().createEntity(this);
        physicEntity = PhysicsEngine.getInstance().createEntity(this);
        //ioEntity = IOEngine.getInstance().createEntity(this);
    }

    // GETTERS & SETTERS //

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public GraphicEntity getGraphicEntity() { return graphicEntity; }

    public PhysicEntity getPhysicEntity() { return physicEntity; }

    public IOEntity getIoEntity() { return ioEntity; }
}
