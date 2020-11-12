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
     * Liste des identifiants générés pour les entités
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
        GraphicsEngine.getInstance().createEntity(id);
        IOEngine.getInstance().createEntity(id);
        PhysicsEngine.getInstance().createEntity(id);
    }

    /**
     * Exécuter le jeu
     */
    public static void start() {
        GraphicsEngine.showWindow();
    }
}
