package engines.input_output;

/**
 * Entité entrées/sorties
 */
public interface InputOutputEntity {
    /**
     * Générer une nouvelle entité
     * @return entité entrées/sorties
     */
    static InputOutputObject generateEntity() {
        return new InputOutputObject();
    }

    /**
     * Activer / désactiver les entrées/sorties clavier
     * @param value valeur
     */
    void enableKeyboardInputOutput(boolean value);

    /**
     * Activer / désactiver les entrées/sorties souris
     * @param value valeur
     */
    void enableMouseInputOutput(boolean value);

    /**
     * Obtenir le clavier
     * @return clavier
     */
    KeyboardInputOutput getKeyboard();

    /**
     * Obtenir la souris
     * @return souris
     */
    MouseInputOutput getMouse();
}
