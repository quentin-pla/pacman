package gameplay;

import engines.graphics.GraphicsEngine;
import engines.input_output.IOEngine;
import engines.kernel.Event;
import engines.kernel.KernelEngine;
import engines.physics.PhysicsEngine;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Joueur
 */
public class Player {
    /**
     * Identifiant entité liée
     */
    private int entityID;

    /**
     * Directions de déplacement
     */
    public enum MoveDirection {
        UP,RIGHT,DOWN,LEFT
    }

    /**
     * Vitesse de déplacement
     */
    private int moveSpeed;

    /**
     * Animations
     */
    private Map<String, Integer> animations = new HashMap<>();

    /**
     * Identifiant de l'animation en cours
     */
    private Integer currentAnimationID = 0;

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
        this.entityID = KernelEngine.generateEntity();
        this.moveSpeed = moveSpeed;
        this.defaultTexture = new int[]{spriteSheetID, row, col};
        GraphicsEngine.resize(entityID, height, width);
        PhysicsEngine.setSpeed(entityID, moveSpeed);
        initAnimations(spriteSheetID);
        initEvents();
        bindEvents();
    }

    /**
     * Initialiser les animation
     * @param spriteSheetID identifiant du fichier de textures
     */
    private void initAnimations(int spriteSheetID) {
        int moveUP = GraphicsEngine.generateAnimation(spriteSheetID, 4, true);
        GraphicsEngine.addFrameToAnimation(moveUP,1,3);
        GraphicsEngine.addFrameToAnimation(moveUP,1,7);
        GraphicsEngine.addFrameToAnimation(moveUP,1,6);
        animations.put(MoveDirection.UP.name(), moveUP);

        int moveRIGHT = GraphicsEngine.generateAnimation(spriteSheetID, 4, true);
        GraphicsEngine.addFrameToAnimation(moveRIGHT,1,3);
        GraphicsEngine.addFrameToAnimation(moveRIGHT,1,2);
        GraphicsEngine.addFrameToAnimation(moveRIGHT,1,1);
        animations.put(MoveDirection.RIGHT.name(), moveRIGHT);

        int moveDOWN = GraphicsEngine.generateAnimation(spriteSheetID, 4, true);
        GraphicsEngine.addFrameToAnimation(moveDOWN,1,3);
        GraphicsEngine.addFrameToAnimation(moveDOWN,1,9);
        GraphicsEngine.addFrameToAnimation(moveDOWN,1,8);
        animations.put(MoveDirection.DOWN.name(), moveDOWN);

        int moveLEFT = GraphicsEngine.generateAnimation(spriteSheetID, 4, true);
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
                GraphicsEngine.bindTexture(entityID, defaultTexture[0], defaultTexture[1], defaultTexture[2]);
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
        switch (direction) {
            case UP:
                PhysicsEngine.goUp(entityID, moveSpeed);
                break;
            case RIGHT:
                PhysicsEngine.goRight(entityID, moveSpeed);
                break;
            case DOWN:
                PhysicsEngine.goDown(entityID, moveSpeed);
                break;
            case LEFT:
                PhysicsEngine.goLeft(entityID, moveSpeed);
                break;
        }
        GraphicsEngine.bindAnimation(entityID, animations.get(direction.name()));
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
        PhysicsEngine.bindEventOnCollision(entityID, "onCollision");
    }

    // GETTERS //

    public int getEntityID() {
        return entityID;
    }

    public int getMoveSpeed() {
        return moveSpeed;
    }

    public Map<String, Integer> getAnimations() {
        return animations;
    }

    public int[] getDefaultTexture() { return defaultTexture; }
}
