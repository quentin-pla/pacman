package gameplay;

import engines.AI.AIEngine;
import engines.graphics.Color;
import engines.graphics.GraphicsEngine;
import engines.graphics.Scene;
import engines.input_output.IOEngine;
import engines.kernel.Entity;
import engines.kernel.KernelEngine;
import engines.physics.PhysicEntity;
import engines.physics.PhysicsEngine;
import engines.sound.SoundEngine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Gameplay
 */
public class Gameplay {
    /**
     * Directions de déplacement
     */
    public enum MoveDirection {
        UP,RIGHT,DOWN,LEFT
    }

    /**
     * Moteur noyau
     */
    private final KernelEngine kernelEngine;

    /**
     * Identifiant du fichier contenant les textures du jeu
     */
    private final int textures;

    /**
     * Identifiant scène
     */
    private Scene menuView;

    /**
     * Niveaux disponibles
     */
    private ArrayList<Level> levels;

    /**
     * Joueur
     */
    private Pacman pacman;

    /**
     * Fantomes
     */
    private Map<String,Ghost> ghosts;

    /**
     * Constructeur
     */
    public Gameplay() {
        this.kernelEngine = new KernelEngine();
        this.textures = kernelEngine.getGraphicsEngine().loadSpriteSheet("assets/sprite_sheet.png", 12, 11);
        this.levels = new ArrayList<>();
        initGameplay();
    }

    /**
     * Initialiser le gameplay
     */
    private void initGameplay() {
        //Activation des entrées / sorties
        ioEngine().enableKeyboardIO();
        ioEngine().enableMouseIO();
        //Initialiser les joueurs
        initPlayers();
        //Initialiser les évènements
        initEvents();
        //Initialiser les sons
        initSounds();
        //Initialiser le menu
        initMenu();
        //Initialiser le niveau par défaut
        initDefaultLevel();
    }

