package engines.UI;

/**
 * Bouton
 */
public class Button {
    /**
     * Évènement lié au bouton
     */
    private Runnable event;

    /**
     * Contenu du bouton
     */
    private String content;

    /**
     * Position horizontale
     */
    private int x;

    /**
     * Position verticale
     */
    private int y;

    /**
     * Hauteur
     */
    private int height;

    /**
     * Largeur
     */
    private int width;

    /**
     * Constructeur
     */
    protected Button(String content, Runnable event) {
        this.content = content;
        this.event = event;
    }

    // GETTERS & SETTERS //

    public int getX() {
        return x;
    }

    protected void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    protected void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    protected void setWidth(int width) {
        this.width = width;
    }
}
