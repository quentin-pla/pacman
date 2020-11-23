package gameplay;

import engines.graphics.GraphicsEngine;
import engines.kernel.Entity;
import engines.kernel.KernelEngine;
import engines.physics.PhysicsEngine;

import java.util.ArrayList;

/**
 * Niveau de jeu
 */
public class Level extends Entity {
    /**
     * Matrice d'entités graphiques
     */
    private Entity[][] matrix;

    /**
     * Entités présentes sur le niveau
     */
    private ArrayList<Entity> level_entities = new ArrayList<>();

    /**
     * Joueur
     */
    private Player player;

    /**
     * Chronomètre
     */
    private float timer;

    /**
     * Score actuel
     */
    private int actual_score;

    /**
     * Constructeur surchargé
     * @param rows nombre de lignes
     * @param cols nombre de colonnes
     */
    public Level(int rows, int cols) {
        super();
        PhysicsEngine.resize(getPhysicEntity(),(rows - 1) * 30, (cols - 1) * 30);
        this.matrix = new Entity[rows][cols];
        fill();
    }

    /**
     * Initialiser les évènements
     */
    public void initEvents() {
        KernelEngine.addEvent("drawLevel", () -> {
            for (Entity entity : level_entities)
                GraphicsEngine.draw(entity.getGraphicEntity());
        });
        KernelEngine.addEvent("updateLevel", () -> {
            for (Entity entity : level_entities)
                GraphicsEngine.update(entity.getGraphicEntity());
        });
    }

    /**
     * Remplir la matrice
     */
    public void fill() {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = KernelEngine.generateEntity();
                PhysicsEngine.move(matrix[row][col].getPhysicEntity(), (30 * col), (30 * row));
                level_entities.add(matrix[row][col]);
            }
        }
    }

    /**
     * Faire apparaitre le joueur à un endroit
     * @param player joueur
     * @param row ligne
     * @param col colonne
     */
    public void addPlayer(Player player, int row, int col) {
        if (this.player == null) {
            PhysicsEngine.addBoundLimits(player.getPhysicEntity(), getBounds()[0], getBounds()[1], getBounds()[2], getBounds()[3]);
            Entity entity = matrix[row][col];
            PhysicsEngine.move(player.getPhysicEntity(), entity.getGraphicEntity().getX(), entity.getGraphicEntity().getY());
            this.player = player;
            level_entities.add(player);
        }
    }

    /**
     * Récupérer les limites du niveau
     * @return limites
     */
    public int[] getBounds() {
        return getPhysicEntity().getBounds();
    }


//    @Override
//    public void translate(int x, int y) {
//        super.translate(x, y);
//        //Translation des objets du niveau
//        for (Entity entity : level_entities) entity.translate(x, y);
//        //Mise à jour des limites de déplacement pour le joueur
//        if (player != null) player.addMoveBounds(getBounds());
//    }
//
//    @Override
//    public void move(int x, int y) {
//        super.move(x,y);
//        //Translation des objets du niveau
//        for (Entity entity : level_entities) entity.translate(x, y);
//        //Mise à jour des limites de déplacement pour le joueur
//        if (player != null) player.addMoveBounds(getBounds());
//    }
//
//    // GETTERS //
//
//    public Entity[][] getMatrix() {
//        return matrix;
//    }
//
//    public ArrayList<Entity> getLevel_entities() {
//        return level_entities;
//    }
//
//    public Player getPlayer() {
//        return player;
//    }
//
//    public float getTimer() {
//        return timer;
//    }
//
//    public int getActual_score() {
//        return actual_score;
//    }
}
