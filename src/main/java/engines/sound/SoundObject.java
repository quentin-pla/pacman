package engines.sound;

public class SoundObject implements SoundEntity {
    /**
     * Constructeur
     */
    protected SoundObject() {}

    /**
     * Cloner l'entité
     * @return clone
     */
    public SoundObject clone() {
        return new SoundObject();
    }
}
