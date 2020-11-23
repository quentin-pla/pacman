import gameplay.Gameplay;
import engines.sound.SoundEngine;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static java.lang.Thread.sleep;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        SoundEngine test = new SoundEngine();
        test.loadSound("testpause.wav","pause");
        test.playSound("pause");
        sleep(1000);
        test.pauseSound("pause");
        sleep(2000);
        test.resumeSound("pause");
        new Gameplay();
        while (true){
            //boucle pour tester le son car si le programme n'est pas retenu le son s'arrete
        }

    }
}
