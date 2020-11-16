package engines.physics;

import engines.kernel.Engine;
import engines.kernel.Entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Moteur physique
 */
public class PhysicsEngine implements Engine<PhysicEntity> {
    /**
     * Liste des entités physiques
     */
    private static final HashMap<Integer, PhysicEntity> entities = new HashMap<>();

    /**
     * Matrice
     */
    private static Integer[][] matrix = new Integer[300][300];

    /**
     * Instance unique
     */
    private static PhysicsEngine instance;

    /**
     * Constructeur privé
     */
    private PhysicsEngine() {}

    /**
     * Récupérer l'instance
     * @return instance
     */
    public static PhysicsEngine getInstance() {
        if (instance == null) instance = new PhysicsEngine();
        return instance;
    }

    /**
     * Ajouter des collisions entre deux entités
     * @param id1 identifiant entité 1
     * @param id2 identifiant entité 2
     */
    public static void addCollisions(int id1, int id2) {
        entities.get(id1).getCollisions().add(id2);
        entities.get(id2).getCollisions().add(id1);
    }

    /**
     * Ajouter des limites de déplacement
     * @param id identifiant de l'entité
     */
    public static void addBoundLimits(int id, int x1, int y1, int x2, int y2) {
        entities.get(id).setBoundLimits(new int[]{x1,y1,x2,y2});
    }

    /**
     * Vérifier qu'il n'y a pas d'objet à la position (x,y)
     * @param x Position x à vérifier
     * @param y Position y à vérifier
     * @return booléen
     */
    public static boolean isEntityPresent(int x, int y) {
        return matrix[x][y] == null;
    }

    /**
     * Vérifier si une entité est en collision
     * @param id identifiant de l'entité
     * @return s'il y a une collision
     */
    public static boolean isInCollision(int id) {
        PhysicEntity o = entities.get(id);
        for (int collisionID : o.getCollisions())
            if (isCollision(id,collisionID))
                return true;
        return false;
    }

    /**
     * Vérifier s'il y a une collision entre deux entités
     * @param id1 identifiant de l'entité 1
     * @param id2 identifiant de l'entité 2
     * @return s'il y a collision
     */
    public static boolean isCollision(int id1, int id2) {
        PhysicEntity o1 = entities.get(id1);
        PhysicEntity o2 = entities.get(id2);
        return o1.getX() + o1.getWidth() > o2.getX()
            && o1.getY() + o1.getHeight() > o2.getY()
            && o1.getX() < o2.getX() + o2.getWidth()
            && o1.getY() < o2.getY() + o2.getHeight();
    }

    /**
     * Vérifier si une entité est comprise dans une autre entité
     * @param childID identifiant de l'entité enfant
     * @param parentID identifiant de l'entité parente
     * @return si l'enfant est dans le parent
     */
    public static boolean isInside(int childID, int parentID) {
        PhysicEntity child = entities.get(childID);
        PhysicEntity parent = entities.get(parentID);
        return ((child.getX() > parent.getX()
                && child.getY() > parent.getY())
            && ((child.getX() + child.getWidth() < parent.getX() + parent.getWidth()
                && child.getY() > parent.getY()))
            && ((child.getX() > parent.getX()
                && child.getY() + child.getHeight() < parent.getY() + parent.getHeight()))
            && ((child.getX() + child.getWidth() < parent.getX() + parent.getWidth()
                && child.getY() + child.getHeight() < parent.getY() + parent.getHeight())));
    }

    /**
     * Vérifier si une entité est bien dans ses limites de déplacement
     * @param id identifiant
     * @return si l'entité respecte ses limites de déplacement
     */
    public static boolean isInBounds(int id) {
        PhysicEntity entity = entities.get(id);
        int[] boundLimits = entity.getBoundLimits();
        return entity.getX() >= boundLimits[0]
            && entity.getX() + entity.getWidth() <= boundLimits[2]
            && entity.getY() >= boundLimits[1]
            && entity.getY() + entity.getHeight() <= boundLimits[3];
    }

