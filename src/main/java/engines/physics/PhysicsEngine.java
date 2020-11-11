package engines.physics;

import engines.graphics.GraphicEntity;
import engines.kernel.Entity;

import java.util.HashMap;
import java.util.Set;

/**
 * Moteur physique
 */
public class PhysicsEngine {

    private Set<PhysicsEntity> objects;
    private HashMap<Integer, PhysicsEntity> id_objects;
    private Entity[][] matrix;

    /**
     * Constructeur
     */
    public PhysicsEngine(HashMap<Integer, PhysicsEntity> id_objects, Entity[][] matrix) {
        this.id_objects = id_objects;
        this.objects.addAll(this.id_objects.values());
        this.matrix = matrix;
    }

    /**
     * Vérifier s'il y a une collision
     * @param o1 entité physique 1
     * @param o2 entité physique 2
     * @return booléen
     */

    /*public boolean isCollision(int id1, int id2) {
        PhysicsEntity o1 = this.id_objects.get(id1);
        PhysicsEntity o2 = this.id_objects.get(id2);
        return o1.getX() == o2.getX() && o1.getY() == o2.getY();
    }*/

    /**
     * Vérifier qu'il n'y a pas d'objet à la position (x,y)
     * @param x Position x à vérifier
     * @param y Position y à vérifier
     * @return booléen
     */
    public boolean isCollision(int x, int y) {
        return this.matrix[x][y].getId() > -1;
    }

    /**
     * Déplacement vers le haut
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public void goUp(int id, int mul) {
        // S'il n'y a pas d'objets à la position où l'on souhaite se déplacer
        PhysicsEntity o = this.id_objects.get(id);
        if (!isCollision(o.getX(),o.getY() - mul))
            o.setY(o.getY() - mul);
    }

    /**
     * Déplacement vers la droite
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public void goRight(int id, int mul) {
        PhysicsEntity o = this.id_objects.get(id);
        // S'il n'y a pas d'objets à la position où l'on souhaite se déplacer
        if (!isCollision(o.getX() + mul,o.getY()))
            o.setX(o.getX() + mul);
    }

    /**
     * Déplacement vers la gauche
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public void goLeft(int id, int mul) {
        PhysicsEntity o = this.id_objects.get(id);
        // S'il n'y a pas d'objets à la position où l'on souhaite se déplacer
        if (!isCollision(o.getX() - mul,o.getY()))
            o.setX(o.getX() - mul);
    }

    /**
     * Déplacement vers le bas
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public void goDown(int id, int mul) {
        PhysicsEntity o = this.id_objects.get(id);
        // S'il n'y a pas d'objets à la position où l'on souhaite se déplacer
        if (!isCollision(o.getX(),o.getY() + mul))
            o.setY(o.getY() + mul);
    }


    /**
     * Déplacement de l'entité physique à la position indiquée
     * @param id identifiant de l'entité physique
     * @param x position x
     * @param y position y
     */
    public void goTo(int id, int x, int y) {
        PhysicsEntity o = this.id_objects.get(id);
        o.setX(x);
        o.setY(y);
    }

    /**
     * Modification de la  vitesse de déplacement
     * @param id identifiant de l'entité physique
     * @param speed vitesse
     */
    public void setSpeed(int id, int speed) {
        PhysicsEntity o = this.id_objects.get(id);
        o.setSpeed(speed);
    }

    /**
     * Ajout d'une nouvelle entité physique à la liste des objets physiques
     * @param o entité physique
     */
    public void addNewPhysicalEntity(PhysicsEntity o) {
        this.objects.add(o);
    }

    /**
     * Ajout d'une nouvelle entité physique à la liste des objets physiques
     * @param objects Liste d'entités physiques
     */
    public void addNewPhysicalEntity(Set<PhysicsEntity> objects) {
        this.objects.addAll(objects);
    }

    /**
     * Ajout d'une nouvelle entité physique à la liste des objets physiques
     * @param x position x de l'entité
     * @param y position y de l'entité
     * @param speed vitesse de déplacement de l'entité
     */
    public void addNewPhysicalEntity(int x, int y, int speed) {
        this.objects.add(new PhysicsEntity(x, y, speed));
    }
}
