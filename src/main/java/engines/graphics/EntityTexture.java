package engines.graphics;

import api.SwingRenderer;

/**
 * Texture d'entité
 */
public abstract class EntityTexture extends SwingRenderer {
    /**
     * Recouvrir un carreau
     * @param tile carreau
     */
    protected abstract void cover(Entity tile);

    /**
     * Mettre à jour la texture
     */
    protected abstract void update();

    /**
     * Cloner une texture
     */
    public abstract EntityTexture clone();
}
