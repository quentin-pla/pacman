import engines.graphics.GraphicsEngine;
import engines.kernel.KernelEngine;
import gameplay.Player;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        //Transfert des textures
        int spriteSheet = GraphicsEngine.loadSpriteSheet("sprite_sheet.png", 11, 11);

        Player player = new Player(30, 30, 2, spriteSheet, 1, 3);

        //Scène principale

        int mainScene = GraphicsEngine.generateScene(300, 300);
        GraphicsEngine.setSceneBackgroundColor(mainScene,0,0,0);
        GraphicsEngine.bindScene(mainScene);
        GraphicsEngine.addToCurrentScene(player.getId());

        //Démarrage du jeu
        KernelEngine.start();

    }
}
