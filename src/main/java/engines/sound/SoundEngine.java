package engines.sound;

/**
 * Moteur audio
 */
public interface SoundEngine {
    /**
     * Générer une nouvelle entité
     * @return entité audio
     */
    static SoundEntity generateEntity() { return new SoundEntity(); }
}
