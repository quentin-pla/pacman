package engines.kernel;

import engines.graphics.GraphicEntity;
import engines.graphics.GraphicsEngine;
import engines.input_output.IOEngine;
import engines.physics.PhysicEntity;
import engines.physics.PhysicsEngine;
import engines.sound.SoundEngine;

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
     * Moteur audio
     */
    private SoundEngine soundEngine;

    /**
     * Dernier id généré
     */
    private int lastID = 0;

    /**
     * Liste des évènements du jeu
     */
    private final Map<String, Runnable> events = new HashMap<>();

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
        this.soundEngine = new SoundEngine(this);
    }

    /**
     * Générer une nouvelle entité
     * @return id généré
     */
    public Entity generateEntity() {
        return new Entity(this);
    }

    /**
     * Supprimer une entité
     * @param entity entité
     */
    public void removeEntity(Entity entity) {
        graphicsEngine.removeEntity(entity);
        physicsEngine.removeEntity(entity);
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
     * Exécuter le moteur noyau
     */
    public void start() {
        graphicsEngine.showWindow();
        //Rafraichissement 60 images par seconde
        new Timer(delay, refresh).start();
    }

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
        try {
            events.get(eventName).run();
        } catch (NullPointerException e) {
            System.err.println("ERREUR : Nom de l'évènement introuvable.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Mettre à jour les coordonnées des entités entre les moteurs
     */
    public void notifyEntityUpdate(EngineEntity entity) {
        if (entity instanceof PhysicEntity) {
            PhysicEntity physicEntity = (PhysicEntity) entity;
            GraphicEntity graphicEntity = entity.getParent().getGraphicEntity();
            graphicsEngine.move(graphicEntity, physicEntity.getX(), physicEntity.getY());
            graphicsEngine.resize(graphicEntity, physicEntity.getWidth(), physicEntity.getHeight());
        }

    }

    // GETTERS //

    public GraphicsEngine getGraphicsEngine() { return graphicsEngine; }

    public IOEngine getIoEngine() { return ioEngine; }

    public PhysicsEngine getPhysicsEngine() { return physicsEngine; }

    public SoundEngine getSoundEngine() { return soundEngine; }
}
