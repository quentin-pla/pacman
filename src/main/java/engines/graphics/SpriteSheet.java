package engines.graphics;

import java.util.HashMap;
import java.util.Map;

/**
 * Fichier de textures
 */
public class SpriteSheet {
    /**
     * Fichiers de textures transférés
     */
    private static Map<String,SpriteSheet> loaded = new HashMap<>();

    /**
     * Textture
     */
    private Texture texture;

    /**
     * Nombre de textures horizontales / verticales
     */
    private int[] size;

    /**
     * Constructeur
     * @param height nombre de textures horizontales
     * @param width nombre de textures verticales
     */
    private SpriteSheet(Texture texture, int height, int width) {
        this.texture = texture;
        this.size = new int[]{height,width};
    }

    /**
     * Transférer une texture
     * @param link lien du fichier
     */
    protected static SpriteSheet load(String link, int height, int width) {
        if (!loaded.containsKey(link)) {
            loaded.put(link, new SpriteSheet(Texture.load(link), height, width));
        } else {
            try {
                throw new Exception("Fichier de texture déjà transféré");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return loaded.get(link);
    }

    /**
     * Obtenir un fichier de texture
     * @param link lien du fichier
     * @return texture
     */
    protected static SpriteSheet get(String link) {
        return loaded.get(link);
    }

    // GETTERS //

    public Texture getTexture() { return texture; }

    public int[] getSize() { return size; }
}
