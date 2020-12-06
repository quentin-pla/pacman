package engines.graphics;

import api.SwingRenderer;
import engines.kernel.Entity;
import engines.kernel.EventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Moteur graphique
 */
public class GraphicsEngine implements GraphicEvent {
    /**
     * Liste des entités graphiques
     */
    private final ConcurrentMap<Integer, GraphicEntity> entities = new ConcurrentHashMap<>();

    /**
     * Liste des textures transférées
     */
    private final Map<Integer,Texture> textures = new HashMap<>();

    /**
     * Liste des fichiers de textures transférés
     */
    private final Map<Integer,SpriteSheet> spriteSheets = new HashMap<>();

    /**
     * Liste des animations
     */
    private final Map<Integer,SpriteAnimation> animations = new HashMap<>();

    /**
     * Liste des écouteurs d'évènements
     */
    private final ArrayList<EventListener> eventsListeners = new ArrayList<>();

    /**
     * Constructeur
     */
    public GraphicsEngine() {}

    @Override
    public void notifyEvent(String event) {
        for (EventListener listener : eventsListeners)
            listener.onEvent(event);
    }

    @Override
    public void notifyEntityUpdate(GraphicEntity entity) {
        for (EventListener listener : eventsListeners)
            listener.onEntityUpdate(entity);
    }

    @Override
    public void subscribeEvents(EventListener listener) {
        eventsListeners.add(listener);
    }

    //-----------------------------------------------//
    //--------- MÉTHODES ENTITÉS GRAPHIQUES ---------//
    //-----------------------------------------------//

    /**
     * Dessine une entité graphique
     * @param entity entité à dessiner
     */
    public void draw(Entity entity) {
        GraphicEntity graphicEntity = entity.getGraphicEntity();
        if (graphicEntity.getColor() != null)
            graphicEntity.getColor().cover(graphicEntity);
        if (graphicEntity.getTexture() != null) {
            graphicEntity.getTexture().update();
            graphicEntity.getTexture().cover(graphicEntity);
        }
        if (graphicEntity.getText() != null)
            graphicEntity.getText().cover(graphicEntity);
    }

    /**
     * Supprime une entité graphique de sa scène
     * @param entity entité à supprimer
     */
    public void erase(Entity entity) {
        Scene scene = entity.getGraphicEntity().getScene();
        if (scene != null)
            scene.removeEntity(entity.getGraphicEntity());
    }

    /**
     * Ajoute une couleur à une entité graphique
     * @param entity entité à colorer
     * @param r intensité de rouge
     * @param g intensité de vert
     * @param b intensité de bleu
     */
    public void bindColor(Entity entity, int r, int g, int b) {
        entity.getGraphicEntity().setColor(new Color(r,g,b));
    }

    /**
     * Ajoute une couleur à une entité graphique
     * @param entity entité à colorer
     * @param color couleur à ajouter
     */
    public void bindColor(Entity entity, Color color) {
        entity.getGraphicEntity().setColor(color);
    }

    /**
     * Suppression de la couleur d'une entité graphique
     * @param entity entité à décolorer
     */
    public void unbindColor(Entity entity) {
        entity.getGraphicEntity().setColor(null);
    }

    /**
     * Ajoute un texte à une entité graphique
     * @param entity entité à colorer
     * @param text contenu
     * @param color couleur
     * @param fontSize taille police
     * @param center centrer le texte
     */
    public void bindText(Entity entity, String text, Color color, int fontSize, boolean center) {
        entity.getGraphicEntity().setText(new Text(text,color.getSwingColor(),fontSize,center));
    }

    /**
     * Suppression du texte d'une entité graphique
     * @param entity entité à décolorer
     */
    public void unbindText(Entity entity) {
        entity.getGraphicEntity().setText(null);
    }

    /**
     * Ajoute une texture à une entité graphique
     * @param entity entité à texturer
     * @param textureID identifiant de la texture
     */
    public void bindTexture(Entity entity, int textureID) {
        entity.getGraphicEntity().setTexture(textures.get(textureID));
    }

    /**
     * Ajoute une texture à une entité graphique depuis un fichier de textures
     * @param entity entité à texturer
     * @param spriteSheetID identifiant du fichier de textures
     * @param row ligne
     * @param col colonne
     */
    public void bindTexture(Entity entity, int spriteSheetID, int row, int col) {
        entity.getGraphicEntity().setTexture(spriteSheets.get(spriteSheetID).getSprite(row, col));
    }

    /**
     * Attache une animation à une entité graphique
     * @param entity entité
     * @param animationID identifiant de l'animation
     */
    public void bindAnimation(Entity entity, int animationID) {
        SpriteAnimation animation = animations.get(animationID);
        if (!animation.isLooping()) animation.reset();
        entity.getGraphicEntity().setTexture(animations.get(animationID));
    }

    /**
     * Suppression de la texture d'une entité graphique
     * @param entity entité à détexturer
     */
    public void unbindTexture(Entity entity) {
        entity.getGraphicEntity().setTexture(null);
    }

    /**
     * Ajouter une entité à la scène courante
     * @param entity entité
     */
    public void addToCurrentScene(Entity entity) {
        Window.getActualScene().addEntity(entity.getGraphicEntity());
    }

