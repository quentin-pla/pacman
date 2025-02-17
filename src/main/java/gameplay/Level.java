package gameplay;

import engines.graphics.Color;
import engines.graphics.Scene;
import engines.kernel.Entity;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Niveau de jeu
 */
public class Level {
    /**
     * Gameplay
     */
    private final Gameplay gameplay;

    /**
     * Scène liée
     */
    private final Scene scene;

    /**
     * Matrice d'entités graphiques
     */
    private final Entity[][] matrix;

    /**
     * Liste des murs du niveau
     */
    private boolean[][] walls;

    /**
     * Murs déjà cassés
     */
    private boolean wallsAlreadyBroken;

    /**
     * Nombre de boules
     */
    private int balls;

    /**
     * Nombre de gommes
     */
    private int gommes;

    /**
     * Nombre de breakers
     */
    private int breakers;

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
    private final AtomicInteger livesCount;

    /**
     * Barrière blanche
     */
    private Entity fence;

    /**
     * Entités vies
     */
    private Entity[] livesEntity;

    /**
     * Taille par défaut d'une entité de la matrice
     */
    private final int defaultEntitySize = 30;

    /**
     * Constructeur
     * @param rows nombre de lignes
     * @param cols nombre de colonnes
     */
    protected Level(Gameplay gameplay, int rows, int cols) {
        this.gameplay = gameplay;
        this.matrix = new Entity[rows][cols];
        this.walls = new boolean[rows][cols];
        this.wallsAlreadyBroken = false;
        this.actualScore = 0;
        this.livesCount = new AtomicInteger(3);
        this.scene = gameplay.graphicsEngine().generateScene(rows * defaultEntitySize
                + 30, (cols * defaultEntitySize));
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
        gameplay.physicsEngine().resize(defaultFloor,defaultEntitySize,defaultEntitySize);
        gameplay.graphicsEngine().bindColor(defaultFloor,0,0,0);
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = defaultFloor.clone();
                gameplay.physicsEngine().move(matrix[row][col], (defaultEntitySize * col), (defaultEntitySize * row));
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
        if (!scene.getEntities().contains(pacman.getGraphicEntity()))
            gameplay.graphicsEngine().addToScene(scene, pacman);
        pacman.bindDefaultTexture();
        Entity entity = matrix[row][col];
        gameplay.physicsEngine().move(pacman,
                entity.getPhysicEntity().getX(), entity.getPhysicEntity().getY());
    }

    /**
    /**
     * Faire apparaitre un fantome à un endroit
     * @param ghost fantome
     * @param row ligne
     * @param col colonne
     */
    public void spawnGhost(Ghost ghost, int row, int col) {
        if (!scene.getEntities().contains(ghost.getGraphicEntity())) {
            gameplay.kernelEngine().addEvent("pacman" + ghost.getColor() + "ghostCollision",
                    () -> gameplay.pacmanGhostCollision(ghost));
            gameplay.physicsEngine().bindEventOnCollision(gameplay.getPlayer(), ghost,
                    "pacman" + ghost.getColor() + "ghostCollision");
            gameplay.graphicsEngine().addToScene(scene, ghost);
        }
        Entity entity = matrix[row][col];
        gameplay.physicsEngine().move(ghost,
                entity.getPhysicEntity().getX(), entity.getPhysicEntity().getY());
    }

    /**
     * Ajouter un mur à une position
     * @param row ligne
     * @param col colonne
     */
    public void addWall(int row, int col) {
        Entity wall = matrix[row][col];

        gameplay.physicsEngine().addCollisions(gameplay.getPlayer(), wall);
        gameplay.kernelEngine().addEvent("pacman" + wall + "breakCollision",
                () -> gameplay.pacmanWallCollision(wall));
        gameplay.physicsEngine().bindEventOnCollision(gameplay.getPlayer(), wall, "pacman" + wall + "breakCollision");

        for (Ghost ghost : gameplay.getGhosts().values())
            gameplay.physicsEngine().addCollisions(ghost, wall);

        walls[row][col] = true;
    }

