package gameplay;

import engines.graphics.Scene;
import engines.kernel.Entity;

/**
 * Niveau de jeu
 */
public class Level {
    /**
     * Gameplay
     */
    private Gameplay gameplay;

    /**
     * Scène liée
     */
    private Scene scene;

    /**
     * Matrice d'entités graphiques
     */
    private Entity[][] matrix;

    /**
     * Chronomètre
     */
    private float timer;

    /**
     * Score actuel
     */
    private int actualScore;

    /**
     * Constructeur surchargé
     * @param rows nombre de lignes
     * @param cols nombre de colonnes
     */
    protected Level(Gameplay gameplay, int rows, int cols) {
        this.gameplay = gameplay;
        this.matrix = new Entity[rows][cols];
        this.scene = gameplay.graphicsEngine().generateScene(rows * 30,cols * 30);
        fillMatrix();
    }

    /**
     * Remplir la matrice
     */
    public void fillMatrix() {
        Entity defaultFloor = gameplay.kernelEngine().generateEntity();
        gameplay.physicsEngine().resize(defaultFloor.getPhysicEntity(),30,30);
        gameplay.graphicsEngine().bindColor(defaultFloor.getGraphicEntity(),0,0,0);
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = defaultFloor.clone();
                gameplay.physicsEngine().move(matrix[row][col].getPhysicEntity(), (30 * col), (30 * row));
                gameplay.graphicsEngine().addToScene(scene, matrix[row][col].getGraphicEntity());
            }
        }
    }

    /**
     * Faire apparaitre le joueur à un endroit
     * @param row ligne
     * @param col colonne
     */
    public void spawnPlayer(int row, int col) {
        Pacman pacman = gameplay.getPlayer();
        gameplay.physicsEngine().addBoundLimits(pacman.getPhysicEntity(),0,0,scene.getWidth(),scene.getHeight());
        Entity entity = matrix[row][col];
        gameplay.physicsEngine().move(pacman.getPhysicEntity(), entity.getGraphicEntity().getX(), entity.getGraphicEntity().getY());
        gameplay.graphicsEngine().addToScene(scene, pacman.getGraphicEntity());
    }

    /**
     * Faire apparaitre un fantome à un endroit
     * @param ghost fantome
     * @param row ligne
     * @param col colonne
     */
    public void spawnGhost(Ghost ghost, int row, int col) {
        gameplay.physicsEngine().addBoundLimits(ghost.getPhysicEntity(),0,0,scene.getWidth(),scene.getHeight());
        Entity entity = matrix[row][col];
        gameplay.physicsEngine().move(ghost.getPhysicEntity(), entity.getGraphicEntity().getX(), entity.getGraphicEntity().getY());
        gameplay.graphicsEngine().addToScene(scene, ghost.getGraphicEntity());
    }

    // GETTERS //

    public Entity[][] getMatrix() { return matrix; }

    public float getTimer() { return timer; }

    public int getActualScore() { return actualScore; }

    public Scene getScene() { return scene; }
}
