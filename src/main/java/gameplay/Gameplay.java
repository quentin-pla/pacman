package gameplay;

import engines.AI.AIEngine;
import engines.graphics.Color;
import engines.graphics.GraphicsEngine;
import engines.graphics.Scene;
import engines.input_output.IOEngine;
import engines.kernel.Entity;
import engines.kernel.KernelEngine;
import engines.physics.PhysicEntity;
import engines.physics.PhysicsEngine;
import engines.sound.SoundEngine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Gameplay
 */
public class Gameplay {
    /**
     * Directions de déplacement
     */
    public enum MoveDirection {
        UP,RIGHT,DOWN,LEFT
    }

    /**
     * Moteur noyau
     */
    private final KernelEngine kernelEngine;

    /**
     * Identifiant du fichier contenant les textures du jeu
     */
    private final int textures;

    /**
     * Identifiant scène
     */
    private Scene menuView;

    /**
     * Niveaux disponibles
     */
    private ArrayList<Level> levels;

    /**
     * Joueur
     */
    private final Pacman pacman;

    /**
     * Fantomes
     */
    private final ArrayList<Ghost> ghosts;

    /**
     * Constructeur
     */
    public Gameplay() {
        this.kernelEngine = new KernelEngine();
        this.textures = kernelEngine.getGraphicsEngine().loadSpriteSheet("assets/sprite_sheet.png", 11, 11);
        this.levels = new ArrayList<>();
        this.pacman = new Pacman(this);
        this.ghosts = new ArrayList<>();
        ghosts.add(new Ghost(this));
        initGameplay();
    }

    /**
     * Initialiser le gameplay
     */
    private void initGameplay() {
        //Activation des entrées / sorties
        ioEngine().enableKeyboardIO();
        ioEngine().enableMouseIO();

        initEvents();
        initSounds();
        initMenu();
        initDefaultLevel();
    }

