package engines.input_output;

import engines.kernel.Entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Entité entrées / sorties
 */
public class IOEntity extends Entity {
    /**
     * Liste des méthodes attachées au touches / boutons
     */
    private Map<Integer,Consumer<Void>> bindedMethods = new HashMap<>();

    /**
     * Constructeur
     */
    protected IOEntity(int id) {
        super(id);
    }

    /**
     * Constructeur par clonage
     * @param clone clone
     */
    private IOEntity(IOEntity clone) {
        this.bindedMethods = clone.bindedMethods;
    }

    /**
     * Cloner l'entité
     * @return clone
     */
    public IOEntity clone() {
        return new IOEntity(this);
    }

    // GETTERS //

    public Map<Integer, Consumer<Void>> getBindedMethods() {
        return bindedMethods;
    }
}
