package engines;

import api.Entity;
import api.Window;
import api.textures.SpriteSheet;
import api.textures.Texture;
import api.tiles.ColorTile;
import api.tiles.SpriteSheetTile;
import api.tiles.TextureTile;

import java.util.HashMap;
import java.util.Map;

/**
 * Moteur graphique
 */
public class GraphicsEngine {
    /**
     * Textures sauvegardées
     */
    private static Map<String,Texture> saved_textures = new HashMap<>();

    /**
     * Fichiers de texture sauvegardés
     */
    private static Map<String,SpriteSheet> saved_sprite_sheets = new HashMap<>();

    // CARREAUX //

    /**
     * Dessiner un carreau coloré
     * @param size dimensions
     * @param x position horizontale
     * @param y position verticale
     * @param color couleur
     */
    public static void drawColorTile(int size, int x, int y, float[] color) {
        ColorTile tile = new ColorTile(size, color);
        Window.getScene().addEntity(tile, x, y);
    }

    /**
     * Dessiner un carreau avec une texture
     * @param size dimensions
     * @param x position horizontale
     * @param y position verticale
     * @param name nom de la texture
     */
    public static void drawTextureTile(int size, int x, int y, String name) {
        try {
            if (saved_textures.containsKey(name)) {
                TextureTile tile = new TextureTile(size, saved_textures.get(name));
                Window.getScene().addEntity(tile, x, y);
            }
            else throw new Exception("Texture introuvable");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dessiner un carreau avec un fichier de texture
     * @param size dimensions
     * @param x position horizontale
     * @param y position verticale
     * @param name nom de la texture
     */
    public static void drawSpriteSheetTile(int size, int x, int y, String name, int x_coord, int y_coord) {
        try {
            if (saved_sprite_sheets.containsKey(name)) {
                SpriteSheetTile tile = new SpriteSheetTile(size, saved_sprite_sheets.get(name), x_coord, y_coord);
                Window.getScene().addEntity(tile, x, y);
            }
            else throw new Exception("Fichier de texture introuvable");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ENTITES //

    /**
     * Supprimer une entité
     * @param entity entité
     */
    public static void eraseEntity(Entity entity) {
        Window.getScene().removeEntity(entity);
    }

    /**
     * Translater une entité
     * @param entity entité
     * @param x position horizontale
     * @param y position verticale
     */
    public static void translateEntity(Entity entity, int x, int y) {
        entity.translate(x, y);
    }

    // TEXTURES //

    /**
     * Transférer une texture à OpenGL
     * @param link lien du fichier
     */
    public static void uploadTexture(String link, String name) {
        try {
            if (!saved_textures.containsKey(name))
                saved_textures.put(name, new Texture(link));
            else throw new Exception("Nom de texture déjà existant");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Transférer un fichier de texture à OpenGL
     * @param link lien du fichier
     * @param height nombre d'éléments horizontaux
     * @param width nombre d'éléments verticaux
     */
    public static void uploadSpriteSheet(String link, String name, int height, int width) {
        try {
            if (!saved_sprite_sheets.containsKey(name))
                saved_sprite_sheets.put(name, new SpriteSheet(link, height, width));
            else throw new Exception("Nom de fichier de texture déjà existant");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Texture> getSaved_textures() {
        return saved_textures;
    }
}
