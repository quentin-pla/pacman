package api;

import javax.swing.*;

/**
 * Fenêtre JFrame (bibliothèque Swing)
 */
public class SwingWindow {
    /**
     * Instance unique
     */
    private static SwingWindow instance;

    /**
     * Constructeur privé
     */
    private SwingWindow() {}

    /**
     * Récupérer l'instance
     * @return instance
     */
    protected static SwingWindow getInstance() {
        if (instance == null) instance = new SwingWindow();
        return instance;
    }

    /**
     * Fenêtre
     */
    private static final JFrame window = new JFrame();

    /**
     * Initialiser la fenêtre
     * @param title titre
     */
    public static void initWindow(String title) {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle(title);
        window.setVisible(true);
    }

    /**
     * Savoir si la fenêtre est en cours d'exécution
     * @return booléen
     */
    public static boolean isWindowOpen() {
        return window != null;
    }

    /**
     * Terminer l'exécution de la fenêtre
     */
    public static void stopWindow() {
        window.dispose();
    }

    /**
     * Rafraichir la fenêtre
     */
    public static void refreshWindow() {
        getCurrentScene().revalidate();
        getCurrentScene().repaint();
    }

    /**
     * Afficher une scène
     * @param scene scène
     */
    public static void showScene(SwingScene scene) {
        //Supprimer la scène courante
        if (window.getContentPane().getComponents().length > 0)
            window.getContentPane().remove(0);
        //Ajout de la nouvelle scène
        window.getContentPane().add(scene);
        //Dimensionnement de la fenêtre à la scène
        window.pack();
    }

    /**
     * Obtenir la scène courante
     * @return scène actuelle
     */
    public static SwingScene getCurrentScene() {
        return (SwingScene) window.getContentPane().getComponent(0);
    }

    /**
     * Obtenir la fenêtre courante
     * @return fenêtre courante
     */
    public static JFrame getWindow() { return window; }
}
