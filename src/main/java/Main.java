import engines.kernel.Entity;
import gameplay.Gameplay;
import gameplay.Ghost;
import gameplay.Level;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        //Gameplay
        Gameplay gameplay = new Gameplay();

        //Level par défaut
        Level defaultLevel = gameplay.generateLevel(10,10);

//        Ghost ghost = new Ghost(gameplay);

//        defaultLevel.addGhost(ghost,9,5);

//        gameplay.kernelEngine().addEvent("test",() -> {
//            System.out.println("collision");
//        });

        //Génération des murs
        for (int j = 0; j < 6; j++) {
            Entity wall = defaultLevel.getMatrix()[5][j];
            gameplay.graphicsEngine().bindColor(wall.getGraphicEntity(),0,0,255);
            gameplay.physicsEngine().addCollisions(gameplay.getPlayer().getPhysicEntity(), wall.getPhysicEntity());
            for (Ghost ghost : gameplay.getGhosts())
                gameplay.physicsEngine().addCollisions(ghost.getPhysicEntity(), wall.getPhysicEntity());
        }

        for (int j = 0; j < 2; j++) {
            Entity wall = defaultLevel.getMatrix()[j+5][3];
            gameplay.graphicsEngine().bindColor(wall.getGraphicEntity(),0,0,255);
            gameplay.physicsEngine().addCollisions(gameplay.getPlayer().getPhysicEntity(), wall.getPhysicEntity());
            for (Ghost ghost : gameplay.getGhosts())
                gameplay.physicsEngine().addCollisions(ghost.getPhysicEntity(), wall.getPhysicEntity());
        }

//        gameplay.physicsEngine().bindEventOnCollision(ghost.getPhysicEntity(), "test");

//        //Génération des boules
//        for (int j = 0; j < 6; j++) {
//            Entity ball = defaultLevel.getMatrix()[6][j];
//            gameplay.graphicsEngine().bindTexture(ball.getGraphicEntity(),gameplay.getTexturesFile(),10,2);
//            gameplay.kernelEngine().addEvent("eraseBall" + j,() -> {
//                gameplay.kernelEngine().removeEntity(ball);
//                gameplay.soundEngine().playSound("munch");
//            });
//            gameplay.physicsEngine().bindEventOnSameLocation(gameplay.getPlayer().getPhysicEntity(), ball.getPhysicEntity(), "eraseBall" + j);
//        }

        gameplay.playLevel(defaultLevel);
    }
}
