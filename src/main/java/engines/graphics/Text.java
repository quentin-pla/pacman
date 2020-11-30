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
     * Center le texte
     */
    private boolean center;

    /**
     * Constructeur
     * @param content contenu
     * @param color couleur
     * @param fontSize taille police
     * @param center centrer
     */
    public Text(String content, Color color, int fontSize, boolean center) {
        this.content = content;
        this.color = color;
        this.fontSize = fontSize;
        this.center = center;
    }

    @Override
    public void cover(GraphicEntity graphicEntity) {
        SwingRenderer.getInstance().renderText(content, color, fontSize, center,
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