    /**
     * Ajouter un portail de téléportation
     * @param row ligne
     * @param col colonne
     * @param direction direction
     * @param teleportCol colonne sur laquelle se téléporter
     * @param teleportRow ligne sur laquelle se téléporter
     */
    public void addTeleportationPortal(int row, int col, int teleportRow, int teleportCol, Gameplay.MoveDirection direction) {
        Entity portal = matrix[row][col];
        Entity teleportLocation = matrix[teleportRow][teleportCol];

        int teleportX = teleportLocation.getPhysicEntity().getX();
        int teleportY = teleportLocation.getPhysicEntity().getY();

        gameplay.kernelEngine().addEvent("teleport" + portal + gameplay.getPlayer(), () -> {
            gameplay.teleportPlayer(gameplay.getPlayer(), teleportX, teleportY, direction);
        });
        gameplay.physicsEngine().bindEventOnSameLocation(gameplay.getPlayer(), portal,
                "teleport" + portal + gameplay.getPlayer());

        for (Ghost ghost : gameplay.getGhosts().values()) {
            gameplay.kernelEngine().addEvent("teleport" + portal + ghost,
                    () -> gameplay.teleportPlayer(ghost, teleportX, teleportY, direction));
            gameplay.physicsEngine().bindEventOnSameLocation(ghost, portal, "teleport" + portal + ghost);
        }
    }

    /**
     * Ajouter la barrière pour les fantomes
     * @param row ligne
     * @param col colonne
     */
    public void addFence(int row, int col) {
        fence = matrix[row][col];
        gameplay.physicsEngine().addCollisions(gameplay.getPlayer(), fence);
        for (Ghost ghost : gameplay.getGhosts().values())
            gameplay.physicsEngine().addCollisions(ghost, fence);
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
            --balls;
            if (balls == 0) {
                gameplay.showEndGameView("YOU WIN !", new Color(0,255,0));
                gameplay.soundEngine().playSound("win");
            }
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
            updateActualScore(actualScore + 50);
            gameplay.enableEatPowerUP();
        });
        gameplay.physicsEngine().bindEventOnSameLocation(gameplay.getPlayer(), gomme, "eraseGomme" + gommes);
        ++gommes;
    }


    /**
     * Ajouter un breaker
     * @param row ligne
     * @param col colonne
     */

    public void addBreaker(int row, int col) {
        Entity breaker = matrix[row] [col];
        gameplay.graphicsEngine().bindTexture(breaker, gameplay.getTexturesFile(), 9, 6);
        gameplay.kernelEngine().addEvent("eraseBreaker" + breakers, () -> {
            gameplay.kernelEngine().removeEntity(breaker);
            updateActualScore(actualScore + 80);
            gameplay.enableBreakPowerUp();
        });
        gameplay.physicsEngine().bindEventOnSameLocation(gameplay.getPlayer(), breaker, "eraseBreaker" + breakers);
        ++breakers;

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
        livesCount.getAndDecrement();
        if (livesCount.get() > 0)
            gameplay.kernelEngine().removeEntity(livesEntity[livesCount.get()]);
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

    /**
     * Définir la partie visible du niveau
     * @param x position horizontale
     * @param y position verticale
     * @param height hauteur
     * @param width largeur
     */
    public void setVisiblePart(int x, int y, int height, int width) {
        int xDifference = x > scene.getX() ? -x : x;
        int yDifference = y > scene.getY() ? -y : y;
        gameplay.graphicsEngine().setSceneSize(scene, height + yDifference, width + xDifference);
        gameplay.graphicsEngine().setSceneLocation(scene, xDifference, yDifference);
        gameplay.physicsEngine().translate(scoreLabel, xDifference * -1, yDifference * -1);
        for (Entity liveEntity : livesEntity)
            gameplay.physicsEngine().translate(liveEntity, xDifference, yDifference);
    }

    /**
     * Obtenir la position d'une entité dans la matrice
     * @param entity entité
     * @return position de l'entité
     */
    public int[] getMatrixEntityPosition(Entity entity) {
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                if (matrix[i][j] == entity)
                    return new int[]{i, j};
        return null;
    }

    /**
     * Obtenir la position dans la matrice d'une entité externe à la matrice
     * @param entity entité
     * @return position
     */
    public int[] getEntityPosition(Entity entity) {
        int row = entity.getPhysicEntity().getY()/defaultEntitySize;
        int col = entity.getPhysicEntity().getX()/defaultEntitySize;
        return new int[]{row, col};
    }

    /**
     * Obtenir une entité à une position spécifique dans la matrice
     * @param row ligne
     * @param col colonne
     * @return entité
     */
    public Entity getMatrixEntity(int row, int col){
        return matrix[row][col];
    }

    // GETTERS //

    public Scene getScene() { return scene; }

    public AtomicInteger getLivesCount() { return this.livesCount; }

    public int getActualScore() { return actualScore; }

    public Entity getFence() { return fence; }

    public boolean[][] getWalls() { return walls; }

    public boolean isWallsAlreadyBroken() {
        return wallsAlreadyBroken;
    }

    public void setWallsAlreadyBroken(boolean wallsAlreadyBroken) {
        this.wallsAlreadyBroken = wallsAlreadyBroken;
    }
}
