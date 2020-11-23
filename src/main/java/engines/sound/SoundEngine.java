package engines.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.HashMap;

/**
 * Moteur de sons
 */
public class SoundEngine {

    /**
     * Liste des entités son référencées par leur nom
     */
    private static final HashMap<String, SoundEntity> entities = new HashMap<>();

    /**
     * Liste des time codes de reprise d'un son
     */
    private static final HashMap<String, Long> entitiesPaused = new HashMap<>();

    /**
     * Préchargement d'un son afin de le rendre disponible a tout moment
     * @param path
     * @param soundName
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     */
    public void loadSound(String path,String soundName) throws IOException, UnsupportedAudioFileException, LineUnavailableException {

        //creation du clip audio
        Clip clip = AudioSystem.getClip();
        try {

            //chargement du stream audio et attribution a un clip
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(SoundEngine.class.getResourceAsStream("/sounds/"+ path));
            clip.open(audioInputStream);

        } catch (Exception e){
            System.err.println(e.getMessage());
        }

        //ajout du son "clip" a la liste globale
        entities.put(soundName,new SoundEntity(clip));
        long timeNull = 0;
        //ajout d'un timecode de pause du son à 0 car aucun son n'a été joué pour le moment
        //évite toute erreur liée a la fonction resumeSound() car un timecode n'aurait pas été initialisé
        entitiesPaused.put(soundName, timeNull);
    }

    /**
     * Joue un son
     * @param name
     */
    public void playSound(String name){
        entities.get(name).sound.start();
    }

    /**
     * Stop un son
     * @param name
     */
    public void stopSound(String name){
        entities.get(name).sound.stop();
    }

    /**
     * Joue un son un boucle jusqu'a arret
     * @param name
     */
    public void loopSound(String name){
        entities.get(name).sound.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Mets un son en pause
     * @param name
     */
    public void pauseSound(String name){
        //récupère le timeCode auquel le son joué se trouve
        long tempTimeCode = entities.get(name).sound.getMicrosecondPosition();
        //sauvegarde ce time code dans la bonne hashmap
        entitiesPaused.put(name,tempTimeCode);
        //stop le son joué
        entities.get(name).sound.stop();
    }

    /**
     * Joue le son a partir du moment auquel il a été arrêté
     * @param name
     */
    public void resumeSound(String name){
        //recupère le timecode du son en pause
        long timeCode = entitiesPaused.get(name);
        //applique au son ce timecode et le lance
        entities.get(name).sound.setMicrosecondPosition(timeCode);
        entities.get(name).sound.start();
        //reset le timecode dans la hashmap afin de ne pas garder une donnée désormais éronnée
        long timeReset = 0;
        entitiesPaused.put(name,timeReset);
    }

}
