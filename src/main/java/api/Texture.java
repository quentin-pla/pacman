package api;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

/**
 * Texture
 */
public class Texture {
    /**
     * ID de la texture
     */
    private int id;

    /**
     * Constructeur
     * @param link lien vers le fichier
     */
    public Texture(String link) {
        generate("target/classes/assets/" + link);
    }

    /**
     * Générer la texture
     * @param link lien vers le fichier
     */
    public void generate(String link) {
        //Générer la texture sur le GPU
        id = glGenTextures();
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
        ByteBuffer image = stbi_load(link, width, height, channels, 0);

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
    }

    /**
     * Lier la texture
     */
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Détacher la texture
     */
    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
