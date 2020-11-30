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
        this.defaultTextureCoords = new int[]{4, 1};
        gameplay.physicsEngine().resize(getPhysicEntity(), 30, 30);
        gameplay.physicsEngine().setSpeed(getPhysicEntity(), 3);
        gameplay.graphicsEngine().bindTexture(getGraphicEntity(),
                gameplay.getTexturesFile(), 4, 1);
        initAnimations(gameplay.getTexturesFile());
    }

    public Ghost(Gameplay gameplay, String color) {
        super(gameplay.kernelEngine());
        this.gameplay = gameplay;
        gameplay.physicsEngine().resize(getPhysicEntity(), 30, 30);
        gameplay.physicsEngine().setSpeed(getPhysicEntity(), 3);

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
        gameplay.graphicsEngine().addFrameToAnimation(moveUP,defaultTextureCoords[0],7);
        gameplay.graphicsEngine().addFrameToAnimation(moveUP,defaultTextureCoords[0],8);
        animations.put(Gameplay.MoveDirection.UP.name(), moveUP);

        int moveRIGHT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,defaultTextureCoords[0],1);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,defaultTextureCoords[0],2);
        animations.put(Gameplay.MoveDirection.RIGHT.name(), moveRIGHT);

        int moveDOWN = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,defaultTextureCoords[0],3);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,defaultTextureCoords[0],4);
        animations.put(Gameplay.MoveDirection.DOWN.name(), moveDOWN);

        int moveLEFT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsSpeed, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,defaultTextureCoords[0],5);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,defaultTextureCoords[0],6);
        animations.put(Gameplay.MoveDirection.LEFT.name(), moveLEFT);
    }

    public Gameplay.MoveDirection getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(Gameplay.MoveDirection lastDirection) {
        this.lastDirection = lastDirection;
    }
}
