package api;

import java.util.ArrayList;

/**
 * Scène
 */
public class Scene {
    /**
     * Hauteur
     */
    private int height;

    /**
     * Largeur
     */
    private int width;

    /**
     * Liste des entités présentes dans la scène
     */
    private ArrayList<Entity> entities;

    /**
     * Consctructeur par défaut
     * @param height hateur
     * @param width largeur
     */
    public Scene(int height, int width) {
        this.height = height;
        this.width = width;
        this.entities = null;
    }

    /**
     * Constructeur surchargé
     * @param height hauteur
     * @param width largeur
     * @param entities liste des entités
     */
    public Scene(int height, int width, ArrayList<Entity> entities) {
        this(height, width);
        this.entities = entities;
    }

    /**
     * Ajouter une entité à la scène
     * @param entity entité
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Supprimer une entité présente sur la scène
     * @param entity entité
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    // GETTERS

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
