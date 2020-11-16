package engines.physics;

import engines.kernel.EngineEntity;
import engines.kernel.Entity;

import java.util.ArrayList;

/**
 * Entité physique
 */
public class PhysicEntity extends EngineEntity {
    /**
     * Liste des entités en collision avec celle-ci
     */
    private ArrayList<Integer> collisions = new ArrayList<>();

    /**
     * Limites de déplacement
     */
    private int[] boundLimits = new int[4];

    /**
     * Vitesse de déplacement
     */
    private int speed;

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
        this.collisions = clone.collisions;
        this.speed = clone.speed;
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
}
