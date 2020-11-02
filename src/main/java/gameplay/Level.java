package gameplay;

import engines.graphics.Entity;
import engines.graphics.GraphicsEngine;
import engines.graphics.Scene;
import engines.graphics.EntitiesMatrix;

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
        super(rows,cols, GraphicsEngine.newEntity(30,30));
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
        Entity entity = getMatrix()[row][col];
        GraphicsEngine.moveEntity(player, entity.getX(), entity.getY());
        this.player = player;
        game_objects.add(player);
    }

    @Override
    protected void setScene(Scene scene) {
        super.setScene(scene);
        //Ajout des objets du niveau à la scène
        for (Entity entity : game_objects)
            GraphicsEngine.addEntityToCurrentScene(entity, entity.getX(), entity.getY());
    }

    @Override
    protected void translate(int x, int y) {
        super.translate(x, y);
        //Translation des objets du niveau
        for (Entity entity : game_objects) GraphicsEngine.translateEntity(entity, x, y);
        //Mise à jour des limites de déplacement pour le joueur
        if (player != null) player.addMoveBounds(getBounds());
    }

    @Override
    protected void move(int x, int y) {
        super.move(x,y);
        //Translation des objets du niveau
        for (Entity entity : game_objects) GraphicsEngine.translateEntity(entity, x, y);
        //Mise à jour des limites de déplacement pour le joueur
        if (player != null) player.addMoveBounds(getBounds());
    }

    /**
     * Récupérer les limites du niveau
     * @return limites
     */
    public int[] getBounds() { return new int[]{x, y, x + width, y + height}; }
}