    /**
     * Initialiser les évènements du jeu
     */
    private void initEvents() {
        //Jouer un niveau
        kernelEngine.addEvent("playLevel", () -> playLevel(levels.get(0)));
        //Déplacer le fantome rouge
        kernelEngine.addEvent("moveRedGhost", () -> updateGhostDirection(ghosts.get("red")));
        //Déplacer le fantome rouge
        kernelEngine.addEvent("moveBlueGhost", () -> updateGhostDirection(ghosts.get("blue")));
        //Se déplacer vers le haut
        kernelEngine.addEvent("pacmanGoUp", () -> switchPacmanDirection(MoveDirection.UP));
        //Se déplacer vers la droite
        kernelEngine.addEvent("pacmanGoRight", () -> switchPacmanDirection(MoveDirection.RIGHT));
        //Se déplacer vers le bas
        kernelEngine.addEvent("pacmanGoDown", () -> switchPacmanDirection(MoveDirection.DOWN));
        //Se déplacer vers la gauche
        kernelEngine.addEvent("pacmanGoLeft", () -> switchPacmanDirection(MoveDirection.LEFT));
        //Lorsqu'il y a une collision
        kernelEngine.addEvent("pacmanOnCollision", () -> {
            if (pacman.getCurrentAnimationID() != 0)
                if (graphicsEngine().getAnimation(pacman.getCurrentAnimationID()).isPlaying())
                    graphicsEngine().playPauseAnimation(pacman.getCurrentAnimationID());
        });
        //Rejouer l'animation courante
        kernelEngine.addEvent("pacmanPlayCurrentAnimation", () -> {
            if (!graphicsEngine().getAnimation(pacman.getCurrentAnimationID()).isPlaying())
                graphicsEngine().playPauseAnimation(pacman.getCurrentAnimationID());
        });
        ioEngine().bindEventOnLastKey(KeyEvent.VK_UP, "pacmanGoUp");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_RIGHT, "pacmanGoRight");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_DOWN, "pacmanGoDown");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_LEFT, "pacmanGoLeft");
        physicsEngine().bindEventOnCollision(pacman.getPhysicEntity(), "pacmanOnCollision");
        aiEngine().bindEvent(ghosts.get("red"), "moveRedGhost");
        aiEngine().bindEvent(ghosts.get("blue"), "moveBlueGhost");
    }

    /**
     * Initialiser les sons du jeu
     */
    private void initSounds() {
        soundEngine().loadSound("munch_1.wav","munch1");
        soundEngine().loadSound("munch_2.wav","munch2");
        soundEngine().loadSound("game_start.wav","gameStart");
    }

    /**
     * Initialiser le menu
     */
    private void initMenu() {
        menuView = graphicsEngine().generateScene(400,400);
        Entity button = kernelEngine.generateEntity();
        physicsEngine().resize(button,100,50);
        physicsEngine().move(button.getPhysicEntity(), 150,240);
        graphicsEngine().bindColor(button,50,50,50);
        graphicsEngine().bindText(button, "PLAY", new Color(255,255,255), 20, true);
        graphicsEngine().addToScene(menuView, button);
        ioEngine().bindEventOnClick(button,"playLevel");
        Entity menuLogo = kernelEngine.generateEntity();
        int logoTexture = kernelEngine.getGraphicsEngine().loadTexture("assets/menu_logo.png");
        physicsEngine().resize(menuLogo,300,71);
        physicsEngine().move(menuLogo.getPhysicEntity(), 50,120);
        graphicsEngine().bindTexture(menuLogo,logoTexture);
        graphicsEngine().addToScene(menuView, menuLogo);
    }

    /**
     * Initialiser les joueurs
     */
    private void initPlayers() {
        pacman = new Pacman(this);
        ghosts = new HashMap<>();
        ghosts.put("red", new Ghost(this, "red"));
        ghosts.put("blue", new Ghost(this, "blue"));
        ghosts.put("pink", new Ghost(this, "pink"));
        ghosts.put("orange", new Ghost(this, "orange"));
    }

    /**
     * Initialiser le niveau par défaut
     */
    private void initDefaultLevel() {
        //Level par défaut
        Level defaultLevel = generateLevel(21,19);

        //Génération des murs
        Map<Integer,int[]> wallRows = new HashMap<>();

        wallRows.put(0, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18});
        wallRows.put(1, new int[]{0, 9, 18});
        wallRows.put(2, new int[]{0, 2, 3, 5, 6, 7, 9, 11, 12, 13, 15, 16, 18});
        wallRows.put(3, new int[]{0, 18});
        wallRows.put(4, new int[]{0, 2, 3, 5, 7, 8, 9, 10, 11, 13, 15, 16, 18});
        wallRows.put(5, new int[]{0, 5, 9, 13, 18});
        wallRows.put(6, new int[]{0, 1, 2, 3, 5, 6, 7, 9, 11, 12, 13, 15, 16, 17, 18});
        wallRows.put(7, new int[]{3, 5, 13, 15});
        wallRows.put(8, new int[]{0, 1, 2, 3, 5, 7, 8, 10, 11, 13, 15, 16, 17, 18});
        wallRows.put(9, new int[]{7, 11});
        wallRows.put(10, new int[]{0, 1, 2, 3, 5, 7, 8, 9, 10, 11, 13, 15, 16, 17, 18});
        wallRows.put(11, new int[]{3, 5, 13, 15});
        wallRows.put(12, new int[]{0, 1, 2, 3, 5, 7, 8, 9, 10, 11, 13, 15, 16, 17, 18});
        wallRows.put(13, new int[]{0, 9, 18});
        wallRows.put(14, new int[]{0, 2, 3, 5, 6, 7, 9, 11, 12, 13, 15, 16, 18});
        wallRows.put(15, new int[]{0, 3, 15, 18});
        wallRows.put(16, new int[]{0, 1, 3, 5, 7, 8, 9, 10, 11, 13, 15, 17, 18});
        wallRows.put(17, new int[]{0, 5, 9, 13, 18});
        wallRows.put(18, new int[]{0, 2, 3, 4, 5, 6, 7, 9, 11, 12, 13, 14, 15, 16, 18});
        wallRows.put(19, new int[]{0, 18});
        wallRows.put(20, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18});

        for (Map.Entry<Integer,int[]> row : wallRows.entrySet())
            for (int col : row.getValue())
                defaultLevel.addWall(row.getKey(),col);

        defaultLevel.applyWallTextures();

        //Génération des balles
        Map<Integer,int[]> ballRows = new HashMap<>();

        ballRows.put(1, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17});
        ballRows.put(2, new int[]{1, 4, 8, 10, 14, 17});
        ballRows.put(3, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17});
        ballRows.put(4, new int[]{1, 4, 6, 12, 14, 17});
        ballRows.put(5, new int[]{1, 2, 3, 4, 6, 7, 8, 10, 11, 12, 14, 15, 16, 17});
        ballRows.put(6, new int[]{4, 8, 10, 14});
        ballRows.put(7, new int[]{4, 6, 7, 8, 9, 10, 11, 12, 14});
        ballRows.put(8, new int[]{4, 6, 12, 14});
        ballRows.put(9, new int[]{4, 5, 6, 12, 13, 14});
        ballRows.put(10, new int[]{4, 6, 12, 14});
        ballRows.put(11, new int[]{4, 6, 7, 8, 9, 10, 11, 12, 14});
        ballRows.put(12, new int[]{4, 6, 12, 14});
        ballRows.put(13, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17});
        ballRows.put(14, new int[]{1, 4, 8, 10, 14, 17});
        ballRows.put(15, new int[]{1, 2, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 16, 17});
        ballRows.put(16, new int[]{2, 4, 6, 12, 14, 16});
        ballRows.put(17, new int[]{1, 2, 3, 4, 6, 7, 8, 10, 11, 12, 14, 15, 16, 17});
        ballRows.put(18, new int[]{1, 8, 10, 17});
        ballRows.put(19, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17});

        for (Map.Entry<Integer,int[]> row : ballRows.entrySet())
            for (int col : row.getValue())
                defaultLevel.addBall(row.getKey(),col);

        //Ajout de la barrière
        defaultLevel.addFence(8, 9);

        levels.add(defaultLevel);
    }

    /**
     * Mettre à jour la position d'un fanôme
     * @param ghost fantôme
     */
    protected void updateGhostDirection(Ghost ghost) {
        PhysicEntity playerPhysic = pacman.getPhysicEntity();
        PhysicEntity ghostPhysic = ghost.getPhysicEntity();

        int playerXmiddle = (playerPhysic.getX() + playerPhysic.getWidth()) / 2;
        int playerYmiddle = (playerPhysic.getY() + playerPhysic.getHeight()) / 2;
        int ghostXmiddle = (ghostPhysic.getX() + ghostPhysic.getWidth()) / 2;
        int ghostYmiddle = (ghostPhysic.getY() + ghostPhysic.getHeight()) / 2;
        int xDistance = playerXmiddle - ghostXmiddle;
        int yDistance = playerYmiddle - ghostYmiddle;

        MoveDirection xDirection = xDistance == 0 ? null : xDistance < 0 ? MoveDirection.LEFT : MoveDirection.RIGHT;
        MoveDirection yDirection = yDistance == 0 ? null : yDistance < 0 ? MoveDirection.UP : MoveDirection.DOWN;

        MoveDirection nextDirection = Math.abs(xDistance) > Math.abs(yDistance) ? xDirection : yDirection;

        boolean somethingUP     = physicsEngine().isSomethingUp(ghost.getPhysicEntity()) != null;
        boolean somethingRIGHT  = physicsEngine().isSomethingRight(ghost.getPhysicEntity()) != null;
        boolean somethingDOWN   = physicsEngine().isSomethingDown(ghost.getPhysicEntity()) != null;
        boolean somethingLEFT   = physicsEngine().isSomethingLeft(ghost.getPhysicEntity()) != null;

        if (yDirection == MoveDirection.UP && ghost.getLastDirection() != MoveDirection.UP && !somethingUP) {
            ghost.setCurrentDirection(MoveDirection.UP);
            ghost.setLastDirection(null);
        }
        else if (yDirection == MoveDirection.DOWN && ghost.getLastDirection() != MoveDirection.DOWN && !somethingDOWN) {
            ghost.setCurrentDirection(MoveDirection.DOWN);
            ghost.setLastDirection(null);
        }
        else if (xDirection == MoveDirection.LEFT && ghost.getLastDirection() != MoveDirection.LEFT && !somethingLEFT) {
            ghost.setCurrentDirection(MoveDirection.LEFT);
            ghost.setLastDirection(null);
        }
        else if (xDirection == MoveDirection.RIGHT && ghost.getLastDirection() != MoveDirection.RIGHT && !somethingRIGHT) {
            ghost.setCurrentDirection(MoveDirection.RIGHT);
            ghost.setLastDirection(null);
        }
        else {
            if (nextDirection == MoveDirection.UP && somethingUP || nextDirection == MoveDirection.DOWN && somethingDOWN) {
                if (!somethingLEFT) {
                    ghost.setLastDirection(MoveDirection.RIGHT);
                    ghost.setCurrentDirection(MoveDirection.LEFT);
                } else if (!somethingRIGHT) {
                    ghost.setLastDirection(MoveDirection.LEFT);
                    ghost.setCurrentDirection(MoveDirection.RIGHT);
                } else {
                    if (nextDirection == MoveDirection.UP) ghost.setCurrentDirection(MoveDirection.DOWN);
                    else ghost.setCurrentDirection(MoveDirection.UP);
                    ghost.setLastDirection(nextDirection);
                }
            } else if (nextDirection == MoveDirection.LEFT && somethingLEFT || nextDirection == MoveDirection.RIGHT && somethingRIGHT) {
                if (!somethingUP) {
                    ghost.setLastDirection(MoveDirection.DOWN);
                    ghost.setCurrentDirection(MoveDirection.UP);
                } else if (!somethingDOWN) {
                    ghost.setLastDirection(MoveDirection.UP);
                    ghost.setCurrentDirection(MoveDirection.DOWN);
                } else {
                    if (nextDirection == MoveDirection.LEFT) ghost.setCurrentDirection(MoveDirection.RIGHT);
                    else ghost.setCurrentDirection(MoveDirection.LEFT);
                    ghost.setLastDirection(nextDirection);
                }
            }
        }

        if (Math.abs(xDistance) <= 1 && Math.abs(yDistance) <= 1) {
            ghost.setLastDirection(null);
            ghost.setCurrentDirection(null);
        }

        if (ghost.getCurrentDirection() != null) {
            callEventFromDirection(ghost, ghost.getCurrentDirection());
            graphicsEngine().bindAnimation(ghost, ghost.getAnimations().get(ghost.getCurrentDirection().name()));
        }
    }

    /**
     * Changer la direction de pacman
     * @param direction direction
     */
    protected void switchPacmanDirection(MoveDirection direction) {
        pacman.setCurrentAnimationID(pacman.getAnimations().get(direction.name()));
        kernelEngine().notifyEvent("pacmanPlayCurrentAnimation");
        setEntityNextDirection(pacman,direction);
    }

    /**
     * Déterminer la prochaine direction de l'entité
     * @param entity entité
     * @param direction direction
     */
    private void setEntityNextDirection(Player entity, MoveDirection direction) {
        PhysicEntity entityNearby;
        switch (direction) {
            case UP:
                entityNearby = physicsEngine().isSomethingUp(entity.getPhysicEntity());
                if (entityNearby == null)
                    entity.setCurrentDirection(MoveDirection.UP);
                break;
            case RIGHT:
                entityNearby = physicsEngine().isSomethingRight(entity.getPhysicEntity());
                if (entityNearby == null)
                    entity.setCurrentDirection(MoveDirection.RIGHT);
                break;
            case DOWN:
                entityNearby = physicsEngine().isSomethingDown(entity.getPhysicEntity());
                if (entityNearby == null)
                    entity.setCurrentDirection(MoveDirection.DOWN);
                break;
            case LEFT:
                entityNearby = physicsEngine().isSomethingLeft(entity.getPhysicEntity());
                if (entityNearby == null)
                    entity.setCurrentDirection(MoveDirection.LEFT);
                break;
            default:
                break;
        }
        if (entity.getCurrentDirection() != null) {
            callEventFromDirection(entity, entity.getCurrentDirection());
            graphicsEngine().bindAnimation(entity, entity.getAnimations().get(entity.getCurrentDirection().name()));
        }
    }

    /**
     * Appeler la méthode de déplacement en fonction de la direction courante
     */
    private void callEventFromDirection(Entity entity, MoveDirection direction) {
        if (direction != null) {
            switch (direction) {
                case UP:
                    physicsEngine().goUp(entity.getPhysicEntity());
                    break;
                case RIGHT:
                    physicsEngine().goRight(entity.getPhysicEntity());
                    break;
                case DOWN:
                    physicsEngine().goDown(entity.getPhysicEntity());
                    break;
                case LEFT:
                    physicsEngine().goLeft(entity.getPhysicEntity());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Générer un niveau
     * @param rows nombre de lignes
     * @param cols nombre de colonnes
     */
    public Level generateLevel(int rows, int cols) {
        Level level = new Level(this,rows,cols);
        this.levels.add(level);
        return level;
    }

    /**
     * Jouer un niveau
     * @param level level
     */
    public void playLevel(Level level) {
        soundEngine().playSound("gameStart");
        level.spawnPlayer(15,9);
        level.spawnGhost(ghosts.get("red"),7,9);
        level.spawnGhost(ghosts.get("blue"),9,8);
        level.spawnGhost(ghosts.get("pink"),9,9);
        level.spawnGhost(ghosts.get("orange"),9,10);
        graphicsEngine().bindScene(level.getScene());
    }

    /**
     * Lancer le jeu
     */
    public void start() {
        graphicsEngine().bindScene(menuView);
        kernelEngine.start();
    }

    // GETTERS //

    public int getTexturesFile() { return textures; }

    public KernelEngine kernelEngine() { return kernelEngine; }

    public GraphicsEngine graphicsEngine() { return kernelEngine.getGraphicsEngine(); }

    public IOEngine ioEngine() { return kernelEngine.getIoEngine(); }

    public PhysicsEngine physicsEngine() { return kernelEngine.getPhysicsEngine(); }

    public SoundEngine soundEngine() { return kernelEngine.getSoundEngine(); }

    public AIEngine aiEngine() { return kernelEngine.getAiEngine(); }

    public Pacman getPlayer() { return pacman; }

    public Map<String,Ghost> getGhosts() { return ghosts; }

    public ArrayList<Level> getLevels() { return levels; }
}
