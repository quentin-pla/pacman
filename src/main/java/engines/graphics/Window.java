package engines.graphics;

import engines.graphics.api.GLFW;

import java.util.HashMap;
import java.util.Map;

import static engines.graphics.GraphicsEngine.generateTextures;

/**
 * Fenêtre
 */
public class Window extends GLFW {
    /**
     * Hauteur
     */
    private static int height;

    /**
     * Largeur
     */
    private static int width;

    /**
     * Échelle
     */
    private static int scale = 1;

    /**
     * Titre de la fenêtre
     */
    private static String title = "";

    /**
     * Scènes liées à la fenêtre
     */
    private static Map<String, Scene> scenes = new HashMap<>();

    /**
     * Scène affichée
     */
    private static Scene actual_scene;

    /**
     * Afficher la fenêtre
     */
    public static void show() {
        try {
            if (scenes.isEmpty())
                throw new Exception("Aucune scène spécifiée");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        init();
        update();
        stop();
    }

    /**
     * Initialiser la fenêtre
     */
    private static void init() {
        initGLFWWindow(width * scale, height * scale, title);
        generateTextures();
    }

    /**
     * Mettre à jour la fenêtre
     */
    private static void update() {
        while (isGLFWWindowRunning()) {
            clearGLFWWindow();
            actual_scene.render();
            swapBuffers();
        }
    }

    /**
     * Terminer l'exécution de la fenêtre
     */
    private static void stop() {
        stopGLFWWindow();
    }

    /**
     * Ajouter une nouvelle scène
     * @param scene scène
     */
    public static void addScene(Scene scene, String name) {
        scenes.put(name, scene);
        if (actual_scene == null) bindScene(name);
    }

    /**
     * Attacher une scène
     * @param name nom de la scène
     */
    public static void bindScene(String name) {
        if (!scenes.containsKey(name)) {
            try {
                throw new Exception("Scène introuvable");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        Scene scene = scenes.get(name);
        Window.actual_scene = scene;
        Window.height = scene.getHeight();
        Window.width = scene.getWidth();
    }

    // GETTERS & SETTERS //

    public static int getScale() {
        return scale;
    }

    public static void setScale(int scale) {
        Window.scale = scale;
    }

    public static String getTitle() {
        return title;
    }

    public static void setTitle(String title) {
        Window.title = title;
    }
}