    /**
     * Ajouter une entité à une scène spécifique
     * @param scene scène
     * @param entity entité
     */
    public void addToScene(Scene scene, Entity entity) {
        scene.addEntity(entity.getGraphicEntity());
    }

    /**
     * Déplacement de l'entité graphique à la position indiquée
     * @param entity entité graphique
     * @param x position x
     * @param y position y
     */
    public void move(Entity entity, int x, int y) {
        entity.getGraphicEntity().setX(x);
        entity.getGraphicEntity().setY(y);
        notifyEntityUpdate(entity.getGraphicEntity());
    }

    /**
     * Translater une entité graphique
     * @param entity entité
     * @param x position horizontale
     * @param y position verticale
     */
    public void translate(Entity entity, int x, int y) {
        entity.getGraphicEntity().setX(entity.getGraphicEntity().getX() + x);
        entity.getGraphicEntity().setY(entity.getGraphicEntity().getY() + y);
        notifyEntityUpdate(entity.getGraphicEntity());
    }

    /**
     * Redimensionne une entité graphique
     *
     * @param entity entité à redimensionner
     * @param w largeur
     * @param h hauteur
     */
    public void resize(Entity entity, int w, int h) {
        entity.getGraphicEntity().setWidth(w);
        entity.getGraphicEntity().setHeight(h);
        notifyEntityUpdate(entity.getGraphicEntity());
    }

    /**
     * Redimensionne en hauteur d'une entité graphique
     * @param entity entité à redimensionner
     * @param h hauteur
     */
    public void resizeHeight(Entity entity, int h) {
        entity.getGraphicEntity().setHeight(h);
        notifyEntityUpdate(entity.getGraphicEntity());
    }

    /**
     * Redimensionne en largeur d'une entité graphique
     * @param entity entité à redimensionner
     * @param w largeur
     */
    public void resizeWidth(Entity entity, int w) {
        entity.getGraphicEntity().setWidth(w);
        notifyEntityUpdate(entity.getGraphicEntity());
    }

    //-----------------------------------------------//
    //------------ MÉTHODES TEXTURES ----------------//
    //-----------------------------------------------//

    /**
     * Transférer une texture
     * @param link lien du fichier
     * @return identifiant de la texture
     */
    public int loadTexture(String link) {
        if (!SwingRenderer.getInstance().isTextureLoaded(link)) {
            SwingRenderer.getInstance().loadTexture(link);
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
    public int loadSpriteSheet(String link, int height, int width) {
        if (!SwingRenderer.getInstance().isTextureLoaded(link)) {
            SwingRenderer.getInstance().loadSpriteSheet(link, height, width);
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
    public int generateAnimation(int spriteSheetID, int speed, boolean looping) {
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
    public void addFrameToAnimation(int id, int row, int col) {
        animations.get(id).addFrame(row, col);
    }

    /**
     * Jouer / Arrêter une animation
     * @param id identifiant de l'animation
     */
    public void playPauseAnimation(int id) {
        animations.get(id).playPause();
    }

    /**
     * Réinitialiser une animation
     * @param id identifiant de l'animation
     */
    public void resetAnimation(int id) {
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
    public Scene generateScene(int height, int width) {
        return new Scene(this, height, width);
    }

    /**
     * Définir la couleur de fond d'une scène
     * @param scene scène
     * @param r intensité rouge
     * @param g intensité vert
     * @param b intensité bleu
     */
    public void setSceneBackgroundColor(Scene scene, int r, int g, int b) {
        scene.setBackgroundColor(new Color(r,g,b));
    }

    /**
     * Définir la position de la scène dans la fenêtre
     * @param scene scène
     * @param x position horizontale
     * @param y position verticale
     */
    public void setSceneLocation(Scene scene, int x, int y) {
        scene.setLocation(x, y);
    }

    /**
     * Définir la taille d'une scène
     * @param scene scène
     * @param height hauteur
     * @param width largeur
     */
    public void setSceneSize(Scene scene, int height, int width) {
        scene.setSize(height, width);
    }

    /**
     * Attacher une scène à la fenêtre
     * @param scene scène
     */
    public void bindScene(Scene scene) {
        Window.bindScene(scene);
    }

    //-----------------------------------------------//
    //----------------MÉTHODES FENETRE --------------//
    //-----------------------------------------------//

    /**
     * Afficher la fenêtre
     */
    public void showWindow() {
        Window.show();
    }

    /**
     * Rafraichir la fenêtre
     */
    public void refreshWindow() { Window.refresh(); }

    /**
     * Arrêter la fenêtre
     */
    public void stopWindow() {
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

    /**
     * Supprimer une entité
     * @param entity entité
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity.getId());
        erase(entity);
    }

    // GETTERS & SETTERS //

    public Texture getTexture(int id) { return textures.get(id); }

    public SpriteAnimation getAnimation(int id) { return animations.get(id); }

    public Map<Integer, GraphicEntity> getEntities() { return entities; }

    public Map<Integer, Texture> getTextures() { return textures; }

    public Map<Integer, SpriteSheet> getSpriteSheets() { return spriteSheets; }

    public Map<Integer, SpriteAnimation> getAnimations() { return animations; }

    public ArrayList<EventListener> getEventsListeners() { return eventsListeners; }
}
