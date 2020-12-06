package engines.sound;

import engines.kernel.KernelEngine;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.sound.sampled.Clip;

import static java.lang.Thread.sleep;

/**
 * Tests moteur intelligence artificielle
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SoundEngineTest {
    /**
     * Moteur noyau
     */
    private static KernelEngine kernelEngine;

    /**
     * Moteur audio
     */
    private static SoundEngine soundEngine;

    /**
     * Initialisation
     */
    @BeforeClass
    public static void init() {
        kernelEngine = new KernelEngine();
        soundEngine = kernelEngine.getSoundEngine();
    }

    /**
     * Test pour vérifier les attributs à la création
     */
    @Test
    public void Test00CheckAttributes() {
        Assert.assertTrue(soundEngine.getSounds().isEmpty());
    }

    /**
     * Test pour charger un son
     */
    @Test
    public void Test01LoadSound() {
        soundEngine.loadSound("munch_1.wav", "munch");
        Assert.assertTrue(soundEngine.getSounds().containsKey("munch"));
    }

    /**
     * Test pour jouer un son
     */
    @Test
    public void Test02PlaySound() {
        soundEngine.setGlobalVolume(0);
        int frames = soundEngine.getSounds().get("munch").getFrameLength();
        Assert.assertEquals(0, soundEngine.getSounds().get("munch").getFramePosition());
        soundEngine.playSound("munch");
        delay(200);
        Assert.assertEquals(frames, soundEngine.getSounds().get("munch").getFramePosition());
    }

    /**
     * Test pour stopper un son
     */
    @Test
    public void Test03StopSound() {
        soundEngine.stopSound("munch");
        Assert.assertEquals(0, soundEngine.getSounds().get("munch").getMicrosecondPosition());
    }

    /**
     * Test pour stopper tous les sons
     */
    @Test
    public void Test04ClearSounds() {
        soundEngine.playSound("munch");
        delay(10);
        soundEngine.clearSounds();
        Assert.assertEquals(0, soundEngine.getSounds().get("munch").getMicrosecondPosition());
    }

    /**
     * Test pour jouer un son en boucle
     */
    @Test
    public void Test05LoopSound() {
        soundEngine.loopSound("munch");
        delay(200);
        Assert.assertNotEquals(0, soundEngine.getSounds().get("munch").getMicrosecondLength());
        delay(200);
        Assert.assertNotEquals(0, soundEngine.getSounds().get("munch").getMicrosecondLength());
        soundEngine.stopSound("munch");
    }

    /**
     * Test pour mettre un son en pause
     */
    @Test
    public void Test06PauseSound() {
        Clip sound = soundEngine.getSounds().get("munch");
        soundEngine.playSound("munch");
        delay(250);
        soundEngine.pauseSound("munch");
        Assert.assertTrue(sound.getFramePosition() > 0);
        Assert.assertTrue(sound.getFramePosition() < sound.getFrameLength());
    }

    /**
     * Test pour reprendre la lecture d'un son
     */
    @Test
    public void Test07ResumeSounds() {
        Clip sound = soundEngine.getSounds().get("munch");
        int state = sound.getFramePosition();
        soundEngine.resumeSound("munch");
        delay(200);
        Assert.assertTrue(sound.getFramePosition() > state);
        Assert.assertEquals(sound.getFrameLength(), sound.getFramePosition());
    }

    /**
     * Test pour définir le volume sonore global
     */
    @Test
    public void Test09SetGlobalVolume() {
        soundEngine.setGlobalVolume(1);
        Assert.assertEquals(1, soundEngine.getGlobalVolume());
    }

    /**
     * Test pour incrémenter le volume global de 5
     */
    @Test
    public void Test10IncrementGlobalVolume() {
        soundEngine.setGlobalVolume(0);
        soundEngine.incrementGlobalVolume();
        Assert.assertEquals(5, soundEngine.getGlobalVolume());
    }

    /**
     * Test pour décrémenter le volume global de 5
     */
    @Test
    public void Test11DecrementGlobalVolume() {
        soundEngine.decrementGlobalVolume();
        Assert.assertEquals(0, soundEngine.getGlobalVolume());
    }

    // UTILITAIRES //

    /**
     * Attendre pendant un moment
     * @param time temps
     */
    private void delay(int time) {
        try {
            sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
