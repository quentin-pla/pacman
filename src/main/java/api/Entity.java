package api;

/**
 * Entité
 */
public interface Entity {
    /**
     * Générer l'entité
     */
    public void render();

    /**
     * Définir sa position
     * @param x position horizontale
     * @param y position verticale
     */
    public void setPosition(int x, int y);
}
