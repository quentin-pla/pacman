package engines.physics;

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
public class PhysicsEngineTest {
    /**
     * Moteur noyau
     */
    private static KernelEngine kernelEngine;

    /**
     * Moteur de physique
     */
    private static PhysicsEngine physicsEngine;

    /**
     * Entité 1
     */
    private static Entity entity1;

    /**
     * Entité 2
     */
    private static Entity entity2;

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
        physicsEngine = kernelEngine.getPhysicsEngine();
        testValue = 0;
        kernelEngine.addEvent("test", () -> ++testValue);
        entity1 = kernelEngine.generateEntity();
        entity2 = kernelEngine.generateEntity();
    }

    /**
     * Test pour vérifier les attributs à la création
     */
    @Test
    public void Test00CheckAttributes() {
        Assert.assertEquals(1, physicsEngine.getEventsListeners().size());
        Assert.assertEquals(kernelEngine, physicsEngine.getEventsListeners().get(0));
        Assert.assertEquals(0, physicsEngine.getCenteredEvents().size());
        Assert.assertEquals(0, physicsEngine.getCollisionsEvents().size());
    }

    /**
     * Test pour ajouter des collisions entre deux entités
     */
    @Test
    public void Test01AddCollisions() {
        Assert.assertTrue(entity1.getPhysicEntity().getCollisions().isEmpty());
        Assert.assertTrue(entity2.getPhysicEntity().getCollisions().isEmpty());
        physicsEngine.addCollisions(entity1, entity2);
        Assert.assertTrue(entity1.getPhysicEntity().getCollisions().contains(entity2.getPhysicEntity()));
        Assert.assertTrue(entity2.getPhysicEntity().getCollisions().contains(entity1.getPhysicEntity()));
    }

    /**
     * Test pour supprimer des collisions entre entités
      */
    @Test
    public void Test02RemoveCollisions() {
        physicsEngine.removeCollisions(entity1, entity2);
        Assert.assertFalse(entity1.getPhysicEntity().getCollisions().contains(entity2.getPhysicEntity()));
        Assert.assertFalse(entity2.getPhysicEntity().getCollisions().contains(entity1.getPhysicEntity()));
    }

    /**
     * Test pour ajouter une limite de déplacement
     */
    @Test
    public void Test03AddBoundLimits() {
        Assert.assertNull(entity1.getPhysicEntity().getBoundLimits());
        physicsEngine.addBoundLimits(entity1,10,10,20,40);
        int[] boundsLimits = entity1.getPhysicEntity().getBoundLimits();
        Assert.assertEquals(10, boundsLimits[0]);
        Assert.assertEquals(10, boundsLimits[1]);
        Assert.assertEquals(20, boundsLimits[2]);
        Assert.assertEquals(40, boundsLimits[3]);
    }

    /**
     * Test pour attacher un évènement à une collision
     */
    @Test
    public void Test04BindEventOnCollision() {
        physicsEngine.bindEventOnCollision(entity1, "test");
        boolean contained = false;
        for (PhysicEntity[] key : physicsEngine.getCollisionsEvents().keySet()) {
            if (key[0] == entity1.getPhysicEntity() && key[1] == null) {
                contained = true;
                break;
            }
        }
        Assert.assertTrue(contained);
        Assert.assertTrue(physicsEngine.getCollisionsEvents().containsValue("test"));

        physicsEngine.bindEventOnCollision(entity1, entity2,"test");
        contained = false;
        for (PhysicEntity[] key : physicsEngine.getCollisionsEvents().keySet()) {
            if (key[0] == entity1.getPhysicEntity() && key[1] == entity2.getPhysicEntity()) {
                contained = true;
                break;
            }
        }
        Assert.assertTrue(contained);
    }

    /**
     * Test pour attacher un évènement lorsque deux entités sont superposées
     */
    @Test
    public void Test05BindEventOnSameLocation() {
        physicsEngine.bindEventOnSameLocation(entity1, entity2, "test");
        boolean contained = false;
        for (PhysicEntity[] key : physicsEngine.getCenteredEvents().keySet()) {
            if (key[0] == entity1.getPhysicEntity() && key[1] == entity2.getPhysicEntity()) {
                contained = true;
                break;
            }
        }
        Assert.assertTrue(contained);
        Assert.assertTrue(physicsEngine.getCenteredEvents().containsValue("test"));
    }

    /**
     * Test pour vérifier si une entité est en collision
     */
    @Test
    public void Test06IsInCollision() {
        physicsEngine.addCollisions(entity1, entity2);
        physicsEngine.resize(entity1, 10, 10);
        physicsEngine.resize(entity2, 10, 10);
        physicsEngine.move(entity2, 10,0);
        Assert.assertFalse(physicsEngine.isInCollision(entity1));
        Assert.assertFalse(physicsEngine.isInCollision(entity1, entity2));
        physicsEngine.move(entity2, 5,0);
        Assert.assertTrue(physicsEngine.isInCollision(entity1));
        Assert.assertTrue(physicsEngine.isInCollision(entity1, entity2));
    }

    /**
     * Test pour vérifier si une entité est à l'intérieur d'une autre entité
     */
    @Test
    public void Test07IsInside() {
        physicsEngine.addCollisions(entity1, entity2);
        physicsEngine.resize(entity1, 5, 5);
        physicsEngine.move(entity1, 2,2);
        physicsEngine.move(entity2, 0,0);
        Assert.assertTrue(physicsEngine.isInside(entity1, entity2));
        physicsEngine.move(entity1, 20,2);
        Assert.assertFalse(physicsEngine.isInside(entity1, entity2));
    }

    /**
     * Test pour vérifier si une entité est dans ses limites de déplacement
     */
    @Test
    public void Test08IsInBounds() {
        physicsEngine.addBoundLimits(entity1, 0,0,10,10);
        Assert.assertFalse(physicsEngine.isInBounds(entity1));
        physicsEngine.move(entity1, 2,2);
        Assert.assertTrue(physicsEngine.isInBounds(entity1));
    }

    /**
     * Test pour vérifier si deux entités sont centrées
     */
    @Test
    public void Test09IsCentered() {
        physicsEngine.resize(entity1, 10, 10);
        physicsEngine.resize(entity2, 10, 10);
        physicsEngine.move(entity1, 0,0);
        physicsEngine.move(entity2, 0,0);
        Assert.assertTrue(physicsEngine.isCentered(entity1, entity2));
        physicsEngine.move(entity2, 5,0);
        Assert.assertFalse(physicsEngine.isCentered(entity1, entity2));
    }

    /**
     * Test pour diriger l'entité vers le haut
     */
    @Test
    public void Test10GoUp() {
        physicsEngine.removeCollisions(entity1, entity2);
        physicsEngine.addBoundLimits(entity1, 0,0,10,10);
        physicsEngine.resize(entity1, 1, 1);
        physicsEngine.move(entity1, 5, 5);
        physicsEngine.goUp(entity1);
        Assert.assertEquals(entity1.getPhysicEntity().getX(), entity1.getPhysicEntity().getX());
        Assert.assertEquals(entity1.getPhysicEntity().getLastY() - 1, entity1.getPhysicEntity().getY());
        physicsEngine.move(entity1, 20, 5);
        Assert.assertEquals(entity1.getPhysicEntity().getX(), entity1.getPhysicEntity().getX());
        Assert.assertEquals(entity1.getPhysicEntity().getY(), entity1.getPhysicEntity().getY());
    }

    /**
     * Test pour diriger l'entité vers la droite
     */
    @Test
    public void Test11GoRight() {
        physicsEngine.move(entity1, 5, 5);
        physicsEngine.goRight(entity1);
        Assert.assertEquals(entity1.getPhysicEntity().getLastX() + 1, entity1.getPhysicEntity().getX());
        Assert.assertEquals(entity1.getPhysicEntity().getY(), entity1.getPhysicEntity().getY());
        physicsEngine.move(entity1, 20, 5);
        Assert.assertEquals(entity1.getPhysicEntity().getX(), entity1.getPhysicEntity().getX());
        Assert.assertEquals(entity1.getPhysicEntity().getY(), entity1.getPhysicEntity().getY());
    }

    /**
     * Test pour diriger l'entité vers le bas
     */
    @Test
    public void Test12GoDown() {
        physicsEngine.move(entity1, 5, 5);
        physicsEngine.goDown(entity1);
        Assert.assertEquals(entity1.getPhysicEntity().getX(), entity1.getPhysicEntity().getX());
        Assert.assertEquals(entity1.getPhysicEntity().getLastY() + 1, entity1.getPhysicEntity().getY());
        physicsEngine.move(entity1, 20, 5);
        Assert.assertEquals(entity1.getPhysicEntity().getX(), entity1.getPhysicEntity().getX());
        Assert.assertEquals(entity1.getPhysicEntity().getY(), entity1.getPhysicEntity().getY());
    }

    /**
     * Test pour diriger l'entité vers la gauche
     */
    @Test
    public void Test12GoLeft() {
        physicsEngine.move(entity1, 5, 5);
        physicsEngine.goLeft(entity1);
        Assert.assertEquals(entity1.getPhysicEntity().getLastX() - 1, entity1.getPhysicEntity().getX());
        Assert.assertEquals(entity1.getPhysicEntity().getY(), entity1.getPhysicEntity().getY());
        physicsEngine.move(entity1, 20, 5);
        Assert.assertEquals(entity1.getPhysicEntity().getX(), entity1.getPhysicEntity().getX());
        Assert.assertEquals(entity1.getPhysicEntity().getY(), entity1.getPhysicEntity().getY());
    }

    /**
     * Test pour bouger une entité
     */
    @Test
    public void Test13Move() {
        int oldX = entity1.getPhysicEntity().getX();
        int oldY = entity1.getPhysicEntity().getY();
        physicsEngine.move(entity1, 5, 5);
        Assert.assertEquals(oldX, entity1.getPhysicEntity().getLastX());
        Assert.assertEquals(oldY, entity1.getPhysicEntity().getLastY());
        Assert.assertEquals(5, entity1.getPhysicEntity().getX());
        Assert.assertEquals(5, entity1.getPhysicEntity().getY());
    }

    /**
     * Test pour translater une entité
     */
    @Test
    public void Test14Translate() {
        int oldX = entity1.getPhysicEntity().getX();
        int oldY = entity1.getPhysicEntity().getY();
        physicsEngine.translate(entity1, -1, 1);
        Assert.assertEquals(oldX, entity1.getPhysicEntity().getLastX());
        Assert.assertEquals(oldY, entity1.getPhysicEntity().getLastY());
        Assert.assertEquals(4, entity1.getPhysicEntity().getX());
        Assert.assertEquals(6, entity1.getPhysicEntity().getY());
    }

    /**
     * Test pour redimensionner une entité
     */
    @Test
    public void Test15Resize() {
        physicsEngine.resize(entity1, 3, 3);
        Assert.assertEquals(3, entity1.getPhysicEntity().getHeight());
        Assert.assertEquals(3, entity1.getPhysicEntity().getWidth());
    }

    /**
     * Test pour redimensionner la hauteur d'une entité
     */
    @Test
    public void Test16ResizeHeight() {
        physicsEngine.resizeHeight(entity1, 4);
        Assert.assertEquals(4, entity1.getPhysicEntity().getHeight());
    }

    /**
     * Test pour redimensionner la largeur d'une entité
     */
    @Test
    public void Test17ResizeWidth() {
        physicsEngine.resizeWidth(entity1, 4);
        Assert.assertEquals(4, entity1.getPhysicEntity().getWidth());
    }

    /**
     * Test pour changer la vitesse d'une entité
     */
    @Test
    public void Test18SetSpeed() {
        physicsEngine.setSpeed(entity1, 2);
        Assert.assertEquals(2, entity1.getPhysicEntity().getSpeed());
    }

    /**
     * Test de la création d'une entité physique
     */
    @Test
    public void Test19CreateEntity() {
        Entity entity = kernelEngine.generateEntity();
        Assert.assertEquals(3, physicsEngine.getEntities().size());
        Assert.assertTrue(physicsEngine.getEntities().containsKey(entity.getId()));
        Assert.assertEquals(entity.getPhysicEntity(), physicsEngine.getEntities().get(entity.getId()));
    }
}
