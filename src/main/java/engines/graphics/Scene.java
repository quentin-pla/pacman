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
    private ArrayList<GraphicEntity> entities = new ArrayList<>();

    /**
     * Consctructeur par défaut
     * @param height hateur
     * @param width largeur
     * @param background_color couleur de fond
     */
    public Scene(int height, int width, Color background_color) {
        super(height, width);
        setBackgroundColor(background_color);
    }

    /**
     * Ajouter une entité à la scène
     * @param entity entité
     * @param x position horizontale
     * @param y position verticale
     */
    public void addEntity(GraphicEntity entity, int x, int y) {
        entities.add(entity);
        entity.setScene(this);
        entity.move(x, y);
    }

    /**
     * Supprimer une entité présente sur la scène
     * @param entity entité
     */
    public void removeEntity(GraphicEntity entity) {
        entities.remove(entity);
    }

    /**
     * Définir la couleur de fond
     * @param color couleur
     */
    public void setBackgroundColor(Color color) {
        super.setBackgroundColor(color.getRed(), color.getGreen(), color.getBlue());
        background_color = color;
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        graphics = (Graphics2D) g;
        for (GraphicEntity entity : entities) {
            entity.update();
            entity.draw();
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
