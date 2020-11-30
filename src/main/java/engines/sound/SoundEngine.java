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

    private float Globalvolume = 1;

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
     * récupérer tous les souns
     * @return
     */
    public Map<String, Clip> getSounds() {
        return sounds;
    }

    /**
     * récupérer le volume actuel
     * @param name
     * @return
     */
    public float getVolume(String name) {
        Clip sound = sounds.get(name);
        FloatControl gainControl = (FloatControl) sound.getControl(FloatControl.Type.MASTER_GAIN);
        return (float) Math.pow(10f, gainControl.getValue() / 20f);
    }

    /**
     * mettre un nouveau volume
     * @param name
     * @param volume
     */
    public void setVolume(String name,float volume) {
        Clip sound = sounds.get(name);
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) sound.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
    }

    public float getGlobalvolume(){
        return Globalvolume;
    }

    public void setGlobalVolume(float volume) {
        for(Map.Entry<String, Clip> entry : sounds.entrySet()) {
            Clip clip = entry.getValue();
            if (volume < 0f || volume > 1f)
                throw new IllegalArgumentException("Volume not valid: " + volume);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(20f * (float) Math.log10(volume));
            Globalvolume = (float) Math.pow(10f, gainControl.getValue() / 20f);
        }
    }

    public void incrementGlobalVolume(){
        if (Globalvolume < 0.95){
            setGlobalVolume((getGlobalvolume()*100+5)/100);
        } else {
            setGlobalVolume(1);
        }
    }

    public void decrementGlobalVolume(){
        if (Globalvolume > 0.05){
            setGlobalVolume((getGlobalvolume()*100-5)/100);
        } else {
            setGlobalVolume(0);
        }
    }
}
