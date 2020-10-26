package engines;

import api.API;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Moteur entrée/sortie
 */
public class InputOutputEngine {
    /**
     * Initialiser le moteur entrée/sortie
     */
    public static void init() {
        //Ajout d'un écouteur sur les entrées clavier
        glfwSetKeyCallback(API.getWindow(), (window, key, scancode, action, mods) -> {
            //Si une touche est pressée
            if (action == GLFW_PRESS) {
                switch (key) {
                    case GLFW_KEY_UP:
                    case GLFW_KEY_RIGHT:
                    case GLFW_KEY_LEFT:
                    case GLFW_KEY_DOWN:
                    case GLFW_MOUSE_BUTTON_LEFT:
                        //Envoi de l'information au moteur noyau
                        KernelEngine.newInput(key);
                        break;
                    case GLFW_KEY_ESCAPE:
                        //Sortie du jeu
                        glfwSetWindowShouldClose(window, true);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
