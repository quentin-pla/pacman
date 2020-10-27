package engines;

import api.Entity;
import api.Window;
import api.textures.SpriteSheet;
import api.textures.Texture;
import api.tiles.ColorTile;
import api.tiles.SpriteSheetTile;
import api.tiles.TextureTile;
import api.tiles.Tile;

import java.util.HashMap;
import java.util.Map;

/**
 * Moteur graphique
 */
public class GraphicsEngine {
    /**
     * Textures sauvegardées
     */
    private Map<String,Texture> saved_textures;

    /**
     * Fichiers de texture sauvegardés
     */
    private Map<String,SpriteSheet> saved_sprite_sheets;

    /**
     * Instance
     */
    private static GraphicsEngine instance;

    /**
     * Constructeur
     */
    private GraphicsEngine() {
        saved_textures = new HashMap<>();
        saved_sprite_sheets = new HashMap<>();
    }

    /**
     * Obtenir l'instance
     * @return instance
     */
    public static GraphicsEngine get() {
        if (instance == null) instance = new GraphicsEngine();
        return instance;
    }

    // CARREAUX //

    /**
     * Dessiner un carreau coloré
     * @param size dimensions
     * @param x position horizontale
     * @param y position verticale
     * @param color couleur
     */
    public void drawColorTile(int size, int x, int y, float[] color) {
        ColorTile tile = new ColorTile(size, color);
        Window.get().getScene().addEntity(tile, x, y);
    }

    /**
     * Dessiner un carreau avec une texture
     * @param size dimensions
     * @param x position horizontale
     * @param y position verticale
     * @param link lien vers la texture
     */
    public void drawTextureTile(int size, int x, int y, String link) {
        if (!saved_textures.containsKey(link))
            saved_textures.put(link, new Texture(link));
        TextureTile tile = new TextureTile(size, saved_textures.get(link));
        Window.get().getScene().addEntity(tile, x, y);
    }

    /**
     * Dessiner un carreau avec un fichier de texture
     * @param size dimensions
     * @param x position horizontale
     * @param y position verticale
     * @param link lien vers la texture
     */
    public void drawSpriteSheetTile(int size, int x, int y, String link, int x_coord, int y_coord) {
        if (saved_sprite_sheets.containsKey(link)) {
            SpriteSheetTile tile = new SpriteSheetTile(size, saved_sprite_sheets.get(link), x_coord, y_coord);
            Window.get().getScene().addEntity(tile, x, y);
        }
    }

    /**
     * Supprimer un carreau
     * @param entity entité
     */
    public void eraseTile(Entity entity) {
        Window.get().getScene().removeEntity(entity);
    }

    /**
     * Translater un carreau
     * @param tile carreau
     * @param x position horizontale
     * @param y position verticale
     */
    public void translateTile(Tile tile, int x, int y) {
        tile.translate(x, y);
    }

    // TEXTURES //

    /**
     * Transférer une texture à OpenGL
     * @param link lien du fichier
     */
    public void uploadTexture(String link) {
        if (!saved_textures.containsKey(link))
            saved_textures.put(link, new Texture(link));
    }

    /**
     * Transférer un fichier de texture à OpenGL
     * @param link lien du fichier
     * @param height nombre d'éléments horizontaux
     * @param width nombre d'éléments verticaux
     */
    public void uploadSpriteSheet(String link, int height, int width) {
        if (!saved_sprite_sheets.containsKey(link))
            saved_sprite_sheets.put(link, new SpriteSheet(link, height, width));
    }
}
