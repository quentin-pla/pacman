package gameplay;

import engines.graphics.Scene;
import engines.graphics.Text;
import engines.kernel.Entity;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

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

    private boolean[][] walls;

    /**
     * Nombre de boules
     */
    private int balls;

    /**
     * Chronomètre
     */
    private float timer;

    /**
     * Score actuel
     */
    private int actualScore;

    private JLabel scoreLabel;

    /**
     * Constructeur surchargé
     * @param rows nombre de lignes
     * @param cols nombre de colonnes
     */
    protected Level(Gameplay gameplay, int rows, int cols) {
        this.gameplay = gameplay;
        this.matrix = new Entity[rows][cols];
        this.walls = new boolean[rows][cols];
        this.actualScore = 0;
        for (int i = 0; i < rows; i++)
            Arrays.fill(walls[i],false);
        this.scene = gameplay.graphicsEngine().generateScene(rows * 30 + 70,cols * 30);
        fillMatrix();

        this.scoreLabel = new JLabel("Score : " + Integer.toString(this.actualScore));
        this.addScore();
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

    /**
     * Ajouter un mur à une position
     * @param row ligne
     * @param col colonne
     */
    public void addWall(int row, int col) {
        Entity wall = matrix[row][col];
        gameplay.physicsEngine().addCollisions(gameplay.getPlayer().getPhysicEntity(), wall.getPhysicEntity());
        for (Ghost ghost : gameplay.getGhosts().values())
            gameplay.physicsEngine().addCollisions(ghost.getPhysicEntity(), wall.getPhysicEntity());
        walls[row][col] = true;
    }

    /**
     * Ajouter une boule
     * @param row ligne
     * @param col colonne
     */
    public void addBall(int row, int col) {
        Entity ball = matrix[row][col];
        gameplay.graphicsEngine().bindTexture(ball.getGraphicEntity(),gameplay.getTexturesFile(),10,2);
        gameplay.kernelEngine().addEvent("eraseBall" + balls,() -> {
            gameplay.kernelEngine().removeEntity(ball);
            gameplay.soundEngine().playSound("munch");
            this.setActualScore(this.actualScore + 10);
        });
        gameplay.physicsEngine().bindEventOnSameLocation(gameplay.getPlayer().getPhysicEntity(), ball.getPhysicEntity(), "eraseBall" + balls);
        ++balls;
    }

    /**
     * Appliquer la texture de chaque mur
     */
    public void applyWallTextures() {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if (walls[row][col]) {
                    Entity wall = matrix[row][col];
                    boolean isWallUp = row > 0 && walls[row - 1][col];
                    boolean isWallRight = col < walls[0].length - 1 && walls[row][col + 1];
                    boolean isWallDown = row < walls.length - 1 && walls[row + 1][col];
                    boolean isWallLeft = col > 0 && walls[row][col - 1];
                    if (isWallUp && isWallRight && isWallDown && isWallLeft) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 12, 5);
                    } else if (isWallUp && isWallRight && isWallDown) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 12, 6);
                    } else if (isWallRight && isWallDown && isWallLeft) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 12, 7);
                    } else if (isWallDown && isWallLeft && isWallUp) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 12, 8);
                    } else if (isWallLeft && isWallUp && isWallRight) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 12, 9);
                    } else if (isWallUp && isWallRight) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 11, 8);
                    } else if (isWallRight && isWallDown) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 11, 10);
                    } else if (isWallDown && isWallLeft) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 11, 11);
                    } else if (isWallLeft && isWallUp) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 11, 9);
                    } else if (isWallUp && isWallDown) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 12, 11);
                    } else if (isWallLeft && isWallRight) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 12, 10);
                    } else if (isWallUp) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 12, 4);
                    } else if (isWallRight) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 12, 1);
                    } else if (isWallDown) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 12, 2);
                    } else if (isWallLeft) {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 12, 3);
                    } else {
                        gameplay.graphicsEngine().bindTexture(wall.getGraphicEntity(), gameplay.getTexturesFile(), 11, 7);
                    }
                }
            }
        }
    }

    public void setActualScore(int score) {
        this.actualScore = score;
        this.scoreLabel.setText("Score : " + Integer.toString(this.actualScore));
    }

    public void addScore() {
        this.scene.setLayout(null);
        this.scene.add(scoreLabel);
        scoreLabel.setLocation(10,this.scene.getHeight()-80);
        scoreLabel.setSize(100,50);
        scoreLabel.setFont(new Font("Plain", Font.PLAIN, 17));
        scoreLabel.setForeground(Color.WHITE);
    }

    // GETTERS //

    public Entity[][] getMatrix() { return matrix; }

    public float getTimer() { return timer; }

    public int getActualScore() { return actualScore; }

    public Scene getScene() { return scene; }
}
