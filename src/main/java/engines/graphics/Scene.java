package engines.graphics;

import api.SwingScene;

import java.awt.*;
import java.util.ArrayList;

/**
 * Scène
 */
public class Scene extends SwingScene {
    /**
     * Couleur de fond
     */
    private Color background_color;

    /**
     * Liste des entités présentes dans la scène
     */
    private final ArrayList<GraphicEntity> entities = new ArrayList<>();

    /**
     * Consctructeur par défaut
     * @param height hateur
     * @param width largeur
     */
    protected Scene(int height, int width) {
        super(height, width);
    }

    /**
     * Ajouter une entité à la scène
     * @param entity entité
     */
    protected void addEntity(GraphicEntity entity) {
        entities.add(entity);
        entity.setScene(this);
    }

    /**
     * Supprimer une entité présente sur la scène
     * @param entity entité
     */
    protected void removeEntity(int entity) {
        entities.remove(entity);
    }

    /**
     * Définir la couleur de fond
     * @param color couleur
     */
    protected void setBackgroundColor(Color color) {
        super.setBackgroundColor(color.getRed(), color.getGreen(), color.getBlue());
        background_color = color;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        graphics = (Graphics2D) g;
        for (GraphicEntity entity : entities) {
            GraphicsEngine.update(entity.getId());
            GraphicsEngine.draw(entity.getId());
        }
    }

    // GETTERS //

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ArrayList<GraphicEntity> getEntities() {
        return entities;
    }

    public Color getBackgroundColor() { return background_color; }
}
