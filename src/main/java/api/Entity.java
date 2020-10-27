package api;

/**
 * Entité
 */
public interface Entity {
    /**
     * Générer l'entité à une certaine position
     * @param x position horizontale
     * @param y position verticale
     */
    public void render(int x, int y);
}
