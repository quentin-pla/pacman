package gameplay;

import engines.graphics.Entity;
import engines.graphics.SpriteAnimation;
import engines.graphics.EntityTexture;

import java.util.HashMap;
import java.util.Map;

import static engines.input_output.InputOutputEngine.*;
import static org.lwjgl.glfw.GLFW.*;

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
     * Vitesse de déplacement (en pixels)
     * Par défaut 1 pixel
     */
    private int move_speed;

    /**
     * Limites de déplacement
     */
    private int[] move_bounds;

    /**
     * Texture par défaut
     */
    private EntityTexture default_texture;

    /**
     * Animations
     */
    private Map<String, SpriteAnimation> animations = new HashMap<>();

    /**
     * Constructeur
     * @param height hauteur
     * @param width largeur
     * @param move_speed vitesse de déplacement
     * @param default_texture texture par défaut
     */
    public Player(int height, int width, int move_speed, EntityTexture default_texture) {
        super(height, width);
        this.move_speed = move_speed;
        this.default_texture = default_texture;
    }

    /**
     * Ajouter des limites de déplacement
     * @param bounds limites
     */
    public void addMoveBounds(int[] bounds) {
        move_bounds = bounds;
    }

    /**
     * Définir une animation de mouvement
     * @param direction direction
     * @param spriteAnimation animation
     */
    public void animateMove(MoveDirection direction, SpriteAnimation spriteAnimation) {
        animations.put(direction.name(), spriteAnimation);
    }

    /**
     * Déplacer le joueur
     */
    private void movePlayer() {
        switch (getLastPressedKey()) {
            case GLFW_KEY_UP:
                if (move_bounds == null || y > move_bounds[1]) y -= move_speed;
                bindTexture(animations.get(MoveDirection.UP.name()));
                break;
            case GLFW_KEY_RIGHT:
                if (move_bounds == null || x < move_bounds[2]) x += move_speed;
                bindTexture(animations.get(MoveDirection.RIGHT.name()));
                break;
            case GLFW_KEY_DOWN:
                if (move_bounds == null || y < move_bounds[3]) y += move_speed;
                bindTexture(animations.get(MoveDirection.DOWN.name()));
                break;
            case GLFW_KEY_LEFT:
                if (move_bounds == null || x > move_bounds[0]) x -= move_speed;
                bindTexture(animations.get(MoveDirection.LEFT.name()));
                break;
            default:
                bindTexture(default_texture);
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        movePlayer();
    }
}
