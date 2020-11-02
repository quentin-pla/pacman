package engines.graphics;

import java.util.HashMap;

/**
 * Texture
 */
public class Texture extends EntityTexture {
    /**
     * Textures transférées
     */
    private static HashMap<String,Texture> loaded = new HashMap<>();

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
    private Texture(String link) {
        this.link = link;
    }

    /**
     * Transférer une texture
     * @param link lien du fichier
     */
    public static Texture load(String link) {
        if (!loaded.containsKey(link)) {
            loaded.put(link, new Texture(link));
        } else {
            try {
                throw new Exception("Texture déjà transférée");
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

    /**
     * Générer les textures transférées
     */
    protected static void generateLoadedTextures() {
        for (Texture texture : loaded.values())
            texture.setId(generateTexture(texture.getLink()));
    }

    @Override
    public void cover(Entity entity) {
        renderTexturedQUAD(entity.height, entity.width, entity.x, entity.y, id);
    }

    @Override
    public void update() {}

    @Override
    public EntityTexture clone() {
        return this;
    }

    // GETTERS & SETTERS //

    protected Integer getId() { return id; }

    protected void setId(Integer id) { this.id = id; }

    public String getLink() { return link; }
}
