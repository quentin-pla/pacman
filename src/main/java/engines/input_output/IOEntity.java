package engines.input_output;

import engines.kernel.EngineEntity;
import engines.kernel.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Entité entrées / sorties
 */
public class IOEntity extends EngineEntity {
    /**
     * Liste des méthodes exécutées lors d'une pression sur une touche ou un bouton
     */
    private Map<Integer,Consumer<Void>> onPressMethods = new HashMap<>();

    /**
     * Liste des méthodes exécutées en fonction de la dernière touche / bouton pressé(e)
     */
    private Map<Integer,Consumer<Void>> onLastMethods = new HashMap<>();

    /**
     * Constructeur
     */
    protected IOEntity(Entity parent) {
        super(parent);
    }

    /**
     * Constructeur par clonage
     * @param clone clone
     */
    private IOEntity(IOEntity clone) {
        this.onPressMethods = clone.onPressMethods;
        this.onLastMethods = clone.onLastMethods;
    }

    /**
     * Cloner l'entité
     * @return clone
     */
    protected IOEntity clone() {
        return new IOEntity(this);
    }

    // GETTERS //

    public Map<Integer, Consumer<Void>> getOnPressMethods() {
        return onPressMethods;
    }

    public Map<Integer, Consumer<Void>> getOnLastMethods() { return onLastMethods; }
}
