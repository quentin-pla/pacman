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
    private final Gameplay gameplay;

    /**
     * Directions interdites
     */
    private final Set<Gameplay.MoveDirection> forbiddenDirection;

    /**
     * Mangé
     */
    private boolean eaten;

    /**
     * Couleur
     */
    private final String color;

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
        this.color = color;
        gameplay.physicsEngine().resize(this, 30, 30);
        gameplay.physicsEngine().setSpeed(this, 2);
        switch (color) {
            case "red"    : defaultTextureCoords = new int[]{3, 1}; break;
            case "pink"   : defaultTextureCoords = new int[]{4, 1}; break;
            case "blue"   : defaultTextureCoords = new int[]{5, 1}; break;
            case "orange" : defaultTextureCoords = new int[]{6, 1};break;
        }
        initAnimations(gameplay.getTexturesFile());
        gameplay.graphicsEngine().bindAnimation(this, animations.get("RIGHT"));
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

        int eatenUP = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(eatenUP,7,7);
        gameplay.graphicsEngine().addFrameToAnimation(eatenUP,7,8);
        animations.put("eaten" + Gameplay.MoveDirection.UP.name(), eatenUP);

        int eatenRIGHT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(eatenRIGHT,7,1);
        gameplay.graphicsEngine().addFrameToAnimation(eatenRIGHT,7,2);
        animations.put("eaten" + Gameplay.MoveDirection.RIGHT.name(), eatenRIGHT);

        int eatenDOWN = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(eatenDOWN,7,3);
        gameplay.graphicsEngine().addFrameToAnimation(eatenDOWN,7,4);
        animations.put("eaten" + Gameplay.MoveDirection.DOWN.name(), eatenDOWN);

        int eatenLEFT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(eatenLEFT,7,5);
        gameplay.graphicsEngine().addFrameToAnimation(eatenLEFT,7,6);
        animations.put("eaten" + Gameplay.MoveDirection.LEFT.name(), eatenLEFT);

        int fear = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(fear,8,1);
        gameplay.graphicsEngine().addFrameToAnimation(fear,8,2);
        animations.put("fear", fear);

        int fearEnd = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(fearEnd,8,1);
        gameplay.graphicsEngine().addFrameToAnimation(fearEnd,8,2);
        gameplay.graphicsEngine().addFrameToAnimation(fearEnd,8,3);
        gameplay.graphicsEngine().addFrameToAnimation(fearEnd,8,4);
        animations.put("fearEnd", fearEnd);
    }

    public Set<Gameplay.MoveDirection> getForbiddenDirection() { return forbiddenDirection; }

    public boolean getEaten() { return this.eaten; }

    public void setEaten(boolean eaten) { this.eaten = eaten; }

    public String getColor() { return color; }
}
