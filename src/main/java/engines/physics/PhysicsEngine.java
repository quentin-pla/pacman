package engines.physics;

import java.util.Set;

public class PhysicsEngine {

    public PhysicsEngine() {

    }

    public boolean isCollision(PhysicsObject o1, PhysicsObject o2) {
        return o1.getX() == o2.getX() && o1.getY() == o2.getY();
    }

    public void goUp(PhysicsObject o, int mul) {
        o.setY(o.getY() - mul);
    }

    public void goRight(PhysicsObject o, int mul) {
        o.setX(o.getX() + mul);
    }

    public void goLeft(PhysicsObject o, int mul) {
        o.setX(o.getX() - mul);
    }

    public void goDown(PhysicsObject o, int mul) {
        o.setY(o.getY() + mul);
    }
}
