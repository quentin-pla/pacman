package engines.AI;

import engines.kernel.Entity;
import engines.kernel.EventListener;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AIEngine implements AIEvent {
    /**
     * Liste des entités utilisant de l'intelligence artificielle
     */
    private final ConcurrentMap<Integer, AIEntity> entities = new ConcurrentHashMap<>();

    /**
     * Liste des écouteurs d'évènements
     */
    private final ArrayList<EventListener> eventsListeners = new ArrayList<>();

    /**
     * Constructeur
     */
    public AIEngine() {}

    @Override
    public void notifyEvent(String event) {
        for (EventListener listener : eventsListeners)
            listener.onEvent(event);
    }

    @Override
    public void subscribeEvents(EventListener listener) {
        eventsListeners.add(listener);
    }

    /**
     * Supprimer tous les évènements
     */
    public void clearEvents() {
        for (AIEntity entity : entities.values())
            entity.setEvent(null);
    }

    /**
     * Rafraichir les entités
     */
    public void updateEntities() {
        for (AIEntity entity : entities.values())
            updateEntity(entity);
    }

    /**
     * Rafraichir une entité
     * @param entity entité
     */
    public void updateEntity(AIEntity entity) {
        if (entity.getEvent() != null)
            notifyEvent(entity.getEvent());
    }

    /**
     * Attacher un évènement à une entité
     * @param entity entité
     * @param event évènement
     */
    public void bindEvent(Entity entity, String event) {
        entity.getAiEntity().setEvent(event);
    }

    /**
     * Créer une nouvelle entité
     * @param parent entité parente
     * @return entité intelligence artificielle
     */
    public AIEntity createEntity(Entity parent) {
        AIEntity entity = new AIEntity(parent);
        entities.put(parent.getId(), entity);
        return entity;
    }

    /**
     * Supprimer une entité
     * @param entity entité
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity.getId());
    }

    // GETTERS //

    public ConcurrentMap<Integer, AIEntity> getEntities() {
        return entities;
    }
}
