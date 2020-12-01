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
    private int actual_frame = 0;

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
    private boolean add_substract = true;

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
    private SpriteSheet spriteSheet;

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
        this.actual_frame = clone.actual_frame;
        this.frames = new ArrayList<>(clone.frames);
        this.add_substract = clone.add_substract;
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
        actual_frame = 0;
        time = 0;
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
                if (add_substract) ++actual_frame;
                else --actual_frame;
                if (actual_frame <= 0) {
                    if (!looping) reset();
                    add_substract = !add_substract;
                }
                else if (actual_frame >= frames.size() - 1)
                    add_substract = !add_substract;
                time = 0;
            }
        }
    }

    @Override
    public SpriteAnimation clone() {
        return new SpriteAnimation(this);
    }

    // GETTERS & SETTERS //

    public Sprite getFrame() { return frames.get(actual_frame); }

    public SpriteSheet getSpriteSheet() { return spriteSheet; }

    public ArrayList<Sprite> getFrames() { return frames; }

    public boolean isPlaying() { return playing; }

    public void setSpeed(int speed) { this.speed = speed; }

    public void setLooping(boolean looping) { this.looping = looping; }
}