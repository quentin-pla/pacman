import engines.graphics.GraphicsEngine;
import engines.kernel.KernelEngine;
import engines.physics.PhysicsEngine;
import gameplay.Player;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        //Transfert des textures
        int spriteSheet = GraphicsEngine.loadSpriteSheet("assets/sprite_sheet.png", 11, 11);

        //Joueur
        Player player = new Player(30, 30, 2, spriteSheet, 1, 3);
        PhysicsEngine.move(player.getEntityID(),0,0);

        //Objet simple
        int object = KernelEngine.generateEntity();
        PhysicsEngine.move(object,135,135);
        GraphicsEngine.resize(object,30,30);
        GraphicsEngine.bindColor(object,255,0,0);

        //Ajout des collisions
        PhysicsEngine.addCollisions(player.getEntityID(), object);
        PhysicsEngine.addBoundLimits(player.getEntityID(), 0,0,300,300);

        //Scène principale
        int mainScene = GraphicsEngine.generateScene(300, 300);
        GraphicsEngine.setSceneBackgroundColor(mainScene,0,0,0);
        GraphicsEngine.bindScene(mainScene);
        GraphicsEngine.addToCurrentScene(object);
        GraphicsEngine.addToCurrentScene(player.getEntityID());

        //Démarrage du jeu
        KernelEngine.start();
    }
}
