package engines.graphics;

import api.SwingWindow;
import engines.kernel.Entity;
import engines.kernel.KernelEngine;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Tests moteur graphique
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GraphicsEngineTest {
    /**
     * Moteur noyau
     */
    private static KernelEngine kernelEngine;

    /**
     * Moteur IA
     */
    private static GraphicsEngine graphicsEngine;

    /**
     * Scène
     */
    private static Scene scene;

    /**
     * Entité
     */
    private static Entity entity;

    /**
     * Identifiant texture
     */
    private static int textureID;

    /**
     * Identifiant fichier de textures
     */
    private static int spriteSheetID;

    /**
     * Identifiant de l'animation
     */
    private static int animationID;

    /**
     * Initialisation
     */
    @BeforeClass
    public static void init() {
        kernelEngine = new KernelEngine();
        graphicsEngine = kernelEngine.getGraphicsEngine();
        scene = graphicsEngine.generateScene(100,100);
        graphicsEngine.bindScene(scene);
    }

    /**
     * Test pour vérifier les attributs à la création
     */
    @Test
    public void Test00CheckAttributes() {
        Assert.assertTrue(graphicsEngine.getEntities().isEmpty());
        Assert.assertTrue(graphicsEngine.getTextures().isEmpty());
        Assert.assertTrue(graphicsEngine.getAnimations().isEmpty());
        Assert.assertTrue(graphicsEngine.getSpriteSheets().isEmpty());
        Assert.assertEquals(1, graphicsEngine.getEventsListeners().size());
        Assert.assertEquals(kernelEngine, graphicsEngine.getEventsListeners().get(0));
    }

    /**
     * Test pour créer une entité graphique
     */
    @Test
    public void Test01CreateEntity() {
        Assert.assertTrue(graphicsEngine.getEntities().isEmpty());
        entity = kernelEngine.generateEntity();
        Assert.assertEquals(1, graphicsEngine.getEntities().size());
        Assert.assertTrue(graphicsEngine.getEntities().containsKey(entity.getId()));
        Assert.assertEquals(entity.getGraphicEntity(), graphicsEngine.getEntities().get(entity.getId()));
    }

    /**
     * Test pour dessiner une entité graphique
     */
    @Test
    public void Test02DrawEntity() {
        //Vérification que la scène est vide
        Assert.assertTrue(checkPaintedPixelsArea(getSceneRender(),0,0,scene.getHeight(),
                scene.getWidth(),scene.getBackground()));

        //Ajout de l'entité graphique dans la scène
        graphicsEngine.addToScene(scene,entity);
        GraphicEntity graphicEntity = entity.getGraphicEntity();

        //Modification de l'entité pour qu'elle représente un carré rouge
        graphicEntity.setX(10);
        graphicEntity.setY(10);
        graphicEntity.setHeight(5);
        graphicEntity.setWidth(5);
        graphicsEngine.bindColor(entity, new engines.graphics.Color(255,0,0));
        Assert.assertTrue(checkPaintedEntityPixels(getSceneRender(), graphicEntity));

        //Modification des valeurs et de la couleur du carré
        graphicEntity.setX(30);
        graphicEntity.setY(20);
        graphicEntity.setHeight(10);
        graphicEntity.setWidth(10);
        graphicsEngine.bindColor(entity, new engines.graphics.Color(0,255,0));
        Assert.assertTrue(checkPaintedEntityPixels(getSceneRender(), graphicEntity));
    }

    /**
     * Test pour effacer une entité peinte sur la scène
     */
    @Test
    public void Test03EraseEntity() {
        graphicsEngine.erase(entity);
        Assert.assertTrue(checkPaintedPixelsArea(getSceneRender(),0,0,scene.getHeight(),
                scene.getWidth(),scene.getBackground()));
    }

    /**
     * Test pour associer une couleur à une entité graphique
     */
    @Test
    public void Test04BindColor() {
        graphicsEngine.bindColor(entity, 100, 50, 10);
        Color color = entity.getGraphicEntity().getColor();
        Assert.assertEquals(100, color.getRed());
        Assert.assertEquals(50, color.getGreen());
        Assert.assertEquals(10, color.getBlue());
        graphicsEngine.bindColor(entity, new Color(200,80,60));
        color = entity.getGraphicEntity().getColor();
        Assert.assertEquals(200, color.getRed());
        Assert.assertEquals(80, color.getGreen());
        Assert.assertEquals(60, color.getBlue());
    }

    /**
     * Test pour dissocier la couleur d'une entité graphique
     */
    @Test
    public void Test05UnbindColor() {
        graphicsEngine.unbindColor(entity);
        Assert.assertNull(entity.getGraphicEntity().getColor());
    }

    /**
     * Test pour associer un texte à une entité graphique
     */
    @Test
    public void Test06BindText() {
        graphicsEngine.bindText(entity, "test", new Color(0,0,255), 20, false);
        Text text = new Text("test", new Color(0,0,255).getSwingColor(), 20, false);
        Text textEntity = (Text) entity.getGraphicEntity().getText();
        Assert.assertEquals(text.getContent(), textEntity.getContent());
        Assert.assertEquals(text.getFontSize(), textEntity.getFontSize());
        Assert.assertEquals(text.getColor(), textEntity.getColor());
    }

    /**
     * Test pour dissocier le texte d'une entité graphique
     */
    @Test
    public void Test07UnbindText() {
        graphicsEngine.unbindText(entity);
        Assert.assertNull(entity.getGraphicEntity().getText());
    }

    /**
     * Test pour charger une texture
     */
    @Test
    public void Test08LoadTexture() {
        Assert.assertTrue(graphicsEngine.getTextures().isEmpty());
        textureID = graphicsEngine.loadTexture("assets/menu_logo.png");
        Assert.assertEquals(1, graphicsEngine.getTextures().size());
        Assert.assertTrue(graphicsEngine.getTextures().containsKey(textureID));
    }

    /**
     * Test pour associer une texture à une entité
     */
    @Test
    public void Test09BindTexture() {
        graphicsEngine.bindTexture(entity, textureID);
        Texture texture = graphicsEngine.getTexture(textureID);
        Assert.assertEquals(texture, entity.getGraphicEntity().getTexture());
    }

    /**
     * Test pour dissocier une texture d'une entité
     */
    @Test
    public void Test10UnbindTexture() {
        Assert.assertNotNull(entity.getGraphicEntity().getTexture());
        graphicsEngine.unbindTexture(entity);
        Assert.assertNull(entity.getGraphicEntity().getTexture());
    }

    /**
     * Test pour charger un fichier de textures
     */
    @Test
    public void Test11LoadSpriteSheet() {
        Assert.assertTrue(graphicsEngine.getSpriteSheets().isEmpty());
        spriteSheetID = graphicsEngine.loadSpriteSheet("assets/sprite_sheet.png", 12, 11);
        Assert.assertEquals(1, graphicsEngine.getSpriteSheets().size());
        Assert.assertTrue(graphicsEngine.getSpriteSheets().containsKey(spriteSheetID));
    }

    /**
     * Test pour associer une texture provenant d'un fichier de textures
     */
    @Test
    public void Test12BindSprite() {
        Assert.assertNull(entity.getGraphicEntity().getTexture());
        graphicsEngine.bindTexture(entity, textureID, 1, 1);
        Sprite texture = graphicsEngine.getSpriteSheets().get(spriteSheetID).getSprite(1,1);
        Assert.assertEquals(texture, entity.getGraphicEntity().getTexture());
    }

    /**
     * Test pour générer une animation
     */
    @Test
    public void Test13GenerateAnimation() {
        Assert.assertTrue(graphicsEngine.getAnimations().isEmpty());
        animationID = graphicsEngine.generateAnimation(spriteSheetID, 5, true);
        Assert.assertEquals(1, graphicsEngine.getAnimations().size());
        Assert.assertTrue(graphicsEngine.getAnimations().containsKey(animationID));
    }

    /**
     * Test pour associer une animation à une entité
     */
    @Test
    public void Test14BindAnimation() {
        graphicsEngine.bindAnimation(entity, animationID);
        SpriteAnimation animation = graphicsEngine.getAnimation(animationID);
        Assert.assertEquals(animation, entity.getGraphicEntity().getTexture());
    }

    /**
     * Test pour ajouter une entité à une scène spécifique
     */
    @Test
    public void Test15AddToScene() {
        graphicsEngine.addToScene(scene, entity);
        Assert.assertEquals(scene, entity.getGraphicEntity().getScene());
        Assert.assertTrue(scene.getEntities().contains(entity.getGraphicEntity()));
        scene.getEntities().remove(entity.getGraphicEntity());
    }

    /**
     * Test pour ajouter une entité à la scène courante de la fenêtre
     */
    @Test
    public void Test16AddToCurrentScene() {
        graphicsEngine.addToCurrentScene(entity);
        Assert.assertEquals(scene, entity.getGraphicEntity().getScene());
        Assert.assertTrue(scene.getEntities().contains(entity.getGraphicEntity()));
        scene.getEntities().remove(entity.getGraphicEntity());
    }

    /**
     * Test pour bouger une entité
     */
    @Test
    public void Test17MoveEntity() {
        graphicsEngine.move(entity, 10, 5);
        Assert.assertEquals(10, entity.getGraphicEntity().getX());
        Assert.assertEquals(5, entity.getGraphicEntity().getY());
    }

    /**
     * Test pour translater une entité
     */
    @Test
    public void Test18TranslateEntity() {
        int oldX = entity.getGraphicEntity().getX();
        int oldY = entity.getGraphicEntity().getY();
        graphicsEngine.translate(entity, 5, 2);
        Assert.assertEquals(oldX + 5, entity.getGraphicEntity().getX());
        Assert.assertEquals(oldY + 2, entity.getGraphicEntity().getY());
    }

    /**
     * Test pour redimensionner une entité
     */
    @Test
    public void Test20ResizeEntity() {
        graphicsEngine.resize(entity, 20, 40);
        Assert.assertEquals(20, entity.getGraphicEntity().getWidth());
        Assert.assertEquals(40, entity.getGraphicEntity().getHeight());
    }

    /**
     * Test pour redimensionner la hauteur d'une entité
     */
    @Test
    public void Test21ResizeHeight() {
        graphicsEngine.resizeHeight(entity, 30);
        Assert.assertEquals(30, entity.getGraphicEntity().getHeight());
    }

    /**
     * Test pour redimensionner la largeur d'une entité
     */
    @Test
    public void Test22ResizeWidth() {
        graphicsEngine.resizeWidth(entity, 16);
        Assert.assertEquals(16, entity.getGraphicEntity().getWidth());
    }

    /**
     * Test pour ajouter une image à une animation
     */
    @Test
    public void Test23AddFrameToAnimation() {
        graphicsEngine.addFrameToAnimation(animationID, 2,3);
        Sprite image = graphicsEngine.getSpriteSheets().get(spriteSheetID).getSprite(2,3);
        Assert.assertEquals(1, graphicsEngine.getAnimations().get(animationID).getFrames().size());
        Assert.assertTrue(graphicsEngine.getAnimations().get(animationID).getFrames().contains(image));
    }

    /**
     * Test pour jouer / mettre en pause une animation
     */
    @Test
    public void Test24PlayPauseAnimation() {
        Assert.assertTrue(graphicsEngine.getAnimations().get(animationID).isPlaying());
        graphicsEngine.playPauseAnimation(animationID);
        Assert.assertFalse(graphicsEngine.getAnimations().get(animationID).isPlaying());
        graphicsEngine.playPauseAnimation(animationID);
        Assert.assertTrue(graphicsEngine.getAnimations().get(animationID).isPlaying());
    }

    /**
     * Test pour réinitialiser une animation
     */
    @Test
    public void Test25ResetAnimation() {
        graphicsEngine.resetAnimation(animationID);
        SpriteAnimation animation = graphicsEngine.getAnimation(animationID);
        Assert.assertTrue(animation.isPlaying());
        Assert.assertTrue(animation.isAddSubstract());
        Assert.assertEquals(0, animation.getTime());
        Assert.assertEquals(0, animation.getActualFrame());
    }

    /**
     * Test pour générer une scène
     */
    @Test
    public void Test26GenerateScene() {
        Scene scene = graphicsEngine.generateScene(340, 300);
        Assert.assertEquals(graphicsEngine, scene.getGraphicsEngine());
        Assert.assertEquals(340, scene.getHeight());
        Assert.assertEquals(300, scene.getWidth());
        Assert.assertEquals(0, scene.getEntities().size());
    }

    /**
     * Test pour modifier la couleur de fond de la scène
     */
    @Test
    public void Test27SetSceneBackgroundColor() {
        graphicsEngine.setSceneBackgroundColor(scene, 255, 0, 0);
        Assert.assertTrue(checkPaintedPixelsArea(getSceneRender(), 0, 0,
                scene.getHeight(), scene.getWidth(), new java.awt.Color(255,0,0)));
    }

    /**
     * Test pour changer la position de la scène
     */
    @Test
    public void Test28SetSceneLocation() {
        graphicsEngine.setSceneLocation(scene, 10, 20);
        Assert.assertEquals(10, scene.getXLocation());
        Assert.assertEquals(20, scene.getYLocation());
    }

    /**
     * Test pour changer la taille d'une scène
     */
    @Test
    public void Test29SetSceneSize() {
        graphicsEngine.setSceneSize(scene, 250, 350);
        Assert.assertEquals(250, scene.getHeight());
        Assert.assertEquals(350, scene.getWidth());
    }

    /**
     * Test pour attacher une scène à la fenêtre
     */
    @Test
    public void Test30BindScene() {
        graphicsEngine.bindScene(scene);
        Assert.assertEquals(scene, SwingWindow.getInstance().getCurrentScene());
    }

    /**
     * Test pour afficher la fenêtre
     */
    @Test
    public void Test31ShowWindow() {
        graphicsEngine.showWindow();
        Assert.assertTrue(SwingWindow.getInstance().getWindow().isVisible());
    }

    /**
     * Test pour fermer la fenêtre
     */
    @Test
    public void Test32() {
        graphicsEngine.stopWindow();
        Assert.assertFalse(SwingWindow.getInstance().getWindow().isVisible());
    }

    /**
     * Test pour supprimer une entité
     */
    @Test
    public void Test33() {
        graphicsEngine.removeEntity(entity);
        Assert.assertTrue(graphicsEngine.getEntities().isEmpty());
        Assert.assertTrue(scene.getEntities().isEmpty());
    }

    // UTILITAIRES //

    /**
     * Obtenir le rendu graphique de la scène
     * @return rendu graphique
     */
    private BufferedImage getSceneRender() {
        BufferedImage img = new BufferedImage(scene.getWidth(), scene.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        scene.paint(g);
        g.dispose();
        return img;
    }

    /**
     * Vérifier un pixel peint sur la scène
     * @param x position horizontale
     * @param y position verticale
     * @param color couleur
     * @return booléen
     */
    private boolean checkPaintedPixel(BufferedImage sceneRender, int x, int y, java.awt.Color color) {
        return new java.awt.Color(sceneRender.getRGB(x, y)).equals(color);
    }

    /**
     * Vérifier une zone de pixels peints sur la scène
     * @param sceneRender rendu de la scène
     * @param x position horizontale
     * @param y position verticale
     * @param height hauteur
     * @param width largeur
     * @param color couleur
     * @return booléen
     */
    private boolean checkPaintedPixelsArea(BufferedImage sceneRender, int x, int y, int height, int width, java.awt.Color color) {
        for (int i = x; i < x + width; i++)
            for (int j = y; j < y + height; j++)
                if (!checkPaintedPixel(sceneRender, i, j, color))
                    return false;
        return true;
    }

    /**
     * Vérifier qu'une entité graphique a bien été peinte sur la scène
     * en vérifiant pixel par pixel
     * @return booléen
     */
    private boolean checkPaintedEntityPixels(BufferedImage sceneRender, GraphicEntity graphicEntity) {
        return checkPaintedPixelsArea(sceneRender, graphicEntity.getX(), graphicEntity.getY(),
                graphicEntity.getHeight(), graphicEntity.getWidth(), graphicEntity.getColor().getSwingColor());
    }
}
