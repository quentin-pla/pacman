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
     * Moteur graphique
     */
    private GraphicsEngine graphicsEngine;

    /**
     * Moteur entrées / sorties
     */
    private IOEngine ioEngine;

    /**
     * Moteur physique
     */
    private PhysicsEngine physicsEngine;

    /**
     * Dernier id généré
     */
    private int lastID = 0;

    /**
     * Liste des évènements du jeu
     */
    private Map<String, Runnable> events = new HashMap<>();

    /**
     * Délai de rafraichissement du jeu : 60fps
     */
    private final int delay = 1000/60;

    /**
     * Constructeur
     */
    public KernelEngine() {
        this.graphicsEngine = new GraphicsEngine(this);
        this.physicsEngine = new PhysicsEngine(this);
        this.ioEngine = new IOEngine(this);
    }

    /**
     * Générer une nouvelle entité
     * @return id généré
     */
    public Entity generateEntity() {
        return new Entity(this);
    }

    /**
     * Générer un nouvel ID
     * @return nouvel identifiant
     */
    protected int generateNewID() {
        ++lastID;
        return lastID;
    }

    /**
     * Rafraichir le jeu
     */
    private final ActionListener refresh = evt -> {
        ioEngine.updateEntities();
        physicsEngine.updateEntites();
        graphicsEngine.refreshWindow();
    };

    /**
     * Ajouter un évènement au jeu
     * @param name nom de l'évènement
     * @param event évènement
     */
    public void addEvent(String name, Runnable event) {
        events.put(name, event);
    }

    /**
     * Exécuter un évènement spécifique
     * @param eventName nom de l'évènement
     */
    public void notifyEvent(String eventName) {
        events.get(eventName).run();
    }

    /**
     * Exécuter le moteur noyau
     */
    public void start() {
        graphicsEngine.showWindow();
        //Rafraichissement 60 images par seconde
        new Timer(delay, refresh).start();
    }

    // GETTERS //

    public GraphicsEngine getGraphicsEngine() { return graphicsEngine; }

    public IOEngine getIoEngine() { return ioEngine; }

    public PhysicsEngine getPhysicsEngine() { return physicsEngine; }
}
