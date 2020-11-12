package engines.physics;

import engines.kernel.Entity;

/**
 * Entité physique
 */
public class PhysicEntity extends Entity {
    /**
     * Position
     */
    private int x, y;

    /**
     * Largeur
     */
    private int width;

    /**
     * Hauteur
     */
    private int height;

    /**
     * Vitesse de déplacement
     */
    private int speed;

    /**
     * Constructeur
     */
    protected PhysicEntity(int id) {
        super(id);
    }

    /**
     * Constructeur
     */
    protected PhysicEntity(int x, int y, int height, int width, int speed) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.speed = speed;
    }

    /**
     * Constructeur par clonage
     * @param clone entité physique
     */
    private PhysicEntity(PhysicEntity clone) {
        this(clone.x, clone.y, clone.height, clone.width, clone.speed);
    }

    /**
     * Cloner l'entité
     * @return clone
     */
    public PhysicEntity clone() {
        return new PhysicEntity(this);
    }

    // GETTERS & SETTERS //

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int[] getBounds() { return new int[]{x, y, x + width, y + height}; }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
