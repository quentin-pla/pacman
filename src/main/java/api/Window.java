package api;

import engines.InputOutputEngine;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

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
    int height;

    /**
     * Largeur
     */
    int width;

    /**
     * Titre de la fenêtre
     */
    String title;

    /**
     * Fenêtre GLFW
     */
    private long glfwWindow;

    /**
     * Instance fenêtre
     */
    private static Window window = null;

    /**
     * Constructeur
     */
    private Window() {}

    /**
     * Obtenir l'instance de la fenêtre
     * @return instance de la fenêtre
     */
    public static Window get() {
        if (window == null) window = new Window();
        return window;
    }

    /**
     * Exécuter la fenêtre
     */
    public void run() {
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
    public void init() {
        // Affichage console en cas d'erreur
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialiser GLFW
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW.");

        // Configurer GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        // Création de la fenêtre
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
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
    }

    /**
     * Rafraichir la fenêtre
     */
    public void loop() {
        // Tant que la fenêtre est ouverte
        while (!glfwWindowShouldClose(glfwWindow)) {
            // Interroger les évènements
            glfwPollEvents();

            glClearColor(0f, 0f, 0f, 0f);
            glClear(GL_COLOR_BUFFER_BIT);

            //Si la touche espace est appuyée
            if (InputOutputEngine.isMouseButtonPressed(GLFW_MOUSE_BUTTON_LEFT))
                System.out.println("ok");

            glfwSwapBuffers(glfwWindow);
        }
    }

    /**
     * Définir la taille de la fenêtre
     * @param height hauteur
     * @param width largeur
     */
    public void setSize(int height, int width) {
        this.height = height;
        this.width = width;
    }

    /**
     * Définit le titre de la fenêtre
     * @param title titre
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
