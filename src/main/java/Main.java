import engines.graphics.*;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        //Transfert du fichier de texture
        //SpriteSheet.upload("sprite_sheet.png", 11, 11, "pacman");

        //Carreau par défaut pour la matrice
        Tile tile = new Tile(30);
        tile.bindColor(1,1,1,1);

        //Création d'une matrice
        TilesMatrix matrix = new TilesMatrix(10,10, tile);
        matrix.setGap(1);

        matrix.get(1, 1).bindColor(1,0,0,1);

        //Création d'une nouvelle scène
        Scene scene = new Scene(600,600);
        //Ajout de la matrice dans la scène
        scene.addEntity(matrix,100,100);

        //Ajout de la scène dans la fenêtre
        Window.addScene(scene, "game_view");
        //Définition du titre
        Window.setTitle("PACMAN");
        //Affichage de la fenêtre
        Window.show();
    }
}
