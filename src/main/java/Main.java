import engines.graphics.*;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        //Transfert du fichier de texture
        SpriteSheet.upload("sprite_sheet.png", 11, 11, "sprite");

        //Carreau par défaut pour la matrice
        Tile tile = new Tile(30);
        tile.bindColor(1,0,0,1);

        //Création d'une matrice
        TilesMatrix matrix = new TilesMatrix(10,10, tile);

        Tile pacman = new Tile(30);
        pacman.bindSpriteSheet(SpriteSheet.get("sprite"),1,2);

        //Création d'une nouvelle scène
        Scene scene = new Scene(600,600);
        //Ajout des éléments dans la scène
        scene.addEntity(matrix,100,100);
        scene.addEntity(pacman,150,150);

        //Ajout de la scène dans la fenêtre
        Window.addScene(scene, "game_view");
        //Définition du titre
        Window.setTitle("PACMAN");
        //Affichage de la fenêtre
        Window.show();
    }
}
