package engines.input_output;

import api.SwingAPI;

/**
 * Entité entrées / sorties
 */
public class Entity extends SwingAPI implements InputOutputEntity {
    /**
     * Écouteur actions utilisateur clavier
     */
    private KeyboardInputOutput keyboardInputOutput = new KeyboardInputOutput();

    /**
     * Écouteur actions utilisateur souris
     */
    private MouseInputOutput mouseInputOutput = new MouseInputOutput();



    /**
     * Activer / désactiver les entrées/sorties clavier
     * @param value valeur
     */
    public void enableKeyboardInputOutput(boolean value) {
        if (value) SwingAPI.getListenerMethods().addKeyListener(keyboardInputOutput);
        else SwingAPI.getListenerMethods().removeKeyListener(keyboardInputOutput);
    }

    /**
     * Activer / désactiver les entrées/sorties souris
     * @param value valeur
     */
    public void enableMouseInputOutput(boolean value) {
        if (value) SwingAPI.getListenerMethods().addMouseListener(mouseInputOutput);
        else SwingAPI.getListenerMethods().removeMouseListener(mouseInputOutput);
    }

    /**
     * Obtenir le clavier
     * @return clavier
     */
    public KeyboardInputOutput getKeyboard() {
        return keyboardInputOutput;
    }

    /**
     * Obtenir la souris
     * @return souris
     */
    public MouseInputOutput getMouse() {
        return mouseInputOutput;
    }
}
