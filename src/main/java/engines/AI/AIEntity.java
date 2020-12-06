package engines.AI;

import engines.kernel.EngineEntity;
import engines.kernel.Entity;

public class AIEntity extends EngineEntity {
    /**
     * Évènement lié
     */
    private String event;

    /**
     * Constructeur
     * @param parent parent
     */
    public AIEntity(Entity parent) {
        super(parent);
    }

    /**
     * Cloner l'entité
     * @param entity entité
     */
    public void clone(AIEntity entity) {
        this.event = entity.event;
    }

    // GETTERS & SETTERS //

    public String getEvent() { return event; }

    protected void setEvent(String event) { this.event = event; }
}
