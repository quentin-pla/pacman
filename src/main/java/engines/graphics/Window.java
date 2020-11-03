package engines.graphics;

import api.SwingWindow;

import java.util.HashMap;
import java.util.Map;

/**
 * Fenêtre
 */
public class Window extends SwingWindow {
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
    }

    /**
     * Initialiser la fenêtre
     */
    private static void init() {
        initWindow(actual_scene, title);
    }

    /**
     * Terminer l'exécution de la fenêtre
     */
    public static void stop() {
        stopWindow();
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
        SwingWindow.showScene(actual_scene);
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

    public static int getHeight() { return height; }

    public static int getWidth() { return width; }

    public static Map<String, Scene> getScenes() { return scenes; }

    public static Scene getActualScene() { return actual_scene; }
}
