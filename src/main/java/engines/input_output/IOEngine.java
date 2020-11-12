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
    public static final Map<Integer, IOEntity> entities = new HashMap<>();

    /**
     * Écouteur actions utilisateur clavier
     */
    private static final KeyboardIO keyboardIO = new KeyboardIO();

    /**
     * Écouteur actions utilisateur souris
     */
    private static final MouseIO mouseIO = new MouseIO();

    /**
     * Instance unique
     */
    private static IOEngine instance;

    /**
     * Constructeur privé
     */
    private IOEngine() {}

    /**
     * Récupérer l'instance
     * @return instance
     */
    public static IOEngine getInstance() {
        if (instance == null) instance = new IOEngine();
        return instance;
    }

    //------------------------------//
    //----------- CLAVIER -----------//
    //------------------------------//

    /**
     * Activer les entrées/sorties clavier
     */
    public static void enableKeyboardIO() {
        SwingAPI.getListenerMethods().addKeyListener(keyboardIO);
    }

    /**
     * Désactiver les entrées/sorties clavier
     */
    public static void disableKeyboardIO() {
        SwingAPI.getListenerMethods().removeKeyListener(keyboardIO);
    }

    /**
     * Savoir si une touche clavier est pressée
     * @param code code de la touche
     * @return booléen
     */
    public static boolean isKeyPressed(int code) {
        return keyboardIO.getPressedKeys().contains(code);
    }

    /**
     * Savoir si le clavier n'est pas utilisé
     * @return booléen
     */
    public static boolean isKeyboardFree() {
        return keyboardIO.getPressedKeys().isEmpty();
    }

    /**
     * Récupérer la dernière touche pressée au clavier
     * @return code de la touche
     */
    public static int lastPressedKey() { return keyboardIO.getLastPressedKey(); }

    /**
     * Relier une méthode à une touche clavier
     * @param id identifiant de l'entité
     * @param method méthode
     * @param keyCode touche du clavier
     */
    public static void bindMethodToKey(int id, Consumer<Void> method, int keyCode) {
        entities.get(id).getBindedMethods().put(keyCode, method);
    }

    //------------------------------//
    //----------- SOURIS -----------//
    //------------------------------//

    /**
     * Activer les entrées/sorties souris
     */
    public static void enableMouseIO() {
        SwingAPI.getListenerMethods().addMouseListener(mouseIO);
    }

    /**
     * Désactiver les entrées/sorties souris
     */
    public static void disableMouseIO(boolean value) {
        SwingAPI.getListenerMethods().removeMouseListener(mouseIO);
    }

    /**
     * Savoir si un bouton souris est pressée
     * @param code code du bouton
     * @return booléen
     */
    public static boolean isMouseButtonPressed(int code) {
        return mouseIO.getPressedButtons().contains(code);
    }

    /**
     * Savoir si la souris n'est pas utilisée
     * @return booléen
     */
    public static boolean isMouseFree() {
        return mouseIO.getPressedButtons().isEmpty();
    }

    /**
     * Récupérer le dernier bouton pressé depuis la souris
     * @return code du bouton
     */
    public static int lastPressedButton() { return mouseIO.getLastPressedButton(); }

    /**
     * Relier une méthode à un bouton de la souris
     * @param id identifiant de l'entité
     * @param method méthode
     * @param buttonCode bouton de la souris
     */
    public static void bindMethodToButton(int id, Consumer<Void> method, int buttonCode) {
        entities.get(id).getBindedMethods().put(buttonCode, method);
    }

    @Override
    public IOEntity createEntity(int id) {
        IOEntity entity = new IOEntity(id);
        entities.put(id, entity);
        return entity;
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
