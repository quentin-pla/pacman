package engines.graphics;

import api.SwingAPI;
import api.SwingWindow;

/**
 * Fenêtre
 */
public class Window extends SwingAPI {
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
     * Scène affichée
     */
    private static Scene actual_scene;

    /**
     * Afficher la fenêtre
     */
    protected static void show() {
        try {
            if (actual_scene == null)
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
        SwingWindow.initWindow(actual_scene, title);
    }

    /**
     * Terminer l'exécution de la fenêtre
     */
    protected static void stop() {
        SwingWindow.stopWindow();
    }

    /**
     * Attacher une scène
     * @param scene scène
     */
    protected static void bindScene(Scene scene) {
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

    public static Scene getActualScene() { return actual_scene; }
}