    /**
     * Initialiser les évènements du jeu
     */
    private void initEvents() {
        kernelEngine.addEvent("playLevel", () -> playLevel(levels.get(0)));
        //Déplacer un fantome
        kernelEngine.addEvent("moveGhost", () -> {
            for (Ghost ghost : ghosts)
                updateGhostDirection(ghost);
        });
        //Se déplacer vers le haut
        kernelEngine.addEvent("pacmanGoUp", () -> switchPacmanDirection(MoveDirection.UP));
        //Se déplacer vers la droite
        kernelEngine.addEvent("pacmanGoRight", () -> switchPacmanDirection(MoveDirection.RIGHT));
        //Se déplacer vers le bas
        kernelEngine.addEvent("pacmanGoDown", () -> switchPacmanDirection(MoveDirection.DOWN));
        //Se déplacer vers la gauche
        kernelEngine.addEvent("pacmanGoLeft", () -> switchPacmanDirection(MoveDirection.LEFT));
        //Attacher la texture par défaut au joueur
        kernelEngine.addEvent("pacmanBindDefaultTexture", () ->
                graphicsEngine().bindTexture(pacman.getGraphicEntity(),
                        textures, pacman.getDefaultTextureCoords()[0], pacman.getDefaultTextureCoords()[1]));
        //Lorsqu'il y a une collision
        kernelEngine.addEvent("pacmanOnCollision", () -> {
            if (pacman.getCurrentAnimationID() != 0)
                if (graphicsEngine().getAnimation(pacman.getCurrentAnimationID()).isPlaying())
                    graphicsEngine().playPauseAnimation(pacman.getCurrentAnimationID());
        });
        //Rejouer l'animation courante
        kernelEngine.addEvent("pacmanPlayCurrentAnimation", () -> {
            if (!graphicsEngine().getAnimation(pacman.getCurrentAnimationID()).isPlaying())
                graphicsEngine().playPauseAnimation(pacman.getCurrentAnimationID());
        });
        ioEngine().bindEventOnLastKey(KeyEvent.VK_UP, "pacmanGoUp");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_RIGHT, "pacmanGoRight");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_DOWN, "pacmanGoDown");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_LEFT, "pacmanGoLeft");
        ioEngine().bindEventKeyboardFree("pacmanBindDefaultTexture");
        physicsEngine().bindEventOnCollision(pacman.getPhysicEntity(), "pacmanOnCollision");
        for (Ghost ghost : ghosts)
            aiEngine().bindEvent(ghost.getAiEntity(), "moveGhost");
    }

    /**
     * Initialiser les sons du jeu
     */
    private void initSounds() {
        soundEngine().loadSound("munch.wav","munch");
        soundEngine().loadSound("game_start.wav","gameStart");
    }

    /**
     * Initialiser le menu
     */
    private void initMenu() {
        menuView = graphicsEngine().generateScene(400,400);
        Entity button = kernelEngine.generateEntity();
        physicsEngine().resize(button.getPhysicEntity(),100,50);
        physicsEngine().move(button.getPhysicEntity(), 150,175);
        graphicsEngine().bindColor(button.getGraphicEntity(),255,0,0);
        graphicsEngine().bindText(button.getGraphicEntity(), "Jouer", new Color(255,255,255), 20);
        graphicsEngine().addToScene(menuView, button.getGraphicEntity());
        ioEngine().bindEventOnClick(button,"playLevel");
    }

    /**
     * Initialiser le niveau par défaut
     */
    private void initDefaultLevel() {
        //Level par défaut
        Level defaultLevel = generateLevel(10,10);
        //Génération des murs
        for (int j = 0; j < 6; j++) {
            Entity wall = defaultLevel.getMatrix()[5][j];
            graphicsEngine().bindColor(wall.getGraphicEntity(),0,0,255);
            physicsEngine().addCollisions(pacman.getPhysicEntity(), wall.getPhysicEntity());
            for (Ghost ghost : ghosts)
                physicsEngine().addCollisions(ghost.getPhysicEntity(), wall.getPhysicEntity());
        }
        //Génération des boules
        for (int j = 0; j < 6; j++) {
            Entity ball = defaultLevel.getMatrix()[6][j];
            graphicsEngine().bindTexture(ball.getGraphicEntity(),getTexturesFile(),10,2);
            kernelEngine().addEvent("eraseBall" + j,() -> {
                kernelEngine().removeEntity(ball);
                soundEngine().playSound("munch");
            });
            physicsEngine().bindEventOnSameLocation(pacman.getPhysicEntity(), ball.getPhysicEntity(), "eraseBall" + j);
        }
        levels.add(defaultLevel);
    }

    /**
     * Mettre à jour la position d'un fanôme
     * @param ghost fantôme
     */
    protected void updateGhostDirection(Ghost ghost) {


        PhysicEntity playerPhysic = pacman.getPhysicEntity();
        PhysicEntity ghostPhysic = ghost.getPhysicEntity();


        int playerXmiddle = (playerPhysic.getX() + playerPhysic.getWidth())/2;
        int playerYmiddle = (playerPhysic.getY() + playerPhysic.getHeight())/2;
        int ghostXmiddle = (ghostPhysic.getX() + ghostPhysic.getWidth())/2;
        int ghostYmiddle = (ghostPhysic.getY() + ghostPhysic.getHeight())/2;
        int xDistance = playerXmiddle - ghostXmiddle;
        int yDistance = playerYmiddle - ghostYmiddle;

        MoveDirection direction = null;
        PhysicEntity entityOnTheWay;
        PhysicEntity secondEntityOnTheWay;

        //inutiles pour l'instant mais peut etre utilisables plus tard pour faire en sorte que le fantome continue au moins jusqu'a la prochaine intersection
        if (ghost.isKeepDown()){
            direction = MoveDirection.DOWN;
            setEntityNextDirection(ghost,direction);
            return;
        }

        if (ghost.isKeepUp()){
            direction = MoveDirection.UP;
            setEntityNextDirection(ghost,direction);
            return;
        }

        if (ghost.isKeepLeft()){
            direction = MoveDirection.LEFT;
            setEntityNextDirection(ghost,direction);
            return;
        }

        if (ghost.isKeepRight()){
            direction = MoveDirection.RIGHT;
            setEntityNextDirection(ghost,direction);
            return;
        }

        //condition finie et commentée pour le up
        if (Math.abs(xDistance) > Math.abs(yDistance)) {
            if (xDistance < 0) {
                entityOnTheWay = physicsEngine().isSomethingLeftInLine(ghostPhysic,Math.abs(xDistance));
                if (entityOnTheWay != null){
                    int entityOnTheWayXMiddle = (entityOnTheWay.getX() + entityOnTheWay.getWidth())/2;
                    int xDistanceWithWall =  entityOnTheWayXMiddle - ghostXmiddle;

                    //a vérifier selon comportement : peut etre a ajouter dans le if (Math.abs(xDistance) > Math.abs(xDistanceWithWall)) &&
                    if ((Math.abs(xDistanceWithWall) <= (ghostPhysic.getWidth()/2 + entityOnTheWay.getWidth()/2)/2)){ //si la distance entre fantome et pacman est inférieure a la distance entre fantome et mur
                        if (yDistance < 0) direction = MoveDirection.UP;
                        else direction = MoveDirection.DOWN;
                    }
                    else {
                        direction = MoveDirection.LEFT;
                    }
                }
                else {
                    direction = MoveDirection.LEFT;
                }
            }
            else {
                entityOnTheWay = physicsEngine().isSomethingRightInLine(ghostPhysic,Math.abs(xDistance));
                if (entityOnTheWay != null){
                    int entityOnTheWayXMiddle = (entityOnTheWay.getX() + entityOnTheWay.getWidth())/2;
                    int xDistanceWithWall =  entityOnTheWayXMiddle - ghostXmiddle;
                    if ((Math.abs(xDistanceWithWall) <= (ghostPhysic.getWidth()/2 + entityOnTheWay.getWidth()/2)/2)){ //si la distance entre fantome et pacman est inférieure a la distance entre fantome et mur
                        if (yDistance <= 0) direction = MoveDirection.UP;
                        else direction = MoveDirection.DOWN;
                    }
                    else {
                        direction = MoveDirection.RIGHT;
                    }
                }
                else {
                    direction = MoveDirection.RIGHT;
                }
            }
        } else {
            //si yDistance < 0 signifie que pacman est au dessus du fantome donc on va globalement vouloir aller en haut
            if (yDistance <= 0) {
                //on check si une entité va se retrouver a un moment donné ou a un autre sur le chemin
                entityOnTheWay = physicsEngine().isSomethingUpInLine(ghostPhysic,Math.abs(yDistance));
                if (entityOnTheWay != null){
                    //si oui on fait d'autres vérif
                    // on enregistre la distance entre le fantome et l'entité
                    int entityOnTheWayYMiddle = (entityOnTheWay.getY() + entityOnTheWay.getHeight())/2;
                    int yDistanceWithWall =  entityOnTheWayYMiddle - ghostYmiddle;

                    //on vérifie laquelle des distances entre pacman et le fantome et entre le mur et le fantome est la plus grande
                    //si le fantome est plus proche du mur que de pacman on entre dans d'autres vérifications
                    if ((Math.abs(yDistanceWithWall) <= ((ghostPhysic.getHeight()/2 + entityOnTheWay.getHeight()/2)/2))){ //si la distance entre fantome et pacman est inférieure a la distance entre fantome et mur
                        //si xDistance < 0 signifie que pacman est a gauche du fantome
                        if (xDistance <= 0){
                            //on vérifie si il y a encore un mur sur le chemin en allant a gauche (au moins sur la longueur de
                            // celui qui blogue le passage initialement et si il y en a un pn va donc a droite
                            secondEntityOnTheWay = physicsEngine().isSomethingLeftInLine(ghostPhysic,Math.abs(entityOnTheWay.getX() + ghostPhysic.getWidth()));
                            if (secondEntityOnTheWay != null){
                                direction = MoveDirection.RIGHT;
                            }
                            //s'il n'y a pas d'entité on vérifie si on touche le mur de limite
                            else {
                                if(entityOnTheWay.getX()- ghostPhysic.getWidth() <= 0){
                                    //si sur toute la longueur du bloc on va toucher la limite a un moment on va a droite
                                    //System.out.println("right");
                                    direction = MoveDirection.RIGHT;
                                } else {
                                    direction = MoveDirection.LEFT;
                                }
                                //si on a pas décidés d'aller a droite on a gauche
                            }
                            //si sur le chemin il n'y a aucun moyen d'aller vers le haut en allant a gauche alors aller a droite
                        }
                        else {
                            //faut tester maitenant quand on est juste en dessous de pacman x distance = 0 et x distance > 0
                            direction = MoveDirection.RIGHT;
                        }
                    }
                    else {
                        direction = MoveDirection.UP;
                    }
                }
                //s'il n'y a aucune entité sur le chemin on va droit vers pacman
                else {
                    direction = MoveDirection.UP;
                }
            }
            else {
                entityOnTheWay = physicsEngine().isSomethingDownInLine(ghostPhysic,Math.abs(yDistance));
                if (entityOnTheWay != null){
                    int entityOnTheWayYMiddle = (entityOnTheWay.getY() + entityOnTheWay.getHeight())/2;
                    int yDistanceWithWall =  entityOnTheWayYMiddle - ghostYmiddle;
                    if ((Math.abs(yDistanceWithWall) <= (ghostPhysic.getHeight()/2 + entityOnTheWay.getHeight()/2)/2)){ //si la distance entre fantome et pacman est inférieure a la distance entre fantome et mur
                        if (xDistance <= 0) direction = MoveDirection.LEFT;
                        else direction = MoveDirection.RIGHT;
                    }
                    else {
                        direction = MoveDirection.DOWN;
                    }
                }
                else {
                    direction = MoveDirection.DOWN;
                }
            }
        }
        setEntityNextDirection(ghost,direction);
    }

    /**
     * Changer la direction de pacman
     * @param direction direction
     */
    protected void switchPacmanDirection(MoveDirection direction) {
        pacman.setCurrentAnimationID(pacman.getAnimations().get(direction.name()));
        kernelEngine().notifyEvent("pacmanPlayCurrentAnimation");
        setEntityNextDirection(pacman,direction);
    }

    /**
     * Déterminer la prochaine direction de l'entité
     * @param entity entité
     * @param direction direction
     */
    private void setEntityNextDirection(Player entity, MoveDirection direction) {
        PhysicEntity entityNearby;
        switch (direction) {
            case UP:
                entityNearby = physicsEngine().isSomethingUp(entity.getPhysicEntity());
                if (entityNearby == null)
                    entity.setCurrentDirection(MoveDirection.UP);
                break;
            case RIGHT:
                entityNearby = physicsEngine().isSomethingRight(entity.getPhysicEntity());
                if (entityNearby == null)
                    entity.setCurrentDirection(MoveDirection.RIGHT);
                break;
            case DOWN:
                entityNearby = physicsEngine().isSomethingDown(entity.getPhysicEntity());
                if (entityNearby == null)
                    entity.setCurrentDirection(MoveDirection.DOWN);
                break;
            case LEFT:
                entityNearby = physicsEngine().isSomethingLeft(entity.getPhysicEntity());
                if (entityNearby == null)
                    entity.setCurrentDirection(MoveDirection.LEFT);
                break;
            default:
                break;
        }
        callEventFromDirection(entity, entity.getCurrentDirection());
        graphicsEngine().bindAnimation(entity.getGraphicEntity(), entity.getAnimations().get(entity.getCurrentDirection().name()));
    }

    /**
     * Appeler la méthode de déplacement en fonction de la direction courante
     */
    private void callEventFromDirection(Entity entity, MoveDirection direction) {
        if (direction != null) {
            switch (direction) {
                case UP:
                    physicsEngine().goUp(entity.getPhysicEntity());
                    break;
                case RIGHT:
                    physicsEngine().goRight(entity.getPhysicEntity());
                    break;
                case DOWN:
                    physicsEngine().goDown(entity.getPhysicEntity());
                    break;
                case LEFT:
                    physicsEngine().goLeft(entity.getPhysicEntity());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Générer un niveau
     * @param rows nombre de lignes
     * @param cols nombre de colonnes
     */
    public Level generateLevel(int rows, int cols) {
        Level level = new Level(this,rows,cols);
        this.levels.add(level);
        return level;
    }

    /**
     * Jouer un niveau
     * @param level level
     */
    public void playLevel(Level level) {
        soundEngine().playSound("gameStart");
        level.spawnPlayer(1,1);
        for (Ghost ghost : ghosts)
            level.spawnGhost(ghost,9,5);
        graphicsEngine().bindScene(level.getScene());
        kernelEngine.start();
    }

    /**
     * Lancer le jeu
     */
    public void start() {
        graphicsEngine().bindScene(menuView);
        kernelEngine.start();
    }

    // GETTERS //

    public int getTexturesFile() { return textures; }

    public KernelEngine kernelEngine() { return kernelEngine; }

    public GraphicsEngine graphicsEngine() { return kernelEngine.getGraphicsEngine(); }

    public IOEngine ioEngine() { return kernelEngine.getIoEngine(); }

    public PhysicsEngine physicsEngine() { return kernelEngine.getPhysicsEngine(); }

    public SoundEngine soundEngine() { return kernelEngine.getSoundEngine(); }

    public AIEngine aiEngine() { return kernelEngine.getAiEngine(); }

    public Pacman getPlayer() { return pacman; }

    public ArrayList<Ghost> getGhosts() { return ghosts; }
}
