package engines.physics;

import engines.kernel.Entity;
import engines.kernel.KernelEngine;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Moteur physique
 */
public class PhysicsEngine {
    /**
     * Moteur noyau
     */
    private KernelEngine kernelEngine;

    /**
     * Liste des entités physiques
     */
    private final ConcurrentMap<Integer, PhysicEntity> entities = new ConcurrentHashMap<>();

    /**
     * Liste des évènements liés aux collisions en entrée
     */
    private final ConcurrentMap<PhysicEntity[], String> collisionsEvents = new ConcurrentHashMap<>();

    /**
     * Liste des évènements lorsque deux entités sont centrées
     */
    private final ConcurrentMap<PhysicEntity[], String> centeredEvents = new ConcurrentHashMap<>();

    /**
     * Constructeur
     * @param kernelEngine moteur noyau
     */
    public PhysicsEngine(KernelEngine kernelEngine) {
        this.kernelEngine = kernelEngine;
    }

    /**
     * Mettre à jour les entités physiques
     */
    public void updateEntites() {
        for (PhysicEntity entity : entities.values()) {
            if (isInCollision(entity) || !isInBounds(entity)) {
                move(entity, entity.getLastX(), entity.getLastY());
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
                kernelEngine.notifyEvent(event.getValue());
        }
        for (Map.Entry<PhysicEntity[],String> event : centeredEvents.entrySet()) {
            if (isCentered(event.getKey()[0],event.getKey()[1]))
                kernelEngine.notifyEvent(event.getValue());
        }
    }

    /**
     * Ajouter des collisions entre deux entités
     * @param entity1 entité 1
     * @param entity2 entité 2
     */
    public void addCollisions(PhysicEntity entity1, PhysicEntity entity2) {
        entity1.getCollisions().add(entity2);
        entity2.getCollisions().add(entity1);
    }

    /**
     * Ajouter des limites de déplacement
     * @param entity entité
     * @param x1 coordonnée horizontale 1
     * @param x2 coordonnée horizontale 2
     * @param y1 coordonnée verticale 1
     * @param y2 coordonnée verticale 2
     */
    public void addBoundLimits(PhysicEntity entity, int x1, int y1, int x2, int y2) {
        entity.setBoundLimits(new int[]{x1,y1,x2,y2});
    }

    /**
     * Attacher un évènement lorsqu'une entité entre en collision
     * @param entity entité
     * @param eventName nom de l'évènement
     */
    public void bindEventOnCollision(PhysicEntity entity, String eventName) {
        collisionsEvents.put(new PhysicEntity[]{entity, null}, eventName);
    }

    /**
     * Attacher un évènement lorsque deux entités entrent en collision
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @param eventName nom de l'évènement
     */
    public void bindEventOnCollision(PhysicEntity entity1, PhysicEntity entity2, String eventName) {
        collisionsEvents.put(new PhysicEntity[]{entity1, entity2}, eventName);
    }

    /**
     * Attacher un évènement à lorsque deux entités sont sur la même position
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @param eventName nom de l'évènement
     */
    public void bindEventOnSameLocation(PhysicEntity entity1, PhysicEntity entity2, String eventName) {
        centeredEvents.put(new PhysicEntity[]{entity1, entity2}, eventName);
    }

    /**
     * Vérifier si une entité est en collision
     * @param entity entité
     * @return s'il y a une collision
     */
    public boolean isInCollision(PhysicEntity entity) {
        for (PhysicEntity collision : entity.getCollisions())
            if (isInCollision(collision,entity))
                return true;
        return false;
    }

    /**
     * Obtenir une entité à position spécifique
     * @param x position horizontale
     * @param y position verticale
     * @return id de l'entité sinon null
     */
    public ArrayList<PhysicEntity> getEntityAtPosition(int x, int y, int height, int width) {
        ArrayList<PhysicEntity> collidingEntities = new ArrayList<>();
        for (PhysicEntity entity : entities.values()) {
            if (x + width > entity.getX() && y + height > entity.getY()
                    && x < entity.getX() + entity.getWidth()
                    && y < entity.getY() + entity.getHeight())
                collidingEntities.add(entity);
        }
        return collidingEntities;
    }

    /**
     * Vérifier si une entité est présente en haut
     * @param entity entité
     * @return s'il y a une entité
     */
    public PhysicEntity isSomethingUp(PhysicEntity entity) {
        ArrayList<PhysicEntity> elements = getEntityAtPosition(entity.getX(), entity.getY() - 1, entity.getHeight(), entity.getWidth());
        for (PhysicEntity entity1 : elements)
            if (entity.getCollisions().contains(entity1))
                return entity1;
        return null;
    }

    /**
     * Vérifier si une entité est présente à droite
     * @param entity entité
     * @return s'il y a une entité
     */
    public PhysicEntity isSomethingRight(PhysicEntity entity) {
        ArrayList<PhysicEntity> elements = getEntityAtPosition(entity.getX() + 1, entity.getY(), entity.getHeight(), entity.getWidth());
        for (PhysicEntity entity1 : elements)
            if (entity.getCollisions().contains(entity1))
                return entity1;
        return null;
    }

    /**
     * Vérifier si une entité est présente en bas
     * @param entity entité
     * @return s'il y a une entité
     */
    public PhysicEntity isSomethingDown(PhysicEntity entity) {
        ArrayList<PhysicEntity> elements = getEntityAtPosition(entity.getX(), entity.getY() + 1, entity.getHeight(), entity.getWidth());
        for (PhysicEntity entity1 : elements)
            if (entity.getCollisions().contains(entity1))
                return entity1;
        return null;
    }

    /**
     * Vérifier si une entité est présente à gauche
     * @param entity entité
     * @return s'il y a une entité
     */
    public PhysicEntity isSomethingLeft(PhysicEntity entity) {
        ArrayList<PhysicEntity> elements = getEntityAtPosition(entity.getX() - 1, entity.getY(), entity.getHeight(), entity.getWidth());
        for (PhysicEntity entity1 : elements)
            if (entity.getCollisions().contains(entity1))
                return entity1;
        return null;
    }

    /**
     * Vérifier s'il y a une collision entre deux entités
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @return s'il y a collision
     */
    public boolean isInCollision(PhysicEntity entity1, PhysicEntity entity2) {
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
    public boolean isInside(PhysicEntity childE, PhysicEntity parentE) {
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
    public boolean isInBounds(PhysicEntity entity) {
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
    public boolean isCentered(PhysicEntity e1, PhysicEntity e2) {
        return (e1.getX() + e1.getWidth()) / 2 == (e2.getX() + e2.getWidth()) / 2
                && (e1.getY() + e1.getHeight()) / 2 == (e2.getY() + e2.getHeight()) / 2;
    }

    /**
     * Déplacement vers le haut
     * @param entity entité physique
     */
    public void goUp(PhysicEntity entity) {
        translate(entity, 0, -entity.getSpeed());
        kernelEngine.notifyEntityUpdate(entity);
    }

    /**
     * Déplacement vers la droite
     * @param entity entité physique
     */
    public void goRight(PhysicEntity entity) {
        translate(entity, entity.getSpeed(), 0);
        kernelEngine.notifyEntityUpdate(entity);
    }

    /**
     * Déplacement vers la gauche
     * @param entity entité physique
     */
    public void goLeft(PhysicEntity entity) {
        translate(entity, -entity.getSpeed(), 0);
        kernelEngine.notifyEntityUpdate(entity);
    }

    /**
     * Déplacement vers le bas
     * @param entity entité physique
     */
    public void goDown(PhysicEntity entity) {
        translate(entity, 0, entity.getSpeed());
        kernelEngine.notifyEntityUpdate(entity);
    }

    /**
     * Déplacement de l'entité physique à la position indiquée
     * @param entity entité physique
     * @param x position x
     * @param y position y
     */
    public void move(PhysicEntity entity, int x, int y) {
        entity.setLastX(entity.getX());
        entity.setLastY(entity.getY());
        entity.setX(x);
        entity.setY(y);
        kernelEngine.notifyEntityUpdate(entity);
    }

    /**
     * Translater une entité physique
     * @param entity entité
     * @param x position horizontale
     * @param y position verticale
     */
    public void translate(PhysicEntity entity, int x, int y) {
        entity.setLastX(entity.getX());
        entity.setLastY(entity.getY());
        entity.setX(entity.getX() + x);
        entity.setY(entity.getY() + y);
        kernelEngine.notifyEntityUpdate(entity);
    }

    /**
     * Redimensionne une entité graphique
     *
     * @param entity entité à redimensionner
     * @param w largeur
     * @param h hauteur
     */
    public void resize(PhysicEntity entity, int w, int h) {
        entity.setWidth(w);
        entity.setHeight(h);
        kernelEngine.notifyEntityUpdate(entity);
    }

    /**
     * Redimensionne en hauteur d'une entité physique
     * @param entity entité à redimensionner
     * @param h hauteur
     */
    public void resizeHeight(PhysicEntity entity, int h) {
        entity.setHeight(h);
        kernelEngine.notifyEntityUpdate(entity);
    }

    /**
     * Redimensionne en largeur d'une entité physique
     * @param entity entité à redimensionner
     * @param w largeur
     */
    public void resizeWidth(PhysicEntity entity, int w) {
        entity.setWidth(w);
        kernelEngine.notifyEntityUpdate(entity);
    }

    /**
     * Modification de la  vitesse de déplacement
     * @param entity entité physique
     * @param speed vitesse
     */
    public void setSpeed(PhysicEntity entity, int speed) {
        entity.setSpeed(speed);
        kernelEngine.notifyEntityUpdate(entity);
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

    /**
     * Supprimer une entité
     * @param entity entité
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity.getId());

        ArrayList<PhysicEntity[]> removes = new ArrayList<>();

        for (PhysicEntity[] entities : collisionsEvents.keySet())
            if (entities[0] == entity.getPhysicEntity() || entities[1] == entity.getPhysicEntity())
                removes.add(entities);
        for (PhysicEntity[] element : removes)
            collisionsEvents.remove(element);

        removes.clear();
        Set<PhysicEntity[]> centeredEventsEntities = centeredEvents.keySet();
        for (PhysicEntity[] entities : centeredEventsEntities)
            if (entities[0] == entity.getPhysicEntity() || entities[1] == entity.getPhysicEntity())
                removes.add(entities);
        for (PhysicEntity[] element : removes)
            centeredEvents.remove(element);
    }

    // GETTERS //

    public ConcurrentMap<Integer, PhysicEntity> getEntities() {
        return entities;
    }
}
