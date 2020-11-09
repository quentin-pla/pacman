package engines.kernel;

import engines.sound.SoundEngine;
import engines.sound.SoundEntity;

/**
 * Décorateur entité audio
 */
public interface SoundDecorator extends SoundEngine {
    /**
     * Obtenir l'entité audio
     * @return instance
     */
    SoundEntity getAudio();
}
