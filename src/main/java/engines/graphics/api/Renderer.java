package engines.graphics.api;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

/**
 * Générateur de graphismes
 */
public class Renderer {
    /**
     * Générer un carré
     * @param size taille en pixels
     * @param x position horizontale
     * @param y position verticale
     * @param color couleur
     */
    protected static void renderQUAD(int size, int x, int y, float[] color) {
        glColor4f(color[0], color[1], color[2], color[3]);
        glBegin(GL_QUADS);
        glVertex2i(x, y);
        glVertex2i(x + size, y);
        glVertex2i(x + size, y + size);
        glVertex2i(x, y + size);
        glEnd();
    }

    /**
     * Générer un carré texturisé
     * @param size taille en pixels
     * @param x position horizontale
     * @param y position verticale
     * @param texture_id id texture
     */
    protected static void renderTexturedQUAD(int size, int x, int y, int texture_id) {
        bindTexture(texture_id);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0); glVertex2i(x, y);
        glTexCoord2f(1, 0); glVertex2i(x + size, y);
        glTexCoord2f(1, 1); glVertex2i(x + size, y + size);
        glTexCoord2f(0, 1); glVertex2i(x, y + size);
        glEnd();
        unbindTexture();
    }

    /**
     * Générer un carré texturisé à partir d'un fichier de texture
     * @param size taille en pixels
     * @param x position horizontale
     * @param y position verticale
     * @param texture_id id texture
     * @param height nombre d'éléments horizontaux fichier de texture
     * @param width nombre d'élements verticaux fichier de texture
     * @param sprite_coords coordonnées de la texture
     */
    protected static void renderSpriteSheetQUAD(int size, int x, int y, int texture_id, int height, int width, int[] sprite_coords) {
        bindTexture(texture_id);
        glBegin(GL_QUADS);
        glTexCoord2f((sprite_coords[1] + 0f) / width,(sprite_coords[0] + 0f) / height);
        glVertex2i(x, y);
        glTexCoord2f((sprite_coords[1] + 1f) / width,(sprite_coords[0] + 0f) / height);
        glVertex2i(x + size, y);
        glTexCoord2f((sprite_coords[1] + 1f) / width,(sprite_coords[0] + 1f) / height);
        glVertex2i(x + size, y + size);
        glTexCoord2f((sprite_coords[1] + 0f) / width,(sprite_coords[0] + 1f) / height);
        glVertex2i(x, y + size);
        glEnd();
        unbindTexture();
    }

    /**
     * Générer une texture
     * @param link lien vers la texture
     * @return ID de la texture générée
     */
    protected static int generateTexture(String link) {
        //Générer la texture sur le GPU
        int id = glGenTextures();
        //Lier la texture
        glBindTexture(GL_TEXTURE_2D, id);
        //Répéter l'image dans les deux directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        //Pixéliser l'image en cas d'étirement
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //Pixéliser l'image en cas de rétrécissement
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //Largeur
        IntBuffer width = BufferUtils.createIntBuffer(1);
        //Hauteur
        IntBuffer height = BufferUtils.createIntBuffer(1);
        //Canaux
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        //Récupération de l'image
        ByteBuffer image = stbi_load("target/classes/assets/" + link, width, height, channels, 0);

        if (image != null) {
            //Image RGB
            if (channels.get(0) == 3)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            //Image RGBA
            else if (channels.get(0) == 4)
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);

            //Libérer les données
            stbi_image_free(image);
        }
        return id;
    }

    /**
     * Attacher une texture
     * @param id id texture
     */
    private static void bindTexture(int id) {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Détacher une texture
     */
    private static void unbindTexture() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
