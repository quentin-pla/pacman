package engines.kernel;

import engines.graphics.*;

/**
 * Décorateur entité graphique
 */
public interface GraphicsDecorator extends GraphicsEngine {
    /**
     * Obtenir l'entité graphique
     * @return instance
     */
    GraphicsEntity getGraphics();

    @Override
    default void draw() { getGraphics().draw(); }

    @Override
    default void update() { getGraphics().update(); }

    @Override
    default void erase() { getGraphics().erase(); }

    @Override
    default void translate(int x, int y) { getGraphics().translate(x, y); }

    @Override
    default void move(int x, int y) { getGraphics().move(x, y); }

    @Override
    default void resize(int height, int width) { getGraphics().resize(height, width); }

    @Override
    default void bindColor(int red, int green, int blue) {
        getGraphics().bindColor(red, green, blue);
    }

    @Override
    default void unbindColor() { getGraphics().unbindColor(); }

    @Override
    default void bindTexture(Cover texture) { getGraphics().bindTexture(texture); }

    @Override
    default void unbindTexture() { getGraphics().unbindTexture(); }

    @Override
    default Scene getScene() { return getGraphics().getScene(); }

    @Override
    default void setScene(Scene scene) { getGraphics().setScene(scene); }

    @Override
    default int getHeight() { return getGraphics().getHeight(); }

    @Override
    default int getWidth() { return getGraphics().getWidth(); }

    @Override
    default int getX() { return getGraphics().getX(); }

    @Override
    default int getY() { return getGraphics().getY(); }

    @Override
    default Color getColor() { return getGraphics().getColor(); }

    @Override
    default Cover getTexture() { return getGraphics().getTexture(); }
}
