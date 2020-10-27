package api.tiles;

import api.Entity;
import api.Scene;

/**
 * Matrice de carreaux
 */
public class TilesMatrix implements Entity {
    /**
     * Scène dans laquelle il se situe
     */
    protected Scene scene;

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
        this.x = 0;
        this.y = 0;
    }

    @Override
    public void draw() {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                Tile tile = matrix[i][j];
                tile.translate(x + (tile.getSize() * i) + (i * gap), y + (tile.getSize() * j) + (j * gap));
                tile.draw();
            }
        }
    }

    @Override
    public void erase() {
        scene.removeEntity(this);
    }

    @Override
    public void translate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // GETTERS & SETTERS //

    @Override
    public Scene getScene() { return scene; }

    @Override
    public void setScene(Scene scene) { this.scene = scene; }

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
