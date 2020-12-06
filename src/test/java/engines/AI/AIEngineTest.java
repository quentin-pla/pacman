package engines.AI;

import engines.kernel.Entity;
import engines.kernel.KernelEngine;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Tests moteur intelligence artificielle
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AIEngineTest {
    /**
     * Moteur noyau
     */
    private static KernelEngine kernelEngine;

    /**
     * Moteur IA
     */
    private static AIEngine aiEngine;

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
        aiEngine = kernelEngine.getAiEngine();
        testValue = 0;
        kernelEngine.addEvent("test", () -> ++testValue);
    }

    /**
     * Test pour vérifier les attributs à la création
     */
    @Test
    public void Test00CheckAttributes() {
        Assert.assertTrue(aiEngine.getEntities().isEmpty());
        Assert.assertEquals(1, aiEngine.getEventsListeners().size());
        Assert.assertEquals(kernelEngine, aiEngine.getEventsListeners().get(0));
    }

    /**
     * Test pour créer une entité intelligence artificielle
     */
    @Test
    public void Test01CreateEntity() {
        Assert.assertTrue(aiEngine.getEntities().isEmpty());
        entity = kernelEngine.generateEntity();
        Assert.assertEquals(1, aiEngine.getEntities().size());
        Assert.assertTrue(aiEngine.getEntities().containsKey(entity.getId()));
        Assert.assertEquals(entity.getAiEntity(), aiEngine.getEntities().get(entity.getId()));
    }

    /**
     * Test pour attacher un évènement à une entité IA
     */
    @Test
    public void Test02BindEvent() {
        aiEngine.bindEvent(entity,"test");
        Assert.assertEquals("test", entity.getAiEntity().getEvent());
    }

    /**
     * Test pour mettre à jour une entité IA
     */
    @Test
    public void Test03UpdateEntity() {
        Assert.assertEquals(0, testValue);
        aiEngine.updateEntity(entity.getAiEntity());
        Assert.assertEquals(1, testValue);
    }

    /**
     * Test pour mettre à jour toutes les entités IA
     */
    @Test
    public void Test04UpdateEntities() {
        Assert.assertEquals(1, testValue);
        aiEngine.updateEntities();
        Assert.assertEquals(2, testValue);
    }

    /**
     * Test pour supprimer un évènement lié à une entité IA
     */
    @Test
    public void Test05ClearEvents() {
        Assert.assertEquals("test", entity.getAiEntity().getEvent());
        aiEngine.clearEvent(entity.getAiEntity());
        Assert.assertNull(entity.getAiEntity().getEvent());
        aiEngine.updateEntity(entity.getAiEntity());
        Assert.assertEquals(2, testValue);
    }

    /**
     * Test pour supprimer l'évènement lié de chaque entité IA
     */
    @Test
    public void Test06ClearEvents() {
        aiEngine.bindEvent(entity,"test");
        Assert.assertEquals("test", entity.getAiEntity().getEvent());
        aiEngine.clearEvents();
        Assert.assertNull(entity.getAiEntity().getEvent());
        aiEngine.updateEntity(entity.getAiEntity());
        Assert.assertEquals(2, testValue);
    }
}
