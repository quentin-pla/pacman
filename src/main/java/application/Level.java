package application;

import engines.graphics.TilesMatrix;

/**
 * Niveau de jeu
 */
public class Level {
    /**
     * Matrice
     */
    private TilesMatrix matrix;

    /**
     * Chronomètre
     */
    private float timer;

    /**
     * Constructeur
     * @param height hauteur
     * @param width largeur
     * @param max_time temps maximum
     */
    public Level(int height, int width, int max_time) {
        this.matrix = new TilesMatrix(height, width, 30);
        this.timer = max_time;
    }
}
