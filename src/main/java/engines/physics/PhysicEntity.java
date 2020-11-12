package engines.physics;

import engines.kernel.EngineEntity;
import engines.kernel.Entity;

/**
 * Entité physique
 */

public class PhysicEntity extends EngineEntity {
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
}
