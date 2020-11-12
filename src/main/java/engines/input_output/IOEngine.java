package engines.input_output;

import api.SwingAPI;
import engines.kernel.Engine;
import engines.kernel.Entity;

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
    private IOEngine() {
    }

    /**
     * Récupérer l'instance
     *
     * @return instance
     */
    public static IOEngine getInstance() {
        if (instance == null) instance = new IOEngine();
        return instance;
    }

    //-------------------------------//
    //----------- ENTITÉS -----------//
    //-------------------------------//

    /**
     * Mettre à jour les entités en fonction des touches pressées
     */
    public static void updateEntities() {
        for (IOEntity entity : entities.values()) {
            for (Map.Entry<Integer, Consumer<Void>> bind : entity.getOnPressMethods().entrySet()) {
                if (bind.getKey() == null && isKeyboardFree())
                    if (isKeyboardFree()) bind.getValue().accept(null);
                else if (bind.getKey() != null && isKeyPressed(bind.getKey()))
                    bind.getValue().accept(null);
            }
            for (Map.Entry<Integer, Consumer<Void>> bind : entity.getOnLastMethods().entrySet()) {
                if (lastPressedKey() == bind.getKey())
                    bind.getValue().accept(null);
            }
        }
    }

    //-------------------------------//
    //----------- CLAVIER -----------//
    //-------------------------------//

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
        entities.get(id).getOnPressMethods().put(keyCode, method);
    }

    /**
     * Relier une méthode lorsque le clavier est libre
     * @param id identifiant de l'entité
     * @param method méthode
     */
    public static void bindMethodToKeyboardFree(int id, Consumer<Void> method) {
        entities.get(id).getOnPressMethods().put(null, method);
    }

    /**
     * Relier une méthode à la dernière touche pressée
     * @param id identifiant de l'entité
     * @param method méthode
     */
    public static void bindMethodToLastKey(int id, Consumer<Void> method, int keyCode) {
        entities.get(id).getOnLastMethods().put(keyCode, method);
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
        entities.get(id).getOnPressMethods().put(buttonCode, method);
    }

    /**
     * Relier une méthode lorsque le clavier est libre
     * @param id identifiant de l'entité
     * @param method méthode
     */
    public static void bindMethodToMouseFree(int id, Consumer<Void> method) {
        entities.get(id).getOnPressMethods().put(null, method);
    }

    /**
     * Relier une méthode à la dernière touche pressée
     * @param id identifiant de l'entité
     * @param method méthode
     */
    public static void bindMethodToLastButton(int id, Consumer<Void> method, int buttonCode) {
        entities.get(id).getOnLastMethods().put(buttonCode, method);
    }

    // GETTERS //

    @Override
    public IOEntity createEntity(Entity parent) {
        IOEntity entity = new IOEntity(parent);
        entities.put(parent.getId(), entity);
        return entity;
    }

    @Override
    public Map<Integer, IOEntity> getEntities() {
        return entities;
    }

    @Override
    public IOEntity getEntity(int id) {
        return entities.get(id);
    }
}
