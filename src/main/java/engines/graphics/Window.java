package engines.graphics;

import engines.graphics.api.GLFW;

import java.util.HashMap;
import java.util.Map;

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
    protected static void show() {
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
        Texture.generateLoadedTextures();
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
    protected static void stop() {
        stopGLFWWindow();
    }

    /**
     * Ajouter une nouvelle scène
     * @param scene scène
     */
    protected static void addScene(Scene scene, String name) {
        scenes.put(name, scene);
        if (actual_scene == null) bindScene(name);
    }

    /**
     * Attacher une scène
     * @param name nom de la scène
     */
    protected static void bindScene(String name) {
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

    protected static int getScale() {
        return scale;
    }

    protected static void setScale(int scale) {
        Window.scale = scale;
    }

    protected static String getTitle() {
        return title;
    }

    protected static void setTitle(String title) {
        Window.title = title;
    }

    protected static int getHeight() { return height; }

    protected static int getWidth() { return width; }

    protected static Map<String, Scene> getScenes() { return scenes; }

    protected static Scene getActualScene() { return actual_scene; }
}
