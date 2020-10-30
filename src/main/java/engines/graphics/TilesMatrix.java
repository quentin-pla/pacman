package engines.graphics;

/**
 * Matrice de carreaux
 */
public class TilesMatrix extends Entity {
    /**
     * Matrice
     */
    private Tile[][] matrix;

    /**
     * Constructeur
     * @param rows lignes
     * @param columns colonnes
     * @param tile carreau
     */
    public TilesMatrix(int rows, int columns, Tile tile) {
        super(0,0);
        this.matrix = new Tile[rows][columns];
        fill(tile);
    }

    /**
     * Remplir la matrice
     * @param tile carreau
     */
    public void fill(Tile tile) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = new Tile(tile);
                matrix[row][col].setX((tile.getSize() * row));
                matrix[row][col].setY((tile.getSize() * col));
            }
        }
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
        for (Tile[] tiles : matrix)
            for (Tile tile : tiles)
                tile.draw();
    }

    @Override
    protected void translate(int x, int y) {
        super.translate(x, y);
        for (Tile[] tiles : matrix)
            for (Tile tile : tiles)
                tile.translate(x + tile.getX(), y + tile.getY());
    }

    // GETTERS & SETTERS //

    protected Tile[][] getMatrix() {
        return matrix;
    }

    protected void setMatrix(Tile[][] matrix) {
        this.matrix = matrix;
    }
}
