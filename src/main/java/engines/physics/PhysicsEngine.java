package engines.physics;

import engines.kernel.Entity;

import java.util.Set;

/**
 * Moteur physique
 */
public class PhysicsEngine {

    private Set<PhysicsEntity> objects;
    private Entity[][] matrix;

    /**
     * Constructeur
     */
    public PhysicsEngine(Set<PhysicsEntity> objects, Entity[][] matrix) {
        this.objects = objects;
        this.matrix = matrix;
    }

    /**
     * Vérifier s'il y a une collision
     * @param o1 entité physique 1
     * @param o2 entité physique 2
     * @return booléen
     */
    public boolean isCollision(PhysicsEntity o1, PhysicsEntity o2) {
        return o1.getX() == o2.getX() && o1.getY() == o2.getY();
    }


    /**
     * Vérifier qu'il n'y a pas d'objet à la position (x,y)
     * @param x Position x à vérifier
     * @param y Position y à vérifier
     * @return booléen
     */
    public boolean isCollision(int x, int y) {
        return  this.matrix[x][y].getId() > -1;
    }

    /**
     * Déplacement vers le haut
     * @param o entité physique
     * @param mul multiplicateur
     */
    public void goUp(PhysicsEntity o, int mul) {
        // S'il n'y a pas d'objets à la position où l'on souhaite se déplacer
        if (!isCollision(o.getX(),o.getY() - mul))
            o.setY(o.getY() - mul);
    }

    /**
     * Déplacement vers la droite
     * @param o entité physique
     * @param mul multiplicateur
     */
    public void goRight(PhysicsEntity o, int mul) {
        // S'il n'y a pas d'objets à la position où l'on souhaite se déplacer
        if (!isCollision(o.getX() + mul,o.getY()))
            o.setX(o.getX() + mul);
    }

    /**
     * Déplacement vers la gauche
     * @param o entité physique
     * @param mul multiplicateur
     */
    public void goLeft(PhysicsEntity o, int mul) {
        // S'il n'y a pas d'objets à la position où l'on souhaite se déplacer
        if (!isCollision(o.getX() - mul,o.getY()))
            o.setX(o.getX() - mul);
    }

    /**
     * Déplacement vers le bas
     * @param o entité physique
     * @param mul multiplicateur
     */
    public void goDown(PhysicsEntity o, int mul) {
        // S'il n'y a pas d'objets à la position où l'on souhaite se déplacer
        if (!isCollision(o.getX(),o.getY() + mul))
            o.setY(o.getY() + mul);
    }
}
