package engines.UI;

import engines.kernel.KernelEngine;

/**
 * Moteur interface utilisateur
 */
public class UIEngine {
    /**
     * Moteur noyau
     */
    private KernelEngine kernelEngine;

    /**
     * Constructeur
     * @param kernelEngine moteur noyau
     */
    public UIEngine(KernelEngine kernelEngine) {
        this.kernelEngine = kernelEngine;
    }

    /**
     * Créer un bouton
     * @param content contenu
     * @param event évènement
     * @return nouveau bouton
     */
    public Button createButton(String content, Runnable event) {
        return new Button(content, event);
    }

    /**
     * Redimensionner un bouton
     * @param button bouton
     * @param height hauteur
     * @param width largeur
     */
    public void resizeButton(Button button, int height, int width) {
        button.setHeight(height);
        button.setWidth(width);
    }



    // GETTERS //

    public KernelEngine getKernelEngine() {
        return kernelEngine;
    }
}
