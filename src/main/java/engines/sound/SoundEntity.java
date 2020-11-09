package engines.sound;

/**
 * Entité audio
 */
public interface SoundEntity {
    /**
     * Générer une nouvelle entité
     * @return entité audio
     */
    static SoundObject generateEntity() { return new SoundObject(); }
}
