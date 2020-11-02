import application.Level;
import application.Player;
import engines.graphics.*;
import static engines.graphics.GraphicsEngine.*;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        //Transfert des textures
        SpriteSheet sprite_sheet = loadSpriteSheet("sprite_sheet.png", 11, 11);
        Sprite coin = newSprite(sprite_sheet,10,2);
        Sprite pacman_skin = newSprite(sprite_sheet,1,3);

        //Sol du niveau
        Entity floor = newEntity(30, 30);
        colorEntity(floor,0,0,1,1);
        dressEntity(floor,coin);

        //Niveau
        Level level = new Level(10,10, floor);

        //Création d'un joueur
        Player pacman = new Player(30,30,2,pacman_skin);

        //Création des animations de mouvement
        SpriteAnimation move = newAnimation(sprite_sheet,5,true);
        addFrameToAnimation(move, 1, 3);
        SpriteAnimation move_up = cloneAnimation(move);
        addFrameToAnimation(move_up, 1, 7);
        addFrameToAnimation(move_up, 1, 6);
        SpriteAnimation move_right = cloneAnimation(move);
        addFrameToAnimation(move_right, 1, 2);
        addFrameToAnimation(move_right, 1, 1);
        SpriteAnimation move_down = cloneAnimation(move);
        addFrameToAnimation(move_down, 1, 9);
        addFrameToAnimation(move_down, 1, 8);
        SpriteAnimation move_left = cloneAnimation(move);
        addFrameToAnimation(move_left, 1, 4);
        addFrameToAnimation(move_left, 1, 5);

        //Ajout des animations de mouvement
        pacman.animateMove(Player.MoveDirection.UP, move_up);
        pacman.animateMove(Player.MoveDirection.RIGHT, move_right);
        pacman.animateMove(Player.MoveDirection.DOWN, move_down);
        pacman.animateMove(Player.MoveDirection.LEFT, move_left);

        //Apparition du joueur
        level.addPlayer(pacman, 0,0);

        //Création d'une nouvelle scène
        GraphicsEngine.addScene("game_view",600,600);
        //Ajout du niveau dans la scène
        GraphicsEngine.addEntityToCurrentScene(level,50,50);
        //Définition du titre
        GraphicsEngine.setWindowTitle("PACMAN");
        //Affichage de la fenêtre
        GraphicsEngine.showWindow();
    }
}
