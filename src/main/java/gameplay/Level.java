package gameplay;

import engines.graphics.Color;
import engines.graphics.Scene;
import engines.kernel.Entity;

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

    /**
     * Liste des murs du niveau
     */
    private boolean[][] walls;

    /**
     * Nombre de boules
     */
    private int balls;


    private int gommes;

    /**
     * Chronomètre
     */
    private float timer;

    /**
     * Score actuel
     */
    private int actualScore;

    /**
     * Score du niveau
     */
    private Entity scoreLabel;

    /**
     * Nombre de vies
     */
    private int livesCount = 3;

    /**
     * Barrière blanche
     */
    private Entity fence;

    /**
     * Entités vies
     */
    private Entity[] livesEntity;
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
        this.scene = gameplay.graphicsEngine().generateScene(rows * 30 + 30,cols * 30);
        fillMatrix();
        initScore();
        initLives();
    }

    /**
     * Remplir la matrice
     */
    public void fillMatrix() {
        for (int i = 0; i < matrix.length; i++)
            Arrays.fill(walls[i],false);
        Entity defaultFloor = gameplay.kernelEngine().generateEntity();
        gameplay.physicsEngine().resize(defaultFloor,30,30);
        gameplay.graphicsEngine().bindColor(defaultFloor,0,0,0);
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = defaultFloor.clone();
                gameplay.physicsEngine().move(matrix[row][col], (30 * col), (30 * row));
                gameplay.graphicsEngine().addToScene(scene, matrix[row][col]);
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
        gameplay.physicsEngine().addBoundLimits(pacman,0,0,scene.getWidth(),scene.getHeight());
        pacman.bindDefaultTexture();
        Entity entity = matrix[row][col];
        gameplay.physicsEngine().move(pacman,
                entity.getPhysicEntity().getX(), entity.getPhysicEntity().getY());
        if (!scene.getEntities().contains(pacman.getGraphicEntity()))
            gameplay.graphicsEngine().addToScene(scene, pacman);
    }

    /**
     * Faire apparaitre un fantome à un endroit
     * @param ghost fantome
     * @param row ligne
     * @param col colonne
     */
    public void spawnGhost(Ghost ghost, int row, int col) {
        gameplay.physicsEngine().addBoundLimits(ghost,0,0,scene.getWidth(),scene.getHeight());
        gameplay.physicsEngine().addCollisions(gameplay.getPlayer(), ghost);
        Entity entity = matrix[row][col];
        gameplay.physicsEngine().move(ghost,
                entity.getPhysicEntity().getX(), entity.getPhysicEntity().getY());
        if (!scene.getEntities().contains(ghost.getGraphicEntity()))
            gameplay.graphicsEngine().addToScene(scene, ghost);
    }

    /**
     * Ajouter un mur à une position
     * @param row ligne
     * @param col colonne
     */
    public void addWall(int row, int col) {
        Entity wall = matrix[row][col];
        gameplay.physicsEngine().addCollisions(gameplay.getPlayer(), wall);
        for (Ghost ghost : gameplay.getGhosts().values())
            gameplay.physicsEngine().addCollisions(ghost, wall);
        walls[row][col] = true;
    }

    /**
     * Ajouter la barrière pour les fantomes
     * @param row ligne
     * @param col colonne
     */
    public void addFence(int row, int col) {
        fence = matrix[row][col];
        gameplay.physicsEngine().addCollisions(gameplay.getPlayer(), fence);
        gameplay.graphicsEngine().bindTexture(fence,gameplay.getTexturesFile(),11,6);
    }

    /**
     * Ajouter une boule
     * @param row ligne
     * @param col colonne
     */
    public void addBall(int row, int col) {
        Entity ball = matrix[row][col];
        gameplay.graphicsEngine().bindTexture(ball,gameplay.getTexturesFile(),10,2);
        gameplay.kernelEngine().addEvent("eraseBall" + balls,() -> {
            gameplay.kernelEngine().removeEntity(ball);
            gameplay.soundEngine().playSound(gameplay.getPlayer().getMunchSound());
            updateActualScore(actualScore + 10);
        });
        gameplay.physicsEngine().bindEventOnSameLocation(gameplay.getPlayer(), ball, "eraseBall" + balls);
        ++balls;
    }

    /**
     * Ajouter une grosse boule
     * @param row ligne
     * @param col colonne
     */
    public void addGomme(int row, int col) {
        Entity gomme = matrix[row] [col];
        gameplay.graphicsEngine().bindTexture(gomme, gameplay.getTexturesFile(), 10, 1);
        gameplay.kernelEngine().addEvent("eraseGomme" + gommes, () -> {
            gameplay.kernelEngine().removeEntity(gomme);
            gameplay.soundEngine().playSound(gameplay.getPlayer().getMunchSound());
            updateActualScore(actualScore + 50);
            updateFear(true);
        });
        gameplay.physicsEngine().bindEventOnSameLocation(gameplay.getPlayer(), gomme, "eraseGomme" + gommes);
        ++gommes;
    }

    /**
     * creating invisible target for scattering
     * @param row ligne
     * @param col colonne
     */
    public Entity addTarget(int row, int col){
        Entity target = matrix[row] [col];
        return target;
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
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 12, 5);
                    } else if (isWallUp && isWallRight && isWallDown) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 12, 6);
                    } else if (isWallRight && isWallDown && isWallLeft) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 12, 7);
                    } else if (isWallDown && isWallLeft && isWallUp) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 12, 8);
                    } else if (isWallLeft && isWallUp && isWallRight) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 12, 9);
                    } else if (isWallUp && isWallRight) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 11, 8);
                    } else if (isWallRight && isWallDown) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 11, 10);
                    } else if (isWallDown && isWallLeft) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 11, 11);
                    } else if (isWallLeft && isWallUp) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 11, 9);
                    } else if (isWallUp && isWallDown) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 12, 11);
                    } else if (isWallLeft && isWallRight) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 12, 10);
                    } else if (isWallUp) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 12, 4);
                    } else if (isWallRight) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 12, 1);
                    } else if (isWallDown) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 12, 2);
                    } else if (isWallLeft) {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 12, 3);
                    } else {
                        gameplay.graphicsEngine().bindTexture(wall, gameplay.getTexturesFile(), 11, 7);
                    }
                }
            }
        }
    }

    /**
     * Mettre à jour le score actuel
     * @param score score
     */
    public void updateActualScore(int score) {
        actualScore = score;
        gameplay.graphicsEngine().bindText(scoreLabel,
                "Score : " + actualScore, new Color(255,255,255), 17, false);
    }

    /**
     * Mettre à jour les vies restantes de pacman
     */
    public void updateLives() {
        if (livesCount > 0)
            gameplay.kernelEngine().removeEntity(livesEntity[livesCount-1]);
        livesCount--;
    }

    /**
     * Mettre à jour la peur des fantômes
     * @param fear les fantômes sont appeurés
     */
    public void updateFear(boolean fear) {
        this.gameplay.setGhostFear(fear);
    }

    /**
     * Initialiser le score
     */
    public void initScore() {
        scoreLabel = gameplay.kernelEngine().generateEntity();
        gameplay.physicsEngine().move(scoreLabel, 10, scene.getHeight() - 10);
        gameplay.physicsEngine().resize(scoreLabel, 200, 50);
        gameplay.graphicsEngine().addToScene(scene,scoreLabel);
        updateActualScore(actualScore);
    }

    /**
     * Initialiser les vies
     */
    public void initLives() {
        Entity life = gameplay.kernelEngine().generateEntity();
        gameplay.physicsEngine().resize(life,20,20);
        gameplay.physicsEngine().move(life, scene.getWidth()-110,scene.getHeight()-28);
        gameplay.graphicsEngine().bindTexture(life,
        gameplay.getTexturesFile(),1 , 1);

        Entity life2 = gameplay.kernelEngine().generateEntity();
        gameplay.physicsEngine().resize(life2,20,20);
        gameplay.physicsEngine().move(life2,scene.getWidth()-70,scene.getHeight()-28);
        gameplay.graphicsEngine().bindTexture(life2,
        gameplay.getTexturesFile(),1 , 1);

        Entity life3 = gameplay.kernelEngine().generateEntity();
        gameplay.physicsEngine().resize(life3,20,20);
        gameplay.physicsEngine().move(life3,scene.getWidth()-30,scene.getHeight()-28);
        gameplay.graphicsEngine().bindTexture(life3,
        gameplay.getTexturesFile(),1 , 1);

        gameplay.graphicsEngine().addToScene(scene, life);
        gameplay.graphicsEngine().addToScene(scene, life2);
        gameplay.graphicsEngine().addToScene(scene, life3);

        livesEntity = new Entity[] {life, life2, life3};
    }

    // GETTERS //

    public Scene getScene() { return scene; }

    public int getLivesCount() { return this.livesCount; }

    public int getActualScore() { return actualScore; }

    public Entity getFence() { return fence; }
}
