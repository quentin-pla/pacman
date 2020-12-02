package engines.graphics;

import api.SwingRenderer;

import java.util.ArrayList;

/**
 * Animation depuis un fichier de textures
 */
public class SpriteAnimation extends Cover {
    /**
     * Image de départ
     */
    private int actualFrame = 0;

    /**
     * Images de l'animation
     */
    private ArrayList<Sprite> frames = new ArrayList<>();

    /**
     * Vitesse
     */
    private int speed;

    /**
     * En boucle
     */
    private boolean looping;

    /**
     * Additionner ou soustraire
     */
    private boolean addSubstract = true;

    /**
     * En cours
     */
    private boolean playing = true;

    /**
     * Temps
     */
    private int time = 0;

    /**
     * Fichier de texture
     */
    private final SpriteSheet spriteSheet;

    /**
     * Constructeur
     * @param spriteSheet fichier de texture
     * @param speed vitesse
     * @param looping en boucle
     */
    protected SpriteAnimation(SpriteSheet spriteSheet, int speed, boolean looping) {
        this.spriteSheet = spriteSheet;
        this.speed = speed;
        this.looping = looping;
    }

    /**
     * Constructeur par clonage
     * @param clone clone
     */
    private SpriteAnimation(SpriteAnimation clone) {
        this(clone.spriteSheet, clone.speed, clone.looping);
        this.actualFrame = clone.actualFrame;
        this.frames = new ArrayList<>(clone.frames);
        this.addSubstract = clone.addSubstract;
        this.playing = clone.playing;
        this.time = clone.time;
    }

    /**
     * Ajouter une image à l'animation
     * @param row ligne
     * @param col colonne
     */
    protected void addFrame(int row, int col) {
        frames.add(spriteSheet.getSprite(row, col));
    }

    /**
     * Supprimer les images de l'animation
     */
    protected void clearFrame() {
        frames = new ArrayList<>();
    }

    /**
     * Jouer / Mettre en pause l'animation
     */
    protected void playPause() { playing = !playing; }

    /**
     * Réinitialiser l'animation
     */
    protected void reset() {
        time = 0;
        playing = true;
        actualFrame = 0;
        addSubstract = true;
    }

    @Override
    protected void cover(GraphicEntity graphicEntity) {
        SwingRenderer.getInstance().renderTexturedRect(graphicEntity.getHeight(), graphicEntity.getWidth(),
                graphicEntity.getX(), graphicEntity.getY(), getFrame().getLink());
    }

    @Override
    protected void update() {
        if (playing) {
            ++time;
            if (time > speed) {
                if (actualFrame < frames.size() - 1 && addSubstract)
                    ++actualFrame;
                else if (actualFrame > 0 && !addSubstract)--actualFrame;
                if (actualFrame == 0) addSubstract = true;
                else if (actualFrame == frames.size() - 1) {
                    if (!looping) playPause();
                    addSubstract = false;
                }
                time = 0;
            }
        }
    }

    @Override
    public SpriteAnimation clone() {
        return new SpriteAnimation(this);
    }

    // GETTERS & SETTERS //

    public Sprite getFrame() { return frames.get(actualFrame); }

    public SpriteSheet getSpriteSheet() { return spriteSheet; }

    public ArrayList<Sprite> getFrames() { return frames; }

    public boolean isPlaying() { return playing; }

    public void setSpeed(int speed) { this.speed = speed; }

    public boolean isLooping() { return looping; }

    public void setLooping(boolean looping) { this.looping = looping; }
}