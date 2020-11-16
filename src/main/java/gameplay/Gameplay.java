package gameplay;

import engines.graphics.GraphicsEngine;
import engines.input_output.IOEngine;
import engines.kernel.Event;
import engines.kernel.KernelEngine;
import engines.physics.PhysicsEngine;

import java.awt.event.KeyEvent;

public class Gameplay {

    static int spriteSheet = GraphicsEngine.loadSpriteSheet("assets/sprite_sheet.png", 11, 11);
    static Player player = new Player(30, 30, 2, spriteSheet, 1, 3);


    enum EVENTS {
        GoUp(new Event() {
            @Override
            public void run() {

                PhysicsEngine.goUp(player.getEntityID(), player.getMoveSpeed());
                GraphicsEngine.bindAnimation(player.getEntityID(), player.getAnimations().get(Player.MoveDirection.UP.name()));
            }
        }),

        GoDown(new Event() {
            @Override
            public void run() {

                PhysicsEngine.goDown(player.getEntityID(), player.getMoveSpeed());
                GraphicsEngine.bindAnimation(player.getEntityID(), player.getAnimations().get(Player.MoveDirection.DOWN.name()));
            }
        }),
        GoLeft(new Event() {
            @Override
            public void run() {

                PhysicsEngine.goLeft(player.getEntityID(), player.getMoveSpeed());
                GraphicsEngine.bindAnimation(player.getEntityID(), player.getAnimations().get(Player.MoveDirection.LEFT.name()));
            }
        }),
        GoRight(new Event() {
            @Override
            public void run() {

                PhysicsEngine.goRight(player.getEntityID(), player.getMoveSpeed());
                GraphicsEngine.bindAnimation(player.getEntityID(), player.getAnimations().get(Player.MoveDirection.RIGHT.name()));
            }
        });

        Event event;
        EVENTS(Event event) {
            this.event = event;
        }

        Event getEvent() {return this.event;}
    }

    public static void main(String[] args) {



        /*Event goDown= new Event() {
            @Override
            public void run() {

                PhysicsEngine.goDown(player.getEntityID(), player.getMoveSpeed());
                GraphicsEngine.bindAnimation(player.getEntityID(), player.getAnimations().get(Player.MoveDirection.DOWN.name()));
            }
        };

        Event goLeft= new Event() {
            @Override
            public void run() {

                PhysicsEngine.goLeft(player.getEntityID(), player.getMoveSpeed());
                GraphicsEngine.bindAnimation(player.getEntityID(), player.getAnimations().get(Player.MoveDirection.LEFT.name()));
            }
        };

        Event goRight= new Event() {
            @Override
            public void run() {

                PhysicsEngine.goRight(player.getEntityID(), player.getMoveSpeed());
                GraphicsEngine.bindAnimation(player.getEntityID(), player.getAnimations().get(Player.MoveDirection.RIGHT.name()));
            }
        };*/


        KernelEngine.addEvent("GoUp", EVENTS.GoUp.getEvent());
        IOEngine.bindEventOnLastKey(KeyEvent.VK_UP, "GoUp");

        KernelEngine.addEvent("GoDown", EVENTS.GoDown.getEvent());
        IOEngine.bindEventOnLastKey(KeyEvent.VK_DOWN, "GoDown");

        KernelEngine.addEvent("GoRight", EVENTS.GoRight.getEvent());
        IOEngine.bindEventOnLastKey(KeyEvent.VK_RIGHT, "GoRight");

        KernelEngine.addEvent("GoLeft", EVENTS.GoLeft.getEvent());
        IOEngine.bindEventOnLastKey(KeyEvent.VK_LEFT, "GoLeft");

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
