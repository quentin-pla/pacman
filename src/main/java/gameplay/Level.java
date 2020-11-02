package gameplay;

import engines.graphics.EntitiesMatrix;
import engines.graphics.Entity;
import engines.graphics.Scene;

import java.util.ArrayList;

/**
 * Niveau de jeu
 */
public class Level extends EntitiesMatrix {
    /**
     * Entités présentes sur le niveau
     */
    private ArrayList<Entity> game_objects = new ArrayList<>();

    /**
     * Joueur
     */
    private Player player;

    /**
     * Chronomètre
     */
    private float timer;

    /**
     * Score actuel
     */
    private int actual_score;

    /**
     * Constructeur
     * @param rows nombre de lignes
     * @param cols nombre de colonnes
     */
    public Level(int rows, int cols) {
        super(rows,cols, new Entity(30,30));
    }

    /**
     * Constructeur surchargé
     * @param rows nombre de lignes
     * @param cols nombre de colonnes
     * @param floor sol par défaut
     */
    public Level(int rows, int cols, Entity floor) {
        super(rows,cols, floor);
    }

    /**
     * Faire apparaitre le joueur à un endroit
     * @param player joueur
     * @param row ligne
     * @param col colonne
     */
    public void addPlayer(Player player, int row, int col) {
        player.addMoveBounds(getBounds());
        Entity entity = matrix[row][col];
        player.move(entity.getX(), entity.getY());
        this.player = player;
        game_objects.add(player);
    }

    @Override
    public void setScene(Scene scene) {
        super.setScene(scene);
        //Ajout des objets du niveau à la scène
        for (Entity entity : game_objects)
            scene.addEntity(entity, entity.getX(), entity.getY());
    }

    @Override
    public void translate(int x, int y) {
        super.translate(x, y);
        //Translation des objets du niveau
        for (Entity entity : game_objects) entity.translate(x, y);
        //Mise à jour des limites de déplacement pour le joueur
        if (player != null) player.addMoveBounds(getBounds());
    }

    @Override
    public void move(int x, int y) {
        super.move(x,y);
        //Translation des objets du niveau
        for (Entity entity : game_objects) entity.translate(x, y);
        //Mise à jour des limites de déplacement pour le joueur
        if (player != null) player.addMoveBounds(getBounds());
    }

    /**
     * Récupérer les limites du niveau
     * @return limites
     */
    public int[] getBounds() { return new int[]{x, y, x + width, y + height}; }
}
