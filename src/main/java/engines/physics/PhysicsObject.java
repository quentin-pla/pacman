package engines.physics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Objet physique
 */
public class PhysicsObject implements PhysicsEntity {

    private int x, y;
    private int speed;


    /**
     * Constructeur
     */
    protected PhysicsObject(int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    /**
     * Cloner l'entité
     * @return
     */
    public PhysicsObject clone() {
        return new PhysicsObject(this.x, this.y, this.speed);
    }

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
