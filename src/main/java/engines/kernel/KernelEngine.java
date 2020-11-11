package engines.kernel;

import engines.graphics.GraphicsEngine;
import engines.input_output.IOEngine;
import engines.physics.PhysicsEngine;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Moteur noyau
 */
public class KernelEngine {
    /**
     * Moteur graphique
     */
    private static GraphicsEngine graphicsEngine = new GraphicsEngine();

    /**
     * Moteur entrées / sorties
     */
    private static IOEngine ioEngine = new IOEngine();

    /**
     * Moteur physique
     */
    private static PhysicsEngine physicsEngine = new PhysicsEngine();

    /**
     * Liste des entités générées
     */
    private static ArrayList<Integer> entities = new ArrayList<>();

    /**
     * Générer un nouvel id pour une entité
     * @return id généré
     */
    public static int generateEntity() {
        int id = entities.isEmpty() ? 1 : Collections.max(entities) + 1;
        entities.add(id);
        initEntity(id);
        return id;
    }

    /**
     * Initialiser une entité
     * @param id identifiant
     */
    private static void initEntity(int id) {
        graphicsEngine.createEntity(id);
        ioEngine.createEntity(id);
        physicsEngine.createEntity(id);
    }

    /**
     * Exécuter le jeu
     */
    public static void start() {
        graphicsEngine.showWindow();
    }
}
