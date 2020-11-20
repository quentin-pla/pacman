package engines.kernel;

import engines.graphics.GraphicsEngine;
import engines.input_output.IOEngine;
import engines.physics.PhysicsEngine;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Moteur noyau
 */
public class KernelEngine {
    /**
     * Dernier id généré
     */
    private static int lastID = 0;

    /**
     * Liste des évènements du jeu
     */
    private static Map<String, Event> events = new HashMap<>();

    /**
     * Délai de rafraichissement du jeu : 60fps
     */
    private final static int delay = 1000/60;

    /**
     * Générer une nouvelle entité
     * @return id généré
     */
    public static Entity generateEntity() {
        return new Entity();
    }

    /**
     * Générer un nouvel ID
     * @return nouvel identifiant
     */
    protected static int generateNewID() {
        ++lastID;
        return lastID;
    }

    /**
     * Rafraichir le jeu
     */
    private static final ActionListener refresh = evt -> {
        IOEngine.updateEntities();
        PhysicsEngine.updateEntites();
        GraphicsEngine.refreshWindow();
    };

    /**
     * Ajouter un évènement au jeu
     * @param name nom de l'évènement
     * @param event évènement
     */
    public static void addEvent(String name, Event event) {
        events.put(name, event);
    }

    /**
     * Exécuter un évènement spécifique
     * @param eventName nom de l'évènement
     */
    public static void notifyEvent(String eventName) {
        events.get(eventName).run();
    }

    /**
     * Exécuter le moteur noyau
     */
    public static void start() {
        GraphicsEngine.showWindow();
        //Rafraichissement 60 images par seconde
        new Timer(delay, refresh).start();
    }
}
