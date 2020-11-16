package engines.kernel;

import engines.graphics.GraphicsEngine;
import engines.input_output.IOEngine;
import engines.physics.PhysicsEngine;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Moteur noyau
 */
public class KernelEngine {
    /**
     * Liste des identifiants générés pour les entités
     */
    private static final Map<Integer,Entity> entities = new HashMap<>();


    private static Map<String, Event> events = new HashMap<>();

    /**
     * Délai de rafraichissement du jeu : 60fps
     */
    private static int delay = 1000/60;

    /**
     * Temps de rafraichissement 60 images par seconde
     */
    private static Timer timer;

    /**
     * Générer un nouvel id pour une entité
     * @return id généré
     */
    public static int generateEntity() {
        Entity entity = new Entity();
        entities.put(entity.getId(), entity);
        return entity.getId();
    }

    /**
     * Générer un nouvel ID
     * @return nouvel identifiant
     */
    protected static int generateNewID() {
        return entities.isEmpty() ? 1 : Collections.max(entities.keySet()) + 1;
    }

    /**
     * Rafraichir le jeu
     */
    private static final ActionListener refresh = evt -> {
        IOEngine.updateEntities();
        PhysicsEngine.updateEntites();
        GraphicsEngine.refreshWindow();
    };


    public static void addEvent(String name, Event event) {
        events.put(name, event);
    }

    public static void notifyEvent(String eventName) {
        events.get(eventName).run();
    }

    /**
     * Exécuter le jeu
     */
    public static void start() {
        GraphicsEngine.showWindow();
        timer = new Timer(delay, refresh);
        timer.start();
    }
}
