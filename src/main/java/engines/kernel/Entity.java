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
     * Position x
     */
    protected int x;

    /**
     * Position y
     */
    protected int y;

    /**
     * Hauteur
     */
    protected int height;

    /**
     * Largeur
     */
    protected int width;

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
        x = 0;
        y = 0;
        height = 0;
        width = 0;
        graphicEntity = GraphicsEngine.getInstance().createEntity(this);
        physicEntity = PhysicsEngine.getInstance().createEntity(this);
        ioEntity = IOEngine.getInstance().createEntity(this);
    }

    // GETTERS & SETTERS //

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getX() { return x; }

    public void setX(int x) { this.x = x; }

    public int getY() { return y; }

    public void setY(int y) { this.y = y; }

    public int getHeight() { return height; }

    public void setHeight(int height) { this.height = height; }

    public int getWidth() { return width; }

    public void setWidth(int width) { this.width = width; }
}
