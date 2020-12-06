package engines.input_output;

import api.SwingWindow;
import engines.graphics.Scene;
import engines.kernel.Entity;
import engines.kernel.KernelEngine;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static java.lang.Thread.sleep;

/**
 * Tests moteur entrées/sorties
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IOEngineTest {
    /**
     * Moteur noyau
     */
    private static KernelEngine kernelEngine;

    /**
     * Moteur entrées / sorties
     */
    private static IOEngine ioEngine;

    /**
     * Valeur de test
     */
    private static int testValue;

    /**
     * Touche du clavier de test
     */
    private static int testKey;

    /**
     * Scène
     */
    private static Scene scene;

    /**
     * Robot utilisé pour simuler les touches pressées
     */
    private static Robot robot;

    /**
     * Entité
     */
    private static Entity entity;

    /**
     * Initialisation
     * @throws AWTException exception
     */
    @BeforeClass
    public static void init() throws AWTException {
        kernelEngine = new KernelEngine();
        ioEngine = kernelEngine.getIoEngine();
        testValue = 0;
        testKey = KeyEvent.VK_UP;
        robot = new Robot();
        entity = kernelEngine.generateEntity();
        kernelEngine.addEvent("test", () -> ++testValue);
        scene = kernelEngine.getGraphicsEngine().generateScene(100,100);
        kernelEngine.getPhysicsEngine().move(entity, 5, 10);
        kernelEngine.getPhysicsEngine().resize(entity, 10, 10);
        kernelEngine.getGraphicsEngine().bindColor(entity, 255, 0, 0);
        kernelEngine.getGraphicsEngine().addToScene(scene, entity);
        kernelEngine.getGraphicsEngine().bindScene(scene);
        kernelEngine.start();
    }

    /**
     * Test pour vérifier les attributs à la création
     */
    @Test
    public void Test00CheckAttributes() {
        Assert.assertNotNull(ioEngine.getKeyboardIO());
        Assert.assertNotNull(ioEngine.getMouseIO());
        Assert.assertNotNull(ioEngine.getBindedClickEvents());
        Assert.assertNotNull(ioEngine.getBindedEvents());
        Assert.assertNotNull(ioEngine.getBindedEventsOnLastKey());
        Assert.assertEquals(1, ioEngine.getEventsListeners().size());
        Assert.assertEquals(kernelEngine, ioEngine.getEventsListeners().get(0));
    }

    /**
     * Test pour activer les entrées / sorties clavier
     */
    @Test
    public void Test01EnableKeyboardIO() {
        ioEngine.enableKeyboardIO();
        Assert.assertEquals(1, SwingWindow.getInstance().getWindow().getKeyListeners().length);
    }

    /**
     * Test pour désactiver les entrées / sorties clavier
     */
    @Test
    public void Test02DisableKeyboardIO() {
        ioEngine.disableKeyboardIO();
        Assert.assertEquals(0, SwingWindow.getInstance().getWindow().getKeyListeners().length);
        ioEngine.enableKeyboardIO();
    }

    /**
     * Test pour activer les entrées / sorties souris
     */
    @Test
    public void Test03EnableMouseIO() {
        ioEngine.enableMouseIO();
        Assert.assertEquals(1, SwingWindow.getInstance().getWindow().getMouseListeners().length);
    }

    /**
     * Test pour désactiver les entrées / sorties souris
     */
    @Test
    public void Test04DisableMouseIO() {
        ioEngine.disableMouseIO();
        Assert.assertEquals(0, SwingWindow.getInstance().getWindow().getMouseListeners().length);
        ioEngine.enableMouseIO();
    }

    /**
     * Test pour attacher un évènement à une touche du clavier
     */
    @Test
    public void Test05BindEvent() {
        ioEngine.bindEvent(testKey, "test");
        Assert.assertTrue(ioEngine.getBindedEvents().containsKey(testKey));
        Assert.assertEquals("test", ioEngine.getBindedEvents().get(testKey));
        Assert.assertTrue(ioEngine.isKeyBindedToEvent(testKey));
        testValue = 0;
        simulateKey(testKey);
        Assert.assertTrue(testValue > 0);
    }

    /**
     * Test pour détacher un évènement lié à une touche
     */
    @Test
    public void Test06UnbindEvent() {
        ioEngine.unbindEvent(testKey);
        Assert.assertTrue(ioEngine.getBindedEvents().isEmpty());
        Assert.assertFalse(ioEngine.isKeyBindedToEvent(testKey));
        testValue = 0;
        simulateKey(testKey);
        Assert.assertEquals(0, testValue);
    }

    /**
     * Test pour attacher un évènement à une dernière touche pressée du clavier
     */
    @Test
    public void Test07BindEventOnLastKey() {
        ioEngine.bindEventOnLastKey(testKey, "test");
        Assert.assertTrue(ioEngine.getBindedEventsOnLastKey().containsKey(testKey));
        Assert.assertEquals("test", ioEngine.getBindedEventsOnLastKey().get(testKey));
        Assert.assertTrue(ioEngine.isKeyBindedToEvent(testKey));
        testValue = 0;
        simulateKey(testKey);
        Assert.assertTrue(testValue > 0);
    }

    /**
     * Test pour détacher un évènement lié à une dernière touche pressée au clavier
     */
    @Test
    public void Test08UnbindEventOnLastKey() {
        ioEngine.unbindEventOnLastKey(testKey);
        Assert.assertTrue(ioEngine.getBindedEventsOnLastKey().isEmpty());
        Assert.assertFalse(ioEngine.isKeyBindedToEvent(testKey));
        testValue = 0;
        simulateKey(testKey);
        Assert.assertEquals(0, testValue);
    }

    /**
     * Test pour attacher un évènement lorsque le clavier est libre
     */
    @Test
    public void Test09BindEventKeyboardFree() {
        ioEngine.bindEventKeyboardFree("test");
        Assert.assertTrue(ioEngine.getBindedEvents().containsKey(-1));
        Assert.assertEquals("test", ioEngine.getBindedEvents().get(-1));
        Assert.assertTrue(ioEngine.isKeyBindedToEvent(-1));
        testValue = 0;
        delay(100);
        Assert.assertTrue(testValue > 0);
    }

    /**
     * Test pour détacher un évènement lorsque le clavier est libre
     */
    @Test
    public void Test10UnbindEventKeyboardFree() {
        ioEngine.unbindEventKeyboardFree();
        Assert.assertTrue(ioEngine.getBindedEvents().isEmpty());
        Assert.assertFalse(ioEngine.isKeyBindedToEvent(-1));
    }

    /**
     * Test pour vérifier si une touche est pressée ou non
     */
    @Test
    public void Test11IsKeyPressed() {
        ioEngine.bindEvent(testKey, "test");
        Assert.assertFalse(ioEngine.isKeyPressed(testKey));
        simulateKeyPress(testKey);
        Assert.assertTrue(ioEngine.isKeyPressed(testKey));
        simulateKeyRelease(testKey);
        ioEngine.unbindEvent(testKey);
    }

    /**
     * Test pour vérifier si le clavier est libre
     */
    @Test
    public void Test12IsKeyboardFree() {
        Assert.assertTrue(ioEngine.isKeyboardFree());
        ioEngine.bindEvent(testKey, "test");
        simulateKeyPress(testKey);
        Assert.assertFalse(ioEngine.isKeyboardFree());
        simulateKeyRelease(testKey);
        ioEngine.unbindEvent(testKey);
    }

    /**
     * Test pour vérifier la dernière touche pressée
     */
    @Test
    public void Test13LastPressedKey() {
        ioEngine.bindEvent(testKey, "test");
        simulateKey(testKey);
        Assert.assertEquals(testKey, ioEngine.lastPressedKey());
    }

    /**
     * Test pour restaurer la dernière touche pressée
     */
    @Test
    public void Test14ResetLastPressedKey() {
        ioEngine.resetLastPressedKey();
        Assert.assertEquals(-1, ioEngine.lastPressedKey());
    }

    /**
     * Test pour vérifier si un bouton de la souris est pressée
     */
    @Test
    public void Test15IsMouseButtonPressed() {
        Assert.assertTrue(ioEngine.getMouseIO().getPressedButtons().isEmpty());
        simulateButtonPress(10, 10);
        Assert.assertFalse(ioEngine.getMouseIO().getPressedButtons().isEmpty());
        simulateButtonRelease();
    }

    /**
     * Test pour vérifier si la souris n'est pas utilisée
     */
    @Test
    public void Test16IsMouseFree() {
        Assert.assertTrue(ioEngine.isMouseFree());
        simulateButtonPress(0,0);
        Assert.assertFalse(ioEngine.isMouseFree());
        simulateButtonRelease();
    }

    /**
     * Test pour réinitialiser le dernier bouton pressé
     */
    @Test
    public void Test18ResetLastClick() {
        ioEngine.resetLastClick();
        Assert.assertEquals(-1, ioEngine.lastPressedButton());
    }

    /**
     * Test pour attacher un évènement à un click sur une entité
     */
    @Test
    public void Test19BindEventOnClick() {
        ioEngine.bindEventOnClick(entity, "test");
        Assert.assertEquals(1, ioEngine.getBindedClickEvents().size());
        Assert.assertTrue(ioEngine.getBindedClickEvents().containsKey(entity));
        Assert.assertEquals("test", ioEngine.getBindedClickEvents().get(entity));
        testValue = 0;
        simulateClick(entity.getGraphicEntity().getX() + 1, entity.getGraphicEntity().getY() + 1);
        Assert.assertTrue(testValue > 0);
        testValue = 0;
        kernelEngine.getPhysicsEngine().move(entity, 50, 40);
        simulateClick(40, 30);
        Assert.assertEquals(0, testValue);
        simulateClick(entity.getGraphicEntity().getX() + 1, entity.getGraphicEntity().getY() + 1);
        Assert.assertTrue(testValue > 0);
    }

    /**
     * Test pour détacher un évènement à un click sur une entité
     */
    @Test
    public void Test20UnbindEventOnClick() {
        ioEngine.unbindEventOnClick(entity);
        Assert.assertTrue(ioEngine.getBindedClickEvents().isEmpty());
    }

    // UTILITAIRES //

    /**
     * Simuler une touche pressée au clavier
     * @param key touche
     */
    private void simulateKey(Integer key) {
        simulateKeyPress(key);
        simulateKeyRelease(key);
    }

    /**
     * Simuler un click à la souris
     */
    private void simulateClick(int x, int y) {
        simulateButtonPress(x, y);
        simulateButtonRelease();
    }

    /**
     * Simuler une touche enfoncée au clavier
     * @param key touche
     */
    private void simulateKeyPress(Integer key) {
        robot.keyPress(key);
        robot.delay(200);
    }

    /**
     * Simuler un click pressé
     * @param x position horizontale
     * @param y position verticale
     */
    private void simulateButtonPress(int x, int y) {
        Point sceneLocation = scene.getLocationOnScreen();
        robot.mouseMove((int) (sceneLocation.getX() + x), (int) (sceneLocation.getY() + y));
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(200);
    }

    /**
     * Simuler la fin d'un enfoncement de touche
     * @param key touche
     */
    private void simulateKeyRelease(Integer key) {
        robot.keyRelease(key);
        robot.delay(200);
    }

    /**
     * Simuler la fin d'un click à la souris
     */
    private void simulateButtonRelease() {
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(200);
    }

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
