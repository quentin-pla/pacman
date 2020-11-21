package engines.graphics;

import api.SwingRenderer;
import engines.kernel.Entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Moteur graphique
 */
public class GraphicsEngine {
    /**
     * Liste des entités graphiques
     */
    private static final Map<Integer, GraphicEntity> entities = new HashMap<>();

    /**
     * Liste des scènes
     */
    private static final Map<Integer, Scene> scenes = new HashMap<>();

    /**
     * Liste des textures transférées
     */
    private static final Map<Integer,Texture> textures = new HashMap<>();

    /**
     * Liste des fichiers de textures transférés
     */
    private static final Map<Integer,SpriteSheet> spriteSheets = new HashMap<>();

    /**
     * Liste des animations
     */
    private static final Map<Integer,SpriteAnimation> animations = new HashMap<>();

    /**
     * Instance unique
     */
    private static GraphicsEngine instance;

    /**
     * Constructeur privé
     */
    private GraphicsEngine() {}

    /**
     * Récupérer l'instance
     * @return instance
     */
    public static GraphicsEngine getInstance() {
        if (instance == null) instance = new GraphicsEngine();
        return instance;
    }

    //-----------------------------------------------//
    //--------- MÉTHODES ENTITÉS GRAPHIQUES ---------//
    //-----------------------------------------------//

    /**
     * Cloner une entité graphique
     * @param entity entité à cloner
     * @return clone
     */
    public static GraphicEntity clone(GraphicEntity entity) {
        return entity.clone();
    }

    /**
     * Dessine une entité graphique
     * @param entity entité à dessiner
     */
    public static void draw(GraphicEntity entity) {
        if (entity.getColor() != null) entity.getColor().cover(entity);
        if (entity.getTexture() != null) entity.getTexture().cover(entity);
    }

    /**
     * Met à jour une entité graphique
     * @param entity entité à mettre à jour
     */
    public static void update(GraphicEntity entity) {
        if (entity.getTexture() != null) entity.getTexture().update();
    }

    /**
     * Supprime une entité graphique de sa scène
     * @param entity entité à supprimer
     */
    public static void erase(GraphicEntity entity) {
        Scene scene = entity.getScene();
        scene.removeEntity(entity);
    }

    /**
     * Ajoute une couleur à une entité graphique
     * @param entity entité à colorer
     * @param r intensité de rouge
     * @param g intensité de vert
     * @param b intensité de bleu
     */
    public static void bindColor(GraphicEntity entity, int r, int g, int b) {
        entity.setColor(new Color(r,g,b));
    }

    /**
     * Ajoute une couleur à une entité graphique
     * @param entity entité à colorer
     * @param color couleur à ajouter
     */
    public static void bindColor(GraphicEntity entity, Color color) {
        entity.setColor(color);
    }

    /**
     * Suppression de la couleur d'une entité graphique
     * @param entity entité à décolorer
     */
    public static void unbindColor(GraphicEntity entity) {
        entity.setColor(null);
    }

    /**
     * Ajoute une texture à une entité graphique
     * @param entity entité à texturer
     * @param textureID identifiant de la texture
     */
    public static void bindTexture(GraphicEntity entity, int textureID) {
        entity.setTexture(textures.get(textureID));
    }

    /**
     * Ajoute une texture à une entité graphique depuis un fichier de textures
     * @param entity entité à texturer
     * @param spriteSheetID identifiant du fichier de textures
     * @param row ligne
     * @param col colonne
     */
    public static void bindTexture(GraphicEntity entity, int spriteSheetID, int row, int col) {
        entity.setTexture(spriteSheets.get(spriteSheetID).getSprite(row, col));
    }

    /**
     * Attache une animation à une entité graphique
     * @param entity entité
     * @param animationID identifiant de l'animation
     */
    public static void bindAnimation(GraphicEntity entity, int animationID) {
        entity.setTexture(animations.get(animationID));
    }

    /**
     * Suppression de la texture d'une entité graphique
     * @param entity entité à détexturer
     */
    public static void unbindTexture(GraphicEntity entity) {
        entity.setTexture(null);
    }

    /**
     * Ajouter une entité à la scène courante
     * @param entity entité
     */
    public static void addToCurrentScene(GraphicEntity entity) {
        Window.getActualScene().addEntity(entity);
    }

    //-----------------------------------------------//
    //------------ MÉTHODES TEXTURES ----------------//
    //-----------------------------------------------//

