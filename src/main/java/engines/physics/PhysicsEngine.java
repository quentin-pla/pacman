package engines.physics;

/**
 * Moteur physique
 */
public class PhysicsEngine {
    /**
     * Constructeur
     */
    public PhysicsEngine() {}

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
     * Déplacement vers le haut
     * @param o entité physique
     * @param mul multiplicateur
     */
    public void goUp(PhysicsEntity o, int mul) {
        o.setY(o.getY() - mul);
    }

    /**
     * Déplacement vers la droite
     * @param o entité physique
     * @param mul multiplicateur
     */
    public void goRight(PhysicsEntity o, int mul) {
        o.setX(o.getX() + mul);
    }

    /**
     * Déplacement vers la gauche
     * @param o entité physique
     * @param mul multiplicateur
     */
    public void goLeft(PhysicsEntity o, int mul) {
        o.setX(o.getX() - mul);
    }

    /**
     * Déplacement vers le bas
     * @param o entité physique
     * @param mul multiplicateur
     */
    public void goDown(PhysicsEntity o, int mul) {
        o.setY(o.getY() + mul);
    }
}
