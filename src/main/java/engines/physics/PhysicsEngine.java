package engines.physics;

import engines.kernel.Entity;
import engines.kernel.EventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Moteur physique
 */
public class PhysicsEngine implements CollisionEvent {
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
     * Liste des écouteurs d'évènements
     */
    private final ArrayList<EventListener> eventsListeners = new ArrayList<>();

    /**
     * Constructeur
     */
    public PhysicsEngine() {}

    @Override
    public void notifyCollision(String event) {
        for (EventListener listener : eventsListeners)
            listener.onEvent(event);
    }

    @Override
    public void notifyEntityUpdate(PhysicEntity entity) {
        for (EventListener listener : eventsListeners)
            listener.onEntityUpdate(entity);
    }

    @Override
    public void subscribeEvents(EventListener listener) {
        eventsListeners.add(listener);
    }

    /**
     * Supprimer tous les évènements
     */
    public void clearEvents() {
        collisionsEvents.clear();
        centeredEvents.clear();
    }

    /**
     * Mettre à jour les entités physiques
     */
    public void updateEntites() {
        for (PhysicEntity entity : entities.values())
            updateEntity(entity);
    }

    /**
     * Mettre à jour une entité
     * @param entity entité
     */
    public void updateEntity(PhysicEntity entity) {
        entity.setColliding(isInCollision(entity.getParent()) || !isInBounds(entity.getParent()));
        for (Map.Entry<PhysicEntity[], String> event : collisionsEvents.entrySet()) {
            PhysicEntity e1 = event.getKey()[0];
            PhysicEntity e2 = event.getKey()[1];
            if (e1 == entity || e2 == entity) {
                if ((e2 == null && e1.isColliding())
                        || (e2 != null && isInCollision(e1.getParent(), e2.getParent())))
                    notifyCollision(event.getValue());
            }
        }
        for (Map.Entry<PhysicEntity[], String> event : centeredEvents.entrySet()) {
            PhysicEntity e1 = event.getKey()[0];
            PhysicEntity e2 = event.getKey()[1];
            if (e1 == entity || e2 == entity) {
                if (isCentered(e1.getParent(), e2.getParent()))
                    notifyCollision(event.getValue());
            }
        }
        if (entity.isColliding())
            move(entity.getParent(), entity.getLastX(), entity.getLastY());
    }

    /**
     * Ajouter des collisions entre deux entités
     * @param entity1 entité 1
     * @param entity2 entité 2
     */
    public void addCollisions(Entity entity1, Entity entity2) {
        entity1.getPhysicEntity().getCollisions().add(entity2.getPhysicEntity());
        entity2.getPhysicEntity().getCollisions().add(entity1.getPhysicEntity());
    }

    /**
     * Supprimer des collisions entre deux entités
     * @param entity1 entité 1
     * @param entity2 entité 2
     */
    public void removeCollisions(Entity entity1, Entity entity2) {
        entity1.getPhysicEntity().getCollisions().remove(entity2.getPhysicEntity());
        entity2.getPhysicEntity().getCollisions().remove(entity1.getPhysicEntity());
    }

    /**
     * Ajouter des limites de déplacement
     * @param entity entité
     * @param x1 coordonnée horizontale 1
     * @param x2 coordonnée horizontale 2
     * @param y1 coordonnée verticale 1
     * @param y2 coordonnée verticale 2
     */
    public void addBoundLimits(Entity entity, int x1, int y1, int x2, int y2) {
        entity.getPhysicEntity().setBoundLimits(new int[]{x1,y1,x2,y2});
    }

    /**
     * Attacher un évènement lorsqu'une entité entre en collision
     * @param entity entité
     * @param eventName nom de l'évènement
     */
    public void bindEventOnCollision(Entity entity, String eventName) {
        collisionsEvents.put(new PhysicEntity[]{entity.getPhysicEntity(), null}, eventName);
    }

    /**
     * Attacher un évènement lorsque deux entités entrent en collision
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @param eventName nom de l'évènement
     */
    public void bindEventOnCollision(Entity entity1, Entity entity2, String eventName) {
        collisionsEvents.put(new PhysicEntity[]{entity1.getPhysicEntity(), entity2.getPhysicEntity()}, eventName);
    }

