package engines.graphics;

import api.SwingRenderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Texture
 */
public class Texture extends EntityTexture {
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
    protected void cover(Entity entity) {
        renderTexturedRect(entity.getHeight(), entity.getWidth(), entity.getX(), entity.getY(), link);
    }

    @Override
    protected void update() {}

    @Override
    public EntityTexture clone() {
        return this;
    }

    // GETTERS & SETTERS //

    public String getLink() { return link; }
}
