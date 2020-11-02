package engines.graphics;

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
    private ArrayList<Entity> entities = new ArrayList<>();

    /**
     * Consctructeur par défaut
     * @param height hateur
     * @param width largeur
     */
    protected Scene(int height, int width) {
        this.height = height;
        this.width = width;
    }

    /**
     * Ajouter une entité à la scène
     * @param entity entité
     * @param x position horizontale
     * @param y position verticale
     */
    protected void addEntity(Entity entity, int x, int y) {
        entities.add(entity);
        entity.setScene(this);
        entity.move(x, y);
    }

    /**
     * Supprimer une entité présente sur la scène
     * @param entity entité
     */
    protected void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Générer la scène
     */
    protected void render() {
        for (Entity entity : entities) {
            entity.update();
            entity.draw();
        }
    }

    // GETTERS //

    protected int getHeight() {
        return height;
    }

    protected int getWidth() {
        return width;
    }

    protected ArrayList<Entity> getEntities() {
        return entities;
    }
}
