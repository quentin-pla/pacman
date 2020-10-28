package api;

import engines.InputOutputEngine;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.concurrent.TimeUnit;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Fenêtre
 */
public class Window {
    /**
     * Hauteur
     */
    private static int height;

    /**
     * Largeur
     */
    private static int width;

    /**
     * Échelle
     */
    private static int scale = 1;

    /**
     * Titre de la fenêtre
     */
    private static String title;

    /**
     * Fenêtre GLFW
     */
    private static long glfwWindow;

    /**
     * Scène liée à la fenêtre
     */
    private static Scene scene;

    /**
     * Afficher la fenêtre
     */
    public static void show() {
        try {
            if (scene == null)
                throw new Exception("Aucune scène spécifiée");
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
        loop();

        // Libérer la mémoire
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminer le programme
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Initialiser la fenêtre
     */
    private static void init() {
        // Affichage console en cas d'erreur
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialiser GLFW
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW.");

        // Configurer GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        // Création de la fenêtre
        glfwWindow = glfwCreateWindow(width * scale, height * scale, title, NULL, NULL);
        //Vérification que la fenêtre a bien été créée
        if (glfwWindow == NULL)
            throw new IllegalStateException("Failed to create the GLFW window.");

        // Définition de la fonction de rappel pour les entrées clavier
        glfwSetKeyCallback(glfwWindow, InputOutputEngine::keyCallback);
        // Définition de la fonction de rappel pour les entrées souris
        glfwSetMouseButtonCallback(glfwWindow, InputOutputEngine::mouseCallback);

        // Indiquer le contexte OpenGL
        glfwMakeContextCurrent(glfwWindow);
        // Activer la synchronisation verticale
        glfwSwapInterval(1);

        // Afficher la fenêtre
        glfwShowWindow(glfwWindow);

        //Liaison avec OpenGL
        GL.createCapabilities();

        //Initialiser OpenGL
        initOpenGL();
    }

    /**
     * Initialiser OpenGL
     */
    private static void initOpenGL() {
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
     * Rafraichir la fenêtre
     */
    private static void loop() {
        // Tant que la fenêtre est ouverte
        while (!glfwWindowShouldClose(glfwWindow)) {
            // Interroger les évènements
            glfwPollEvents();
            //Définition couleur pour effacer l'écran
            glClearColor(0f, 0f, 0f, 0f);
            //Effacer l'écran
            glClear(GL_COLOR_BUFFER_BIT);

            //Générer la scène
            scene.render();

            //Échanger les tampons de la fenêtre
            glfwSwapBuffers(glfwWindow);

            //Ajout d'un timer pour limiter l'utilisation de la batterie
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // GETTERS & SETTERS //

    public static void setScene(Scene scene) {
        Window.scene = scene;
        Window.height = scene.getHeight();
        Window.width = scene.getWidth();
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setTitle(String title) {
        Window.title = title;
    }
}
