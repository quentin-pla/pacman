package gameplay;

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
     * Moteur noyau
     */
    private final KernelEngine kernelEngine;

    /**
     * Identifiant du fichier contenant les textures du jeu
     */
    private final int textures;

    /**
     * Joueur
     */
    private final Player player;

    /**
     * Constructeur
     */
    public Gameplay() {
        this.kernelEngine = new KernelEngine();
        this.textures = kernelEngine.getGraphicsEngine().loadSpriteSheet("assets/sprite_sheet.png", 11, 11);
        this.player = new Player(this,30,30,2,textures,1,3);
        initGameplay();
    }

    /**
     * Initialiser le gameplay
     */
    private void initGameplay() {
        //Activation des entrées / sorties clavier
        ioEngine().enableKeyboardIO();

        //Id des murs
        ArrayList<Entity> walls = new ArrayList<>();

        //Génération des murs
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 6; j++) {
                Entity wall = kernelEngine.generateEntity();
                walls.add(wall);
                physicsEngine().move(wall.getPhysicEntity(),30 + (30*j),100 + (60*i));
                physicsEngine().resize(wall.getPhysicEntity(),30,30);
                graphicsEngine().bindColor(wall.getGraphicEntity(),0,0,255);
                physicsEngine().addCollisions(player.getPhysicEntity(), wall.getPhysicEntity());
            }
        }
        Entity wall = kernelEngine.generateEntity();
        walls.add(wall);
        physicsEngine().move(wall.getPhysicEntity(),0,160);
        physicsEngine().resize(wall.getPhysicEntity(),30,30);
        graphicsEngine().bindColor(wall.getGraphicEntity(),0,0,255);
        physicsEngine().addCollisions(player.getPhysicEntity(), wall.getPhysicEntity());

        //Id des boules
        ArrayList<Entity> balls = new ArrayList<>();

        //Génération des boules
        for (int i = 0; i < 6; i++) {
            Entity ball = kernelEngine.generateEntity();
            balls.add(ball);
            physicsEngine().move(ball.getPhysicEntity(),30 + (30*i),130);
            physicsEngine().resize(ball.getPhysicEntity(),30,30);
            graphicsEngine().bindTexture(ball.getGraphicEntity(),textures,10,2);
            kernelEngine.addEvent("eraseBall" + i,() -> graphicsEngine().erase(ball.getGraphicEntity()));
            physicsEngine().bindEventOnSameLocation(player.getPhysicEntity(), ball.getPhysicEntity(), "eraseBall" + i);
        }

        //Ajout des collisions de la scène
        physicsEngine().addBoundLimits(player.getPhysicEntity(), 0,0,300,300);

        //Scène principale
        int mainScene = graphicsEngine().generateScene(300, 300);
        graphicsEngine().setSceneBackgroundColor(mainScene,0,0,0);
        graphicsEngine().bindScene(mainScene);
        for (Entity entity : walls)
            graphicsEngine().addToCurrentScene(entity.getGraphicEntity());
        for (Entity entity : balls)
            graphicsEngine().addToCurrentScene(entity.getGraphicEntity());
        graphicsEngine().addToCurrentScene(player.getGraphicEntity());

        //Démarrage du jeu
        kernelEngine.start();
    }

    // GETTERS //

    public int getTexturesFile() { return textures; }

    public KernelEngine kernelEngine() { return kernelEngine; }

    public GraphicsEngine graphicsEngine() { return kernelEngine.getGraphicsEngine(); }

    public IOEngine ioEngine() { return kernelEngine.getIoEngine(); }

    public PhysicsEngine physicsEngine() { return kernelEngine.getPhysicsEngine(); }
}
