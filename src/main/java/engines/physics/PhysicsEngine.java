package engines.physics;

import engines.kernel.Engine;
import engines.kernel.Entity;
import engines.kernel.KernelEngine;

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
     * Liste des évènements liés aux collisions en entrée
     */
    private static final Map<Integer[], String> collisionsEvents = new HashMap<>();

    /**
     * Liste des évènements lorsque deux entités sont centrées
     */
    private static final Map<Integer[], String> centeredEvents = new HashMap<>();

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
     * Mettre à jour les entités physiques
     */
    public static void updateEntites() {
        for (PhysicEntity entity : entities.values()) {
            if (isInCollision(entity.getId()) || !isInBounds(entity.getId())) {
                PhysicsEngine.move(entity.getId(), entity.getLastX(), entity.getLastY());
                entity.setColliding(true);
            }
            else entity.setColliding(false);
        }
        for (Map.Entry<Integer[],String> event : collisionsEvents.entrySet()) {
            PhysicEntity e1 = entities.get(event.getKey()[0]);
            PhysicEntity e2 = event.getKey()[1] == null ? null : entities.get(event.getKey()[1]);
            if ((e2 == null && e1.isColliding())
                    || (e2 != null && e1.isColliding() && e2.isColliding())
                    || (e2 != null && isInCollision(event.getKey()[0], event.getKey()[1])))
                KernelEngine.notifyEvent(event.getValue());
        }
        for (Map.Entry<Integer[],String> event : centeredEvents.entrySet()) {
            if (isCentered(event.getKey()[0],event.getKey()[1]))
                KernelEngine.notifyEvent(event.getValue());
        }
    }

    /**
     * Ajouter des collisions entre deux entités
     * @param id1 identifiant entité 1
     * @param id2 identifiant entité 2
     */
    public static void addCollisions(int id1, int id2) {
        entities.get(id1).getCollisions().add(id2);
        entities.get(id2).getCollisions().add(id1);
    }

    /**
     * Ajouter des limites de déplacement
     * @param id identifiant de l'entité
     */
    public static void addBoundLimits(int id, int x1, int y1, int x2, int y2) {
        entities.get(id).setBoundLimits(new int[]{x1,y1,x2,y2});
    }

    /**
     * Attacher un évènement lorsqu'une entité entre en collision
     * @param id identifiant de l'entité
     * @param eventName nom de l'évènement
     */
    public static void bindEventOnCollision(int id, String eventName) {
        collisionsEvents.put(new Integer[]{id, null}, eventName);
    }

    /**
     * Attacher un évènement lorsque deux entités entrent en collision
     * @param id1 identifiant de l'entité 1
     * @param id2 identifiant de l'entité 2
     * @param eventName nom de l'évènement
     */
    public static void bindEventOnCollision(int id1, int id2, String eventName) {
        collisionsEvents.put(new Integer[]{id1, id2}, eventName);
    }

    /**
     * Attacher un évènement à lorsque deux entités sont sur la même position
     * @param id1 identifiant de l'entité 1
     * @param id2 identifiant de l'entité 2
     * @param eventName nom de l'évènement
     */
    public static void bindEventOnSameLocation(int id1, int id2, String eventName) {
        centeredEvents.put(new Integer[]{id1, id2}, eventName);
    }

    /**
     * Vérifier si une entité est en collision
     * @param id identifiant de l'entité
     * @return s'il y a une collision
     */
    public static boolean isInCollision(int id) {
        PhysicEntity o = entities.get(id);
        for (int collisionID : o.getCollisions())
            if (isInCollision(id,collisionID))
                return true;
        return false;
    }

    /**
     * Vérifier s'il y a une collision entre deux entités
     * @param id1 identifiant de l'entité 1
     * @param id2 identifiant de l'entité 2
     * @return s'il y a collision
     */
    public static boolean isInCollision(int id1, int id2) {
        PhysicEntity o1 = entities.get(id1);
        PhysicEntity o2 = entities.get(id2);
        return o1.getX() + o1.getWidth() > o2.getX()
            && o1.getY() + o1.getHeight() > o2.getY()
            && o1.getX() < o2.getX() + o2.getWidth()
            && o1.getY() < o2.getY() + o2.getHeight();
    }

    /**
     * Vérifier si une entité est comprise dans une autre entité
     * @param childID identifiant de l'entité enfant
     * @param parentID identifiant de l'entité parente
     * @return si l'enfant est dans le parent
     */
    public static boolean isInside(int childID, int parentID) {
        PhysicEntity child = entities.get(childID);
        PhysicEntity parent = entities.get(parentID);
        return ((child.getX() > parent.getX()
                && child.getY() > parent.getY())
            && ((child.getX() + child.getWidth() < parent.getX() + parent.getWidth()
                && child.getY() > parent.getY()))
            && ((child.getX() > parent.getX()
                && child.getY() + child.getHeight() < parent.getY() + parent.getHeight()))
            && ((child.getX() + child.getWidth() < parent.getX() + parent.getWidth()
                && child.getY() + child.getHeight() < parent.getY() + parent.getHeight())));
    }

    /**
     * Vérifier si une entité est bien dans ses limites de déplacement
     * @param id identifiant
     * @return si l'entité respecte ses limites de déplacement
     */
    public static boolean isInBounds(int id) {
        PhysicEntity entity = entities.get(id);
        int[] boundLimits = entity.getBoundLimits();
        return boundLimits == null || entity.getX() >= boundLimits[0]
            && entity.getX() + entity.getWidth() <= boundLimits[2]
            && entity.getY() >= boundLimits[1]
            && entity.getY() + entity.getHeight() <= boundLimits[3];
    }

    /**
     * Vérifier si deux entités sont à la même position
     * @param id1 identifiant entité 1
     * @param id2 identifiant entité 2
     * @return si les entités sont à la même position
     */
    public static boolean isCentered(int id1, int id2) {
        PhysicEntity e1 = entities.get(id1);
        PhysicEntity e2 = entities.get(id2);
        return (e1.getX() + e1.getWidth()) / 2 == (e2.getX() + e2.getWidth()) / 2
            && (e1.getY() + e1.getHeight()) / 2 == (e2.getY() + e2.getHeight()) / 2;
    }

    /**
     * Déplacement vers le haut
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public static void goUp(int id, int mul) {
        PhysicsEngine.translate(id, 0, -mul);
    }

    /**
     * Déplacement vers la droite
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public static void goRight(int id, int mul) {
        PhysicsEngine.translate(id, mul, 0);
    }

    /**
     * Déplacement vers la gauche
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public static void goLeft(int id, int mul) {
        PhysicsEngine.translate(id, -mul, 0);
    }

    /**
     * Déplacement vers le bas
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public static void goDown(int id, int mul) {
        PhysicsEngine.translate(id, 0, mul);
    }

    /**
     * Déplacement de l'entité physique à la position indiquée
     * @param id identifiant de l'entité physique
     * @param x position x
     * @param y position y
     */
    public static void move(int id, int x, int y) {
        PhysicEntity o = entities.get(id);
        o.setLastX(o.getX());
        o.setLastY(o.getY());
        o.setX(x);
        o.setY(y);
    }

    /**
     * Translater une entité physique
     * @param id identifiant
     * @param x position horizontale
     * @param y position verticale
     */
    public static void translate(int id, int x, int y) {
        PhysicEntity o = entities.get(id);
        o.setLastX(o.getX());
        o.setLastY(o.getY());
        o.setX(o.getX() + x);
        o.setY(o.getY() + y);
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
    public PhysicEntity createEntity(Entity parent) {
        PhysicEntity entity = new PhysicEntity(parent);
        entities.put(parent.getId(), entity);
        return entity;
    }

    // GETTERS //

    @Override
    public Map<Integer, PhysicEntity> getEntities() { return entities; }

    @Override
    public PhysicEntity getEntity(int id) { return entities.get(id); }
}
