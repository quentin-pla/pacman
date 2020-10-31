package engines.graphics;

import engines.graphics.api.Renderer;

import java.util.HashMap;
import java.util.Map;

/**
 * Moteur graphique
 */
public class GraphicsEngine extends Renderer {
    /**
     * Textures sauvegardées
     */
    private static Map<String,Texture> saved_textures = new HashMap<>();

    /**
     * Fichiers de texture sauvegardés
     */
    private static Map<String,SpriteSheet> saved_sprite_sheets = new HashMap<>();

    // ENTITES //

    /**
     * Dessiner une entité
     * @param entity entité
     */
    protected static void drawEntity(Entity entity) {
        entity.draw();
    }

    /**
     * Supprimer une entité
     * @param entity entité
     */
    protected static void eraseEntity(Entity entity) {
        entity.erase();
    }

    /**
     * Translater une entité
     * @param entity entité
     * @param x position horizontale à additionner
     * @param y position verticale à additionner
     */
    protected static void translateEntity(Entity entity, int x, int y) {
        entity.translate(x, y);
    }

    // TEXTURES //

    /**
     * Transférer une texture à OpenGL
     * @param link lien du fichier
     */
    protected static void uploadTexture(String link, String name) {
        if (!saved_textures.containsKey(name))
            saved_textures.put(name, new Texture(link));
        else {
            try {
                throw new Exception("Nom de texture déjà existant");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Transférer un fichier de texture à OpenGL
     * @param link lien du fichier
     * @param height nombre d'éléments horizontaux
     * @param width nombre d'éléments verticaux
     */
    protected static void uploadSpriteSheet(String link, int height, int width, String name) {
        if (!saved_sprite_sheets.containsKey(name))
            saved_sprite_sheets.put(name, new SpriteSheet(link, height, width));
        else {
            try {
                throw new Exception("Nom de fichier de texture déjà existant");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Générer les textures
     */
    protected static void generateTextures() {
        for (Texture texture : saved_textures.values())
            texture.setId(generateTexture(texture.getLink()));
        for (SpriteSheet sprite_sheet : saved_sprite_sheets.values())
            sprite_sheet.getTexture().setId(generateTexture(sprite_sheet.getTexture().getLink()));
    }

    /**
     * Obtenir une texture
     * @param name nom de la texture
     * @return texture
     */
    protected static Texture getTexture(String name) {
        return saved_textures.get(name);
    }

    /**
     * Obtenir un fichier de texture
     * @param name nom du fichier de texture
     * @return fichier de texture
     */
    protected static SpriteSheet getSpriteSheet(String name) {
        return saved_sprite_sheets.get(name);
    }
}
