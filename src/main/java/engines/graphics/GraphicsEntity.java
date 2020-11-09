package engines.graphics;

/**
 * Entité graphique
 */
public interface GraphicsEntity {
    /**
     * Générer une nouvelle entité
     * @return entité graphique
     */
    static GraphicsObject generateEntity() {
        return new GraphicsObject();
    }

    /**
     * Dessiner l'entité
     */
    void draw();

    /**
     * Mettre à jour l'entité
     */
    void update();

    /**
     * Effacer l'entité
     */
    void erase();

    /**
     * Translater l'entité
     * @param x nombre à additionner à la position horizontale
     * @param y nombre à additionner à la position verticale
     */
    void translate(int x, int y);

    /**
     * Déplacer l'entité
     * @param x nouvelle position horizontale
     * @param y nouvelle position verticale
     */
    void move(int x, int y);

    /**
     * Redimensionner l'entité
     * @param height hauteur
     * @param width largeur
     */
    void resize(int height, int width);

    /**
     * Attacher une couleur
     * @param red intensité rouge
     * @param green intensité vert
     * @param blue intensité bleu
     */
    void bindColor(int red, int green, int blue);

    /**
     * Détacher la couleur
     */
    void unbindColor();

    /**
     * Attacher une texture
     * @param texture texture
     */
    void bindTexture(Cover texture);

    /**
     * Détacher la texture
     */
    void unbindTexture();

    // GETTERS & SETTERS //

    Scene getScene();

    void setScene(Scene scene);

    int getHeight();

    int getWidth();

    int getX();

    int getY();

    Color getColor();

    Cover getTexture();
}
