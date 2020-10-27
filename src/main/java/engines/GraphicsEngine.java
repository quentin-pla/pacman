package engines;

import api.Matrix;
import api.Texture;
import api.Tile;

/**
 * Moteur graphique
 */
public class GraphicsEngine {
    /**
     * Instance
     */
    private static GraphicsEngine instance;

    /**
     * Constructeur
     */
    private GraphicsEngine() {}

    /**
     * Obtenir l'instance
     * @return instance
     */
    public static GraphicsEngine get() {
        if (instance == null) instance = new GraphicsEngine();
        return instance;
    }

    /**
     * Générer les graphismes
     */
    public static void render() {
        Texture texture = new Texture("pacman.png");
        Tile[][] tiles = new Tile[10][10];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                tiles[i][j] = new Tile(20, texture);
        Matrix matrix = new Matrix(tiles, 0);
        renderMatrix(0,0, matrix);
    }

    /**
     * Afficher un carreau
     * @param x position horizontale
     * @param y position verticale
     * @param tile carreau
     */
    public static void renderTile(int x, int y, Tile tile) {
        tile.render(x,y);
    }

    /**
     * Générer une matrice
     * @param x position horizontale
     * @param y position verticale
     * @param matrix matrice de carreaux
     */
    public static void renderMatrix(int x, int y, Matrix matrix) {
        matrix.render(x,y);
    }
}
