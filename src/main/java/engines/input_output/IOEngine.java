package engines.input_output;

import api.SwingAPI;
import engines.kernel.Entity;
import engines.kernel.KernelEngine;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Moteur entrées / sorties
 */
public class IOEngine extends SwingAPI {
    /**
     * Moteur noyau
     */
    private KernelEngine kernelEngine;

    /**
     * Écouteur actions utilisateur clavier
     */
    private final KeyboardIO keyboardIO = new KeyboardIO();

    /**
     * Écouteur actions utilisateur souris
     */
    private final MouseIO mouseIO = new MouseIO();

    /**
     * Constructeur
     * @param kernelEngine moteur noyau
     */
    public IOEngine(KernelEngine kernelEngine) {
        this.kernelEngine = kernelEngine;
    }

    //-------------------------------//
    //----------- ENTITÉS -----------//
    //-------------------------------//

    /**
     * Mettre à jour les entités en fonction des touches pressées
     */
    public void updateEntities() {
        for (Map.Entry<Integer,String> event : bindedEvents.entrySet())
            if ((event.getKey() == -1 && isKeyboardFree()) || isKeyPressed(event.getKey()))
                kernelEngine.notifyEvent(event.getValue());
        for (Map.Entry<Integer,String> event: bindedEventsOnLastKey.entrySet())
            if(lastPressedKey() == event.getKey())
                kernelEngine.notifyEvent(event.getValue());
        if (lastClickCoordinates() != null) {
            for (Map.Entry<Entity, String> event : bindedClickEvents.entrySet())
                kernelEngine.checkClickEvent(event.getKey(), event.getValue());
            resetLastClick();
        }
    }

    //-------------------------------//
    //----------- CLAVIER -----------//
    //-------------------------------//

    /**
     * Liste des évènements attachés à une touche ou un bouton
     */
    public final Map<Integer, String> bindedEvents = new HashMap<>();

    /**
     * Liste des évènements attachés à la dernière touche / bouton
     */
    public final Map<Integer, String> bindedEventsOnLastKey = new HashMap<>();

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
     * Attacher un évènement à une touche clavier
     * @param keyCode code de la touche
     * @param eventName nom de l'évènement
     */
    public void bindEvent(Integer keyCode, String eventName) {
        bindedEvents.put(keyCode, eventName);
    }

    /**
     * Attacher un évènement à la dernière touche pressée
     * @param keyCode code de la touche
     * @param eventName nom de l'évènement
     */
    public void bindEventOnLastKey(int keyCode, String eventName) {
        bindedEventsOnLastKey.put(keyCode, eventName);
    }

    /**
     * Attacher un évènement lorsqu'aucune touche est pressée
     * @param eventName nom de l'évènement
     */
    public void bindEventKeyboardFree(String eventName) {
        bindedEvents.put(-1, eventName);
    }

    //------------------------------//
    //----------- SOURIS -----------//
    //------------------------------//

    /**
     * Liste des évènements attachés à un click
     */
    public final Map<Entity,String> bindedClickEvents = new HashMap<>();

    /**
     * Activer les entrées/sorties souris
     */
    public void enableMouseIO() {
        SwingAPI.getListenerMethods().addMouseListener(mouseIO);
    }

    /**
     * Désactiver les entrées/sorties souris
     */
    public void disableMouseIO() {
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
     * Récupérer les coordonnées du dernier click
     * @return coordonnées dernier click
     */
    public Point lastClickCoordinates() { return mouseIO.getClickCoords(); }

    public void resetLastClick() { mouseIO.setClickCoords(null); }

    /**
     * Attacher un évènement lors d'un click
     * @param entity entité
     * @param eventName nom de l'évènement
     */
    public void bindEventOnClick(Entity entity, String eventName) {
        bindedClickEvents.put(entity,eventName);
    }
}
