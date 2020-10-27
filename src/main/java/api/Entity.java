package api;

/**
 * Entité
 */
public interface Entity {
    /**
     * Dessiner l'entité
     */
    void draw();

    /**
     * Effacer l'entité
     */
    void erase();

    /**
     * Translater l'entité
     * @param x position horizontale
     * @param y position verticale
     */
    void translate(int x, int y);

    // GETTERS & SETTERS //

    Scene getScene();

    void setScene(Scene scene);
}
