package gameplay;

import engines.graphics.GraphicsEngine;
import engines.graphics.Scene;
import engines.input_output.IOEngine;
import engines.kernel.KernelEngine;
import engines.physics.PhysicEntity;
import engines.physics.PhysicsEngine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

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
    private final Player player;

    /**
     * Constructeur
     */
    public Gameplay() {
        this.kernelEngine = new KernelEngine();
        this.textures = kernelEngine.getGraphicsEngine().loadSpriteSheet("assets/sprite_sheet.png", 11, 11);
        this.levels = new ArrayList<>();
        this.player = new Player(this, 30, 30, 2, textures, 1, 3);
        initGameplay();
    }

    /**
     * Initialiser le gameplay
     */
    private void initGameplay() {
        //Activation des entrées / sorties clavier
        ioEngine().enableKeyboardIO();
        initEvents();
        initMenu();
    }

    /**
     * Initialiser les évènements du jeu
     */
    private void initEvents() {
        //Se déplacer vers le haut
        kernelEngine().addEvent("pacmanGoUp", () -> switchPacmanDirection(MoveDirection.UP));
        //Se déplacer vers la droite
        kernelEngine().addEvent("pacmanGoRight", () -> switchPacmanDirection(MoveDirection.RIGHT));
        //Se déplacer vers le bas
        kernelEngine().addEvent("pacmanGoDown", () -> switchPacmanDirection(MoveDirection.DOWN));
        //Se déplacer vers la gauche
        kernelEngine().addEvent("pacmanGoLeft", () -> switchPacmanDirection(MoveDirection.LEFT));
        //Attacher la texture par défaut au joueur
        kernelEngine().addEvent("pacmanBindDefaultTexture", () ->
                graphicsEngine().bindTexture(player.getGraphicEntity(),
                        textures, player.getDefaultTextureCoords()[0], player.getDefaultTextureCoords()[1]));
        //Lorsqu'il y a une collision
        kernelEngine().addEvent("pacmanOnCollision", () -> {
            if (player.getCurrentAnimationID() != 0)
                if (graphicsEngine().getAnimation(player.getCurrentAnimationID()).isPlaying())
                    graphicsEngine().playPauseAnimation(player.getCurrentAnimationID());
        });
        //Rejouer l'animation courante
        kernelEngine().addEvent("pacmanPlayCurrentAnimation", () -> {
            if (!graphicsEngine().getAnimation(player.getCurrentAnimationID()).isPlaying())
                graphicsEngine().playPauseAnimation(player.getCurrentAnimationID());
        });
        ioEngine().bindEventOnLastKey(KeyEvent.VK_UP, "pacmanGoUp");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_RIGHT, "pacmanGoRight");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_DOWN, "pacmanGoDown");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_LEFT, "pacmanGoLeft");
        ioEngine().bindEventKeyboardFree("pacmanBindDefaultTexture");
        physicsEngine().bindEventOnCollision(player.getPhysicEntity(), "pacmanOnCollision");
    }

    /**
     * Initialiser le menu
     */
    private void initMenu() {
        this.menuView = graphicsEngine().generateScene(400,400);
    }

    /**
     * Changer la direction de pacman
     * @param direction direction
     */
    protected void switchPacmanDirection(MoveDirection direction) {
        player.setCurrentAnimationID(player.getAnimations().get(direction.name()));
        kernelEngine().notifyEvent("pacmanPlayCurrentAnimation");
        PhysicEntity entityNearby;
        switch (direction) {
            case UP:
                entityNearby = physicsEngine().isSomethingUp(player.getPhysicEntity());
                if (entityNearby == null)
                    player.setCurrentDirection(MoveDirection.UP);
                callEventFromDirection();
                break;
            case RIGHT:
                entityNearby = physicsEngine().isSomethingRight(player.getPhysicEntity());
                if (entityNearby == null)
                    player.setCurrentDirection(MoveDirection.RIGHT);
                callEventFromDirection();
                break;
            case DOWN:
                entityNearby = physicsEngine().isSomethingDown(player.getPhysicEntity());
                if (entityNearby == null)
                    player.setCurrentDirection(MoveDirection.DOWN);
                callEventFromDirection();
                break;
            case LEFT:
                entityNearby = physicsEngine().isSomethingLeft(player.getPhysicEntity());
                if (entityNearby == null)
                    player.setCurrentDirection(MoveDirection.LEFT);
                callEventFromDirection();
                break;
        }
        graphicsEngine().bindAnimation(player.getGraphicEntity(), player.getAnimations().get(player.getCurrentDirection().name()));
    }

    /**
     * Appeler la méthode de déplacement en fonction de la direction courante
     */
    private void callEventFromDirection() {
        if (player.getCurrentDirection() != null) {
            switch (player.getCurrentDirection()) {
                case UP:
                    physicsEngine().goUp(player.getPhysicEntity());
                    break;
                case RIGHT:
                    physicsEngine().goRight(player.getPhysicEntity());
                    break;
                case DOWN:
                    physicsEngine().goDown(player.getPhysicEntity());
                    break;
                case LEFT:
                    physicsEngine().goLeft(player.getPhysicEntity());
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
        level.addPlayer(player, 1,1);
        graphicsEngine().bindScene(level.getScene());
        kernelEngine.start();
    }

    // GETTERS //

    public int getTexturesFile() { return textures; }

    public KernelEngine kernelEngine() { return kernelEngine; }

    public GraphicsEngine graphicsEngine() { return kernelEngine.getGraphicsEngine(); }

    public IOEngine ioEngine() { return kernelEngine.getIoEngine(); }

    public PhysicsEngine physicsEngine() { return kernelEngine.getPhysicsEngine(); }

    public Player getPlayer() { return player; }
}
