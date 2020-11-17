package gameplay;

import engines.graphics.GraphicsEngine;
import engines.input_output.IOEngine;
import engines.kernel.Event;
import engines.kernel.KernelEngine;
import engines.physics.PhysicsEngine;

import java.util.ArrayList;

/**
 * Gameplay
 */
public class Gameplay {
    /**
     * Identifiant du fichier contenant les textures du jeu
     */
    private static final int textures = GraphicsEngine.loadSpriteSheet("assets/sprite_sheet.png", 11, 11);

    /**
     * Joueur
     */
    private static Player player;

    /**
     * Constructeur
     */
    public Gameplay() {
        initGameplay();
    }

    private void initGameplay() {
        //Activation des entrées / sorties clavier
        IOEngine.enableKeyboardIO();

        //Joueur
        player = new Player(30, 30, 2, textures, 1, 3);

        //Id des murs
        ArrayList<Integer> walls = new ArrayList<>();

        //Génération des murs
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                int wall = KernelEngine.generateEntity();
                walls.add(wall);
                PhysicsEngine.move(wall,30 + (30*j),100 + (60*i));
                GraphicsEngine.resize(wall,30,30);
                GraphicsEngine.bindColor(wall,0,0,255);
                PhysicsEngine.addCollisions(player.getEntityID(), wall);
            }
        }
        int wall = KernelEngine.generateEntity();
        walls.add(wall);
        PhysicsEngine.move(wall,0,160);
        GraphicsEngine.resize(wall,30,30);
        GraphicsEngine.bindColor(wall,0,0,255);
        PhysicsEngine.addCollisions(player.getEntityID(), wall);

        //Id des boules
        ArrayList<Integer> balls = new ArrayList<>();

        //Génération des boules
        for (int i = 0; i < 6; i++) {
            int ball = KernelEngine.generateEntity();
            balls.add(ball);
            PhysicsEngine.move(ball,30 + (30*i),130);
            GraphicsEngine.resize(ball,30,30);
            GraphicsEngine.bindTexture(ball,textures,10,2);
            KernelEngine.addEvent("eraseBall" + i, new Event() {
                @Override
                public void run() { GraphicsEngine.erase(ball); }
            });
            PhysicsEngine.bindEventOnSameLocation(player.getEntityID(), ball, "eraseBall" + i);
        }

        //Ajout des collisions de la scène
        PhysicsEngine.addBoundLimits(player.getEntityID(), 0,0,300,300);

        //Scène principale
        int mainScene = GraphicsEngine.generateScene(300, 300);
        GraphicsEngine.setSceneBackgroundColor(mainScene,0,0,0);
        GraphicsEngine.bindScene(mainScene);
        for (Integer id : walls)
            GraphicsEngine.addToCurrentScene(id);
        for (Integer id : balls)
            GraphicsEngine.addToCurrentScene(id);
        GraphicsEngine.addToCurrentScene(player.getEntityID());

        //Démarrage du jeu
        KernelEngine.start();
    }

    public static int getTexturesFile() {
        return textures;
    }
}
