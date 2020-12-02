package gameplay;

/**
 * Joueur
 */
public class Pacman extends Player {
    /**
     * Gameplay
     */
    private final Gameplay gameplay;

    /**
     * Son joué lorsqu'une boule est mangée
     */
    private String munchSound;


    /**
     * Constructeur
     * @param gameplay gameplay
     */
    public Pacman(Gameplay gameplay) {
        super(gameplay.kernelEngine());
        this.gameplay = gameplay;
        this.defaultTextureCoords = new int[]{1, 3};
        this.munchSound = "munch1";
        gameplay.physicsEngine().resize(this, 30, 30);
        gameplay.physicsEngine().setSpeed(this, 3);
        bindDefaultTexture();
        initAnimations(gameplay.getTexturesFile());
    }

    /**
     * Attacher la texture par défaut au joueur
     */
    public void bindDefaultTexture() {
        gameplay.graphicsEngine().bindTexture(this,gameplay.getTexturesFile(),
                defaultTextureCoords[0],defaultTextureCoords[1]);
    }

    /**
     * Initialiser les animation
     * @param spriteSheetID identifiant du fichier de textures
     */
    private void initAnimations(int spriteSheetID) {
        int animationsDuration = 3;

        int moveUP = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveUP,1,3);
        gameplay.graphicsEngine().addFrameToAnimation(moveUP,1,7);
        gameplay.graphicsEngine().addFrameToAnimation(moveUP,1,6);
        animations.put(Gameplay.MoveDirection.UP.name(), moveUP);

        int moveRIGHT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,1,3);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,1,2);
        gameplay.graphicsEngine().addFrameToAnimation(moveRIGHT,1,1);
        animations.put(Gameplay.MoveDirection.RIGHT.name(), moveRIGHT);

        int moveDOWN = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,1,3);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,1,9);
        gameplay.graphicsEngine().addFrameToAnimation(moveDOWN,1,8);
        animations.put(Gameplay.MoveDirection.DOWN.name(), moveDOWN);

        int moveLEFT = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, true);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,1,3);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,1,4);
        gameplay.graphicsEngine().addFrameToAnimation(moveLEFT,1,5);
        animations.put(Gameplay.MoveDirection.LEFT.name(), moveLEFT);

        int death = gameplay.graphicsEngine().generateAnimation(spriteSheetID, animationsDuration, false);
        gameplay.graphicsEngine().addFrameToAnimation(death,1,3);
        gameplay.graphicsEngine().addFrameToAnimation(death,2,1);
        gameplay.graphicsEngine().addFrameToAnimation(death,2,2);
        gameplay.graphicsEngine().addFrameToAnimation(death,2,3);
        gameplay.graphicsEngine().addFrameToAnimation(death,2,4);
        gameplay.graphicsEngine().addFrameToAnimation(death,2,5);
        gameplay.graphicsEngine().addFrameToAnimation(death,2,6);
        gameplay.graphicsEngine().addFrameToAnimation(death,2,7);
        gameplay.graphicsEngine().addFrameToAnimation(death,2,8);
        gameplay.graphicsEngine().addFrameToAnimation(death,2,9);
        gameplay.graphicsEngine().addFrameToAnimation(death,2,10);
        gameplay.graphicsEngine().addFrameToAnimation(death,2,11);
        gameplay.graphicsEngine().addFrameToAnimation(death,1,11);
        animations.put("death", death);
    }

    // GETTERS //

    public String getMunchSound() {
        munchSound = munchSound.equals("munch1") ? "munch2" : "munch1";
        return munchSound;
    }
}
