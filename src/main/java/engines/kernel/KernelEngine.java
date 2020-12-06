package engines.kernel;

import api.SwingTimer;
import engines.AI.AIEngine;
import engines.graphics.GraphicEntity;
import engines.graphics.GraphicsEngine;
import engines.graphics.Scene;
import engines.input_output.IOEngine;
import engines.physics.PhysicEntity;
import engines.physics.PhysicsEngine;
import engines.sound.SoundEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Moteur noyau
 */
public class KernelEngine implements EventListener {
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
     * Moteur intelligence artificielle
     */
    private AIEngine aiEngine;

    /**
     * Dernier id généré
     */
    private int lastID = 0;

    /**
     * Mettre les évènements en pause
     */
    private boolean pauseEvents = false;

    /**
     * Mettre le rafraichissement des graphismes en pause
     */
    private boolean pauseGraphics = false;

    /**
     * Liste des évènements du jeu
     */
    private final Map<String, Runnable> events = new HashMap<>();

    /**
     * Liste des entités
     */
    private final ArrayList<Entity> entities = new ArrayList<>();

    /**
     * Délai de rafraichissement du jeu : 60fps
     */
    private final int delay = 1000/60;

    /**
     * Constructeur
     */
    public KernelEngine() {
        this.graphicsEngine = new GraphicsEngine();
        this.physicsEngine = new PhysicsEngine();
        this.ioEngine = new IOEngine();
        this.soundEngine = new SoundEngine();
        this.aiEngine = new AIEngine();
        initListeners();
    }

    @Override
    public void onEvent(String event) {
        if (!pauseEvents) {
            try {
                events.get(event).run();
            } catch (NullPointerException e) {
                System.err.println("ERREUR : Nom de l'évènement introuvable.");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    @Override
    public void onEntityEvent(Entity entity, String eventName) {
        if (!pauseEvents) {
            Point click = ioEngine.lastClickCoordinates();
            if (click != null) {
                ArrayList<PhysicEntity> entities = physicsEngine.getEntityAtPosition(click.x,click.y,0,0);
                if (entities.contains(entity.getPhysicEntity()))
                    onEvent(eventName);
            }
        }
    }

    @Override
    public void onEntityUpdate(EngineEntity entity) {
        if (!pauseGraphics) {
            if (entity instanceof PhysicEntity) {
                PhysicEntity physicEntity = (PhysicEntity) entity;
                graphicsEngine.move(entity.getParent(), physicEntity.getX(), physicEntity.getY());
                graphicsEngine.resize(entity.getParent(), physicEntity.getWidth(), physicEntity.getHeight());
            }
        }
    }

    /**
     * S'abonner aux évènements des moteurs
     */
    private void initListeners() {
        graphicsEngine.subscribeEvents(this);
        ioEngine.subscribeEvents(this);
        physicsEngine.subscribeEvents(this);
        aiEngine.subscribeEvents(this);
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
        aiEngine.removeEntity(entity);
        entities.remove(entity);
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
     * Mettre en pause les évènements
     */
    public void pauseEvents() {
        pauseEvents = true;
    }

    /**
     * Reprendre l'exécution des évènements
     */
    public void resumeEvents() {
        pauseEvents = false;
    }

    /**
     * Mettre en pause le rafraichissement des grahismes
     */
    public void pauseGraphics() {
        pauseGraphics = true;
    }

    /**
     * Reprendre le rafraichissement des graphismes
     */
    public void resumeGraphics() {
        pauseGraphics = false;
    }

    /**
     * Changer de scène
     * @param scene scène
     */
    public void switchScene(Scene scene) {
        boolean resumeEvents = false;
        if (!isEventsPaused()) {
            pauseEvents();
            resumeEvents = true;
        }
        pauseGraphics();
        updateFocusedEntities(scene);
        graphicsEngine.bindScene(scene);
        resumeGraphics();
        if (resumeEvents)
            resumeEvents();
    }

    /**
     * Exécuter le moteur noyau
     */
    public void start() {
        graphicsEngine.showWindow();
        //Rafraichissement 60 images par seconde
        startTimer("refresh", delay, () -> {
            if (!pauseEvents) {
                ioEngine.updateEntities();
                aiEngine.updateEntities();
                physicsEngine.updateEntites();
            }
            if (!pauseGraphics) graphicsEngine.refreshWindow();
        });
    }

    /**
     * Exécuter une commande de manière périodique
     * @param name nom
     * @param delay délai
     * @param command commande
     */
    public void startTimer(String name, int delay, Runnable command) {
        SwingTimer.getInstance().startTimer(name, delay, command);
    }

    /**
     * Arrêter un timer
     * @param name nom
     */
    public void stopTimer(String name) { SwingTimer.getInstance().stopTimer(name); }

    /**
     * Redémarrer un timer
     * @param name nom
     */
    public void restartTimer(String name) { SwingTimer.getInstance().restartTimer(name); }

    /**
     * Mettre à jour les entités sur lesquelles on a le focus
     */
    public void updateFocusedEntities(Scene scene) {
        graphicsEngine.getEntities().clear();
        physicsEngine.getEntities().clear();
        aiEngine.getEntities().clear();
        for (GraphicEntity entity : scene.getEntities()) {
            graphicsEngine.getEntities().put(entity.parent.getId(),entity.parent.getGraphicEntity());
            physicsEngine.getEntities().put(entity.parent.getId(),entity.parent.getPhysicEntity());
            aiEngine.getEntities().put(entity.parent.getId(),entity.parent.getAiEntity());
        }
    }

    /**
     * Ajouter un évènement au jeu
     * @param name nom de l'évènement
     * @param event évènement
     */
    public void addEvent(String name, Runnable event) {
        events.put(name, event);
    }

    // GETTERS //

    public GraphicsEngine getGraphicsEngine() { return graphicsEngine; }

    public IOEngine getIoEngine() { return ioEngine; }

    public PhysicsEngine getPhysicsEngine() { return physicsEngine; }

    public SoundEngine getSoundEngine() { return soundEngine; }

    public AIEngine getAiEngine() { return aiEngine; }

    public boolean isEventsPaused() { return pauseEvents; }

    public boolean isGraphicsPaused() { return pauseGraphics; }

    public ArrayList<Entity> getEntities() { return entities; }

    public Map<String, Runnable> getEvents() { return events; }
}
