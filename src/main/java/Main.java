

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
//        //Transfert des textures
//        SpriteSheet sprite_sheet = SpriteSheet.load("sprite_sheet.png", 11, 11);
//        Sprite pacman_skin = sprite_sheet.getSprite(1,3);
//
//        //Sol du niveau
//        GraphicEntity floor = new GraphicEntity();
//        floor.resize(30,30);
//        floor.bindColor(0,0,255);
//
//        //Niveau
//        Level level = new Level(10,10, floor);
//
//        //Carreau 0 0
//        Entity carreau00 = level.getMatrix()[0][0];
//        carreau00.bindColor(255,0,0);
//
//        //Création d'un joueur
//        Player pacman = new Player(30,30,2,pacman_skin);
//
//        //Création des animations de mouvement
//        SpriteAnimation move = new SpriteAnimation(sprite_sheet,5,true);
//        move.addFrame(1, 3);
//        SpriteAnimation move_up = move.clone();
//        move_up.addFrame(1, 7);
//        move_up.addFrame(1, 6);
//        SpriteAnimation move_right = move.clone();
//        move_right.addFrame(1, 2);
//        move_right.addFrame(1, 1);
//        SpriteAnimation move_down = move.clone();
//        move_down.addFrame(1, 9);
//        move_down.addFrame(1, 8);
//        SpriteAnimation move_left = move.clone();
//        move_left.addFrame(1, 4);
//        move_left.addFrame(1, 5);
//
//        //Ajout des animations de mouvement
//        pacman.animateMove(Player.MoveDirection.UP, move_up);
//        pacman.animateMove(Player.MoveDirection.RIGHT, move_right);
//        pacman.animateMove(Player.MoveDirection.DOWN, move_down);
//        pacman.animateMove(Player.MoveDirection.LEFT, move_left);
//
//        //Apparition du joueur
//        level.addPlayer(pacman, 0,0);
//
//        //Création d'une nouvelle scène
//        Scene scene = new Scene(380,360, new Color(0,0,0));
//        //Ajout du niveau dans la scène
//        scene.addEntity(level,30,30);
//
//        //Ajout de la scène dans la fenêtre
//        Window.addScene(scene,"game_view");
//        //Définition du titre
//        Window.setTitle("PACMAN");
//        //Affichage de la fenêtre
//        Window.show();
    }
}
