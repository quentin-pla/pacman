package engines.physics;

import engines.kernel.EngineEntity;
import engines.kernel.Entity;

import java.util.ArrayList;

/**
 * Entité physique
 */
public class PhysicEntity extends EngineEntity {
    /**
     * Liste des entités pouvant être en collision avec celle-ci
     */
    private ArrayList<Integer> collisions = new ArrayList<>();

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
    }

    /**
     * Constructeur par clonage
     * @param clone entité physique
     */
    private PhysicEntity(PhysicEntity clone) {
        this.isColliding = false;
        this.collisions = clone.collisions;
        this.boundLimits = clone.boundLimits;
        this.speed = clone.speed;
        this.lastX = getX();
        this.lastY = getY();
    }

    /**
     * Cloner l'entité
     * @return clone
     */
    public PhysicEntity clone() {
        return new PhysicEntity(this);
    }

    // GETTERS & SETTERS //

    @Override
    public Entity getParent() { return parent; }

    public int getSpeed() { return speed; }

    public void setSpeed(int speed) { this.speed = speed; }

    public int[] getBounds() { return new int[]{getX(), getY(), getX() + getWidth(), getY() + getHeight()}; }

    public ArrayList<Integer> getCollisions() { return collisions; }

    public int[] getBoundLimits() { return boundLimits; }

    public void setBoundLimits(int[] boundLimits) { this.boundLimits = boundLimits; }

    public int getLastX() { return lastX; }

    public int getLastY() { return lastY; }

    public void setLastX(int lastX) { this.lastX = lastX; }

    public void setLastY(int lastY) { this.lastY = lastY; }

    public boolean isColliding() { return isColliding; }

    public void setColliding(boolean colliding) { isColliding = colliding; }
}