    /**
     * ?
     * @param id identifiant
     * @param x position horizontale
     * @param y position verticale
     * @param mul multiplicateur
     * @param dir direction
     * @return
     */
    public ArrayList<Integer> checkPath(int id, int x, int y, int mul, String dir) {
        ArrayList<Integer> path = new ArrayList<>();
        switch (dir) {
            case "UP":
                for (int i = x; i < x + entities.get(id).getWidth(); i++) {
                    for (int j = y; j > (y-mul); j-- ) {
                        if (!path.contains(matrix[i][j]))
                            path.add(matrix[i][j]);
                    }
                }
            case "DOWN":
                int posy = y+entities.get(id).getHeight();
                for (int i = x; i < x + entities.get(id).getWidth(); i++) {
                    for (int j = posy ; j < (posy+mul); j++ ) {
                        if (!path.contains(matrix[i][j]))
                            path.add(matrix[i][j]);
                    }
                }
            case "LEFT":
                for (int j = y; j < y + entities.get(id).getHeight(); j++) {
                    for (int i = x; i > (x-mul); i-- ) {
                        if (!path.contains(matrix[i][j]))
                            path.add(matrix[i][j]);
                    }
                }
            case "RIGHT":
                int posx = x+entities.get(id).getWidth();
                for (int j = y; j < y + entities.get(id).getHeight(); j++) {
                    for (int i = posx ; i < (posx+mul); i++ ) {
                        if (!path.contains(matrix[i][j]))
                            path.add(matrix[i][j]);
                    }
                }

        }
        return path;
    }

    /**
     * Déplacement vers le haut
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public static void goUp(int id, int mul) {
        PhysicEntity o = entities.get(id);
        o.setY(o.getY() - mul);
        if (isInCollision(id) || !isInBounds(id))
            o.setY(o.getY() + mul);
    }

    /**
     * Déplacement vers la droite
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public static void goRight(int id, int mul) {
        PhysicEntity o = entities.get(id);
        o.setX(o.getX() + mul);
        if (isInCollision(id) || !isInBounds(id))
            o.setX(o.getX() - mul);
    }

    /**
     * Déplacement vers la gauche
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public static void goLeft(int id, int mul) {
        PhysicEntity o = entities.get(id);
        o.setX(o.getX() - mul);
        if (isInCollision(id) || !isInBounds(id))
            o.setX(o.getX() + mul);
    }

    /**
     * Déplacement vers le bas
     * @param id identifiant de l'entité physique
     * @param mul multiplicateur
     */
    public static void goDown(int id, int mul) {
        PhysicEntity o = entities.get(id);
        o.setY(o.getY() + mul);
        if (isInCollision(id) || !isInBounds(id))
            o.setY(o.getY() - mul);
    }

    /**
     * Déplacement de l'entité physique à la position indiquée
     * @param id identifiant de l'entité physique
     * @param x position x
     * @param y position y
     */
    public static void move(int id, int x, int y) {
        PhysicEntity o = entities.get(id);
        o.setX(x);
        o.setY(y);
    }

    /**
     * Translater une entité physique
     * @param id identifiant
     * @param x position horizontale
     * @param y position verticale
     */
    public static void translate(int id, int x, int y) {
        PhysicEntity o = entities.get(id);
        o.setX(o.getX() + x);
        o.setY(o.getY() + y);
    }

    /**
     * Modification de la  vitesse de déplacement
     * @param id identifiant de l'entité physique
     * @param speed vitesse
     */
    public static void setSpeed(int id, int speed) {
        PhysicEntity o = entities.get(id);
        o.setSpeed(speed);
    }

    /**
     * Obtenir les limites
     * @param id identifiant
     */
    public static int[] getBounds(int id) {
        return entities.get(id).getBounds();
    }

    @Override
    public PhysicEntity createEntity(Entity parent) {
        PhysicEntity entity = new PhysicEntity(parent);
        entities.put(parent.getId(), entity);
        return entity;
    }

    // GETTERS //

    @Override
    public Map<Integer, PhysicEntity> getEntities() { return entities; }

    @Override
    public PhysicEntity getEntity(int id) { return entities.get(id); }
}
