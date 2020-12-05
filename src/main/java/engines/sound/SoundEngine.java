package engines.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Moteur audio
 */
public class SoundEngine {
    /**
     * Réservoir de threads
     */
    private static ExecutorService executor = Executors.newFixedThreadPool(4);

    /**
     * Liste des entités son référencées par leur nom
     */
    private final Map<String, Clip> sounds = new HashMap<>();

    /**
     * Volume global
     */
    private int globalVolume = 50;

    /**
     * Sons en cours de lecture
     */
    private final CopyOnWriteArrayList<Clip> playingSounds = new CopyOnWriteArrayList<>();

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
                SoundEngine.class.getResourceAsStream("/sounds/" + path)
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
        executor.execute(() -> {
            Clip sound = this.sounds.get(name);
            sound.setFramePosition(0);
            sound.start();
        });
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
        for (Clip sound : sounds.values())
            sound.stop();
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
        sound.stop();
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
     * Savoir si un son est joué
     * @param name nom
     * @return booléen
     */
    public boolean isSoundPlaying(String name) {
        return playingSounds.contains(sounds.get(name));
    }

    /**
     * Récupérer le volume actuel d'un son
     * @param name nom du son
     * @return volume
     */
    public int getVolume(String name) {
        Clip sound = sounds.get(name);
        FloatControl gainControl = (FloatControl) sound.getControl(FloatControl.Type.MASTER_GAIN);
        return Math.round((float) Math.pow(10f, gainControl.getValue() / 20f))*100;
    }

    /**
     * Modifier le volume d'un son spécifique
     * @param name nom du son
     * @param volume volume de 0 à 100
     */
    public void setSoundVolume(String name, int volume) {
        Clip sound = sounds.get(name);
        float floatVolume = getVolumeAsFloat(volume);
        FloatControl gainControl = (FloatControl) sound.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(floatVolume));
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
