package engines.physics;

/**
 * Entité physique
 */
public class PhysicsEntity {
    /**
     * Position
     */
    private int x, y;

    /**
     * Vitesse de déplacement
     */
    private int speed;

    /**
     * Constructeur
     */
    protected PhysicsEntity(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    /**
     * Cloner l'entité
     * @return
     */
    public PhysicsEntity clone() {
        return new PhysicsEntity(this.x, this.y, this.speed);
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


}
