package gameplay;

import engines.kernel.Entity;
import engines.physics.PhysicEntity;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Joueur
 */
public class Player extends Entity {
    /**
     * Gameplay
     */
    private Gameplay gameplay;

    /**
     * Directions de déplacement
     */
    public enum MoveDirection {
        UP,RIGHT,DOWN,LEFT
    }

    /**
     * Animations
     */
    private Map<String, Integer> animations = new HashMap<>();

    /**
     * Identifiant de l'animation en cours
     */
    private Integer currentAnimationID = 0;

    /**
     * Direction courante
     */
    private MoveDirection currentDirection = null;

    /**
     * Texture par défaut
     * Premier index : identifiant de la texture
     * Deuxième index : ligne de la texture
     * Troisième index : colonne de la texture
     */
    private int[] defaultTexture;

    /**
     * Constructeur
     * @param height hauteur
     * @param width largeur
     * @param moveSpeed vitesse de déplacement
     * @param spriteSheetID texture par défaut
     * @param row ligne
     * @param col colonne
     */
    public Player(Gameplay gameplay, int height, int width, int moveSpeed, int spriteSheetID, int row, int col) {
        super(gameplay.kernelEngine());
        this.gameplay = gameplay;
        this.defaultTexture = new int[]{spriteSheetID, row, col};
        gameplay.physicsEngine().resize(getPhysicEntity(), height, width);
        gameplay.physicsEngine().setSpeed(getPhysicEntity(), moveSpeed);
        initAnimations(spriteSheetID);
        initEvents();
        bindEvents();
    }

    /**
     * Initialiser les animation
     * @param spriteSheetID identifiant du fichier de textures
     */
    private void initAnimations(int spriteSheetID) {
        int animationsSpeed = 4;

        int moveUP = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveUP,1,3);
        gameplay.graphicsEngine().addFrameToAnimation(moveUP,1,7);
        gameplay.graphicsEngine().addFrameToAnimation(moveUP,1,6);
        animations.put(MoveDirection.UP.name(), moveUP);

        int moveRIGHT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,1,3);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,1,2);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,1,1);
        animations.put(MoveDirection.RIGHT.name(), moveRIGHT);

        int moveDOWN = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,1,3);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,1,9);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,1,8);
        animations.put(MoveDirection.DOWN.name(), moveDOWN);

        int moveLEFT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,1,3);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,1,4);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,1,5);
        animations.put(MoveDirection.LEFT.name(), moveLEFT);
    }

    /**
     * Initialiser les évènements liés au joueur
     */
    private void initEvents() {
        //Se déplacer vers le haut
        gameplay.kernelEngine().addEvent("goUp", () -> switchDirection(MoveDirection.UP));
        //Se déplacer vers la droite
        gameplay.kernelEngine().addEvent("goRight", () -> switchDirection(MoveDirection.RIGHT));
        //Se déplacer vers le bas
        gameplay.kernelEngine().addEvent("goDown", () -> switchDirection(MoveDirection.DOWN));
        //Se déplacer vers la gauche
        gameplay.kernelEngine().addEvent("goLeft", () -> switchDirection(MoveDirection.LEFT));
        //Attacher la texture par défaut au joueur
        gameplay.kernelEngine().addEvent("bindDefaultTexture", () ->
            gameplay.graphicsEngine().bindTexture(getGraphicEntity(), defaultTexture[0], defaultTexture[1], defaultTexture[2]));
        //Lorsqu'il y a une collision
        gameplay.kernelEngine().addEvent("onCollision", () -> {
            if (currentAnimationID != 0)
                if (gameplay.graphicsEngine().getAnimation(currentAnimationID).isPlaying())
                    gameplay.graphicsEngine().playPauseAnimation(currentAnimationID);
        });
        //Rejouer l'animation courante
        gameplay.kernelEngine().addEvent("playCurrentAnimation", () -> {
            if (!gameplay.graphicsEngine().getAnimation(currentAnimationID).isPlaying())
                gameplay.graphicsEngine().playPauseAnimation(currentAnimationID);
        });
    }

    /**
     * Changer la direction du joueur
     * @param direction direction
     */
    private void switchDirection(MoveDirection direction) {
        currentAnimationID = animations.get(direction.name());
        gameplay.kernelEngine().notifyEvent("playCurrentAnimation");
        PhysicEntity entityNearby;
        switch (direction) {
            case UP:
                entityNearby = gameplay.physicsEngine().isSomethingUp(getPhysicEntity());
                if (entityNearby == null)
                    currentDirection = MoveDirection.UP;
                callEventFromDirection();
                break;
            case RIGHT:
                entityNearby = gameplay.physicsEngine().isSomethingRight(getPhysicEntity());
                if (entityNearby == null)
                    currentDirection = MoveDirection.RIGHT;
                callEventFromDirection();
                break;
            case DOWN:
                entityNearby = gameplay.physicsEngine().isSomethingDown(getPhysicEntity());
                if (entityNearby == null)
                    currentDirection = MoveDirection.DOWN;
                callEventFromDirection();
                break;
            case LEFT:
                entityNearby = gameplay.physicsEngine().isSomethingLeft(getPhysicEntity());
                if (entityNearby == null)
                    currentDirection = MoveDirection.LEFT;
                callEventFromDirection();
                break;
        }
        gameplay.graphicsEngine().bindAnimation(getGraphicEntity(), animations.get(currentDirection.name()));
    }

    /**
     * Appeler la méthode de déplacement en fonction de la direction courante
     */
    private void callEventFromDirection() {
        if (currentDirection != null) {
            switch (currentDirection) {
                case UP:
                    gameplay.physicsEngine().goUp(getPhysicEntity());
                    break;
                case RIGHT:
                    gameplay.physicsEngine().goRight(getPhysicEntity());
                    break;
                case DOWN:
                    gameplay.physicsEngine().goDown(getPhysicEntity());
                    break;
                case LEFT:
                    gameplay.physicsEngine().goLeft(getPhysicEntity());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Attacher les évènements du joueur
     */
    private void bindEvents() {
        gameplay.ioEngine().bindEventOnLastKey(KeyEvent.VK_UP, "goUp");
        gameplay.ioEngine().bindEventOnLastKey(KeyEvent.VK_DOWN, "goDown");
        gameplay.ioEngine().bindEventOnLastKey(KeyEvent.VK_RIGHT, "goRight");
        gameplay.ioEngine().bindEventOnLastKey(KeyEvent.VK_LEFT, "goLeft");
        gameplay.ioEngine().bindEventKeyboardFree("bindDefaultTexture");
        gameplay.physicsEngine().bindEventOnCollision(getPhysicEntity(), "onCollision");
    }

    // GETTERS //

    public Map<String, Integer> getAnimations() {
        return animations;
    }

    public int[] getDefaultTexture() { return defaultTexture; }
}
