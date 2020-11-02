package engines.graphics;

import engines.graphics.api.Renderer;

/**
 * Moteur graphique
 */
public class GraphicsEngine extends Renderer {

    // ENTITÉS //

    /**
     * Générer une nouvelle entité
     * @param height hauteur
     * @param width largeur
     */
    public static Entity newEntity(int height, int width) {
        return new Entity(height, width);
    }

    /**
     * Dessiner une entité
     * @param entity entité
     */
    public static void drawEntity(Entity entity) {
        entity.draw();
    }

    /**
     * Mettre à jour une entité
     * @param entity entité
     */
    public static void updateEntity(Entity entity) { entity.update(); }

    /**
     * Supprimer une entité
     * @param entity entité
     */
    public static void eraseEntity(Entity entity) {
        entity.erase();
    }

    /**
     * Cloner une entité
     * @param entity entité
     * @return clone
     */
    public static Entity cloneEntity(Entity entity) { return entity.clone(); }

    /**
     * Translater une entité
     * @param entity entité
     * @param x position horizontale à additionner
     * @param y position verticale à additionner
     */
    public static void translateEntity(Entity entity, int x, int y) {
        entity.translate(x, y);
    }

    /**
     * Déplacer une entité
     * @param entity entité
     * @param x position horizontale
     * @param y position verticale
     */
    public static void moveEntity(Entity entity, int x, int y) { entity.move(x, y); }

    /**
     * Redimensionner une entité
     * @param entity entité
     * @param height hauteur
     * @param width largeur
     */
    public static void resizeEntity(Entity entity, int height, int width) { entity.resize(height, width); }

    /**
     * Colorer une entité
     * @param entity entité
     * @param red intensité couleur rouge
     * @param green intensité couleur verte
     * @param blue intensité couleur bleue
     * @param alpha intensité opacité
     */
    public static void colorEntity(Entity entity, float red, float green, float blue, float alpha) {
        entity.bindColor(red, green, blue, alpha);
    }

    /**
     * Habiller une entité avec une texture
     * @param entity entité
     * @param texture texture
     */
    public static void dressEntity(Entity entity, EntityTexture texture) {
        entity.bindTexture(texture);
    }

    // TEXTURES //

    /**
     * Transférer une texture
     * @param link lien vers le fichier
     */
    public static Texture loadTexture(String link) {
        return Texture.load(link);
    }

    /**
     * Transférer un fichier de texture
     * @param link lien du fichier
     * @param height nombre d'éléments horizontaux
     * @param width nombre d'éléments verticaux
     */
    public static SpriteSheet loadSpriteSheet(String link, int height, int width) {
        return SpriteSheet.load(link, height, width);
    }

    /**
     * Transférer une texture provenant d'un fichier de textures
     * @param sprite_sheet fichier de textures
     * @param row ligne
     * @param col colonne
     */
    public static Sprite newSprite(SpriteSheet sprite_sheet, int row, int col) {
        return new Sprite(sprite_sheet, row, col);
    }

    /**
     * Créer une animation
     * @param sprite_sheet fichier de textures
     * @param speed vitesse
     * @param looping en boucle
     */
    public static SpriteAnimation newAnimation(SpriteSheet sprite_sheet, int speed, boolean looping) {
        return new SpriteAnimation(sprite_sheet, speed, looping);
    }

    /**
     * Cloner une animation
     * @param animation animation
     * @return clone animation
     */
    public static SpriteAnimation cloneAnimation(SpriteAnimation animation) {
        return new SpriteAnimation(animation);
    }

    /**
     * Ajouter une image à une animation
     * @param animation animation
     * @param row ligne
     * @param col colonne
     */
    public static void addFrameToAnimation(SpriteAnimation animation, int row, int col) {
        animation.addFrame(row, col);
    }

    /**
     * Jouer / mettre en pause une animation
     * @param animation animation
     */
    public static void playPauseAnimation(SpriteAnimation animation) {
        animation.playPause();
    }

    /**
     * Réinitialiser au départ une animation
     * @param animation animation
     */
    public static void resetAnimation(SpriteAnimation animation) {
        animation.reset();
    }

    /**
     * Obtenir une texture
     * @param name nom de la texture
     * @return texture
     */
    public static Texture getTexture(String name) {
        return Texture.get(name);
    }

    /**
     * Obtenir un fichier de texture
     * @param name nom du fichier de texture
     * @return fichier de texture
     */
    public static SpriteSheet getSpriteSheet(String name) {
        return SpriteSheet.get(name);
    }

    // FENÊTRE //

    /**
     * Afficher la fenêtre principale
     */
    public static void showWindow() {
        Window.show();
    }

    /**
     * Fermer la fenêtre
     */
    public static void closeWindow() {
        Window.stop();
    }

    /**
     * Définir le titre de la fenêtre
     * @param title titre
     */
    public static void setWindowTitle(String title) {
        Window.setTitle(title);
    }

    /**
     * Ajouter une nouvelle scène
     * @param name nom de la scène
     * @param height hauteur
     * @param width largeur
     */
    public static void addScene(String name, int height, int width) {
        Window.addScene(new Scene(height, width), name);
    }

    /**
     * Définir la scène courante
     * @param name nom de la scène
     */
    public static void setCurrentScene(String name) {
        Window.bindScene(name);
    }

    // SCÈNES //

    /**
     * Ajouter une entité à la scène
     * @param entity entité
     * @param x position horizontale
     * @param y position verticale
     */
    public static void addEntityToCurrentScene(Entity entity, int x, int y) {
        Window.getActualScene().addEntity(entity, x, y);
    }

    /**
     * Supprimer une entité de la scène
     * @param entity entité
     */
    public static void removeEntityFromCurrentScene(Entity entity) {
        Window.getActualScene().removeEntity(entity);
    }


}
