package gameplay;

import engines.kernel.Event;
import engines.graphics.GraphicsEngine;
import engines.input_output.IOEngine;
import engines.kernel.Entity;
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
        ArrayList<Entity> walls = new ArrayList<>();

        //Génération des murs
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                Entity wall = KernelEngine.generateEntity();
                walls.add(wall);
                PhysicsEngine.move(wall.getPhysicEntity(),30 + (30*j),100 + (60*i));
                PhysicsEngine.resize(wall.getPhysicEntity(),30,30);
                GraphicsEngine.bindColor(wall.getGraphicEntity(),0,0,255);
                PhysicsEngine.addCollisions(player.getPhysicEntity(), wall.getPhysicEntity());
            }
        }
        Entity wall = KernelEngine.generateEntity();
        walls.add(wall);
        PhysicsEngine.move(wall.getPhysicEntity(),0,160);
        PhysicsEngine.resize(wall.getPhysicEntity(),30,30);
        GraphicsEngine.bindColor(wall.getGraphicEntity(),0,0,255);
        PhysicsEngine.addCollisions(player.getPhysicEntity(), wall.getPhysicEntity());

        //Id des boules
        ArrayList<Entity> balls = new ArrayList<>();

        //Génération des boules
        for (int i = 0; i < 6; i++) {
            Entity ball = KernelEngine.generateEntity();
            balls.add(ball);
            PhysicsEngine.move(ball.getPhysicEntity(),30 + (30*i),130);
            PhysicsEngine.resize(ball.getPhysicEntity(),30,30);
            GraphicsEngine.bindTexture(ball.getGraphicEntity(),textures,10,2);
            KernelEngine.addEvent("eraseBall" + i, new Event() {
                @Override
                public void run() { GraphicsEngine.erase(ball.getGraphicEntity()); }
            });
            PhysicsEngine.bindEventOnSameLocation(player.getPhysicEntity(), ball.getPhysicEntity(), "eraseBall" + i);
        }

        //Ajout des collisions de la scène
        PhysicsEngine.addBoundLimits(player.getPhysicEntity(), 0,0,300,300);

        //Scène principale
        int mainScene = GraphicsEngine.generateScene(300, 300);
        GraphicsEngine.setSceneBackgroundColor(mainScene,0,0,0);
        GraphicsEngine.bindScene(mainScene);
        for (Entity entity : walls)
            GraphicsEngine.addToCurrentScene(entity.getGraphicEntity());
        for (Entity entity : balls)
            GraphicsEngine.addToCurrentScene(entity.getGraphicEntity());
        GraphicsEngine.addToCurrentScene(player.getGraphicEntity());

        //Démarrage du jeu
        KernelEngine.start();
    }

    public static int getTexturesFile() {
        return textures;
    }
}
