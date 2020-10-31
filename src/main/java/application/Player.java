package application;

import engines.graphics.SpriteAnimation;
import engines.graphics.TileTexture;

import java.util.HashMap;
import java.util.Map;

import static engines.input_output.InputOutputEngine.isKeyPressed;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Joueur
 */
public class Player extends GameObject {
    /**
     * Directions de déplacement
     */
    public enum MoveDirection {
        UP,RIGHT,DOWN,LEFT
    }

    /**
     * Directions autorisées déplacement
     * dans le sens des aiguilles d'une montre
     */
    private boolean[] allowed_moves = new boolean[]{true,true,true,true};

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
    private TileTexture default_texture;

    /**
     * Animations
     */
    private Map<String, SpriteAnimation> animations = new HashMap<>();

    /**
     * Constructeur
     * @param size taille du joueur
     */
    public Player(int size, int move_speed, TileTexture default_texture) {
        super(size);
        this.move_speed = move_speed;
        this.default_texture = default_texture;
    }

    /**
     * Changer la manière de déplacer le joueur
     * @param up vers le haut
     * @param right vers la droite
     * @param down vers le bas
     * @param left vers la gauche
     */
    public void allowMoveDirections(boolean up, boolean right, boolean down, boolean left) {
        allowed_moves = new boolean[]{up,right,down,left};
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
        MoveDirection direction = null;
        if (allowed_moves[0] && isKeyPressed(GLFW_KEY_UP)) {
            direction = MoveDirection.UP;
            if (move_bounds == null || y > move_bounds[1]) y -= move_speed;
        }
        if (allowed_moves[1] && isKeyPressed(GLFW_KEY_RIGHT)) {
            direction = MoveDirection.RIGHT;
            if (move_bounds == null || x < move_bounds[2]) x += move_speed;
        }
        if (allowed_moves[2] && isKeyPressed(GLFW_KEY_DOWN)) {
            direction = MoveDirection.DOWN;
            if (move_bounds == null || y < move_bounds[3]) y += move_speed;
        }
        if (allowed_moves[3] && isKeyPressed(GLFW_KEY_LEFT)) {
            direction = MoveDirection.LEFT;
            if (move_bounds == null || x > move_bounds[0]) x -= move_speed;
        }
        if (direction != null && animations.get(direction.name()) != null) {
            if (getTexture() != animations.get(direction.name()))
                bindTexture(animations.get(direction.name()));
        } else {
            bindTexture(default_texture);
        }
    }

    @Override
    public void update() {
        super.update();
        movePlayer();
    }
}
