package engines.physics;

import engines.graphics.GraphicEntity;
import engines.kernel.Engine;

import java.util.HashMap;
import java.util.Map;

/**
 * Moteur physique
 */
public class PhysicsEngine implements Engine<PhysicEntity> {
    /**
     * Liste des entités physiques
     */
    private static final HashMap<Integer, PhysicEntity> entities = new HashMap<>();

    /**
     * Matrice
     */
    private static PhysicEntity[][] matrix;

    /**
     * Instance unique
     */
    private static PhysicsEngine instance;

    /**
     * Constructeur privé
     */
    private PhysicsEngine() {}

    /**
     * Récupérer l'instance
     * @return instance
     */
    public static PhysicsEngine getInstance() {
        if (instance == null) instance = new PhysicsEngine();
        return instance;
    }

    /**
     * Vérifier s'il y a une collision
     * @param o1 entité physique 1
     * @param o2 entité physique 2
     * @return booléen
     */
    /*public boolean isCollision(int id1, int id2) {
        PhysicsEntity o1 = id_objects.get(id1);
        PhysicsEntity o2 = id_objects.get(id2);
        return o1.getX() == o2.getX() && o1.getY() == o2.getY();
    }*/

    /**
     * Vérifier qu'il n'y a pas d'objet à la position (x,y)
     * @param x Position x à vérifier
     * @param y Position y à vérifier
     * @return booléen
     */
    public static boolean isEntityPresent(int x, int y) {
        return matrix[x][y] == null;
    }

    /**
     * Teste de collision entre les hitbox des entités id1 et id2
     * @param id1 identifiant de l'entité 1
     * @param id2 identifiant de l'entité 2
     * @return s'il y a collision ou non
     */
    public static boolean isCollision(int id1, int id2) {
        PhysicEntity o1 = entities.get(id1);
        PhysicEntity o2 = entities.get(id2);

        if((o2.getX() >= o1.getX() +  o1.getWidth())      // trop à droite
                || (o2.getX() + o2.getWidth() <= o1.getX()) // trop à gauche
                || (o2.getY() >= o1.getY() + o1.getHeight()) // trop en bas
                || (o2.getY() + o2.getHeight() <= o1.getHeight()))  // trop en haut
            return false;
        else
            return true;
    }
    /**
     * Déplacement vers le haut
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public static void goUp(int id, int mul) {
        // S'il n'y a pas d'objets à la position où l'on souhaite se déplacer
        PhysicEntity o = entities.get(id);
        if (!isEntityPresent(o.getX(),o.getY() - mul))
            o.setY(o.getY() - mul);
    }

    /**
     * Déplacement vers la droite
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public static void goRight(int id, int mul) {
        PhysicEntity o = entities.get(id);
        // S'il n'y a pas d'objets à la position où l'on souhaite se déplacer
        if (!isEntityPresent(o.getX() + mul,o.getY()))
            o.setX(o.getX() + mul);
    }

    /**
     * Déplacement vers la gauche
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public static void goLeft(int id, int mul) {
        PhysicEntity o = entities.get(id);
        // S'il n'y a pas d'objets à la position où l'on souhaite se déplacer
        if (!isEntityPresent(o.getX() - mul,o.getY()))
            o.setX(o.getX() - mul);
    }

    /**
     * Déplacement vers le bas
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public static void goDown(int id, int mul) {
        PhysicEntity o = entities.get(id);
        // S'il n'y a pas d'objets à la position où l'on souhaite se déplacer
        if (!isEntityPresent(o.getX(),o.getY() + mul))
            o.setY(o.getY() + mul);
    }


    /**
     * Déplacement de l'entité physique à la position indiquée
     * @param id identifiant de l'entité physique
     * @param x position x
     * @param y position y
     */
    public static void goTo(int id, int x, int y) {
        PhysicEntity o = entities.get(id);
        o.setX(x);
        o.setY(y);
    }

    /**
     * Modification de la  vitesse de déplacement
     * @param id identifiant de l'entité physique
     * @param speed vitesse
     */
    public static void setSpeed(int id, int speed) {
        PhysicEntity o = entities.get(id);
        o.setSpeed(speed);
    }

    /**
     * Obtenir les limites
     * @param id identifiant
     */
    public static int[] getBounds(int id) {
        return entities.get(id).getBounds();
    }

    @Override
    public PhysicEntity createEntity(int id) {
        PhysicEntity entity = new PhysicEntity(id);
        entities.put(id, entity);
        return entity;
    }

    // GETTERS //

    @Override
    public Map<Integer, PhysicEntity> getEntities() { return entities; }

    @Override
    public PhysicEntity getEntity(int id) { return entities.get(id); }
}
