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
        this.entities = new ArrayList<>();
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
     * @param x position horizontale
     * @param y position verticale
     */
    public void addEntity(Entity entity, int x, int y) {
        entity.setScene(this);
        entity.translate(x, y);
        entities.add(entity);
    }

    /**
     * Supprimer une entité présente sur la scène
     * @param entity entité
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Générer la scène
     */
    public void render() {
        for (Entity entity : entities) entity.draw();
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
