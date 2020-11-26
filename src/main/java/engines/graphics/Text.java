package engines.graphics;

import api.SwingRenderer;

import java.awt.Color;

/**
 * Texte
 */
public class Text extends Cover {
    /**
     * Contenu
     */
    private String content;

    /**
     * Couleur
     */
    private Color color;

    /**
     * Taille de la police
     */
    private int fontSize;

    /**
     * Constructeur
     * @param content contenu
     * @param color couleur
     * @param fontSize taille police
     */
    protected Text(String content, Color color, int fontSize) {
        this.content = content;
        this.color = color;
        this.fontSize = fontSize;
    }

    @Override
    public void cover(GraphicEntity graphicEntity) {
        SwingRenderer.getInstance().renderText(content, color, fontSize,
                graphicEntity.getX(), graphicEntity.getY(), graphicEntity.getHeight(), graphicEntity.getWidth());
    }

    @Override
    public void update() {}

    @Override
    public Cover clone() {
        return this;
    }

    // GETTERS //

    public String getContent() { return content; }
}
