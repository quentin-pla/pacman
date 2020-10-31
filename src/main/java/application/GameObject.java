package application;

import engines.graphics.Tile;

public abstract class GameObject extends Tile {
    /**
     * Constructeur avec dimensions
     * @param height hauteur
     * @param width largeur
     */
    public GameObject(int height, int width) {
        super(height, width);
    }

    /**
     * Constructeur avec taille
     * @param size taille
     */
    public GameObject(int size) {
        super(size);
    }

    /**
     * Constructeur par clonage
     * @param clone clone
     */
    public GameObject(Tile clone) {
        super(clone);
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
