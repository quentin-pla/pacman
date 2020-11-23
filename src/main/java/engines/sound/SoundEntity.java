package engines.sound;

import javax.sound.sampled.Clip;

/**
 * Entité sonore
 */
public class SoundEntity {

    /**
     * clip de son
     */
    Clip sound;

    /**
     * Constructeur
     * @param loadedSound
     */
    protected SoundEntity(Clip loadedSound){
        sound = loadedSound;
    }
}
