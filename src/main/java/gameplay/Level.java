//package gameplay;
//
//import engines.kernel.Entity;
//
//import java.util.ArrayList;
//
///**
// * Niveau de jeu
// */
//public class Level {
//    /**
//     * Matrice d'entités graphiques
//     */
//    private Entity[][] matrix;
//
//    /**
//     * Entités présentes sur le niveau
//     */
//    private ArrayList<Entity> level_entities = new ArrayList<>();
//
//    /**
//     * Joueur
//     */
//    private Player player;
//
//    /**
//     * Chronomètre
//     */
//    private float timer;
//
//    /**
//     * Score actuel
//     */
//    private int actual_score;
//
//    /**
//     * Constructeur surchargé
//     * @param rows nombre de lignes
//     * @param cols nombre de colonnes
//     * @param floor sol par défaut
//     */
//    public Level(int rows, int cols, Entity floor) {
//        this.resize((rows - 1) * floor.getHeight(), (cols - 1) * floor.getWidth());
//        this.matrix = new Entity[rows][cols];
//        fill(floor);
//    }
//
//    /**
//     * Remplir la matrice
//     * @param entity entité
//     */
//    public void fill(Entity entity) {
//        for (int row = 0; row < matrix.length; row++) {
//            for (int col = 0; col < matrix[row].length; col++) {
//                matrix[row][col] = entity.clone();
//                matrix[row][col].move((entity.getWidth() * col),(entity.getHeight() * row));
//                level_entities.add(matrix[row][col]);
//            }
//        }
//    }
//
//    /**
//     * Faire apparaitre le joueur à un endroit
//     * @param player joueur
//     * @param row ligne
//     * @param col colonne
//     */
//    public void addPlayer(Player player, int row, int col) {
//        player.addMoveBounds(getBounds());
//        Entity entity = matrix[row][col];
//        player.move(entity.getX(), entity.getY());
//        this.player = player;
//        level_entities.add(player);
//    }
//
//    /**
//     * Récupérer les limites du niveau
//     * @return limites
//     */
//    public int[] getBounds() {
//        return new int[]{getX(), getY(), getX() + getWidth(), getY() + getHeight()};
//    }
//
//    @Override
//    public void draw() {
//        super.draw();
//        for (Entity entity : level_entities)
//            entity.draw();
//    }
//
//    @Override
//    public void update() {
//        super.update();
//        for (Entity entity : level_entities)
//            entity.update();
//    }
//
//    @Override
//    public void translate(int x, int y) {
//        super.translate(x, y);
//        //Translation des objets du niveau
//        for (Entity entity : level_entities) entity.translate(x, y);
//        //Mise à jour des limites de déplacement pour le joueur
//        if (player != null) player.addMoveBounds(getBounds());
//    }
//
//    @Override
//    public void move(int x, int y) {
//        super.move(x,y);
//        //Translation des objets du niveau
//        for (Entity entity : level_entities) entity.translate(x, y);
//        //Mise à jour des limites de déplacement pour le joueur
//        if (player != null) player.addMoveBounds(getBounds());
//    }
//
//    // GETTERS //
//
//    public Entity[][] getMatrix() {
//        return matrix;
//    }
//
//    public ArrayList<Entity> getLevel_entities() {
//        return level_entities;
//    }
//
//    public Player getPlayer() {
//        return player;
//    }
//
//    public float getTimer() {
//        return timer;
//    }
//
//    public int getActual_score() {
//        return actual_score;
//    }
//}
