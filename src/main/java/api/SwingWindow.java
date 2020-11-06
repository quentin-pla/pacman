package api;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
    private static JFrame window = new JFrame();

    /**
     * Délai de rafraichissement
     */
    private static int delay = 1000/60;

    /**
     * Évènement pour repeindre la scène courante
     */
    private static ActionListener taskPerformer = evt -> {
        getCurrentScene().revalidate();
        getCurrentScene().repaint();
    };

    /**
     * Temps de rafraichissement 60 images par seconde
     */
    private static Timer timer = new Timer(delay, taskPerformer);

    /**
     * Initialiser la fenêtre
     */
    public static void initWindow(SwingScene scene, String title) {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(new Dimension(scene.width, scene.height));
        window.setResizable(false);
        window.setTitle(title);
        window.setVisible(true);
        timer.start();
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
     * Afficher une scène
     * @param scene scène
     */
    public static void showScene(SwingScene scene) {
        //Supprimer la scène courante
        if (window.getContentPane().getComponents().length > 0)
            window.getContentPane().remove(0);
        //Ajout de la nouvelle scène
        window.getContentPane().add(scene);
        //Définition des dimensions
        window.setSize(new Dimension(scene.width, scene.height));
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
