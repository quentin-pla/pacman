import engines.graphics.GraphicsEngine;
import engines.kernel.KernelEngine;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        //Transfert des textures
        GraphicsEngine.loadSpriteSheet("sprite_sheet.png", 11, 11);

        //Sol du niveau
        int floor = KernelEngine.generateEntity();
        GraphicsEngine.resize(floor, 30, 30);
        GraphicsEngine.bindColor(floor, 0, 0, 255);

        //Scène principale
        int mainScene = GraphicsEngine.generateScene(300, 300);
        GraphicsEngine.setSceneBackgroundColor(mainScene,0,0,0);
        GraphicsEngine.bindScene(mainScene);

        //Démarrage du jeu
        KernelEngine.start();
    }
}
