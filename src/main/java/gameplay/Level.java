package gameplay;

import engines.graphics.Scene;
import engines.kernel.Entity;

import java.util.ArrayList;

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
     * Entités présentes sur le niveau
     */
    private ArrayList<Entity> levelEntities = new ArrayList<>();

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
    private int actualScore;

    /**
     * Constructeur surchargé
     * @param rows nombre de lignes
     * @param cols nombre de colonnes
     */
    protected Level(Gameplay gameplay, int rows, int cols) {
        this.gameplay = gameplay;
        this.matrix = new Entity[rows][cols];
        this.scene = gameplay.graphicsEngine().generateScene((rows - 1) * 30,(cols - 1) * 30);
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
     * @param player joueur
     * @param row ligne
     * @param col colonne
     */
    public void addPlayer(Player player, int row, int col) {
        if (this.player != null) {
            levelEntities.remove(this.player);
            gameplay.graphicsEngine().erase(this.player.getGraphicEntity());
        } else {
            gameplay.physicsEngine().addBoundLimits(player.getPhysicEntity(),0,0,scene.getWidth(),scene.getHeight());
            Entity entity = matrix[row][col];
            gameplay.physicsEngine().move(player.getPhysicEntity(), entity.getGraphicEntity().getX(), entity.getGraphicEntity().getY());
            this.player = player;
            levelEntities.add(player);
            gameplay.graphicsEngine().addToScene(scene, this.player.getGraphicEntity());
        }
    }

    // GETTERS //

    public Entity[][] getMatrix() { return matrix; }

    public float getTimer() { return timer; }

    public int getActualScore() { return actualScore; }

    public Scene getScene() { return scene; }
}
