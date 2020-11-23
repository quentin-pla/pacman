package gameplay;

import engines.graphics.GraphicsEngine;
import engines.input_output.IOEngine;
import engines.kernel.Event;
import engines.kernel.Entity;
import engines.kernel.KernelEngine;
import engines.physics.PhysicEntity;
import engines.physics.PhysicsEngine;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Joueur
 */
public class Player extends Entity {
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
    public Player(int height, int width, int moveSpeed, int spriteSheetID, int row, int col) {
        super();
        this.defaultTexture = new int[]{spriteSheetID, row, col};
        PhysicsEngine.resize(getPhysicEntity(), height, width);
        PhysicsEngine.setSpeed(getPhysicEntity(), moveSpeed);
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

        int moveUP = GraphicsEngine.generateAnimation(spriteSheetID, animationsSpeed, true);
        GraphicsEngine.addFrameToAnimation(moveUP,1,3);
        GraphicsEngine.addFrameToAnimation(moveUP,1,7);
        GraphicsEngine.addFrameToAnimation(moveUP,1,6);
        animations.put(MoveDirection.UP.name(), moveUP);

        int moveRIGHT = GraphicsEngine.generateAnimation(spriteSheetID, animationsSpeed, true);
        GraphicsEngine.addFrameToAnimation(moveRIGHT,1,3);
        GraphicsEngine.addFrameToAnimation(moveRIGHT,1,2);
        GraphicsEngine.addFrameToAnimation(moveRIGHT,1,1);
        animations.put(MoveDirection.RIGHT.name(), moveRIGHT);

        int moveDOWN = GraphicsEngine.generateAnimation(spriteSheetID, animationsSpeed, true);
        GraphicsEngine.addFrameToAnimation(moveDOWN,1,3);
        GraphicsEngine.addFrameToAnimation(moveDOWN,1,9);
        GraphicsEngine.addFrameToAnimation(moveDOWN,1,8);
        animations.put(MoveDirection.DOWN.name(), moveDOWN);

        int moveLEFT = GraphicsEngine.generateAnimation(spriteSheetID, animationsSpeed, true);
        GraphicsEngine.addFrameToAnimation(moveLEFT,1,3);
        GraphicsEngine.addFrameToAnimation(moveLEFT,1,4);
        GraphicsEngine.addFrameToAnimation(moveLEFT,1,5);
        animations.put(MoveDirection.LEFT.name(), moveLEFT);
    }

    /**
     * Initialiser les évènements liés au joueur
     */
    private void initEvents() {
        //Se déplacer vers le haut
        KernelEngine.addEvent("goUp", new Event() {
            @Override
            public void run() { switchDirection(MoveDirection.UP); }
        });
        //Se déplacer vers la droite
        KernelEngine.addEvent("goRight", new Event() {
            @Override
            public void run() { switchDirection(MoveDirection.RIGHT); }
        });
        //Se déplacer vers le bas
        KernelEngine.addEvent("goDown", new Event() {
            @Override
            public void run() { switchDirection(MoveDirection.DOWN); }
        });
        //Se déplacer vers la gauche
        KernelEngine.addEvent("goLeft", new Event() {
            @Override
            public void run() { switchDirection(MoveDirection.LEFT); }
        });
        //Attacher la texture par défaut au joueur
        KernelEngine.addEvent("bindDefaultTexture", new Event() {
            @Override
            public void run() {
                GraphicsEngine.bindTexture(getGraphicEntity(), defaultTexture[0], defaultTexture[1], defaultTexture[2]);
            }
        });
        //Lorsqu'il y a une collision
        KernelEngine.addEvent("onCollision", new Event() {
            @Override
            public void run() {
                if (currentAnimationID != 0)
                    if (GraphicsEngine.getAnimation(currentAnimationID).isPlaying())
                        GraphicsEngine.playPauseAnimation(currentAnimationID);
            }
        });
        //Rejouer l'animation courante
        KernelEngine.addEvent("playCurrentAnimation", new Event() {
            @Override
            public void run() {
                if (!GraphicsEngine.getAnimation(currentAnimationID).isPlaying())
                    GraphicsEngine.playPauseAnimation(currentAnimationID);
            }
        });
    }

    /**
     * Changer la direction du joueur
     * @param direction direction
     */
    private void switchDirection(MoveDirection direction) {
        currentAnimationID = animations.get(direction.name());
        KernelEngine.notifyEvent("playCurrentAnimation");
        PhysicEntity entityNearby;
        switch (direction) {
            case UP:
                entityNearby = PhysicsEngine.isSomethingUp(getPhysicEntity());
                if (entityNearby == null)
                    currentDirection = MoveDirection.UP;
                callEventFromDirection();
                break;
            case RIGHT:
                entityNearby = PhysicsEngine.isSomethingRight(getPhysicEntity());
                if (entityNearby == null)
                    currentDirection = MoveDirection.RIGHT;
                callEventFromDirection();
                break;
            case DOWN:
                entityNearby = PhysicsEngine.isSomethingDown(getPhysicEntity());
                if (entityNearby == null)
                    currentDirection = MoveDirection.DOWN;
                callEventFromDirection();
                break;
            case LEFT:
                entityNearby = PhysicsEngine.isSomethingLeft(getPhysicEntity());
                if (entityNearby == null)
                    currentDirection = MoveDirection.LEFT;
                callEventFromDirection();
                break;
        }
        GraphicsEngine.bindAnimation(getGraphicEntity(), animations.get(currentDirection.name()));
    }

    /**
     * Appeler la méthode de déplacement en fonction de la direction courante
     */
    private void callEventFromDirection() {
        if (currentDirection != null) {
            switch (currentDirection) {
                case UP:
                    PhysicsEngine.goUp(getPhysicEntity());
                    break;
                case RIGHT:
                    PhysicsEngine.goRight(getPhysicEntity());
                    break;
                case DOWN:
                    PhysicsEngine.goDown(getPhysicEntity());
                    break;
                case LEFT:
                    PhysicsEngine.goLeft(getPhysicEntity());
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
        IOEngine.bindEventOnLastKey(KeyEvent.VK_UP, "goUp");
        IOEngine.bindEventOnLastKey(KeyEvent.VK_DOWN, "goDown");
        IOEngine.bindEventOnLastKey(KeyEvent.VK_RIGHT, "goRight");
        IOEngine.bindEventOnLastKey(KeyEvent.VK_LEFT, "goLeft");
        IOEngine.bindEventKeyboardFree("bindDefaultTexture");
        PhysicsEngine.bindEventOnCollision(getPhysicEntity(), "onCollision");
    }

    // GETTERS //

    public Map<String, Integer> getAnimations() {
        return animations;
    }

    public int[] getDefaultTexture() { return defaultTexture; }
}
