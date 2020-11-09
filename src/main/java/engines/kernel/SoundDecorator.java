package engines.kernel;

import engines.sound.SoundEntity;
import engines.sound.SoundObject;

/**
 * Décorateur entité audio
 */
public interface SoundDecorator extends SoundEntity {
    /**
     * Obtenir l'entité audio
     * @return instance
     */
    SoundObject getAudio();
}
