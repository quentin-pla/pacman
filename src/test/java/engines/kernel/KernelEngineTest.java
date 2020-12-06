package engines.kernel;

import api.SwingTimer;
import api.SwingWindow;
import engines.graphics.Scene;
import engines.graphics.Window;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static java.lang.Thread.sleep;

/**
 * Tests moteur noyau
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KernelEngineTest {
    /**
     * Moteur noyau
     */
    private static KernelEngine kernelEngine;

    /**
     * Entité
     */
    private static Entity entity;

    /**
     * Valeur de test
     */
    private static int testValue;

    /**
     * Initialisation
     */
    @BeforeClass
    public static void init() {
        kernelEngine = new KernelEngine();
        testValue = 0;
    }

    /**
     * Test pour vérifier les attributs à la création
     */
    @Test
    public void Test00CheckAttributes() {
        Assert.assertNotNull(kernelEngine.getGraphicsEngine());
        Assert.assertNotNull(kernelEngine.getPhysicsEngine());
        Assert.assertNotNull(kernelEngine.getIoEngine());
        Assert.assertNotNull(kernelEngine.getSoundEngine());
        Assert.assertNotNull(kernelEngine.getAiEngine());
        Assert.assertFalse(kernelEngine.getGraphicsEngine().getEventsListeners().isEmpty());
        Assert.assertFalse(kernelEngine.getPhysicsEngine().getEventsListeners().isEmpty());
        Assert.assertFalse(kernelEngine.getIoEngine().getEventsListeners().isEmpty());
        Assert.assertFalse(kernelEngine.getAiEngine().getEventsListeners().isEmpty());
    }

    /**
     * Test pour générer une nouvelle entité
     */
    @Test
    public void Test01GenerateEntity() {
        Entity entity = kernelEngine.generateEntity();
        Assert.assertEquals(1, kernelEngine.getEntities().size());
        Assert.assertEquals(1, entity.getId());
        Assert.assertNotNull(entity.getAiEntity());
        Assert.assertNotNull(entity.getGraphicEntity());
        Assert.assertNotNull(entity.getPhysicEntity());
        Assert.assertEquals(1, kernelEngine.getAiEngine().getEntities().size());
        Assert.assertEquals(1, kernelEngine.getGraphicsEngine().getEntities().size());
        Assert.assertEquals(1, kernelEngine.getPhysicsEngine().getEntities().size());
        kernelEngine.removeEntity(entity);
    }

    /**
     * Test pour cloner une entité
     */
    @Test
    public void Test02CloneEntity() {
        Entity entity = kernelEngine.generateEntity();
        Entity clone = entity.clone();
        Assert.assertEquals(entity.getId() + 1, clone.getId());
        Assert.assertNotEquals(entity.aiEntity, clone.aiEntity);
        Assert.assertNotEquals(entity.graphicEntity, clone.graphicEntity);
        Assert.assertNotEquals(entity.physicEntity, clone.physicEntity);
        Assert.assertEquals(2, kernelEngine.getAiEngine().getEntities().size());
        Assert.assertEquals(2, kernelEngine.getGraphicsEngine().getEntities().size());
        Assert.assertEquals(2, kernelEngine.getPhysicsEngine().getEntities().size());
        kernelEngine.removeEntity(entity);
        kernelEngine.removeEntity(clone);
    }

    /**
     * Test pour supprimer une entité
     */
    @Test
    public void Test03RemoveEntity() {
        Entity entity = kernelEngine.generateEntity();
        Assert.assertEquals(1, kernelEngine.getEntities().size());
        Assert.assertEquals(1, kernelEngine.getAiEngine().getEntities().size());
        Assert.assertEquals(1, kernelEngine.getGraphicsEngine().getEntities().size());
        Assert.assertEquals(1, kernelEngine.getPhysicsEngine().getEntities().size());
        kernelEngine.removeEntity(entity);
        Assert.assertEquals(0, kernelEngine.getEntities().size());
        Assert.assertEquals(0, kernelEngine.getAiEngine().getEntities().size());
        Assert.assertEquals(0, kernelEngine.getGraphicsEngine().getEntities().size());
        Assert.assertEquals(0, kernelEngine.getPhysicsEngine().getEntities().size());
    }

    /**
     * Test pour changer une scène
     */
    @Test
    public void Test04SwitchScene() {
        Scene scene1 = kernelEngine.getGraphicsEngine().generateScene(10,10);
        Scene scene2 = kernelEngine.getGraphicsEngine().generateScene(20,20);
        kernelEngine.switchScene(scene1);
        Assert.assertEquals(scene1, Window.getActualScene());
        kernelEngine.switchScene(scene2);
        Assert.assertEquals(scene2, Window.getActualScene());
    }

    /**
     * Test pour démarrer le moteur noyau
     */
    @Test
    public void Test05Start() {
        kernelEngine.start();
        delay(1000);
        Assert.assertTrue(SwingWindow.getInstance().getWindow().isVisible());
    }

    /**
     * Test pour éxecuter une tache périodique
     */
    @Test
    public void Test06StartTimer() {
        testValue = 0;
        kernelEngine.startTimer("test", 10, () -> ++testValue);
        Assert.assertTrue(SwingTimer.getInstance().getTimers().containsKey("test"));
        delay(50);
        Assert.assertTrue(testValue > 0);
    }

    /**
     * Test pour arrêter une tache périodique
     */
    @Test
    public void Test07StopTimer() {
        kernelEngine.stopTimer("test");
        int actualTestValue = testValue;
        delay(50);
        Assert.assertEquals(actualTestValue, testValue);
    }

    /**
     * Test pour redémarrer une tache périodique
     */
    @Test
    public void Test08RestartTimer() {
        testValue = 0;
        kernelEngine.restartTimer("test");
        delay(50);
        Assert.assertTrue(testValue > 0);
        kernelEngine.stopTimer("test");
    }

    /**
     * Test pour mettre à jour les entités focus lors d'un changement de scène
     */
    @Test
    public void Test09UpdateFocusedEntities() {
        Scene scene1 = kernelEngine.getGraphicsEngine().generateScene(10,10);
        Scene scene2 = kernelEngine.getGraphicsEngine().generateScene(30,10);
        Entity entity1 = kernelEngine.generateEntity();
        Entity entity2 = kernelEngine.generateEntity();
        kernelEngine.getGraphicsEngine().addToScene(scene1, entity1);
        kernelEngine.getGraphicsEngine().addToScene(scene2, entity2);
        kernelEngine.switchScene(scene1);
        Assert.assertEquals(1, kernelEngine.getAiEngine().getEntities().size());
        Assert.assertEquals(1, kernelEngine.getGraphicsEngine().getEntities().size());
        Assert.assertEquals(1, kernelEngine.getPhysicsEngine().getEntities().size());
        Assert.assertTrue(kernelEngine.getGraphicsEngine().getEntities().containsKey(entity1.getId()));
        Assert.assertTrue(kernelEngine.getAiEngine().getEntities().containsKey(entity1.getId()));
        Assert.assertTrue(kernelEngine.getPhysicsEngine().getEntities().containsKey(entity1.getId()));
        kernelEngine.switchScene(scene2);
        Assert.assertEquals(1, kernelEngine.getAiEngine().getEntities().size());
        Assert.assertEquals(1, kernelEngine.getGraphicsEngine().getEntities().size());
        Assert.assertEquals(1, kernelEngine.getPhysicsEngine().getEntities().size());
        Assert.assertTrue(kernelEngine.getGraphicsEngine().getEntities().containsKey(entity2.getId()));
        Assert.assertTrue(kernelEngine.getAiEngine().getEntities().containsKey(entity2.getId()));
        Assert.assertTrue(kernelEngine.getPhysicsEngine().getEntities().containsKey(entity2.getId()));
    }

    /**
     * Test pour ajouter un évènement
     */
    @Test
    public void Test10AddEvent() {
        kernelEngine.addEvent("test", () -> ++testValue);
        Assert.assertEquals(1, kernelEngine.getEvents().size());
    }

    /**
     * Test pour exécuter un évènement
     */
    @Test
    public void Test11OnEvent() {
        testValue = 0;
        kernelEngine.onEvent("test");
        Assert.assertEquals(1, testValue);
    }

    /**
     * Test pour mettre à jour une entité
     */
    @Test
    public void Test12OnEntityUpdate() {
        Entity entity = kernelEngine.getEntities().get(0);
        Assert.assertEquals(0, entity.getGraphicEntity().getX());
        Assert.assertEquals(0, entity.getGraphicEntity().getY());
        Assert.assertEquals(0, entity.getGraphicEntity().getHeight());
        Assert.assertEquals(0, entity.getGraphicEntity().getWidth());
        kernelEngine.getPhysicsEngine().move(entity,10,5);
        kernelEngine.getPhysicsEngine().resize(entity,20,20);
        Assert.assertEquals(10, entity.getGraphicEntity().getX());
        Assert.assertEquals(5, entity.getGraphicEntity().getY());
        Assert.assertEquals(20, entity.getGraphicEntity().getHeight());
        Assert.assertEquals(20, entity.getGraphicEntity().getWidth());
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
