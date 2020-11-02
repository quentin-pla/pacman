package engines.graphics;

import api.Renderer;

/**
 * Texture d'entité
 */
public abstract class EntityTexture extends Renderer {
    /**
     * Recouvrir un carreau
     * @param tile carreau
     */
    public abstract void cover(Entity tile);

    /**
     * Mettre à jour la texture
     */
    public abstract void update();

    /**
     * Cloner une texture
     */
    public abstract EntityTexture clone();
}