    /**
     * Transférer une texture
     * @param link lien du fichier
     * @return identifiant de la texture
     */
    public static int loadTexture(String link) {
        if (!SwingRenderer.isTextureLoaded(link)) {
            SwingRenderer.loadTexture(link);
            int id = textures.isEmpty() ? 1 : Collections.max(textures.keySet()) + 1;
            textures.put(id, new Texture(link));
            return id;
        } else {
            try {
                throw new Exception("Texture déjà chargée");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * Transférer un fichier de textures
     * @param link lien du fichier
     * @param height hauteur
     * @param width largeur
     * @return identifiant du fichier de textures
     */
    public static int loadSpriteSheet(String link, int height, int width) {
        if (!SwingRenderer.isTextureLoaded(link)) {
            SwingRenderer.loadSpriteSheet(link, height, width);
            int id = spriteSheets.isEmpty() ? 1 : Collections.max(spriteSheets.keySet()) + 1;
            spriteSheets.put(id, new SpriteSheet(link, height, width));
            return id;
        } else {
            try {
                throw new Exception("Fichier de texture déjà transféré");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * Générer une animation à partir d'un fichier de textures
     * @param spriteSheetID identifiant du fichier de textures
     * @param speed vitesse de l'animation
     * @param looping animation en boucle
     * @return identifiant de l'animation
     */
    public static int generateAnimation(int spriteSheetID, int speed, boolean looping) {
        int id = animations.isEmpty() ? 1 : Collections.max(animations.keySet()) + 1;
        animations.put(id, new SpriteAnimation(spriteSheets.get(spriteSheetID), speed, looping));
        return id;
    }

    /**
     * Ajouter une image à une animation
     * @param id identifiant de l'animation
     * @param row ligne où est située la texture
     * @param col colonne où est située la texture
     */
    public static void addFrameToAnimation(int id, int row, int col) {
        animations.get(id).addFrame(row, col);
    }

    /**
     * Jouer / Arrêter une animation
     * @param id identifiant de l'animation
     */
    public static void playPauseAnimation(int id) {
        animations.get(id).playPause();
    }

    /**
     * Réinitialiser une animation
     * @param id identifiant de l'animation
     */
    public static void resetAnimation(int id) {
        animations.get(id).reset();
    }

    //-----------------------------------------------//
    //----------------MÉTHODES SCÈNE ----------------//
    //-----------------------------------------------//

    /**
     * Générer une nouvelle scène
     * @param height hauteur
     * @param width largeur
     * @return identifiant de la scène
     */
    public static int generateScene(int height, int width) {
        int id = scenes.isEmpty() ? 1 : Collections.max(scenes.keySet()) + 1;
        scenes.put(id, new Scene(height, width));
        return id;
    }

    /**
     * Définir la couleur de fond d'une scène
     * @param id identifiant
     * @param r intensité rouge
     * @param g intensité vert
     * @param b intensité bleu
     */
    public static void setSceneBackgroundColor(int id, int r, int g, int b) {
        scenes.get(id).setBackgroundColor(new Color(r,g,b));
    }

    /**
     * Attacher une scène à la fenêtre
     * @param id id de la scène
     */
    public static void bindScene(int id) {
        if (!scenes.containsKey(id)) {
            try {
                throw new Exception("Scène introuvable");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        Window.bindScene(scenes.get(id));
    }

    //-----------------------------------------------//
    //----------------MÉTHODES FENETRE ----------------//
    //-----------------------------------------------//

    /**
     * Afficher la fenêtre
     */
    public static void showWindow() {
        Window.show();
    }

    /**
     * Rafraichir la fenêtre
     */
    public static void refreshWindow() { Window.refresh(); }

    /**
     * Arrêter la fenêtre
     */
    public static void stopWindow() {
        Window.stop();
    }

    /**
     * Créer une nouvelle entité
     * @param parent entité parente
     * @return entité graphique
     */
    public GraphicEntity createEntity(Entity parent) {
        GraphicEntity entity = new GraphicEntity(parent);
        entities.put(parent.getId(), entity);
        return entity;
    }

    // GETTERS & SETTERS //

    public static Texture getTexture(int id) { return textures.get(id); }

    public static SpriteAnimation getAnimation(int id) { return animations.get(id); }

    public static Scene getScene(int id) { return scenes.get(id); }

    public static Map<Integer, Scene> getScenes() { return scenes; }
}
