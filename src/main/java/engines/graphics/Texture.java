package engines.graphics;

import static engines.graphics.GraphicsEngine.getTexture;
import static engines.graphics.GraphicsEngine.uploadTexture;

/**
 * Texture
 */
public class Texture {
    /**
     * ID de la texture
     */
    private Integer id = null;

    /**
     * Lien vers le fichier
     */
    private String link;

    /**
     * Constructeur
     * @param link lien vers le fichier
     */
    protected Texture(String link) {
        this.link = link;
    }

    /**
     * Transférer une texture
     * @param link lien du fichier
     * @param name nom de la texture
     */
    public static void upload(String link, String name) {
        uploadTexture(link, name);
    }

    /**
     * Obtenir une texture
     * @param name nom
     * @return texture
     */
    public static Texture get(String name) {
        return getTexture(name);
    }

    // GETTERS & SETTERS //

    protected Integer getId() { return id; }

    protected void setId(Integer id) { this.id = id; }

    protected String getLink() { return link; }
}