    /**
     * Attacher un évènement à lorsque deux entités sont sur la même position
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @param eventName nom de l'évènement
     */
    public void bindEventOnSameLocation(Entity entity1, Entity entity2, String eventName) {
        centeredEvents.put(new PhysicEntity[]{entity1.getPhysicEntity(), entity2.getPhysicEntity()}, eventName);
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
     * Obtenir la distance horizontale entre deux entités
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @return distance horizontale
     */
    public int getHorizontalDistance(Entity entity1, Entity entity2) {
        PhysicEntity entity1Physic = entity1.getPhysicEntity();
        PhysicEntity entity2Physic = entity2.getPhysicEntity();
        int e1XMid = (entity1Physic.getX() + entity1Physic.getWidth());
        int e2XMid = (entity2Physic.getX() + entity2Physic.getWidth());
        return e1XMid - e2XMid;
    }

    /**
     * Obtenir la distance verticale entre deux entités
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @return distance verticale
     */
    public int getVerticalDistance(Entity entity1, Entity entity2) {
        PhysicEntity entity1Physic = entity1.getPhysicEntity();
        PhysicEntity entity2Physic = entity2.getPhysicEntity();
        int e1YMid = (entity1Physic.getY() + entity1Physic.getHeight());
        int e2YMid = (entity2Physic.getY() + entity2Physic.getHeight());
        return e1YMid - e2YMid;
    }

    /**
     * Obtenir la distance entre deux entités
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @return distance
     */
    public int getDistance(Entity entity1, Entity entity2) {
        return Math.max(Math.abs(getHorizontalDistance(entity1, entity2)),
                Math.abs(getVerticalDistance(entity1, entity2)));
    }

    /**
     * Vérifier si une entité est présente en haut
     * @param entity entité
     * @return s'il y a une entité
     */
    public PhysicEntity isSomethingUp(Entity entity) {
        PhysicEntity physicEntity = entity.getPhysicEntity();
        ArrayList<PhysicEntity> elements = getEntityAtPosition(physicEntity.getX(),
                physicEntity.getY() - 1, physicEntity.getHeight(), physicEntity.getWidth());
        for (PhysicEntity entity1 : elements)
            if (physicEntity.getCollisions().contains(entity1))
                return entity1;
        return null;
    }

    /**
     * Vérifier si une entité est présente à droite
     * @param entity entité
     * @return s'il y a une entité
     */
    public PhysicEntity isSomethingRight(Entity entity) {
        PhysicEntity physicEntity = entity.getPhysicEntity();
        ArrayList<PhysicEntity> elements = getEntityAtPosition(physicEntity.getX() + 1,
                physicEntity.getY(), physicEntity.getHeight(), physicEntity.getWidth());
        for (PhysicEntity entity1 : elements)
            if (physicEntity.getCollisions().contains(entity1))
                return entity1;
        return null;
    }

    /**
     * Vérifier si une entité est présente en bas
     * @param entity entité
     * @return s'il y a une entité
     */
    public PhysicEntity isSomethingDown(Entity entity) {
        PhysicEntity physicEntity = entity.getPhysicEntity();
        ArrayList<PhysicEntity> elements = getEntityAtPosition(physicEntity.getX(),
                physicEntity.getY() + 1, physicEntity.getHeight(), physicEntity.getWidth());
        for (PhysicEntity entity1 : elements)
            if (physicEntity.getCollisions().contains(entity1))
                return entity1;
        return null;
    }

    /**
     * Vérifier si une entité est présente à gauche
     * @param entity entité
     * @return s'il y a une entité
     */
    public PhysicEntity isSomethingLeft(Entity entity) {
        PhysicEntity physicEntity = entity.getPhysicEntity();
        ArrayList<PhysicEntity> elements = getEntityAtPosition(physicEntity.getX() - 1,
                physicEntity.getY(), physicEntity.getHeight(), physicEntity.getWidth());
        for (PhysicEntity entity1 : elements)
            if (physicEntity.getCollisions().contains(entity1))
                return entity1;
        return null;
    }

    /**
     * Vérifier si une entité est en collision
     * @param entity entité
     * @return s'il y a une collision
     */
    public boolean isInCollision(Entity entity) {
        for (PhysicEntity collision : entity.getPhysicEntity().getCollisions())
            if (isInCollision(collision.getParent(),entity))
                return true;
        return false;
    }

    /**
     * Vérifier s'il y a une collision entre deux entités
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @return s'il y a collision
     */
    public boolean isInCollision(Entity entity1, Entity entity2) {
        PhysicEntity physicEntity1 = entity1.getPhysicEntity();
        PhysicEntity physicEntity2 = entity2.getPhysicEntity();
        return physicEntity1.getX() + physicEntity1.getWidth() > physicEntity2.getX()
                && physicEntity1.getY() + physicEntity1.getHeight() > physicEntity2.getY()
                && physicEntity1.getX() < physicEntity2.getX() + physicEntity2.getWidth()
                && physicEntity1.getY() < physicEntity2.getY() + physicEntity2.getHeight();
    }

    /**
     * Vérifier si une entité est comprise dans une autre entité
     * @param childE entité enfant
     * @param parentE entité parente
     * @return si l'enfant est dans le parent
     */
    public boolean isInside(Entity childE, Entity parentE) {
        PhysicEntity physicChild = childE.getPhysicEntity();
        PhysicEntity physicParent = parentE.getPhysicEntity();
        return physicChild.getX() > physicParent.getX()
                && physicChild.getY() > physicParent.getY()
                && physicChild.getX() + physicChild.getWidth() < physicParent.getX() + physicParent.getWidth()
                && physicChild.getY() + physicChild.getHeight() < physicParent.getY() + physicParent.getHeight();
    }

    /**
     * Vérifier si une entité est bien dans ses limites de déplacement
     * @param entity entité
     * @return si l'entité respecte ses limites de déplacement
     */
    public boolean isInBounds(Entity entity) {
        PhysicEntity physicEntity = entity.getPhysicEntity();
        int[] boundLimits = physicEntity.getBoundLimits();
        return boundLimits == null || physicEntity.getX() >= boundLimits[0]
                && physicEntity.getX() + physicEntity.getWidth() <= boundLimits[2]
                && physicEntity.getY() >= boundLimits[1]
                && physicEntity.getY() + physicEntity.getHeight() <= boundLimits[3];
    }

    /**
     * Vérifier si deux entités sont à la même position
     * @param entity1 entité 1
     * @param entity2 entité 2
     * @return si les entités sont à la même position
     */
    public boolean isCentered(Entity entity1, Entity entity2) {
        PhysicEntity physicEntity1 = entity1.getPhysicEntity();
        PhysicEntity physicEntity2 = entity2.getPhysicEntity();
        return (physicEntity1.getX() + physicEntity1.getWidth()) / 2 == (physicEntity2.getX() + physicEntity2.getWidth()) / 2
                && (physicEntity1.getY() + physicEntity1.getHeight()) / 2 == (physicEntity2.getY() + physicEntity2.getHeight()) / 2;
    }

    /**
     * Déplacement vers le haut
     * @param entity entité physique
     */
    public void goUp(Entity entity) {
        PhysicEntity physicEntity = entity.getPhysicEntity();
        for (int i = 0; i < physicEntity.getSpeed() && !physicEntity.isColliding(); i++) {
            translate(entity, 0, -1);
            updateEntity(physicEntity);
        }
    }

    /**
     * Déplacement vers la droite
     * @param entity entité physique
     */
    public void goRight(Entity entity) {
        PhysicEntity physicEntity = entity.getPhysicEntity();
        for (int i = 0; i < physicEntity.getSpeed() && !physicEntity.isColliding(); i++) {
            translate(entity, 1, 0);
            updateEntity(physicEntity);
        }
    }

    /**
     * Déplacement vers la gauche
     * @param entity entité physique
     */
    public void goLeft(Entity entity) {
        PhysicEntity physicEntity = entity.getPhysicEntity();
        for (int i = 0; i < physicEntity.getSpeed() && !physicEntity.isColliding(); i++) {
            translate(entity, -1, 0);
            updateEntity(physicEntity);
        }
    }

    /**
     * Déplacement vers le bas
     * @param entity entité physique
     */
    public void goDown(Entity entity) {
        PhysicEntity physicEntity = entity.getPhysicEntity();
        for (int i = 0; i < physicEntity.getSpeed() && !physicEntity.isColliding(); i++) {
            translate(entity, 0, 1);
            updateEntity(physicEntity);
        }
    }

    /**
     * Déplacement de l'entité physique à la position indiquée
     * @param entity entité physique
     * @param x position x
     * @param y position y
     */
    public void move(Entity entity, int x, int y) {
        PhysicEntity physicEntity = entity.getPhysicEntity();
        physicEntity.setLastX(physicEntity.getX());
        physicEntity.setLastY(physicEntity.getY());
        physicEntity.setX(x);
        physicEntity.setY(y);
        notifyEntityUpdate(physicEntity);
    }

    /**
     * Translater une entité physique
     * @param entity entité
     * @param x position horizontale
     * @param y position verticale
     */
    public void translate(Entity entity, int x, int y) {
        PhysicEntity physicEntity = entity.getPhysicEntity();
        physicEntity.setLastX(physicEntity.getX());
        physicEntity.setLastY(physicEntity.getY());
        physicEntity.setX(physicEntity.getX() + x);
        physicEntity.setY(physicEntity.getY() + y);
        notifyEntityUpdate(physicEntity);
    }

    /**
     * Redimensionne une entité graphique
     *
     * @param entity entité à redimensionner
     * @param w largeur
     * @param h hauteur
     */
    public void resize(Entity entity, int w, int h) {
        entity.getPhysicEntity().setWidth(w);
        entity.getPhysicEntity().setHeight(h);
        notifyEntityUpdate(entity.getPhysicEntity());
    }

    /**
     * Redimensionne en hauteur d'une entité physique
     * @param entity entité à redimensionner
     * @param h hauteur
     */
    public void resizeHeight(Entity entity, int h) {
        entity.getPhysicEntity().setHeight(h);
        notifyEntityUpdate(entity.getPhysicEntity());
    }

    /**
     * Redimensionne en largeur d'une entité physique
     * @param entity entité à redimensionner
     * @param w largeur
     */
    public void resizeWidth(Entity entity, int w) {
        entity.getPhysicEntity().setWidth(w);
        notifyEntityUpdate(entity.getPhysicEntity());
    }

    /**
     * Modification de la  vitesse de déplacement
     * @param entity entité physique
     * @param speed vitesse
     */
    public void setSpeed(Entity entity, int speed) {
        entity.getPhysicEntity().setSpeed(speed);
        notifyEntityUpdate(entity.getPhysicEntity());
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

        for (PhysicEntity physicEntity : entities.values())
            physicEntity.getCollisions().remove(entity.getPhysicEntity());

        for (PhysicEntity[] entities : collisionsEvents.keySet())
            if (entities[0] == entity.getPhysicEntity() || entities[1] == entity.getPhysicEntity())
                removes.add(entities);
        for (PhysicEntity[] element : removes)
            collisionsEvents.remove(element);

        removes.clear();

        for (PhysicEntity[] entities : centeredEvents.keySet())
            if (entities[0] == entity.getPhysicEntity() || entities[1] == entity.getPhysicEntity())
                removes.add(entities);
        for (PhysicEntity[] element : removes)
            centeredEvents.remove(element);
    }

    // GETTERS //

    public ConcurrentMap<Integer, PhysicEntity> getEntities() {
        return entities;
    }

    public ConcurrentMap<PhysicEntity[], String> getCollisionsEvents() {
        return collisionsEvents;
    }

    public ConcurrentMap<PhysicEntity[], String> getCenteredEvents() {
        return centeredEvents;
    }

    public ArrayList<EventListener> getEventsListeners() {
        return eventsListeners;
    }
}
