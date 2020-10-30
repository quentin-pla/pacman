package engines.graphics;

import java.util.Arrays;

/**
 * Matrice de carreaux
 */
public class TilesMatrix extends Entity {
    /**
     * Espacement (entre carreaux)
     */
    private int gap;

    /**
     * Matrice
     */
    private Tile[][] matrix;

    /**
     * Constructeur
     * @param height hauteur
     * @param width largeur
     * @param tile_size taille carreau
     */
    public TilesMatrix(int height, int width, int tile_size) {
        super(0,0);
        this.matrix = new Tile[width][height];
        this.gap = 0;
        fill(new Tile(tile_size));
    }

    /**
     * Remplir la matrice
     * @param tile carreau
     */
    public void fill(Tile tile) {
        for (Tile[] tiles : matrix) Arrays.fill(tiles, tile);
    }

    /**
     * Obtenir un carreau spécifique
     * @param row ligne
     * @param column colonne
     */
    public Tile get(int row, int column) {
        return matrix[row][column];
    }

    @Override
    protected void draw() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Tile tile = matrix[i][j];
                tile.translate(x + (tile.getSize() * i) + (i * gap), y + (tile.getSize() * j) + (j * gap));
                tile.draw();
            }
        }
    }

    // GETTERS & SETTERS //

    protected Tile[][] getMatrix() {
        return matrix;
    }

    protected void setMatrix(Tile[][] matrix) {
        this.matrix = matrix;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }
}
