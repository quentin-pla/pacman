package engines.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Moteur audio
 */
public class SoundEngine {
    /**
     * Liste des entités son référencées par leur nom
     */
    private final ConcurrentMap<String, Clip> sounds = new ConcurrentHashMap<>();

    /**
     * Volume global
     */
    private int globalVolume = 50;

    /**
     * Constructeur
     */
    public SoundEngine() {}

    /**
     * Précharger un son pour l'utiliser ultérieurement
     * @param path chamin
     * @param soundName nom du son
     */
    public void loadSound(String path, String soundName) {
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                SoundEngine.class.getResource("/sounds/" + path)
            );
            clip.open(audioInputStream);
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        sounds.put(soundName,clip);
    }

    /**
     * Jouer un son
     * @param name entité
     */
    public void playSound(String name) {
        Clip sound = sounds.get(name);
        sound.setFramePosition(0);
        sound.start();
    }

    /**
     * Arrêter un son
     * @param name nom
     */
    public void stopSound(String name) {
        Clip sound = sounds.get(name);
        sound.setMicrosecondPosition(0);
        sound.stop();
    }

    /**
     * Arrêter tous les sons
     */
    public void clearSounds() {
        for (String sound : sounds.keySet())
            stopSound(sound);
    }

    /**
     * Jouer un son un boucle
     * @param name nom
     */
    public void loopSound(String name) {
        Clip sound = sounds.get(name);
        sound.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Mettre en pause la lecture d'un son
     * @param name nom
     */
    public void pauseSound(String name) {
        Clip sound = sounds.get(name);
        int framePosition = sound.getFramePosition();
        long seconds = sound.getMicrosecondPosition();
        sound.stop();
        sound.setFramePosition(framePosition);
        sound.setMicrosecondPosition(seconds);
    }

    /**
     * Reprendre la lecture d'un son
     * @param name nom
     */
    public void resumeSound(String name) {
        Clip sound = sounds.get(name);
        sound.setMicrosecondPosition(sound.getMicrosecondPosition());
        sound.start();
    }

    /**
     * Définir le volume global
     * @param volume volume
     */
    public void setGlobalVolume(int volume) {
        for(Map.Entry<String, Clip> entry : sounds.entrySet()) {
            Clip clip = entry.getValue();
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(getVolumeAsFloat(volume)));
            globalVolume = volume;
        }
    }

    /**
     * Incrémenter le volume
     */
    public void incrementGlobalVolume(){
        setGlobalVolume(Math.min(globalVolume + 5, 100));
    }

    /**
     * décrémenter le volume
     */
    public void decrementGlobalVolume(){
        setGlobalVolume(Math.max(globalVolume - 5, 0));
    }

    /**
     * Obtenir le volume en float
     * @param volume volume
     * @return volume au format float
     */
    public float getVolumeAsFloat(int volume) {
        if (volume >= 0 && volume <= 100)
            return (float) volume / 100;
        else throw new IllegalArgumentException("Volume invalide: " + volume);
    }

    // GETTERS //

    public int getGlobalVolume() { return globalVolume; }

    public Map<String, Clip> getSounds() { return sounds; }

}
