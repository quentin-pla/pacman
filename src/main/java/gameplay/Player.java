package gameplay;

import engines.kernel.Entity;
import engines.kernel.KernelEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Entité bougeable
 */
public abstract class Player extends Entity {
    /**
     * Direction courante
     */
    protected Gameplay.MoveDirection currentDirection;

    /**
     * Animations
     */
    protected Map<String, Integer> animations = new HashMap<>();

    /**
     * Identifiant de l'animation en cours
     */
    protected Integer currentAnimationID = 0;

    /**
     * Texture par défaut
     * Premier index : ligne de la texture
     * Deuxième index : colonne de la texture
     */
    protected int[] defaultTextureCoords;

    /**
     * Constructeur
     * @param kernelEngine moteur noyau
     */
    protected Player(KernelEngine kernelEngine) {
        super(kernelEngine);
    }

    // GETTERS & SETTERS //

    public Gameplay.MoveDirection getCurrentDirection() {
        return currentDirection;
    }

    public void setCurrentDirection(Gameplay.MoveDirection currentDirection) {
        this.currentDirection = currentDirection;
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

    public int[] getDefaultTextureCoords() {
        return defaultTextureCoords;
    }

    public void setDefaultTextureCoords(int[] defaultTextureCoords) {
        this.defaultTextureCoords = defaultTextureCoords;
    }
}
