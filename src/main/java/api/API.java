package api;

import engines.GraphicsEngine;
import engines.KernelEngine;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * API LWJGL
 */
public class API {
    /**
     * Fenêtre
     */
    private static long window;

    public void run() {
        init();
        loop();

        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    /**
     * Initialiser la fenêtre
     */
    private void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Fenêtre non redimensionnable
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        // Création de la fenêtre
        window = glfwCreateWindow(600, 600, "PACMAN", NULL, NULL);

        // Message d'erreur si la fenêtre n'a pas pu être créée
        if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");

        // Initialisation du moteur noyau
        KernelEngine.init();

        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                window,
                (vidmode.width() - pWidth.get(0)) / 2,
                (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
        // Make the window visible
        glfwShowWindow(window);
    }

    /**
     * Mettre à jour l'affichage graphique
     */
    private void loop() {
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        while (!glfwWindowShouldClose(window)) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            GraphicsEngine.drawGraphics(); // dessiner le contenu de la fenêtre

            glfwSwapBuffers(window); // swap the color buffers
        }
    }

    public static long getWindow() {
        return window;
    }
}