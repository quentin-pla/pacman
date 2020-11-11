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
    private Map<Integer,Consumer<Integer>> bindedMethods = new HashMap<>();

    /**
     * Constructeur
     */
    protected IOEntity() {}

    /**
     * Cloner l'entité
     * @return clone
     */
    public IOEntity clone() {
        return new IOEntity();
    }

    // GETTERS //

    public Map<Integer, Consumer<Integer>> getBindedMethods() {
        return bindedMethods;
    }
}
