package engines.sound;

import engines.kernel.KernelEngine;

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
     * Moteur noyau
     */
    private KernelEngine kernelEngine;

    /**
     * Liste des entités son référencées par leur nom
     */
    private final Map<String, Clip> sounds = new HashMap<>();

    private final CopyOnWriteArrayList<Clip> playingSounds = new CopyOnWriteArrayList<>();

    /**
     * Constructeur
     * @param kernelEngine moteur noyau
     */
    public SoundEngine(KernelEngine kernelEngine) {
        this.kernelEngine = kernelEngine;
    }

    /**
     * Précharger un son pour l'utiliser ultérieurement
     * @param path chamin
     * @param soundName nom du son
     */
    public Clip loadSound(String path,String soundName) {
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
        return clip;
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

    public Map<String, Clip> getSounds() {
        return sounds;
    }
}
