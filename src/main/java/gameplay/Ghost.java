package gameplay;

import java.util.HashSet;
import java.util.Set;

import java.util.HashMap;

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
     * hashmap de patrouille des zones
     */
    private HashMap<String,Boolean> scatterPatrolZones = new HashMap<String, Boolean>();

    /**
     * Si la zone de patrouille est atteinte
     */
    private boolean patroleZoneReached = false;

    /**
     * Direction de déplacement précédente
     */
    private Gameplay.MoveDirection previousDirection;

    /**
     * Garder la direction actuelle
     */
    private HashMap<String,Boolean> keepDirection = new HashMap<>();


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
        scatterPatrolZones.put("TopRight",false);
        scatterPatrolZones.put("TopLeft",false);
        scatterPatrolZones.put("BottomRight",false);
        scatterPatrolZones.put("BottomLeft",false);
        keepDirection.put("KeepUp",false);
        keepDirection.put("KeepDown",false);
        keepDirection.put("KeepLeft",false);
        keepDirection.put("KeepRight",false);

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


    public HashMap<String, Boolean> getScatterPatrolZones() {
        return scatterPatrolZones;
    }

    public void setScatterPatrolZones(HashMap<String, Boolean> scatterPatrolZones) {
        this.scatterPatrolZones = scatterPatrolZones;
    }
    public boolean getEaten() { return this.eaten; }

    public void setEaten(boolean eaten) { this.eaten = eaten; }

    public boolean isPatroleZoneReached() {
        return patroleZoneReached;
    }

    public void setPatroleZoneReached(boolean patroleZoneReached) {
        this.patroleZoneReached = patroleZoneReached;
    }

    public Gameplay.MoveDirection getPreviousDirection() {
        return previousDirection;
    }

    public void setPreviousDirection(Gameplay.MoveDirection previousDirection) {
        this.previousDirection = previousDirection;
    }

    public HashMap<String, Boolean> getKeepDirection() {
        return keepDirection;
    }

    public void setKeepDirection(HashMap<String, Boolean> keepDirection) {
        this.keepDirection = keepDirection;
    }
}
