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
    private ArrayList<GraphicsEntity> entities = new ArrayList<>();

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
     * @param graphicsEntity entité
     * @param x position horizontale
     * @param y position verticale
     */
    public void addEntity(GraphicsEntity graphicsEntity, int x, int y) {
        entities.add(graphicsEntity);
        graphicsEntity.setScene(this);
        graphicsEntity.move(x, y);
    }

    /**
     * Supprimer une entité présente sur la scène
     * @param entity entité
     */
    public void removeEntity(Entity entity) {
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        graphics = (Graphics2D) g;
        for (GraphicsEntity graphicsEntity : entities) {
            graphicsEntity.update();
            graphicsEntity.draw();
        }
    }

    // GETTERS //

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public ArrayList<GraphicsEntity> getEntities() {
        return entities;
    }

    public Color getBackgroundColor() { return background_color; }
}
