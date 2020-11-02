package engines.graphics;

import engines.graphics.api.Renderer;

/**
 * Texture d'entité
 */
public abstract class EntityTexture extends Renderer {
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
    protected abstract EntityTexture clone();
}
