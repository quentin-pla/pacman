package engines.sound;

public class SoundEntity implements SoundEngine {
    /**
     * Constructeur
     */
    protected SoundEntity() {}

    /**
     * Cloner l'entité
     * @return clone
     */
    public SoundEntity clone() {
        return new SoundEntity();
    }
}
