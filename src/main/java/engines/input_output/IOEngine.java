package engines.input_output;

import api.SwingAPI;
import engines.kernel.Engine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Moteur entrées / sorties
 */
public class IOEngine extends SwingAPI implements Engine<IOEntity> {
    /**
     * Liste des entités entrées / sorties
     */
    public static Map<Integer, IOEntity> entities = new HashMap<>();

    /**
     * Écouteur actions utilisateur clavier
     */
    private KeyboardIO keyboardIO = new KeyboardIO();

    /**
     * Écouteur actions utilisateur souris
     */
    private MouseIO mouseIO = new MouseIO();

    //------------------------------//
    //----------- CLAVIER -----------//
    //------------------------------//

    /**
     * Activer les entrées/sorties clavier
     */
    public void enableKeyboardIO() {
        SwingAPI.getListenerMethods().addKeyListener(keyboardIO);
    }

    /**
     * Désactiver les entrées/sorties clavier
     */
    public void disableKeyboardIO() {
        SwingAPI.getListenerMethods().removeKeyListener(keyboardIO);
    }

    /**
     * Savoir si une touche clavier est pressée
     * @param code code de la touche
     * @return booléen
     */
    public boolean isKeyPressed(int code) {
        return keyboardIO.getPressedKeys().contains(code);
    }

    /**
     * Savoir si le clavier n'est pas utilisé
     * @return booléen
     */
    public boolean isKeyboardFree() {
        return keyboardIO.getPressedKeys().isEmpty();
    }

    /**
     * Récupérer la dernière touche pressée au clavier
     * @return code de la touche
     */
    public int lastPressedKey() { return keyboardIO.getLastPressedKey(); }

    /**
     * Relier une méthode à une touche clavier
     * @param id identifiant de l'entité
     * @param method méthode
     * @param keyCode touche du clavier
     */
    public void bindMethodToKey(int id, Consumer<Integer> method, int keyCode) {
        entities.get(id).getBindedMethods().put(keyCode, method);
    }

    //------------------------------//
    //----------- SOURIS -----------//
    //------------------------------//

    /**
     * Activer les entrées/sorties souris
     */
    public void enableMouseIO() {
        SwingAPI.getListenerMethods().addMouseListener(mouseIO);
    }

    /**
     * Désactiver les entrées/sorties souris
     */
    public void disableMouseIO(boolean value) {
        SwingAPI.getListenerMethods().removeMouseListener(mouseIO);
    }

    /**
     * Savoir si un bouton souris est pressée
     * @param code code du bouton
     * @return booléen
     */
    public boolean isMouseButtonPressed(int code) {
        return mouseIO.getPressedButtons().contains(code);
    }

    /**
     * Savoir si la souris n'est pas utilisée
     * @return booléen
     */
    public boolean isMouseFree() {
        return mouseIO.getPressedButtons().isEmpty();
    }

    /**
     * Récupérer le dernier bouton pressé depuis la souris
     * @return code du bouton
     */
    public int lastPressedButton() { return mouseIO.getLastPressedButton(); }

    /**
     * Relier une méthode à un bouton de la souris
     * @param id identifiant de l'entité
     * @param method méthode
     * @param buttonCode bouton de la souris
     */
    public void bindMethodToButton(int id, Consumer<Integer> method, int buttonCode) {
        entities.get(id).getBindedMethods().put(buttonCode, method);
    }

    @Override
    public void createEntity(int id) {
        entities.put(id, new IOEntity());
    }

    // GETTERS //

    @Override
    public Map<Integer, IOEntity> getEntities() {
        return entities;
    }

    @Override
    public IOEntity getEntity(int id) {
        return entities.get(id);
    }
}
