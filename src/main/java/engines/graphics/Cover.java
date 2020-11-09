package engines.graphics;

import api.SwingAPI;

/**
 * Élément graphique pour couvrir une entité graphique
 */
public abstract class Cover extends SwingAPI {
    /**
     * Recouvrir une entité graphique
     * @param graphicEntity carreau
     */
    protected abstract void cover(GraphicEntity graphicEntity);

    /**
     * Mettre à jour l'élément graphique
     */
    protected abstract void update();

    /**
     * Cloner l'élément graphique
     */
    public abstract Cover clone();
}
