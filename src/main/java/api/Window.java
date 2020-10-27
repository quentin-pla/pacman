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
    int height;

    /**
     * Largeur
     */
    int width;

    /**
     * Échelle
     */
    int scale = 1;

    /**
     * Titre de la fenêtre
     */
    String title;

    /**
     * Fenêtre GLFW
     */
    private long glfwWindow;

    /**
     * Scène liée à la fenêtre
     */
    private Scene scene = null;

    /**
     * Instance fenêtre
     */
    private static Window window = null;

    /**
     * Constructeur
     */
    private Window() {
        //Scène par défaut
        scene = new Scene(600,600);
        height = scene.getHeight();
        width = scene.getWidth();
    }

    /**
     * Obtenir l'instance de la fenêtre
     * @return instance de la fenêtre
     */
    public static Window get() {
        if (window == null) window = new Window();
        return window;
    }

    /**
     * Afficher la fenêtre
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
    private void initOpenGL() {
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
    public void loop() {
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

    public void setScene(Scene scene) {
        this.scene = scene;
        this.height = scene.getHeight();
        this.width = scene.getWidth();
    }

    public Scene getScene() {
        return scene;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
