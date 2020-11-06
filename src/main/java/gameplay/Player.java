package gameplay;

import engines.graphics.Cover;
import engines.graphics.SpriteAnimation;
import engines.kernel.Entity;

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
    private Cover default_texture;

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
    public Player(int height, int width, int move_speed, Cover default_texture) {
        this.initGraphics(height, width);
        this.initInputOutput();
        this.getInputOutput().enableKeyboardInputOutput(true);
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
        switch (getKeyboard().getLastPressedKey()) {
            case KeyEvent.VK_UP:
                if (move_bounds == null || getY() > move_bounds[1])
                    translate(0, -move_speed);
                bindTexture(animations.get(MoveDirection.UP.name()));
                break;
            case KeyEvent.VK_RIGHT:
                if (move_bounds == null || getX() < move_bounds[2])
                    translate(move_speed, 0);
                bindTexture(animations.get(MoveDirection.RIGHT.name()));
                break;
            case KeyEvent.VK_DOWN:
                if (move_bounds == null || getY() < move_bounds[3])
                    translate(0, move_speed);
                bindTexture(animations.get(MoveDirection.DOWN.name()));
                break;
            case KeyEvent.VK_LEFT:
                if (move_bounds == null || getX() > move_bounds[0])
                    translate(-move_speed, 0);
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
