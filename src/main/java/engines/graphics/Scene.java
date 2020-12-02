package engines.graphics;

import api.SwingScene;

import java.awt.*;
import java.util.ArrayList;

/**
 * Scène
 */
public class Scene extends SwingScene {
    /**
     * Moteur graphique
     */
    private final GraphicsEngine graphicsEngine;

    /**
     * Liste des entités présentes dans la scène
     */
    private final ArrayList<GraphicEntity> entities = new ArrayList<>();

    /**
     * Consctructeur par défaut
     * @param height hateur
     * @param width largeur
     */
    protected Scene(GraphicsEngine graphicsEngine, int height, int width) {
        super(height, width);
        this.graphicsEngine = graphicsEngine;
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
    protected void removeEntity(GraphicEntity entity) {
        entities.remove(entity);
    }

    /**
     * Définir la couleur de fond
     * @param color couleur
     */
    protected void setBackgroundColor(Color color) {
        super.setBackgroundColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        graphics = (Graphics2D) g.create();
        graphics.setColor(backgroundColor);
        graphics.fillRect(0, 0, width, height);
        for (GraphicEntity entity : entities)
            graphicsEngine.draw(entity.getParent());
        graphics.dispose();
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
}
