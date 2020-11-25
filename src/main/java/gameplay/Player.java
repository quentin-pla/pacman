package gameplay;

import engines.kernel.Entity;

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
    private Gameplay.MoveDirection currentDirection = null;

    /**
     * Texture par défaut
     * Premier index : ligne de la texture
     * Deuxième index : colonne de la texture
     */
    private int[] defaultTextureCoords;

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
        this.defaultTextureCoords = new int[]{row, col};
        gameplay.physicsEngine().resize(getPhysicEntity(), height, width);
        gameplay.physicsEngine().setSpeed(getPhysicEntity(), moveSpeed);
        initAnimations(spriteSheetID);
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
        animations.put(Gameplay.MoveDirection.UP.name(), moveUP);

        int moveRIGHT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,1,3);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,1,2);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,1,1);
        animations.put(Gameplay.MoveDirection.RIGHT.name(), moveRIGHT);

        int moveDOWN = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,1,3);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,1,9);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,1,8);
        animations.put(Gameplay.MoveDirection.DOWN.name(), moveDOWN);

        int moveLEFT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,1,3);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,1,4);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,1,5);
        animations.put(Gameplay.MoveDirection.LEFT.name(), moveLEFT);
    }

    // GETTERS //

    public Gameplay getGameplay() {
        return gameplay;
    }

    public void setGameplay(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    public Map<String, Integer> getAnimations() {
        return animations;
    }

    public void setAnimations(Map<String, Integer> animations) {
        this.animations = animations;
    }

    public Integer getCurrentAnimationID() {
        return currentAnimationID;
    }

    public void setCurrentAnimationID(Integer currentAnimationID) {
        this.currentAnimationID = currentAnimationID;
    }

    public Gameplay.MoveDirection getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Gameplay.MoveDirection currentDirection) {
        this.currentDirection = currentDirection;
    }

    public int[] getDefaultTextureCoords() {
        return defaultTextureCoords;
    }

    public void setDefaultTextureCoords(int[] defaultTextureCoords) {
        this.defaultTextureCoords = defaultTextureCoords;
    }
}
