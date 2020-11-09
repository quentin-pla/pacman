package engines.physics;

import engines.kernel.Entity;

import java.util.ArrayList;
import java.util.Set;

public class PhysicsEngine {

    private ArrayList<PhysicsObject> objects;
    private Entity[][] matrix;

    public PhysicsEngine(ArrayList<PhysicsObject> objects, Entity[][] matrix) {
        this.objects = objects;
        this.matrix = matrix;
    }

    public boolean isCollision(PhysicsObject o1, PhysicsObject o2) {
        return o1.getX() == o2.getX() && o1.getY() == o2.getY();
    }

    public boolean isCollision(int x1, int y1, int x2, int y2) {
        return x1 == x2 && y1 == y2;
    }


    public boolean isCollision(int x, int y) {
        if (this.matrix[x][y] > -1) return true;
    }

    public void goUp(PhysicsObject o, int mul) {

        if (!isCollision(o.getX(), o.getY()-mul))
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
