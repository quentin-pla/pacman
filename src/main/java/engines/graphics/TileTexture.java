package engines.graphics;

import engines.graphics.api.Renderer;

public abstract class TileTexture extends Renderer {
    /**
     * Recouvrir un carreau
     * @param tile carreau
     */
    public abstract void cover(Tile tile);

    public abstract void update();
}
