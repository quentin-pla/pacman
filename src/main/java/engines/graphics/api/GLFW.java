package engines.graphics.api;

import engines.input_output.InputOutputEngine;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Bibliothèque GLFW (Gestionnaire de fenêtres OpenGL)
 */
public class GLFW {
    /**
     * Fenêtre GLFW
     */
    private static long glfwWindow;

    /**
     * Initialiser la fenêtre
     */
    protected static void initGLFWWindow(int width, int height, String title) {
        //Affichage console en cas d'erreur
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialiser GLFW
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW.");

        //Configurer GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        //Création de la fenêtre
        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        //Vérification que la fenêtre a bien été créée
        if (glfwWindow == NULL)
            throw new IllegalStateException("Failed to create the GLFW window.");

        //Définition de la fonction de rappel pour les entrées clavier
        glfwSetKeyCallback(glfwWindow, InputOutputEngine::keyCallback);
        //Définition de la fonction de rappel pour les entrées souris
        glfwSetMouseButtonCallback(glfwWindow, InputOutputEngine::mouseCallback);

        //Indiquer le contexte OpenGL
        glfwMakeContextCurrent(glfwWindow);
        //Activer la synchronisation verticale
        glfwSwapInterval(1);

        //Afficher la fenêtre
        glfwShowWindow(glfwWindow);

        //Liaison avec OpenGL
        GL.createCapabilities();

        //Configurer OpenGL
        initOpenGL(width, height);
    }

    /**
     * Configurer OpenGL
     */
    private static void initOpenGL(int width, int height) {
        //Initialiser OpenGL pour utiliser les pixels dans une scène 2D
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, height, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        //Activer les textures
        glEnable(GL_TEXTURE_2D);
        //Conserver la transparence des textures
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Nettoyer l'affichage
     */
    protected static void clearGLFWWindow() {
        // Interroger les évènements
        glfwPollEvents();
        //Définition couleur pour effacer l'écran
        glClearColor(0f, 0f, 0f, 0f);
        //Effacer l'écran
        glClear(GL_COLOR_BUFFER_BIT);
    }

    /**
     * Savoir si la fenêtre est en cours d'exécution
     * @return booléen
     */
    protected static boolean isGLFWWindowRunning() {
        return !glfwWindowShouldClose(glfwWindow);
    }

    /**
     * Échanger les tampons
     */
    protected static void swapBuffers() {
        glfwSwapBuffers(glfwWindow);
    }

    /**
     * Terminer l'exécution de la fenêtre
     */
    protected static void stopGLFWWindow() {
        // Libérer la mémoire
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminer le programme
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
