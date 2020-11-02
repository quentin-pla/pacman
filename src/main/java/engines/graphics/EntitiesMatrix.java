package engines.graphics;

/**
 * Matrice de carreaux
 */
public class EntitiesMatrix extends Entity {
    /**
     * Matrice
     */
    private Entity[][] matrix;

    /**
     * Constructeur
     * @param rows lignes
     * @param columns colonnes
     * @param entity carreau
     */
    public EntitiesMatrix(int rows, int columns, Entity entity) {
        super(entity.getHeight() * (rows - 1), entity.getWidth() * (columns - 1));
        this.matrix = new Entity[rows][columns];
        fill(entity);
    }

    /**
     * Remplir la matrice
     * @param entity carreau
     */
    public void fill(Entity entity) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = entity.clone();
                matrix[row][col].setX((entity.getWidth() * row));
                matrix[row][col].setY((entity.getHeight() * col));
            }
        }
    }

    /**
     * Obtenir un carreau spécifique
     * @param row ligne
     * @param column colonne
     */
    public Entity get(int row, int column) {
        return matrix[row][column];
    }

    @Override
    protected void draw() {
        for (Entity[] entities : matrix)
            for (Entity entity : entities)
                entity.draw();
    }

    @Override
    public void update() {
        for (Entity[] entities : matrix)
            for (Entity entity : entities)
                entity.update();
    }

    @Override
    protected void translate(int x, int y) {
        super.translate(x, y);
        for (Entity[] entities : matrix)
            for (Entity entity : entities)
                entity.translate(x, y);
    }

    @Override
    protected void move(int x, int y) {
        super.move(x, y);
        for (Entity[] entities : matrix)
            for (Entity entity : entities)
                entity.move(x + entity.getX(), y + entity.getY());
    }

    // GETTERS //

    protected Entity[][] getMatrix() {
        return matrix;
    }
}
