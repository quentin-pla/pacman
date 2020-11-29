package gameplay;

/**
 * Fantome
 */
public class Ghost extends Player {
    /**
     * Gameplay
     */
    private Gameplay gameplay;

    private Gameplay.MoveDirection lastDirection;

    /**
     * Constructeur
     * @param gameplay gameplay
     */
    public Ghost(Gameplay gameplay) {
        super(gameplay.kernelEngine());
        this.gameplay = gameplay;
        this.defaultTextureCoords = new int[]{3, 1};
        gameplay.physicsEngine().resize(getPhysicEntity(), 30, 30);
        gameplay.physicsEngine().setSpeed(getPhysicEntity(), 2);
        gameplay.graphicsEngine().bindTexture(getGraphicEntity(),
                gameplay.getTexturesFile(), defaultTextureCoords[0], defaultTextureCoords[1]);
        initAnimations(gameplay.getTexturesFile());
    }

    /**
     * Initialiser les animation
     * @param spriteSheetID identifiant du fichier de textures
     */
    private void initAnimations(int spriteSheetID) {
        int animationsSpeed = 4;

        int moveUP = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveUP,3,7);
        gameplay.graphicsEngine().addFrameToAnimation(moveUP,3,8);
        animations.put(Gameplay.MoveDirection.UP.name(), moveUP);

        int moveRIGHT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,3,1);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,3,2);
        animations.put(Gameplay.MoveDirection.RIGHT.name(), moveRIGHT);

        int moveDOWN = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,3,3);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,3,4);
        animations.put(Gameplay.MoveDirection.DOWN.name(), moveDOWN);

        int moveLEFT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,3,5);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,3,6);
        animations.put(Gameplay.MoveDirection.LEFT.name(), moveLEFT);
    }

    public Gameplay.MoveDirection getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(Gameplay.MoveDirection lastDirection) {
        this.lastDirection = lastDirection;
    }
}
