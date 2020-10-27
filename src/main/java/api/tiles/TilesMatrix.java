package api.tiles;

/**
 * Matrice de carreaux
 */
public class TilesMatrix {
    /**
     * Position horizontale
     */
    private int x;

    /**
     * Position verticale
     */
    private int y;

    /**
     * Espacement entre les carreaux
     */
    private int gap;

    /**
     * Matrice de carreaux
     */
    private Tile[][] matrix;

    /**
     * Constructeur
     * @param matrix matrice
     */
    public TilesMatrix(Tile[][] matrix, int gap) {
        this.matrix = matrix;
        this.gap = gap;
    }

    /**
     * Générer la matrice
     */
    public void render(int x, int y) {
        this.x = x;
        this.y = y;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Tile tile = matrix[i][j];
                tile.render(
                x + (tile.getSize() * i) + (i * gap),
                y + (tile.getSize() * j) + (j * gap)
                );
            }
        }
    }

    // GETTERS & SETTERS //

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Tile[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Tile[][] matrix) {
        this.matrix = matrix;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }
}
