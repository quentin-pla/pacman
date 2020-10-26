package engines;

import static org.lwjgl.opengl.GL11.*;

/**
 * Moteur graphique
 */
public class GraphicsEngine {
    /**
     * Initialiser le moteur graphique
     */
    public static void init() {
        //
    }

    /**
     * Dessiner le contenu de la fenêtre
     */
    public static void drawGraphics() {
        //Dessiner un carré blanc
        glBegin(GL_QUADS);
            glVertex2d(-0.5, 0.5);
            glVertex2d(0.5, 0.5);
            glVertex2d(0.5, -0.5);
            glVertex2d(-0.5, -0.5);
        glEnd();
    }
}
