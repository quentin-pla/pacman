package engines.physics;

import engines.kernel.Entity;
import engines.kernel.KernelEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Moteur physique
 */
public class PhysicsEngine {
    /**
     * Liste des entités physiques
     */
    private static final HashMap<Integer, PhysicEntity> entities = new HashMap<>();

    /**
     * Liste des évènements liés aux collisions en entrée
     */
    private static final Map<PhysicEntity[], String> collisionsEvents = new HashMap<>();

    /**
     * Liste des évènements lorsque deux entités sont centrées
     */
    private static final Map<PhysicEntity[], String> centeredEvents = new HashMap<>();

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
            if (isInCollision(entity) || !isInBounds(entity)) {
                PhysicsEngine.move(entity, entity.getLastX(), entity.getLastY());
                entity.setColliding(true);
            }
            else entity.setColliding(false);
        }
        for (Map.Entry<PhysicEntity[],String> event : collisionsEvents.entrySet()) {
            PhysicEntity e1 = event.getKey()[0];
            PhysicEntity e2 = event.getKey()[1];
            if ((e2 == null && e1.isColliding())
                    || (e2 != null && e1.isColliding() && e2.isColliding())
                    || (e2 != null && isInCollision(e1, e2)))
                KernelEngine.notifyEvent(event.getValue());
        }
        for (Map.Entry<PhysicEntity[],String> event : centeredEvents.entrySet()) {
            if (isCentered(event.getKey()[0],event.getKey()[1]))
                KernelEngine.notifyEvent(event.getValue());
        }
    }

    /**
     * Ajouter des collisions entre deux entités
     * @param entity1 entité 1
     * @param entity2 entité 2
     */
    public static void addCollisions(PhysicEntity entity1, PhysicEntity entity2) {
        entity1.getCollisions().add(entity2);
        entity2.getCollisions().add(entity1);
    }

    /**
     * Ajouter des limites de déplacement
     * @param entity entité
     */
    public static void addBoundLimits(PhysicEntity entity, int x1, int y1, int x2, int y2) {
        entity.setBoundLimits(new int[]{x1,y1,x2,y2});
    }

    /**
     * Attacher un évènement lorsqu'une entité entre en collision
     * @param entity entité
     * @param eventName nom de l'évènement
     */
    public static void bindEventOnCollision(PhysicEntity entity, String eventName) {
        collisionsEvents.put(new PhysicEntity[]{entity, null}, eventName);
    }

    /**
     * Attacher un évènement lorsque deux entités entrent en collision
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @param eventName nom de l'évènement
     */
    public static void bindEventOnCollision(PhysicEntity entity1, PhysicEntity entity2, String eventName) {
        collisionsEvents.put(new PhysicEntity[]{entity1, entity2}, eventName);
    }

    /**
     * Attacher un évènement à lorsque deux entités sont sur la même position
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @param eventName nom de l'évènement
     */
    public static void bindEventOnSameLocation(PhysicEntity entity1, PhysicEntity entity2, String eventName) {
        centeredEvents.put(new PhysicEntity[]{entity1, entity2}, eventName);
    }

    /**
     * Vérifier si une entité est en collision
     * @param entity entité
     * @return s'il y a une collision
     */
    public static boolean isInCollision(PhysicEntity entity) {
        for (PhysicEntity collision : entity.getCollisions())
            if (isInCollision(collision,entity))
                return true;
        return false;
    }

    /**
     * Vérifier s'il y a une collision entre deux entités
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @return s'il y a collision
     */
    public static boolean isInCollision(PhysicEntity entity1, PhysicEntity entity2) {
        return entity1.getX() + entity1.getWidth() > entity2.getX()
            && entity1.getY() + entity1.getHeight() > entity2.getY()
            && entity1.getX() < entity2.getX() + entity2.getWidth()
            && entity1.getY() < entity2.getY() + entity2.getHeight();
    }

    /**
     * Vérifier si une entité est comprise dans une autre entité
     * @param childE entité enfant
     * @param parentE entité parente
     * @return si l'enfant est dans le parent
     */
    public static boolean isInside(PhysicEntity childE, PhysicEntity parentE) {
        return childE.getX() > parentE.getX()
            && childE.getY() > parentE.getY()
            && childE.getX() + childE.getWidth() < parentE.getX() + parentE.getWidth()
            && childE.getY() + childE.getHeight() < parentE.getY() + parentE.getHeight();
    }

    /**
     * Vérifier si une entité est bien dans ses limites de déplacement
     * @param entity entité
     * @return si l'entité respecte ses limites de déplacement
     */
    public static boolean isInBounds(PhysicEntity entity) {
        int[] boundLimits = entity.getBoundLimits();
        return boundLimits == null || entity.getX() >= boundLimits[0]
            && entity.getX() + entity.getWidth() <= boundLimits[2]
            && entity.getY() >= boundLimits[1]
            && entity.getY() + entity.getHeight() <= boundLimits[3];
    }

    /**
     * Vérifier si deux entités sont à la même position
     * @param e1 entité 1
     * @param e2 entité 2
     * @return si les entités sont à la même position
     */
    public static boolean isCentered(PhysicEntity e1, PhysicEntity e2) {
        return (e1.getX() + e1.getWidth()) / 2 == (e2.getX() + e2.getWidth()) / 2
            && (e1.getY() + e1.getHeight()) / 2 == (e2.getY() + e2.getHeight()) / 2;
    }

    /**
     * Déplacement vers le haut
     * @param entity entité physique
     */
    public static void goUp(PhysicEntity entity) { PhysicsEngine.translate(entity, 0, -entity.getSpeed()); }

    /**
     * Déplacement vers la droite
     * @param entity entité physique
     */
    public static void goRight(PhysicEntity entity) { PhysicsEngine.translate(entity, entity.getSpeed(), 0); }

    /**
     * Déplacement vers la gauche
     * @param entity entité physique
     */
    public static void goLeft(PhysicEntity entity) {
        PhysicsEngine.translate(entity, -entity.getSpeed(), 0);
    }

    /**
     * Déplacement vers le bas
     * @param entity entité physique
     */
    public static void goDown(PhysicEntity entity) {
        PhysicsEngine.translate(entity, 0, entity.getSpeed());
    }

    /**
     * Déplacement de l'entité physique à la position indiquée
     * @param entity entité physique
     * @param x position x
     * @param y position y
     */
    public static void move(PhysicEntity entity, int x, int y) {
        entity.setLastX(entity.getX());
        entity.setLastY(entity.getY());
        entity.setX(x);
        entity.setY(y);
    }

    /**
     * Translater une entité physique
     * @param entity entité
     * @param x position horizontale
     * @param y position verticale
     */
    public static void translate(PhysicEntity entity, int x, int y) {
        entity.setLastX(entity.getX());
        entity.setLastY(entity.getY());
        entity.setX(entity.getX() + x);
        entity.setY(entity.getY() + y);
    }

    /**
     * Redimensionne une entité graphique
     *
     * @param entity entité à redimensionner
     * @param w largeur
     * @param h hauteur
     */
    public static void resize(PhysicEntity entity, int w, int h) {
        entity.setWidth(w);
        entity.setHeight(h);
    }

    /**
     * Redimensionne en hauteur d'une entité graphique
     * @param entity entité à redimensionner
     * @param h hauteur
     */
    public static void resizeHeight(PhysicEntity entity, int h) {
        entity.setHeight(h);
    }

    /**
     * Redimensionne en largeur d'une entité graphique
     * @param entity entité à redimensionner
     * @param w largeur
     */
    public static void resizeWidth(PhysicEntity entity, int w) {
        entity.setWidth(w);
    }

    /**
     * Modification de la  vitesse de déplacement
     * @param entity entité physique
     * @param speed vitesse
     */
    public static void setSpeed(PhysicEntity entity, int speed) {
        entity.setSpeed(speed);
    }

    /**
     * Créer une nouvelle entité
     * @param parent entité parente
     * @return entité physique
     */
    public PhysicEntity createEntity(Entity parent) {
        PhysicEntity entity = new PhysicEntity(parent);
        entities.put(parent.getId(), entity);
        return entity;
    }
}
