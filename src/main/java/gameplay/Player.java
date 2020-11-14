package gameplay;

import engines.graphics.GraphicsEngine;
import engines.input_output.IOEngine;
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
    int entityID;

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
        IOEngine.enableKeyboardIO();
        initAnimations(spriteSheetID);
    }

    /**
     * Initialiser les animation
     * @param spriteSheetID identifiant du fichier de textures
     */
    private void initAnimations(int spriteSheetID) {
        int moveUP = GraphicsEngine.generateAnimation(spriteSheetID, 5, true);
        GraphicsEngine.addFrameToAnimation(moveUP,1,3);
        GraphicsEngine.addFrameToAnimation(moveUP,1,7);
        GraphicsEngine.addFrameToAnimation(moveUP,1,6);
        animations.put(MoveDirection.UP.name(), moveUP);

        int moveRIGHT = GraphicsEngine.generateAnimation(spriteSheetID, 5, true);
        GraphicsEngine.addFrameToAnimation(moveRIGHT,1,3);
        GraphicsEngine.addFrameToAnimation(moveRIGHT,1,2);
        GraphicsEngine.addFrameToAnimation(moveRIGHT,1,1);
        animations.put(MoveDirection.RIGHT.name(), moveRIGHT);

        int moveDOWN = GraphicsEngine.generateAnimation(spriteSheetID, 5, true);
        GraphicsEngine.addFrameToAnimation(moveDOWN,1,3);
        GraphicsEngine.addFrameToAnimation(moveDOWN,1,9);
        GraphicsEngine.addFrameToAnimation(moveDOWN,1,8);
        animations.put(MoveDirection.DOWN.name(), moveDOWN);

        int moveLEFT = GraphicsEngine.generateAnimation(spriteSheetID, 5, true);
        GraphicsEngine.addFrameToAnimation(moveLEFT,1,3);
        GraphicsEngine.addFrameToAnimation(moveLEFT,1,4);
        GraphicsEngine.addFrameToAnimation(moveLEFT,1,5);
        animations.put(MoveDirection.LEFT.name(), moveLEFT);

        bindAnimations();
    }

    /**
     * Déplacer le joueur
     */
    private void bindAnimations() {
        IOEngine.bindMethodToLastKey(entityID, (v)-> {
            PhysicsEngine.goUp(entityID, moveSpeed);
            GraphicsEngine.bindAnimation(entityID, animations.get(MoveDirection.UP.name()));
        }, KeyEvent.VK_UP);

        IOEngine.bindMethodToLastKey(entityID, (v)-> {
            PhysicsEngine.goRight(entityID, moveSpeed);
            GraphicsEngine.bindAnimation(entityID, animations.get(MoveDirection.RIGHT.name()));
        }, KeyEvent.VK_RIGHT);

        IOEngine.bindMethodToLastKey(entityID, (v)-> {
            PhysicsEngine.goDown(entityID, moveSpeed);
            GraphicsEngine.bindAnimation(entityID, animations.get(MoveDirection.DOWN.name()));
        }, KeyEvent.VK_DOWN);

        IOEngine.bindMethodToLastKey(entityID, (v)-> {
            PhysicsEngine.goLeft(entityID, moveSpeed);
            GraphicsEngine.bindAnimation(entityID, animations.get(MoveDirection.LEFT.name()));
        }, KeyEvent.VK_LEFT);

        IOEngine.bindMethodToKeyboardFree(entityID, (v)-> {
            GraphicsEngine.bindTexture(entityID, defaultTexture[0], defaultTexture[1], defaultTexture[2]);
        });
    }

    public int getEntityID() {
        return entityID;
    }
}
