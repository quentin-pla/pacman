package application;

import engines.graphics.Entity;

public abstract class GameObject extends Entity {
    /**
     * Constructeur avec dimensions
     * @param height hauteur
     * @param width largeur
     */
    public GameObject(int height, int width) {
        super(height, width);
    }

    /**
     * Cloner un objet de jeu
     */
    public GameObject clone() {
        return (GameObject) super.clone();
    }

    /**
     * Téléporter l'objet à une position spécifique
     */
    public void teleport(int x, int y) {
        super.move(x, y);
    }

    /**
     * Translater l'objet
     * @param x nombre à additionner à la position horizontale
     * @param y nombre à additionner à la position verticale
     */
    public void translate(int x, int y) {
        super.translate(x,y);
    }

    /**
     * Supprimer l'objet
     */
    public void remove() {
        super.erase();
    }
}
