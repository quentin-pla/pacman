package engines.graphics;

import java.util.ArrayList;

/**
 * Animation depuis un fichier de textures
 */
public class SpriteAnimation extends EntityTexture {
    /**
     * Image de départ
     */
    private int actual_frame = 0;

    /**
     * Images de l'animation
     */
    private ArrayList<int[]> frames = new ArrayList<>();

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
    private SpriteSheet sprite_sheet;

    /**
     * Constructeur
     */
    public SpriteAnimation(SpriteSheet sprite_sheet, int speed, boolean looping) {
        this.sprite_sheet = sprite_sheet;
        this.speed = speed;
        this.looping = looping;
    }

    /**
     * Constructeur par clonage
     * @param clone clone
     */
    private SpriteAnimation(SpriteAnimation clone) {
        this(clone.sprite_sheet, clone.speed, clone.looping);
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
    public void addFrame(int row, int col) {
        frames.add(new int[]{row-1,col-1});
    }

    /**
     * Jouer / Mettre en pause l'animation
     */
    public void playPause() { playing = !playing; }

    /**
     * Réinitialiser l'animation
     */
    public void reset() {
        actual_frame = 0;
        time = 0;
    }

    @Override
    public void cover(Entity entity) {
        renderSpriteQUAD(entity.height, entity.width, entity.x, entity.y, getSpriteSheet().getTexture().getId(),
                getSpriteSheet().getSize(), getActualCoords());
    }

    @Override
    public void update() {
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

    public int[] getActualCoords() { return frames.get(actual_frame); }

    public SpriteSheet getSpriteSheet() { return sprite_sheet; }

    public ArrayList<int[]> getFrames() { return frames; }

    public boolean isPlaying() { return playing; }

    public void setSpeed(int speed) { this.speed = speed; }

    public void setLooping(boolean looping) { this.looping = looping; }
}