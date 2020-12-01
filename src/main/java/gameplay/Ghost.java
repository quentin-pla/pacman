package gameplay;

import java.util.HashSet;
import java.util.Set;

/**
 * Fantome
 */
public class Ghost extends Player {
    /**
     * Gameplay
     */
    private Gameplay gameplay;

    /**
     * Directions interdites
     */
    private final Set<Gameplay.MoveDirection> forbiddenDirection;

    /**
     * Mangé
     */
    private boolean eaten;

    /**
     * Constructeur
     * @param gameplay gameplay
     * @param color couleur
     */
    public Ghost(Gameplay gameplay, String color) {
        super(gameplay.kernelEngine());
        this.gameplay = gameplay;
        this.forbiddenDirection = new HashSet<>();
        this.eaten = false;
        gameplay.physicsEngine().resize(this, 30, 30);
        gameplay.physicsEngine().setSpeed(this, 2);
        switch (color) {
            case "red" :
                this.defaultTextureCoords = new int[]{3, 1};
                break;
            case "pink" :
                this.defaultTextureCoords = new int[]{4, 1};
                break;
            case "blue" :
                this.defaultTextureCoords = new int[]{5, 1};
                break;
            case "orange" :
                this.defaultTextureCoords = new int[]{6, 1};
                break;
        }
        gameplay.graphicsEngine().bindTexture(this, gameplay.getTexturesFile(),
                defaultTextureCoords[0], defaultTextureCoords[1]);
        initAnimations(gameplay.getTexturesFile());
    }

    /**
     * Initialiser les animation
     * @param spriteSheetID identifiant du fichier de textures
     */
    void initAnimations(int spriteSheetID) {
        int animationsDuration = 3;

        int moveUP = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveUP,defaultTextureCoords[0],7);
        gameplay.graphicsEngine().addFrameToAnimation(moveUP,defaultTextureCoords[0],8);
        animations.put(Gameplay.MoveDirection.UP.name(), moveUP);

        int moveRIGHT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,defaultTextureCoords[0],1);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,defaultTextureCoords[0],2);
        animations.put(Gameplay.MoveDirection.RIGHT.name(), moveRIGHT);

        int moveDOWN = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,defaultTextureCoords[0],3);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,defaultTextureCoords[0],4);
        animations.put(Gameplay.MoveDirection.DOWN.name(), moveDOWN);

        int moveLEFT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,defaultTextureCoords[0],5);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,defaultTextureCoords[0],6);
        animations.put(Gameplay.MoveDirection.LEFT.name(), moveLEFT);
    }

    public Set<Gameplay.MoveDirection> getForbiddenDirection() { return forbiddenDirection; }

    public boolean getEaten() { return this.eaten; }

    public void setEaten(boolean eaten) { this.eaten = eaten; }
}
