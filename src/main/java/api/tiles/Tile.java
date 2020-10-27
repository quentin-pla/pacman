package api.tiles;

/**
 * Carreau
 */
public abstract class Tile {
    /**
     * Position horizontale
     */
    protected int x;

    /**
     * Position verticale
     */
    protected int y;

    /**
     * Dimensions
     */
    protected int size;

    /**
     * Constructeur par défaut
     * @param size dimensions
     */
    protected Tile(int size) {
        this.size = size;
    }

    /**
     * Générer le carreau
     * @param x position horizontale
     * @param y position verticale
     */
    public abstract void render(int x, int y);

    // GETTERS & SETTERS //

    public int getSize() {
        return size;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
