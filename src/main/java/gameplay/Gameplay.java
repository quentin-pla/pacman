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
     * Affichage du volume
     */
    private Entity currentVolume;
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
        //Augmenter le volume
        kernelEngine.addEvent("augmentVolume", this::incrementGlobalVolume);
        //Baisser le volume
        kernelEngine.addEvent("downVolume", this::decrementGlobalVolume);

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
        Entity volumePlus = kernelEngine.generateEntity();
        Entity volumeMinus = kernelEngine.generateEntity();
        currentVolume = kernelEngine.generateEntity();

        physicsEngine().resize(volumePlus,50,25);
        physicsEngine().move(volumePlus.getPhysicEntity(), 20,350);
        graphicsEngine().bindColor(volumePlus,50,50,50);
        graphicsEngine().bindText(volumePlus, "-", new Color(255,255,255), 20, true);
        graphicsEngine().addToScene(menuView, volumePlus);
        ioEngine().bindEventOnClick(volumePlus,"downVolume");

        physicsEngine().resize(volumeMinus,50,25);
        physicsEngine().move(volumeMinus.getPhysicEntity(), 330,350);
        graphicsEngine().bindColor(volumeMinus,50,50,50);
        graphicsEngine().bindText(volumeMinus, "+", new Color(255,255,255), 20, true);
        graphicsEngine().addToScene(menuView, volumeMinus);
        ioEngine().bindEventOnClick(volumeMinus,"augmentVolume");

        physicsEngine().resize(currentVolume,200,50);
        physicsEngine().move(currentVolume.getPhysicEntity(), 100,337);
        graphicsEngine().bindColor(currentVolume,50,50,50);
        graphicsEngine().bindText(currentVolume, "Volume is : " + (int) soundEngine().getGlobalvolume()*100, new Color(255,255,255), 20, true);
        graphicsEngine().addToScene(menuView, currentVolume);

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
        for (int j = 0; j < 19; j++) {
            defaultLevel.addWall(0,j);
            defaultLevel.addBall(1,j);
        }
        for (int j = 0; j < 19; j++) {
            defaultLevel.addWall(20, j);
            defaultLevel.addBall(19,j);
        }

        for (int i = 0; i < 6; i++) {
            defaultLevel.addWall(i, 0);
            defaultLevel.addBall(i,1);
        }

        defaultLevel.addWall(8,0);
        defaultLevel.addWall(10,0);

        for (int i = 12; i < 20; i++) {
            defaultLevel.addWall(i, 0);
            defaultLevel.addBall(i,1);
        }

        for (int i = 0; i < 6; i++) {
            defaultLevel.addWall(i, 18);
            defaultLevel.addBall(i,17);
        }
        defaultLevel.addWall(8,18);
        defaultLevel.addWall(10,18);

        for (int i = 12; i < 20; i++) {
            defaultLevel.addWall(i, 18);
            defaultLevel.addBall(i,17);
        }

        defaultLevel.addWall(1,9);



        defaultLevel.addWall(2,2);
        defaultLevel.addWall(2,3);
        defaultLevel.addBall(2,4);
        defaultLevel.addWall(2,5);
        defaultLevel.addWall(2,6);
        defaultLevel.addWall(2,7);
        defaultLevel.addBall(2,8);
        defaultLevel.addWall(2,9);
        defaultLevel.addBall(2,10);
        defaultLevel.addWall(2,11);
        defaultLevel.addWall(2,12);
        defaultLevel.addWall(2,13);
        defaultLevel.addWall(2,15);
        defaultLevel.addWall(2,16);




        defaultLevel.addWall(4,2);
        defaultLevel.addWall(4,3);
        defaultLevel.addWall(4,5);
        defaultLevel.addWall(4,7);
        defaultLevel.addWall(4,8);
        defaultLevel.addWall(4,9);
        defaultLevel.addWall(4,10);
        defaultLevel.addWall(4,11);
        defaultLevel.addWall(4,13);
        defaultLevel.addWall(4,15);
        defaultLevel.addWall(4,16);

        defaultLevel.addWall(5,5);
        defaultLevel.addWall(5,9);
        defaultLevel.addWall(5,13);

        defaultLevel.addWall(6,0);
        defaultLevel.addWall(6,1);
        defaultLevel.addWall(6,2);
        defaultLevel.addWall(6,3);
        defaultLevel.addWall(6,5);
        defaultLevel.addWall(6,6);
        defaultLevel.addWall(6,7);
        defaultLevel.addWall(6,9);
        defaultLevel.addWall(6,11);
        defaultLevel.addWall(6,12);
        defaultLevel.addWall(6,13);
        defaultLevel.addWall(6,15);
        defaultLevel.addWall(6,16);
        defaultLevel.addWall(6,17);
        defaultLevel.addWall(6,18);

        defaultLevel.addWall(7,3);
        defaultLevel.addWall(7,5);
        defaultLevel.addWall(7,13);
        defaultLevel.addWall(7,15);

        defaultLevel.addWall(8,1);
        defaultLevel.addWall(8,2);
        defaultLevel.addWall(8,3);
        defaultLevel.addWall(8,5);
        defaultLevel.addWall(8,7);
        defaultLevel.addWall(8,8);
        defaultLevel.addWall(8,9);
        defaultLevel.addWall(8,10);
        defaultLevel.addWall(8,11);
        defaultLevel.addWall(8,13);
        defaultLevel.addWall(8,15);
        defaultLevel.addWall(8,16);
        defaultLevel.addWall(8,17);

        defaultLevel.addWall(9,7);
        for (int i = 4; i < 7; i++) defaultLevel.addBall(9,i);
        defaultLevel.addWall(9,11);
        for (int i = 12; i < 15; i++) defaultLevel.addBall(9,i);

        defaultLevel.addWall(10,1);
        defaultLevel.addWall(10,2);
        defaultLevel.addWall(10,3);
        defaultLevel.addWall(10,5);
        defaultLevel.addWall(10,7);
        defaultLevel.addWall(10,8);
        //defaultLevel.addWall(10,9);
        defaultLevel.addWall(10,10);
        defaultLevel.addWall(10,11);
        defaultLevel.addWall(10,13);
        defaultLevel.addWall(10,15);
        defaultLevel.addWall(10,16);
        defaultLevel.addWall(10,17);

        defaultLevel.addBall(10,4);
        defaultLevel.addBall(10,6);
        defaultLevel.addBall(10,12);
        defaultLevel.addBall(10,14);

        defaultLevel.addWall(11,3);
        defaultLevel.addWall(11,5);
        defaultLevel.addWall(11,13);
        defaultLevel.addWall(11,15);

        defaultLevel.addWall(12,1);
        defaultLevel.addWall(12,2);
        defaultLevel.addWall(12,3);
        defaultLevel.addWall(12,5);
        defaultLevel.addWall(12,7);
        defaultLevel.addWall(12,8);
        defaultLevel.addWall(12,9);
        defaultLevel.addWall(12,10);
        defaultLevel.addWall(12,11);
        defaultLevel.addWall(12,13);
        defaultLevel.addWall(12,15);
        defaultLevel.addWall(12,16);
        defaultLevel.addWall(12,17);

        defaultLevel.addWall(13,9);

        defaultLevel.addWall(14,2);
        defaultLevel.addWall(14,3);
        defaultLevel.addWall(14,5);
        defaultLevel.addWall(14,6);
        defaultLevel.addWall(14,7);
        defaultLevel.addWall(14,9);
        defaultLevel.addWall(14,11);
        defaultLevel.addWall(14,12);
        defaultLevel.addWall(14,13);
        defaultLevel.addWall(14,15);
        defaultLevel.addWall(14,16);

        defaultLevel.addWall(15,3);
        for (int i = 2; i < 9; i++) defaultLevel.addBall(15, i);
        for (int i = 10; i < 17; i++) defaultLevel.addBall(15, i);
        defaultLevel.addWall(15,15);

        defaultLevel.addWall(16,1);
        defaultLevel.addWall(16,3);
        defaultLevel.addWall(16,5);
        defaultLevel.addWall(16,7);
        defaultLevel.addWall(16,8);
        defaultLevel.addWall(16,9);
        defaultLevel.addWall(16,10);
        defaultLevel.addWall(16,11);
        defaultLevel.addWall(16,13);
        defaultLevel.addWall(16,15);
        defaultLevel.addWall(16,17);

        defaultLevel.addWall(17,5);
        defaultLevel.addWall(17,9);
        defaultLevel.addWall(17,13);

        defaultLevel.addWall(18,2);
        defaultLevel.addWall(18,3);
        defaultLevel.addWall(18,4);
        defaultLevel.addWall(18,5);
        defaultLevel.addWall(18,6);
        defaultLevel.addWall(18,7);
        defaultLevel.addWall(18,9);
        defaultLevel.addWall(18,11);
        defaultLevel.addWall(18,12);
        defaultLevel.addWall(18,13);
        defaultLevel.addWall(18,14);
        defaultLevel.addWall(18,15);
        defaultLevel.addWall(18,16);

        int[] row1 = {1, 2, 3,4,5,6,8,12,13,14,16,17,18,19};

        for (int i = 0; i < row1.length-1; i++) {
            for (int j = 1; j < 18; j++) {
                defaultLevel.addBall(row1[i], j);
            }
        }

        for (int j = 4; j < 15; j++) {
            defaultLevel.addBall(7, j);
            defaultLevel.addBall(11, j);
        }
        defaultLevel.applyWallTextures();

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
            ghost.setForbiddenDirection(null);
        }
        else if (yDirection == MoveDirection.DOWN && ghost.getLastDirection() != MoveDirection.DOWN && !somethingDOWN) {
            ghost.setCurrentDirection(MoveDirection.DOWN);
            ghost.setForbiddenDirection(null);
        }
        else if (xDirection == MoveDirection.LEFT && ghost.getLastDirection() != MoveDirection.LEFT && !somethingLEFT) {
            ghost.setCurrentDirection(MoveDirection.LEFT);
            ghost.setForbiddenDirection(null);
        }
        else if (xDirection == MoveDirection.RIGHT && ghost.getLastDirection() != MoveDirection.RIGHT && !somethingRIGHT) {
            ghost.setCurrentDirection(MoveDirection.RIGHT);
            ghost.setForbiddenDirection(null);
        }
        else {
            if (nextDirection == MoveDirection.UP && somethingUP || nextDirection == MoveDirection.DOWN && somethingDOWN) {
                if (!somethingLEFT) {
                    ghost.setForbiddenDirection(MoveDirection.RIGHT);
                    ghost.setCurrentDirection(MoveDirection.LEFT);
                } else if (!somethingRIGHT) {
                    ghost.setForbiddenDirection(MoveDirection.LEFT);
                    ghost.setCurrentDirection(MoveDirection.RIGHT);
                } else {
                    if (nextDirection == MoveDirection.UP) ghost.setCurrentDirection(MoveDirection.DOWN);
                    else ghost.setCurrentDirection(MoveDirection.UP);
                    ghost.setForbiddenDirection(nextDirection);
                }
            } else if (nextDirection == MoveDirection.LEFT && somethingLEFT || nextDirection == MoveDirection.RIGHT && somethingRIGHT) {
                if (!somethingUP) {
                    ghost.setForbiddenDirection(MoveDirection.DOWN);
                    ghost.setCurrentDirection(MoveDirection.UP);
                } else if (!somethingDOWN) {
                    ghost.setForbiddenDirection(MoveDirection.UP);
                    ghost.setCurrentDirection(MoveDirection.DOWN);
                } else {
                    if (nextDirection == MoveDirection.LEFT) ghost.setCurrentDirection(MoveDirection.RIGHT);
                    else ghost.setCurrentDirection(MoveDirection.LEFT);
                    ghost.setForbiddenDirection(nextDirection);
                }
            }
        }

        if (Math.abs(xDistance) <= 1 && Math.abs(yDistance) <= 1) {
            ghost.setForbiddenDirection(null);
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

    protected void incrementGlobalVolume(){
        soundEngine().incrementGlobalVolume();
        float volume = soundEngine().getGlobalvolume();
        graphicsEngine().bindText(currentVolume, "Volume is : " + (int)(soundEngine().getGlobalvolume()*100), new Color(255,255,255), 20, true);
    }

    protected void decrementGlobalVolume(){
        soundEngine().decrementGlobalVolume();
        float volume = soundEngine().getGlobalvolume();
        graphicsEngine().bindText(currentVolume, "Volume is : " + (int)(soundEngine().getGlobalvolume()*100), new Color(255,255,255), 20, true);
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
