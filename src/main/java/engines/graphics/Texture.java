package engines.graphics;

import api.SwingRenderer;

/**
 * Texture
 */
public class Texture extends Cover {
    /**
     * Lien vers le fichier
     */
    private String link;

    /**
     * Constructeur
     * @param link lien vers le fichier
     */
    protected Texture(String link) {
        this.link = link;
    }

    @Override
    protected void cover(GraphicEntity graphicEntity) {
        SwingRenderer.getInstance().renderTexturedRect(graphicEntity.getHeight(), graphicEntity.getWidth(),
                graphicEntity.getX(), graphicEntity.getY(), link);
    }

    @Override
    protected void update() {}

    @Override
    public Cover clone() {
        return this;
    }

    // GETTERS & SETTERS //

    public String getLink() { return link; }
}
