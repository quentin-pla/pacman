package engines.physics;

import engines.kernel.EngineEntity;
import engines.kernel.Entity;

import java.util.ArrayList;

/**
 * Entité physique
 */
public class PhysicEntity extends EngineEntity {
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
     * Liste des entités pouvant être en collision avec celle-ci
     */
    private ArrayList<PhysicEntity> collisions;

    /**
     * Entité en collision ou pas
     */
    private boolean isColliding;

    /**
     * Limites de déplacement
     */
    private int[] boundLimits;

    /**
     * Vitesse de déplacement
     */
    private int speed;

    /**
     * Dernière position horizontale
     */
    private int lastX;

    /**
     * Dernière position verticale
     */
    private int lastY;

    /**
     * Constructeur
     */
    protected PhysicEntity(Entity parent) {
        super(parent);
        x = 0;
        y = 0;
        height = 0;
        width = 0;
        collisions = new ArrayList<>();
        speed = 1;
        lastX = 0;
        lastY = 0;
    }

    /**
     * Cloner l'entité
     */
    public void clone(PhysicEntity entity) {
        collisions = new ArrayList<>(entity.collisions);
        boundLimits = entity.boundLimits;
        speed = entity.speed;
        lastX = entity.x;
        lastY = entity.y;
        x = entity.x;
        y = entity.y;
        height = entity.height;
        width = entity.width;
    }

    // GETTERS & SETTERS //

    public int getSpeed() { return speed; }

    protected void setSpeed(int speed) { this.speed = speed; }

    public int[] getBounds() { return new int[]{x, y, x + width, y + height}; }

    public ArrayList<PhysicEntity> getCollisions() { return collisions; }

    public int[] getBoundLimits() { return boundLimits; }

    protected void setBoundLimits(int[] boundLimits) { this.boundLimits = boundLimits; }

    public int getX() { return x; }

    protected void setX(int x) { this.x = x; }

    public int getY() { return y; }

    protected void setY(int y) { this.y = y; }

    public int getHeight() { return height; }

    protected void setHeight(int height) { this.height = height; }

    public int getWidth() { return width; }

    protected void setWidth(int width) { this.width = width; }

    public int getLastX() { return lastX; }

    protected void setLastX(int lastX) { this.lastX = lastX; }

    public int getLastY() { return lastY; }

    protected void setLastY(int lastY) { this.lastY = lastY; }

    public boolean isColliding() { return isColliding; }

    public void setColliding(boolean colliding) { isColliding = colliding; }
}
