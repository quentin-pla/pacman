package api;

import engines.input_output.KeyboardInputOutput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Fenêtre JFrame (bibliothèque Swing)
 */
public class SwingWindow {
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
    protected static void initWindow(SwingScene scene, String title) {
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(new Dimension(scene.width, scene.height));
        window.setResizable(false);
        window.setTitle(title);
        window.addKeyListener(KeyboardInputOutput.getInstance());
        window.setVisible(true);
        timer.start();
    }

    /**
     * Savoir si la fenêtre est en cours d'exécution
     * @return booléen
     */
    protected static boolean isWindowOpen() {
        return window != null;
    }

    /**
     * Terminer l'exécution de la fenêtre
     */
    protected static void stopWindow() {
        window.dispose();
    }

    /**
     * Afficher une scène
     * @param scene scène
     */
    protected static void showScene(SwingScene scene) {
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
    protected static SwingScene getCurrentScene() {
        return (SwingScene) window.getContentPane().getComponent(0);
    }
}
