package engines.kernel;

import java.util.Map;

/**
 * Moteur abstrait
 */
public interface Engine<T> {
    /**
     * Créer une entité avec son identifiant
     *
     * @param id identifiant de l'entité
     */
    void createEntity(int id);

    /**
     * Récupérer les entités liées au moteur
     * @return map contenant les identifiants associés à chaque entité
     */
    Map<Integer,T> getEntities();

    /**
     * Obtenir une entité à partir de son identifiant
     * @param id identifiant de l'entité
     * @return entité
     */
    T getEntity(int id);
}
