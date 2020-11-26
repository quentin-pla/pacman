package engines.AI;

import engines.kernel.Entity;
import engines.kernel.KernelEngine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AIEngine {
    /**
     * Moteur noyau
     */
    private KernelEngine kernelEngine;

    /**
     * Liste des entités utilisant de l'intelligence artificielle
     */
    private final ConcurrentMap<Integer, AIEntity> entities = new ConcurrentHashMap<>();

    /**
     * Constructeur
     * @param kernelEngine moteur noyau
     */
    public AIEngine(KernelEngine kernelEngine) {
        this.kernelEngine = kernelEngine;
    }

    /**
     * Rafraichir les entités
     */
    public void updateEntities() {
        for (AIEntity entity : entities.values())
            if (entity.getEvent() != null)
                kernelEngine.notifyEvent(entity.getEvent());
    }

    /**
     * Attacher un évènement à une entité
     * @param entity entité
     * @param event évènement
     */
    public void bindEvent(AIEntity entity, String event) {
        entity.setEvent(event);
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
}
