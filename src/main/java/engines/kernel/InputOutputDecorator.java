package engines.kernel;

import engines.input_output.InputOutputEntity;
import engines.input_output.InputOutputObject;
import engines.input_output.KeyboardInputOutput;
import engines.input_output.MouseInputOutput;

/**
 * Décorateur entité entrées/sorties
 */
public interface InputOutputDecorator extends InputOutputEntity {
    /**
     * Obtenir l'entité entrées/sorties
     * @return instance
     */
    InputOutputObject getInputOutput();

    @Override
    default void enableKeyboardInputOutput(boolean value) {
        getInputOutput().enableKeyboardInputOutput(value);
    }

    @Override
    default void enableMouseInputOutput(boolean value) {
        getInputOutput().enableMouseInputOutput(value);
    }

    @Override
    default KeyboardInputOutput getKeyboard() {
        return getInputOutput().getKeyboard();
    }

    @Override
    default MouseInputOutput getMouse() {
        return getInputOutput().getMouse();
    }
}
