package engines.graphics;

import api.SwingRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Texture
 */
public class Texture extends Cover {
    /**
     * Textures transférées
     */
    private static Map<String,Texture> loaded = new HashMap<>();

    /**
     * Lien vers le fichier
     */
    private String link;

    /**
     * Constructeur
     * @param link lien vers le fichier
     */
    private Texture(String link) {
        this.link = link;
    }

    /**
     * Transférer une texture
     * @param link lien du fichier
     * @return texture
     */
    public static Texture load(String link) {
        if (!SwingRenderer.isTextureLoaded(link)) {
            SwingRenderer.loadTexture(link);
            loaded.put(link, new Texture(link));
        } else {
            try {
                throw new Exception("Texture déjà chargée");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return loaded.get(link);
    }

    /**
     * Obtenir une texture
     * @param link lien du fichier
     * @return texture
     */
    public static Texture get(String link) {
        return loaded.get(link);
    }

    @Override
    protected void cover(GraphicsObject graphicsObject) {
        SwingRenderer.renderTexturedRect(graphicsObject.height, graphicsObject.width,
                graphicsObject.x, graphicsObject.y, link);
    }

    @Override
    protected void update() {}

    @Override
    public Cover clone() {
        return this;
    }

    // GETTERS & SETTERS //

    public String getLink() { return link; }
}
