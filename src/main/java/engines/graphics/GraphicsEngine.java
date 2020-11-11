package engines.graphics;

import engines.kernel.Entity;

import java.util.HashMap;
import java.util.Set;

public class GraphicsEngine {
    private Set<GraphicEntity> objects;
    private HashMap<Integer, GraphicEntity> id_objects;
    private Entity[][] matrix;
    private Scene scene;


    public GraphicsEngine(Scene scene) {
        this.scene = scene;
    }

    public GraphicsEngine(Scene scene, HashMap<Integer, GraphicEntity> id_objects,  Entity[][] matrix) {
        this.scene = scene;
        this.id_objects = id_objects;
        this.matrix = matrix;
        this.objects.addAll(this.id_objects.values());
    }

    public Set<GraphicEntity> getObjects() {
        return objects;
    }

    public void setObjects(Set<GraphicEntity> objects) {
        this.objects = objects;
    }

    public Entity[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Entity[][] matrix) {
        this.matrix = matrix;
    }

    public Scene getScene() { return this.scene;
    }

    public void setScene(Scene scene) { this.scene = scene; }

    public HashMap<Integer, GraphicEntity> getId_objects() {return this.id_objects; }

    public void setId_objects(HashMap<Integer, GraphicEntity> id_objects) {
        this.id_objects = id_objects;
    }

    public GraphicEntity clone(int id) {
        return new GraphicEntity(this.id_objects.get(id));
    }


    /**
     * Dessine une entité graphique
     * @param id identifiant de l'entité à dessiner
     */
    public void draw(int id) {
        GraphicEntity o = this.id_objects.get(id);
        if (o.getColor() != null) o.getColor().cover(o);
        if (o.getTexture() != null) o.getTexture().cover(o);
    }

    /**
     * Met à jour une entité graphique
     * @param id identifiant de l'entité à mettre à jour
     */
    public void update(int id) {
        GraphicEntity o = this.id_objects.get(id);
        if (o.getTexture() != null) o.getTexture().update();
    }

    /**
     * Supprime une entité graphique
     * @param id identifiant de l'entité à supprimer
     */
    public void erase(int id) {
        this.scene.removeEntity(this.id_objects.get(id));
    }

    /**
     * Redimensionne une entité graphique
     *
     * @param id identifiant de l'entité à redimensionner
     * @param w largeur
     * @param h hauteur
     */
    public void resize(int id, int w, int h) {
        GraphicEntity o = this.id_objects.get(id);
        o.setWidth(w);
        o.setHeight(h);
    }

    /**
     * Redimensionne en hauteur d'une entité graphique
     * @param id identifiant de l'entité à redimensionner
     * @param h hauteur
     */
    public void resizeHeight(int id, int h) {
        GraphicEntity o = this.id_objects.get(id);
        o.setHeight(h);
    }

    /**
     * Redimensionne en largeur d'une entité graphique
     * @param id identifiant de l'entité à redimensionner
     * @param w largeur
     */
    public void resizeWidth(int id, int w) {
        GraphicEntity o = this.id_objects.get(id);
        o.setWidth(w);
    }

    /**
     * Ajoute une couleur à une entité graphique
     * @param id identifiant de l'entité à colorer
     * @param r intensité de rouge
     * @param g intensité de vert
     * @param b intensité de bleu
     */
    public void bindColor(int id, int r, int g, int b) {
        GraphicEntity o = this.id_objects.get(id);
        o.setColor(r,g,b);
    }

    /**
     * Ajoute une couleur à une entité graphique
     * @param id identifiant de l'entité à colorer
     * @param color couleur à ajouter
     */
    public void bindColor(int id, Color color) {
        GraphicEntity o = this.id_objects.get(id);
        o.setColor(color);
    }

    /**
     * Suppression de la couleur d'une entité graphique
     * @param id identifiant de l'entité à décolorer
     */
    public void unbindColor(int id) {
        GraphicEntity o = this.id_objects.get(id);
        o.setColor(null);
    }

    /**
     * Ajoute une texture à une entité graphique
     * @param id identifiant de l'entité à texturer
     * @param texture texture à ajouter
     */
    public void bindTexture(int id, Cover texture) {
        GraphicEntity o = this.id_objects.get(id);
        o.setTexture(texture);
    }

    /**
     * Suppression de la texture d'une entité graphique
     * @param id identifiant de l'entité à détexturer
     */
    public void unbindTexture(int id) {
        GraphicEntity o = this.id_objects.get(id);
        o.setTexture(null);
    }

}
