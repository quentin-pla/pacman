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


    public void draw(int id) {
        GraphicEntity o = this.id_objects.get(id);
        if (o.getColor() != null) o.getColor().cover(o);
        if (o.getTexture() != null) o.getTexture().cover(o);
    }

    public void update(int id) {
        GraphicEntity o = this.id_objects.get(id);
        if (o.getTexture() != null) o.getTexture().update();
    }

    public void erase(int id) {
        this.scene.removeEntity(this.id_objects.get(id));
    }

    public void resize(int id, int w, int h) {
        GraphicEntity o = this.id_objects.get(id);
        o.setWidth(w);
        o.setHeight(h);
    }

    public void resizeHeight(int id, int h) {
        GraphicEntity o = this.id_objects.get(id);
        o.setHeight(h);
    }

    public void resizeWidth(int id, int w) {
        GraphicEntity o = this.id_objects.get(id);
        o.setWidth(w);
    }

    public void bindColor(int id, int r, int g, int b) {
        GraphicEntity o = this.id_objects.get(id);
        o.setColor(r,g,b);
    }

    public void bindColor(int id, Color color) {
        GraphicEntity o = this.id_objects.get(id);
        o.setColor(color);
    }

    public void unbindColor(int id) {
        GraphicEntity o = this.id_objects.get(id);
        o.setColor(null);
    }

    public void bindTexture(int id, Cover texture) {
        GraphicEntity o = this.id_objects.get(id);
        o.setTexture(texture);
    }

    public void unbindTexture(int id) {
        GraphicEntity o = this.id_objects.get(id);
        o.setTexture(null);
    }

}
