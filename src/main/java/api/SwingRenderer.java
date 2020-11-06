package api;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Générateur de graphismes à partir de Swing
 */
public class SwingRenderer {
    /**
     * Instance unique
     */
    private static SwingRenderer instance;

    /**
     * Constructeur privé
     */
    private SwingRenderer() {}

    /**
     * Récupérer l'instance
     * @return instance
     */
    protected static SwingRenderer getInstance() {
        if (instance == null) instance = new SwingRenderer();
        return instance;
    }

    /**
     * Environnement graphique
     */
    private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

    /**
     * Configuration graphique
     */
    private static GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

    /**
     * Textures chargées
     */
    private static Map<String,VolatileImage> loaded_textures = new HashMap<>();

    /**
     * Générer un rectangle
     * @param height hauteur en pixels
     * @param width largeur en pixels
     * @param x position horizontale
     * @param y position verticale
     * @param color couleur
     */
    public static void renderRect(int height, int width, int x, int y, Color color) {
        Graphics2D graphics2D = getCurrentGraphics();
        graphics2D.setColor(color);
        graphics2D.fillRect(x,y,width,height);
    }

    /**
     * Générer un carré texturisé
     * @param height hauteur en pixels
     * @param width largeur en pixels
     * @param x position horizontale
     * @param y position verticale
     * @param link lien vers la texture
     */
    public static void renderTexturedRect(int height, int width, int x, int y, String link) {
        getCurrentGraphics().drawImage(loaded_textures.get(link), x, y, width, height, null);
    }

    /**
     * Charger une texture
     * @param link lien du fichier
     */
    public static void loadTexture(String link) {
        BufferedImage texture = getBufferedImage(link);
        VolatileImage volatile_texture = generateVolatileImage(texture);
        loaded_textures.put(link, volatile_texture);
    }

    /**
     * Charger un fichier de textures
     * @param link lien du fichier
     */
    public static void loadSpriteSheet(String link, int sheet_height, int sheet_width) {
        BufferedImage texture = getBufferedImage(link);
        VolatileImage volatile_texture = generateVolatileImage(texture);
        loaded_textures.put(link, volatile_texture);
        int part_width = texture.getWidth() / sheet_width;
        int part_height = texture.getHeight() / sheet_height;
        for (int row = 0; row < sheet_height; row++) {
            for (int col = 0; col < sheet_width; col++) {
                int crop_x = col * part_width;
                int crop_y = row * part_height;
                VolatileImage volatile_part =
                        generateVolatileImage(texture.getSubimage(crop_x, crop_y, part_width, part_height));
                loaded_textures.put(link + row + col, volatile_part);
            }
        }
        texture.flush();
    }

    /**
     * Récupérer une image dans un tampon
     * @param link lien vers l'image
     * @return tampon
     */
    private static BufferedImage getBufferedImage(String link) {
        BufferedImage texture = null;
        try {
            texture = ImageIO.read(new File("target/classes/assets/" + link));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return texture;
    }

    /**
     * Générer une image volatile
     * @param image tampon de l'image
     * @return image volatile
     */
    private static VolatileImage generateVolatileImage(BufferedImage image) {
        VolatileImage volatile_image =
                gc.createCompatibleVolatileImage(image.getWidth(), image.getHeight(), Transparency.BITMASK);
        volatile_image.validate(gc);
        Graphics2D graphics2D = volatile_image.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        graphics2D.setColor(Color.BLACK);
        graphics2D.clearRect(0,0,volatile_image.getWidth(), volatile_image.getHeight());
        graphics2D.drawImage(image,null,0,0);
        graphics2D.dispose();
        return volatile_image;
    }

    /**
     * Vérifier si une texture a été chargée
     * @param link lien de la texture
     */
    public static boolean isTextureLoaded(String link) {
        return loaded_textures.containsKey(link);
    }

    /**
     * Obtenir une couleur Swing
     * @param red rouge
     * @param green vert
     * @param blue bleu
     * @return couleur swing
     */
    public static Color getSwingColor(int red, int green, int blue) {
        return new Color(red, green, blue);
    }

    /**
     * Obtenir l'objet graphique courant
     * @return objet graphique
     */
    private static Graphics2D getCurrentGraphics() {
        return SwingWindow.getCurrentScene().get2DGraphics();
    }
}
