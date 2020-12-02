package gameplay;

import engines.AI.AIEngine;
import engines.graphics.Color;
import engines.graphics.GraphicEntity;
import engines.graphics.GraphicsEngine;
import engines.graphics.Scene;
import engines.input_output.IOEngine;
import engines.kernel.Entity;
import engines.kernel.KernelEngine;
import engines.physics.PhysicEntity;
import engines.physics.PhysicsEngine;
import engines.sound.SoundEngine;

import javax.sound.sampled.LineEvent;
import java.awt.event.KeyEvent;
import java.util.*;

import static java.lang.Thread.sleep;

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
     * Scène menu principal
     */
    private Scene menuView;

    /**
     * Scène fin de jeu
     */
    private Scene endGameView;

    /**
     * Niveaux disponibles
     */
    private ArrayList<Level> levels;

    /**
     * Niveau courant
     */
    private Level currentLevel;

    /**
     * Affichage du volume
     */
    private Entity currentVolume;
    /**
     * Joueur
     */
    private Pacman pacman;

    private HashMap<String,Entity> targets = new HashMap<String,Entity>();

    /**
     * Fantomes
     */
    private Map<String,Ghost> ghosts;

    /**
     * Booléen pour savoir si les fantômes sont appeurés
     */
    private volatile boolean ghostFear;

    /**
     * Constructeur
     */
    public Gameplay() {
        this.kernelEngine = new KernelEngine();
        this.textures = kernelEngine.getGraphicsEngine().loadSpriteSheet("assets/sprite_sheet.png", 12, 11);
        this.levels = new ArrayList<>();
        this.ghostFear = false;
        initGameplay();
    }

    /**
     * Initialiser le gameplay
     */
    private void initGameplay() {
        //Activation des entrées / sorties
        ioEngine().enableKeyboardIO();
        ioEngine().enableMouseIO();
        //Initialiser les joueurs
        initPlayers();
        //Initialiser les évènements
        initEvents();
        //Initialiser les sons
        initSounds();
        //Initialiser le menu
        initMenu();
        //Initialiser la vue de fin de jeu
        initEndGameView();
        //Initialiser le niveau par défaut
        initDefaultLevel();
    }

    /**
     * Initialiser les évènements du jeu
     */
    private void initEvents() {
        //Jouer un niveau
        kernelEngine.addEvent("playLevel", () -> playLevel(levels.get(0)));
        //Jouer une nouvelle partie
        kernelEngine.addEvent("newGame", () -> {
            levels.remove(currentLevel);
            initDefaultLevel();
            playLevel(levels.get(0));
        });
        //Déplacer le fantome rose
        kernelEngine.addEvent("movePinkGhost", this::applyPinkGhostAI);
        //Déplacer le fantome rouge
        kernelEngine.addEvent("moveRedGhost", this::applyPinkGhostAI);
        //Déplacer le fantome Bleu
        kernelEngine.addEvent("moveBlueGhost", this::applyBlueGhostAI);
        //Se déplacer vers le haut
        kernelEngine.addEvent("pacmanGoUp", () -> switchPacmanDirection(MoveDirection.UP));
        //Se déplacer vers la droite
        kernelEngine.addEvent("pacmanGoRight", () -> switchPacmanDirection(MoveDirection.RIGHT));
        //Se déplacer vers le bas
        kernelEngine.addEvent("pacmanGoDown", () -> switchPacmanDirection(MoveDirection.DOWN));
        //Se déplacer vers la gauche
        kernelEngine.addEvent("pacmanGoLeft", () -> switchPacmanDirection(MoveDirection.LEFT));
        //Augmenter le volume
        kernelEngine.addEvent("augmentVolume", this::incrementGlobalVolume);
        //Baisser le volume
        kernelEngine.addEvent("downVolume", this::decrementGlobalVolume);
        //Lorsqu'il y a une collision

        kernelEngine.addEvent("pacmanOnCollision", this::checkPacmanCollisions);



        //Liaison des évènements du niveau
        ioEngine().bindEventOnLastKey(KeyEvent.VK_UP, "pacmanGoUp");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_RIGHT, "pacmanGoRight");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_DOWN, "pacmanGoDown");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_LEFT, "pacmanGoLeft");
        physicsEngine().bindEventOnCollision(pacman, "pacmanOnCollision");
        aiEngine().bindEvent(ghosts.get("pink"), "movePinkGhost");
        aiEngine().bindEvent(ghosts.get("blue"), "moveBlueGhost");
    }

    /**
     * Initialiser les sons du jeu
     */
    private void initSounds() {
        soundEngine().loadSound("munch_1.wav","munch1");
        soundEngine().loadSound("munch_2.wav","munch2");
        soundEngine().loadSound("game_start.wav","gameStart");
        soundEngine().loadSound("death_1.wav","death_1");
        soundEngine().loadSound("death_2.wav","death_2");

        soundEngine().setGlobalVolume(0);
    }

    /**
     * Initialiser le menu
     */
    private void initMenu() {
        menuView = graphicsEngine().generateScene(400,400);
        Entity button = kernelEngine.generateEntity();
        Entity volumePlus = kernelEngine.generateEntity();
        Entity volumeMinus = kernelEngine.generateEntity();
        currentVolume = kernelEngine.generateEntity();

        physicsEngine().resize(volumePlus,50,25);
        physicsEngine().move(volumePlus, 20,350);
        graphicsEngine().bindColor(volumePlus,50,50,50);
        graphicsEngine().bindText(volumePlus, "-", new Color(255,255,255), 20, true);
        graphicsEngine().addToScene(menuView, volumePlus);
        ioEngine().bindEventOnClick(volumePlus,"downVolume");

        physicsEngine().resize(volumeMinus,50,25);
        physicsEngine().move(volumeMinus, 330,350);
        graphicsEngine().bindColor(volumeMinus,50,50,50);
        graphicsEngine().bindText(volumeMinus, "+", new Color(255,255,255), 20, true);
        graphicsEngine().addToScene(menuView, volumeMinus);
        ioEngine().bindEventOnClick(volumeMinus,"augmentVolume");

        physicsEngine().resize(currentVolume,200,50);
        physicsEngine().move(currentVolume, 100,337);
        graphicsEngine().bindColor(currentVolume,50,50,50);
        graphicsEngine().bindText(currentVolume, "Volume is : " + (int) soundEngine().getGlobalvolume()*100, new Color(255,255,255), 20, true);
        graphicsEngine().addToScene(menuView, currentVolume);

        physicsEngine().resize(button,100,50);
        physicsEngine().move(button, 150,240);
        graphicsEngine().bindColor(button,50,50,50);
        graphicsEngine().bindText(button, "PLAY", new Color(255,255,255), 20, true);
        graphicsEngine().addToScene(menuView, button);
        ioEngine().bindEventOnClick(button,"playLevel");

        Entity menuLogo = kernelEngine.generateEntity();
        int logoTexture = kernelEngine.getGraphicsEngine().loadTexture("assets/menu_logo.png");
        physicsEngine().resize(menuLogo,300,71);
        physicsEngine().move(menuLogo, 50,120);
        graphicsEngine().bindTexture(menuLogo,logoTexture);
        graphicsEngine().addToScene(menuView, menuLogo);

    }

    /**
     * Initialiser la page en fin de jeu
     */
    private void initEndGameView() {
        endGameView = graphicsEngine().generateScene(400,400);

        Entity youLost = kernelEngine.generateEntity();
        physicsEngine().resize(youLost,100,50);
        physicsEngine().move(youLost, 150,50);
        graphicsEngine().bindText(youLost, "YOU LOST !", new Color(255,50,0), 25, true);
        graphicsEngine().addToScene(endGameView, youLost);

        Entity score = kernelEngine.generateEntity();
        physicsEngine().resize(score,200,50);
        physicsEngine().move(score, 100,150);
        graphicsEngine().addToScene(endGameView, score);

        Entity newGame = kernelEngine.generateEntity();
        physicsEngine().resize(newGame,200,50);
        physicsEngine().move(newGame, 100,240);
        graphicsEngine().bindColor(newGame,50,50,50);
        graphicsEngine().bindText(newGame, "NEW GAME", new Color(255,255,255), 20, true);
        graphicsEngine().addToScene(endGameView, newGame);
        ioEngine().bindEventOnClick(newGame,"newGame");
    }

    /**
     * Initialiser les joueurs
     */
    private void initPlayers() {
        pacman = new Pacman(this);
        ghosts = new HashMap<>();
        ghosts.put("red", new Ghost(this, "red"));
        ghosts.put("blue", new Ghost(this, "blue"));
        ghosts.put("pink", new Ghost(this, "pink"));
        ghosts.put("orange", new Ghost(this, "orange"));
    }

    /**
     * Initialiser le niveau par défaut
     */
    private void initDefaultLevel() {
        //Level par défaut
        Level defaultLevel = generateLevel(21,19);

        //Génération des murs
        Map<Integer,int[]> wallRows = new HashMap<>();

        wallRows.put(0, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18});
        wallRows.put(1, new int[]{0, 9, 18});
        wallRows.put(2, new int[]{0, 2, 3, 5, 6, 7, 9, 11, 12, 13, 15, 16, 18});
        wallRows.put(3, new int[]{0, 18});
        wallRows.put(4, new int[]{0, 2, 3, 5, 7, 8, 9, 10, 11, 13, 15, 16, 18});
        wallRows.put(5, new int[]{0, 5, 9, 13, 18});
        wallRows.put(6, new int[]{0, 1, 2, 3, 5, 6, 7, 9, 11, 12, 13, 15, 16, 17, 18});
        wallRows.put(7, new int[]{3, 5, 13, 15});
        wallRows.put(8, new int[]{0, 1, 2, 3, 5, 7, 8, 10, 11, 13, 15, 16, 17, 18});
        wallRows.put(9, new int[]{7, 11});
        wallRows.put(10, new int[]{0, 1, 2, 3, 5, 7, 8, 9, 10, 11, 13, 15, 16, 17, 18});
        wallRows.put(11, new int[]{3, 5, 13, 15});
        wallRows.put(12, new int[]{0, 1, 2, 3, 5, 7, 8, 9, 10, 11, 13, 15, 16, 17, 18});
        wallRows.put(13, new int[]{0, 9, 18});
        wallRows.put(14, new int[]{0, 2, 3, 5, 6, 7, 9, 11, 12, 13, 15, 16, 18});
        wallRows.put(15, new int[]{0, 3, 15, 18});
        wallRows.put(16, new int[]{0, 1, 3, 5, 7, 8, 9, 10, 11, 13, 15, 17, 18});
        wallRows.put(17, new int[]{0, 5, 9, 13, 18});
        wallRows.put(18, new int[]{0, 2, 3, 4, 5, 6, 7, 9, 11, 12, 13, 14, 15, 16, 18});
        wallRows.put(19, new int[]{0, 18});
        wallRows.put(20, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18});

        for (Map.Entry<Integer,int[]> row : wallRows.entrySet())
            for (int col : row.getValue())
                defaultLevel.addWall(row.getKey(),col);

        defaultLevel.applyWallTextures();

        //Génération des balles
        Map<Integer,int[]> ballRows = new HashMap<>();

        ballRows.put(1, new int[]{2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16});
        ballRows.put(2, new int[]{1, 4, 8, 10, 14, 17});
        ballRows.put(3, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17});
        ballRows.put(4, new int[]{1, 4, 6, 12, 14, 17});
        ballRows.put(5, new int[]{1, 2, 3, 4, 6, 7, 8, 10, 11, 12, 14, 15, 16, 17});
        ballRows.put(6, new int[]{4, 8, 10, 14});
        ballRows.put(7, new int[]{4, 6, 7, 8, 9, 10, 11, 12, 14});
        ballRows.put(8, new int[]{4, 6, 12, 14});
        ballRows.put(9, new int[]{4, 5, 6, 12, 13, 14});
        ballRows.put(10, new int[]{4, 6, 12, 14});
        ballRows.put(11, new int[]{4, 6, 7, 8, 9, 10, 11, 12, 14});
        ballRows.put(12, new int[]{4, 6, 12, 14});
        ballRows.put(13, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 15, 16, 17});
        ballRows.put(14, new int[]{1, 4, 8, 10, 14, 17});
        ballRows.put(15, new int[]{1, 2, 4, 5, 6, 7, 8, 10, 11, 12, 13, 14, 16, 17});
        ballRows.put(16, new int[]{2, 4, 6, 12, 14, 16});
        ballRows.put(17, new int[]{1, 2, 3, 4, 6, 7, 8, 10, 11, 12, 14, 15, 16, 17});
        ballRows.put(18, new int[]{1, 8, 10, 17});
        ballRows.put(19, new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});



        for (Map.Entry<Integer,int[]> row : ballRows.entrySet())
            for (int col : row.getValue())
                defaultLevel.addBall(row.getKey(),col);

        //Ajout de la barrière
        defaultLevel.addFence(8, 9);

        defaultLevel.addGomme(1,1);
        defaultLevel.addGomme(19,1);

        // Ajout des super gommes
        defaultLevel.addGomme(1,17);
        defaultLevel.addGomme(19,17);

        //ajout target pour scatter (patrouille)
        targets.put("TopLeft", defaultLevel.addTarget(1,1));
        targets.put("TopRight",defaultLevel.addTarget(1,17));
        targets.put("BottomLeft",defaultLevel.addTarget(19,1));
        targets.put("BottomRight",defaultLevel.addTarget(19,17));
    }

    private void reachTarget(Ghost ghost,PhysicEntity target){

        PhysicEntity ghostPhysic = ghost.getPhysicEntity();
        Set<MoveDirection> forbiddenDirections = ghost.getForbiddenDirection();

        //Calcul de la distance horizontale et verticale entre pacman et le fantome
        int playerXmiddle = (target.getX() + target.getWidth()) / 2;
        int playerYmiddle = (target.getY() + target.getHeight()) / 2;
        int ghostXmiddle = (ghostPhysic.getX() + ghostPhysic.getWidth()) / 2;
        int ghostYmiddle = (ghostPhysic.getY() + ghostPhysic.getHeight()) / 2;
        int xDistance = playerXmiddle - ghostXmiddle;
        int yDistance = playerYmiddle - ghostYmiddle;

        //Définition des directions horizontales et verticales
        MoveDirection xDirection = xDistance == 0 ? null
                : xDistance < 0 ? MoveDirection.LEFT : MoveDirection.RIGHT;
        MoveDirection yDirection = yDistance == 0 ? null
                : yDistance < 0 ? MoveDirection.UP : MoveDirection.DOWN;

        //Définition de la prochaine direction
        MoveDirection nextDirection = Math.abs(xDistance) > Math.abs(yDistance) ? xDirection : yDirection;

        //Vérification des collisions
        boolean somethingUP     = physicsEngine().isSomethingUp(ghost) != null;
        boolean somethingRIGHT  = physicsEngine().isSomethingRight(ghost) != null;
        boolean somethingDOWN   = physicsEngine().isSomethingDown(ghost) != null;
        boolean somethingLEFT   = physicsEngine().isSomethingLeft(ghost) != null;

        //Détermination de la prochaine direction
        if (Math.abs(xDistance) <= 1 && Math.abs(yDistance) <= 1) {
            ghost.setCurrentDirection(null);
        } else if (nextDirection == MoveDirection.UP && !forbiddenDirections.contains(MoveDirection.UP) && !somethingUP) {
            ghost.setCurrentDirection(MoveDirection.UP);
            forbiddenDirections.clear();
        } else if (nextDirection == MoveDirection.DOWN && !forbiddenDirections.contains(MoveDirection.DOWN) && !somethingDOWN) {
            ghost.setCurrentDirection(MoveDirection.DOWN);
            forbiddenDirections.clear();
        } else if (nextDirection == MoveDirection.LEFT && !forbiddenDirections.contains(MoveDirection.LEFT) && !somethingLEFT) {
            ghost.setCurrentDirection(MoveDirection.LEFT);
            forbiddenDirections.clear();
        } else if (nextDirection == MoveDirection.RIGHT && !forbiddenDirections.contains(MoveDirection.RIGHT) && !somethingRIGHT) {
            ghost.setCurrentDirection(MoveDirection.RIGHT);
            forbiddenDirections.clear();
        } else {
            if (nextDirection == MoveDirection.UP || nextDirection == MoveDirection.DOWN) {
                if (forbiddenDirections.size() == 1 && forbiddenDirections.contains(nextDirection))
                    if (!somethingUP && nextDirection == MoveDirection.UP
                            || !somethingDOWN && nextDirection == MoveDirection.DOWN)
                        forbiddenDirections.clear();

                if (somethingRIGHT) forbiddenDirections.add(MoveDirection.RIGHT);
                if (somethingLEFT) forbiddenDirections.add(MoveDirection.LEFT);

                if (xDirection != null && !forbiddenDirections.contains(xDirection)) {
                    ghost.setCurrentDirection(xDirection);
                    forbiddenDirections.add(xDirection == MoveDirection.LEFT ? MoveDirection.RIGHT : MoveDirection.LEFT);
                } else if (!forbiddenDirections.contains(MoveDirection.LEFT)) {
                    ghost.setCurrentDirection(MoveDirection.LEFT);
                    forbiddenDirections.add(MoveDirection.RIGHT);
                } else if (!forbiddenDirections.contains(MoveDirection.RIGHT)) {
                    ghost.setCurrentDirection(MoveDirection.RIGHT);
                    forbiddenDirections.add(MoveDirection.LEFT);
                } else {
                    boolean removeXDirections = false;
                    if (xDirection != null)
                        ghost.setCurrentDirection(xDirection);
                    else if (nextDirection == MoveDirection.UP) {
                        forbiddenDirections.add(MoveDirection.UP);
                        if (!somethingDOWN) ghost.setCurrentDirection(MoveDirection.DOWN);
                        else removeXDirections = true;
                    } else {
                        forbiddenDirections.add(MoveDirection.DOWN);
                        if (!somethingUP) ghost.setCurrentDirection(MoveDirection.UP);
                        else removeXDirections = true;
                    }
                    if (removeXDirections)
                        forbiddenDirections.removeAll(Arrays.asList(MoveDirection.LEFT,MoveDirection.RIGHT));
                }
            }
            else if (nextDirection == MoveDirection.LEFT || nextDirection == MoveDirection.RIGHT) {
                if (forbiddenDirections.size() == 1 && forbiddenDirections.contains(nextDirection)) {
                    if (!somethingLEFT && nextDirection == MoveDirection.LEFT)
                        forbiddenDirections.clear();
                    else if (!somethingRIGHT && nextDirection == MoveDirection.RIGHT)
                        forbiddenDirections.clear();
                }

                if (somethingUP) forbiddenDirections.add(MoveDirection.UP);
                if (somethingDOWN) forbiddenDirections.add(MoveDirection.DOWN);

                if (yDirection != null && !forbiddenDirections.contains(yDirection)) {
                    ghost.setCurrentDirection(yDirection);
                    forbiddenDirections.add(yDirection == MoveDirection.UP ? MoveDirection.DOWN : MoveDirection.UP);
                }
                else if (!forbiddenDirections.contains(MoveDirection.UP)) {
                    ghost.setCurrentDirection(MoveDirection.UP);
                    forbiddenDirections.add(MoveDirection.DOWN);
                } else if (!forbiddenDirections.contains(MoveDirection.DOWN)) {
                    ghost.setCurrentDirection(MoveDirection.DOWN);
                    forbiddenDirections.add(MoveDirection.UP);
                } else {
                    boolean removeYDirections = false;
                    if (yDirection != null) ghost.setCurrentDirection(yDirection);
                    else if (nextDirection == MoveDirection.LEFT) {
                        forbiddenDirections.add(MoveDirection.LEFT);
                        if (!somethingRIGHT) ghost.setCurrentDirection(MoveDirection.RIGHT);
                        else removeYDirections = true;
                    } else {
                        forbiddenDirections.add(MoveDirection.RIGHT);
                        if (!somethingLEFT) ghost.setCurrentDirection(MoveDirection.LEFT);
                        else removeYDirections = true;
                    }
                    if (removeYDirections)
                        forbiddenDirections.removeAll(Arrays.asList(MoveDirection.UP, MoveDirection.DOWN));
                }
            }
        }
        if (forbiddenDirections.size() == 4) forbiddenDirections.clear();
        if (ghost.getCurrentDirection() != null) {
            callEventFromDirection(ghost, ghost.getCurrentDirection());
            graphicsEngine().bindAnimation(ghost, ghost.getAnimations().get(ghost.getCurrentDirection().name()));
        }
    }

    /**
     * Appliquer l'intelligence artificielle au fantome rose
     */
    private void applyPinkGhostAI() {
        Ghost ghost = ghosts.get("red");
        PhysicEntity playerPhysic = pacman.getPhysicEntity();
        reachTarget(ghost,playerPhysic);
    }


    protected void applyBlueGhostAI() {

        Ghost ghost = ghosts.get("blue");

        //setting new patrol zone depending on score
        if(currentLevel.getActualScore() == 0){
            ghost.getScatterPatrolZones().put("TopRight",false);
            ghost.getScatterPatrolZones().put("TopLeft",false);
            ghost.getScatterPatrolZones().put("BottomRight",true);
            ghost.getScatterPatrolZones().put("BottomLeft",false);
            ghost.setPatroleZoneReached(false);
        }else if(currentLevel.getActualScore() == 350){
            ghost.getScatterPatrolZones().put("TopRight",false);
            ghost.getScatterPatrolZones().put("TopLeft",false);
            ghost.getScatterPatrolZones().put("BottomRight",false);
            ghost.getScatterPatrolZones().put("BottomLeft",true);
            ghost.setPatroleZoneReached(false);
        }else if(currentLevel.getActualScore() == 700){
            ghost.getScatterPatrolZones().put("TopRight",true);
            ghost.getScatterPatrolZones().put("TopLeft",false);
            ghost.getScatterPatrolZones().put("BottomRight",false);
            ghost.getScatterPatrolZones().put("BottomLeft",false);
            ghost.setPatroleZoneReached(false);
        }else if (currentLevel.getActualScore() == 1200){
            ghost.getScatterPatrolZones().put("TopRight",false);
            ghost.getScatterPatrolZones().put("TopLeft",true);
            ghost.getScatterPatrolZones().put("BottomRight",false);
            ghost.getScatterPatrolZones().put("BottomLeft",false);
            ghost.setPatroleZoneReached(false);
        }

        //vérifie si on a atteint la zone de patrouille
        if (!ghost.isPatroleZoneReached()){

            //on enregistre la position du fantome
            PhysicEntity ghostPhysic = ghost.getPhysicEntity();
            int ghostX = ghostPhysic.getX();
            int ghostY = ghostPhysic.getY();

            //on décide de l'endroit ou aller en fonction de la zone visée
            if (ghost.getScatterPatrolZones().get("TopRight")){
                //on récupère l'entité cible qui est en l'occurence dans le coin haut droit
                PhysicEntity targetTopRight = targets.get("TopRight").getPhysicEntity();
                //pn cherche a rejoindre cette cible
                reachTarget(ghost,targetTopRight);
                //si on atteint la cible, donc que les coordonnées sont les memes, on passe a la suite
                int targetX = (targetTopRight.getX());
                int targetY = (targetTopRight.getY());
                if ((ghostX <= (targetX+4)) && (ghostX >= (targetX-4)) && (ghostY <= (targetY+4)) && (ghostY >= (targetY-4))){
                    ghost.setPatroleZoneReached(true);
                }
                return;
            }
            else if (ghost.getScatterPatrolZones().get("TopLeft")){
                PhysicEntity targetTopLeft = targets.get("TopLeft").getPhysicEntity();
                reachTarget(ghost,targetTopLeft);
                int targetX = (targetTopLeft.getX());
                int targetY = (targetTopLeft.getY());
                if ((ghostX <= (targetX+4)) && (ghostX >= (targetX-4)) && (ghostY <= (targetY+4)) && (ghostY >= (targetY-4))){
                    ghost.setPatroleZoneReached(true);
                }
                return;
            }
            else if (ghost.getScatterPatrolZones().get("BottomRight")){

                PhysicEntity targetBottomRight = targets.get("BottomRight").getPhysicEntity();
                reachTarget(ghost,targetBottomRight);
                int targetX = (targetBottomRight.getX());
                int targetY = (targetBottomRight.getY());

                /*System.out.println(targetX+4);
                System.out.println(targetX-4);
                System.out.println(ghostX);
                System.out.println(targetY+4);
                System.out.println(targetY-4);
                System.out.println(ghostY);*/
                if ((ghostX <= (targetX+4)) && (ghostX >= (targetX-4)) && (ghostY <= (targetY+4)) && (ghostY >= (targetY-4))){
                    System.out.println("pass");
                    ghost.setPatroleZoneReached(true);
                    System.out.println(ghost.isPatroleZoneReached());
                }
                return;
            }
            else if (ghost.getScatterPatrolZones().get("BottomLeft")){
                PhysicEntity targetBottomLeft = targets.get("BottomLeft").getPhysicEntity();
                reachTarget(ghost,targetBottomLeft);
                int targetX = (targetBottomLeft.getX());
                int targetY = (targetBottomLeft.getY());

                if ((ghostX <= (targetX+4)) && (ghostX >= (targetX-4)) && (ghostY <= (targetY+4)) && (ghostY >= (targetY-4))){
                    ghost.setPatroleZoneReached(true);
                }
                return;
            }
        } else {
            //une fois dans la zone on patrouille au hasard
            ghost.setCurrentDirection(updateGhostDirectionWithRandomness(ghost));
        }
        if (ghost.getCurrentDirection() != null) {
            callEventFromDirection(ghost, ghost.getCurrentDirection());
            graphicsEngine().bindAnimation(ghost, ghost.getAnimations().get(ghost.getCurrentDirection().name()));
        }
    }

    protected MoveDirection updateGhostDirectionWithRandomness(Ghost ghost) {
        boolean somethingUP     = physicsEngine().isSomethingUp(ghost) != null;
        boolean somethingRIGHT  = physicsEngine().isSomethingRight(ghost) != null;
        boolean somethingDOWN   = physicsEngine().isSomethingDown(ghost) != null;
        boolean somethingLEFT   = physicsEngine().isSomethingLeft(ghost) != null;

        int nombreAleatoire = 1 + (int)(Math.random() * ((4 - 1) + 1));
        HashMap<String,Boolean> keepDirection = ghost.getKeepDirection() ;
        System.out.println(nombreAleatoire);

        if (keepDirection.get("KeepUp") && somethingLEFT && somethingRIGHT && !somethingUP){
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.UP;
        }
        else if (keepDirection.get("KeepDown") && somethingLEFT && somethingRIGHT && !somethingDOWN){
            ghost.setPreviousDirection(MoveDirection.DOWN);
            return MoveDirection.DOWN;
        }
        else if (keepDirection.get("KeepRight") && somethingUP && somethingDOWN && !somethingRIGHT){
            ghost.setPreviousDirection(MoveDirection.RIGHT);
            return MoveDirection.RIGHT;
        }
        else if (keepDirection.get("KeepLeft") && somethingUP && somethingDOWN && !somethingLEFT){
            ghost.setPreviousDirection(MoveDirection.LEFT);
            return MoveDirection.LEFT;
        }
        ghost.getKeepDirection().put("KeepUp",false);
        ghost.getKeepDirection().put("KeepRight",false);
        ghost.getKeepDirection().put("KeepDown",false);
        ghost.getKeepDirection().put("KeepLeft",false);

        if (nombreAleatoire == 1 && !somethingUP && ghost.getPreviousDirection() != MoveDirection.DOWN){
            ghost.getKeepDirection().put("KeepUp",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.UP;
        }
        else if (nombreAleatoire == 1 && !somethingRIGHT && ghost.getPreviousDirection() != MoveDirection.LEFT){
            ghost.getKeepDirection().put("KeepRight",true);
            ghost.setPreviousDirection(MoveDirection.RIGHT);
            return MoveDirection.RIGHT;
        }
        else if (nombreAleatoire == 1 && !somethingLEFT && ghost.getPreviousDirection() != MoveDirection.RIGHT){
            ghost.getKeepDirection().put("KeepLeft",true);
            ghost.setPreviousDirection(MoveDirection.LEFT);
            return MoveDirection.LEFT;
        }
        else if (nombreAleatoire == 1){
            ghost.getKeepDirection().put("KeepDown",true);
            ghost.setPreviousDirection(MoveDirection.DOWN);
            return MoveDirection.DOWN;
        }

        if (nombreAleatoire == 2 && !somethingRIGHT  && ghost.getPreviousDirection() != MoveDirection.LEFT){
            ghost.getKeepDirection().put("KeepRight",true);
            ghost.setPreviousDirection(MoveDirection.RIGHT);
            return MoveDirection.RIGHT;
        }
        else if (nombreAleatoire == 2 && !somethingDOWN && ghost.getPreviousDirection() != MoveDirection.UP){
            ghost.getKeepDirection().put("KeepDown",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.DOWN;
        }
        else if (nombreAleatoire == 2 && !somethingUP && ghost.getPreviousDirection() != MoveDirection.DOWN){
            ghost.getKeepDirection().put("KeepUp",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.UP;
        }
        else if (nombreAleatoire == 2) {
            ghost.getKeepDirection().put("KeepLeft",true);
            ghost.setPreviousDirection(MoveDirection.LEFT);
            return MoveDirection.LEFT;
        }

        if (nombreAleatoire == 3 && !somethingDOWN && ghost.getPreviousDirection() != MoveDirection.UP){
            ghost.getKeepDirection().put("KeepDown",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.DOWN;
        }
        else if (nombreAleatoire == 3 && !somethingLEFT && ghost.getPreviousDirection() != MoveDirection.RIGHT){
            ghost.getKeepDirection().put("KeepLeft",true);
            ghost.setPreviousDirection(MoveDirection.LEFT);
            return MoveDirection.LEFT;
        }
        else if (nombreAleatoire == 3 && !somethingRIGHT  && ghost.getPreviousDirection() != MoveDirection.LEFT){
            ghost.getKeepDirection().put("KeepRight",true);
            ghost.setPreviousDirection(MoveDirection.RIGHT);
            return MoveDirection.RIGHT;
        }
        else if (nombreAleatoire == 3){
            ghost.getKeepDirection().put("KeepUp",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.UP;
        }

        if (nombreAleatoire == 4 && !somethingLEFT && ghost.getPreviousDirection() != MoveDirection.RIGHT){
            ghost.getKeepDirection().put("KeepLeft",true);
            ghost.setPreviousDirection(MoveDirection.LEFT);
            return MoveDirection.LEFT;
        }
        else if (nombreAleatoire == 4 && !somethingDOWN && ghost.getPreviousDirection() != MoveDirection.UP){
            ghost.getKeepDirection().put("KeepDown",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.DOWN;
        }
        else if (nombreAleatoire == 4 && !somethingUP  && ghost.getPreviousDirection() != MoveDirection.DOWN){
            ghost.getKeepDirection().put("KeepUp",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.UP;
        }
        else if (nombreAleatoire == 4){
            ghost.getKeepDirection().put("KeepRight",true);
            ghost.setPreviousDirection(MoveDirection.RIGHT);
            return MoveDirection.RIGHT;
        }

        ghost.setPreviousDirection(MoveDirection.RIGHT);
        return MoveDirection.RIGHT; //default move
        //random direction

    }


    /**
     * Changer la direction de pacman
     * @param direction direction
     */
    private void switchPacmanDirection(MoveDirection direction) {
        pacman.setCurrentAnimationID(pacman.getAnimations().get(direction.name()));
        if (!graphicsEngine().getAnimation(pacman.getCurrentAnimationID()).isPlaying())
            graphicsEngine().playPauseAnimation(pacman.getCurrentAnimationID());
        setEntityNextDirection(pacman,direction);
    }

    /**
     * Vérifier les collisions de Pacman
     */
    private void checkPacmanCollisions() {

        ArrayList<PhysicEntity> collidingEntities = new ArrayList<>();
        collidingEntities.add(physicsEngine().isSomethingUp(pacman));
        collidingEntities.add(physicsEngine().isSomethingRight(pacman));
        collidingEntities.add(physicsEngine().isSomethingDown(pacman));
        collidingEntities.add(physicsEngine().isSomethingLeft(pacman));

        if (pacman.getCurrentAnimationID() != 0)
            if (graphicsEngine().getAnimation(pacman.getCurrentAnimationID()).isPlaying())
                graphicsEngine().playPauseAnimation(pacman.getCurrentAnimationID());

        for (Ghost ghost : ghosts.values()) {
            if (collidingEntities.contains(ghost.getPhysicEntity())) {

                // Si les fantômes sont appeurés
                if (ghostFear) {
                    this.currentLevel.updateActualScore(this.currentLevel.getActualScore() + 250);
                    physicsEngine().removeCollisions(pacman.getPhysicEntity(), ghost.getPhysicEntity());
                    ghost.setEaten(true);
                    updateEatenGhostSkin();

                    new Thread( () -> {
                        long startTime = System.currentTimeMillis();
                        while(((System.currentTimeMillis() - startTime)/1000) < 7) {;}
                        physicsEngine().addCollisions(pacman, ghost);
                        ghost.setEaten(false);
                        updateEatenGhostSkin();
                    }).start();
                }

                else {
                    currentLevel.updateLives();
                    soundEngine().playSound(pacman.getDeath1Sound());
                    soundEngine().getSounds().get(pacman.getDeath1Sound()).addLineListener(e -> {
                        if (e.getType() == LineEvent.Type.STOP) {
                            soundEngine().playSound(pacman.getDeath2Sound());
                        }
                    });

                    kernelEngine.pauseEvents();
                    graphicsEngine().bindAnimation(pacman, pacman.getAnimations().get("DEATH"));
                    new Thread(() -> {
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        kernelEngine.resumeEvents();
                        if (currentLevel.getLivesCount() > 0) {
                            playLevel(currentLevel);
                        } else {
                            showEndGameView();
                        }
                    }).start();
                }
            }
        }
    }

    /**
     * Incrémente le volume 5 par 5
     */
    private void incrementGlobalVolume(){
        soundEngine().incrementGlobalVolume();
        graphicsEngine().bindText(currentVolume, "Volume is : " + (int)(soundEngine().getGlobalvolume()*100), new Color(255,255,255), 20, true);
    }

    /**
     * Décrémente le son 5 par 5
     * le son étant un logarithme la décrémentation ne se fait pas très bien et
     * nous nous retrouvons avec des nombre du style 24 de volume  mais cela n'est pas important
     */
    private void decrementGlobalVolume(){
        soundEngine().decrementGlobalVolume();
        graphicsEngine().bindText(currentVolume, "Volume is : " + (int)(soundEngine().getGlobalvolume()*100), new Color(255,255,255), 20, true);
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
                entityNearby = physicsEngine().isSomethingUp(entity);
                if (entityNearby == null)
                    entity.setCurrentDirection(MoveDirection.UP);
                break;
            case RIGHT:
                entityNearby = physicsEngine().isSomethingRight(entity);
                if (entityNearby == null)
                    entity.setCurrentDirection(MoveDirection.RIGHT);
                break;
            case DOWN:
                entityNearby = physicsEngine().isSomethingDown(entity);
                if (entityNearby == null)
                    entity.setCurrentDirection(MoveDirection.DOWN);
                break;
            case LEFT:
                entityNearby = physicsEngine().isSomethingLeft(entity);
                if (entityNearby == null)
                    entity.setCurrentDirection(MoveDirection.LEFT);
                break;
            default:
                break;
        }
        if (entity.getCurrentDirection() != null) {
            callEventFromDirection(entity, entity.getCurrentDirection());
            graphicsEngine().bindAnimation(entity, entity.getAnimations().get(entity.getCurrentDirection().name()));
        }
    }

    /**
     * Appeler la méthode de déplacement en fonction de la direction courante
     */
    private void callEventFromDirection(Entity entity, MoveDirection direction) {
        if (direction != null) {
            switch (direction) {
                case UP:
                    physicsEngine().goUp(entity);
                    break;
                case RIGHT:
                    physicsEngine().goRight(entity);
                    break;
                case DOWN:
                    physicsEngine().goDown(entity);
                    break;
                case LEFT:
                    physicsEngine().goLeft(entity);
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
    protected Level generateLevel(int rows, int cols) {
        Level level = new Level(this,rows,cols);
        levels.add(level);
        return level;
    }

    /**
     * Faire apparaitre les joueurs sur le niveau actuel
     */
    protected void spawnPlayersOnLevel() {
        currentLevel.spawnPlayer(15,9);
        currentLevel.spawnGhost(ghosts.get("red"),11,9);
        currentLevel.spawnGhost(ghosts.get("blue"),11,8);
        currentLevel.spawnGhost(ghosts.get("pink"),9,9);
        currentLevel.spawnGhost(ghosts.get("orange"),9,10);
    }

    /**
     * Jouer un niveau
     * @param level level
     */
    protected void playLevel(Level level) {
        ioEngine().resetLastPressedKey();
        currentLevel = level;
        if (currentLevel.getLivesCount() == 3) {
            soundEngine().playSound("gameStart");
            kernelEngine.pauseEvents();
            new Thread(() -> {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                kernelEngine.resumeEvents();
            }).start();
        }
        spawnPlayersOnLevel();
        graphicsEngine().bindScene(currentLevel.getScene());
    }

    /**
     * Mettre à jour les textures des fantômes selon s'ils
     * sont appeurés ou non
     */
    public void updateFearGhostSkin() {
        if (ghostFear) {
            for (Ghost ghost : ghosts.values()) {
                if (!ghost.getEaten()) {
                    graphicsEngine().bindTexture(ghost, textures, 8, 1);

                    int moveUP = ghost.getAnimations().get(MoveDirection.UP.name());
                    graphicsEngine().clearFrameOfAnimation(moveUP);
                    graphicsEngine().addFrameToAnimation(moveUP, 8, 1);
                    graphicsEngine().addFrameToAnimation(moveUP, 8, 2);

                    int moveDOWN = ghost.getAnimations().get(MoveDirection.DOWN.name());
                    graphicsEngine().clearFrameOfAnimation(moveDOWN);
                    graphicsEngine().addFrameToAnimation(moveDOWN, 8, 1);
                    graphicsEngine().addFrameToAnimation(moveDOWN, 8, 2);

                    int moveLEFT = ghost.getAnimations().get(MoveDirection.LEFT.name());
                    graphicsEngine().clearFrameOfAnimation(moveLEFT);
                    graphicsEngine().addFrameToAnimation(moveLEFT, 8, 1);
                    graphicsEngine().addFrameToAnimation(moveLEFT, 8, 2);

                    int moveRIGHT = ghost.getAnimations().get(MoveDirection.RIGHT.name());
                    graphicsEngine().clearFrameOfAnimation(moveRIGHT);
                    graphicsEngine().addFrameToAnimation(moveRIGHT, 8, 1);
                    graphicsEngine().addFrameToAnimation(moveRIGHT, 8, 2);
                }
            }
        }
        else {
            for (Ghost ghost : ghosts.values()) {
                if (!ghost.getEaten()) {
                    graphicsEngine().bindTexture(ghost, textures, ghost.defaultTextureCoords[0], ghost.defaultTextureCoords[1]);
                    int moveUP = ghost.getAnimations().get(MoveDirection.UP.name());
                    graphicsEngine().clearFrameOfAnimation(moveUP);
                    int moveDOWN = ghost.getAnimations().get(MoveDirection.DOWN.name());
                    graphicsEngine().clearFrameOfAnimation(moveDOWN);
                    int moveLEFT = ghost.getAnimations().get(MoveDirection.LEFT.name());
                    graphicsEngine().clearFrameOfAnimation(moveLEFT);
                    int moveRIGHT = ghost.getAnimations().get(MoveDirection.RIGHT.name());
                    graphicsEngine().clearFrameOfAnimation(moveRIGHT);
                    ghost.initAnimations(textures);
                }
            }
        }
    }

    /**
     * Mettre à jour le skin d'un fantome mangé
     */
    public void updateEatenGhostSkin() {
        for (Ghost ghost: ghosts.values()) {
            if (ghost.getEaten()) {
                graphicsEngine().bindTexture(ghost, textures, 7, 1);

                int moveUP = ghost.getAnimations().get(MoveDirection.UP.name());
                graphicsEngine().clearFrameOfAnimation(moveUP);
                graphicsEngine().addFrameToAnimation(moveUP, 7, 1);
                graphicsEngine().addFrameToAnimation(moveUP, 7, 2);

                int moveDOWN = ghost.getAnimations().get(MoveDirection.DOWN.name());
                graphicsEngine().clearFrameOfAnimation(moveDOWN);
                graphicsEngine().addFrameToAnimation(moveDOWN, 7, 1);
                graphicsEngine().addFrameToAnimation(moveDOWN, 7, 2);

                int moveLEFT = ghost.getAnimations().get(MoveDirection.LEFT.name());
                graphicsEngine().clearFrameOfAnimation(moveLEFT);
                graphicsEngine().addFrameToAnimation(moveLEFT, 7, 1);
                graphicsEngine().addFrameToAnimation(moveLEFT, 7, 2);

                int moveRIGHT = ghost.getAnimations().get(MoveDirection.RIGHT.name());
                graphicsEngine().clearFrameOfAnimation(moveRIGHT);
                graphicsEngine().addFrameToAnimation(moveRIGHT, 7, 1);
                graphicsEngine().addFrameToAnimation(moveRIGHT, 7, 2);
            }
            else {
                if (!ghostFear) {
                    graphicsEngine().bindTexture(ghost, textures, ghost.defaultTextureCoords[0], ghost.defaultTextureCoords[1]);
                    int moveUP = ghost.getAnimations().get(MoveDirection.UP.name());
                    graphicsEngine().clearFrameOfAnimation(moveUP);
                    int moveDOWN = ghost.getAnimations().get(MoveDirection.DOWN.name());
                    graphicsEngine().clearFrameOfAnimation(moveDOWN);
                    int moveLEFT = ghost.getAnimations().get(MoveDirection.LEFT.name());
                    graphicsEngine().clearFrameOfAnimation(moveLEFT);
                    int moveRIGHT = ghost.getAnimations().get(MoveDirection.RIGHT.name());
                    graphicsEngine().clearFrameOfAnimation(moveRIGHT);
                    ghost.initAnimations(textures);
                }
            }
        }
    }

    /**
     * Afficher la vue de fin de jeu
     */
    protected void showEndGameView() {
        GraphicEntity score = endGameView.getEntities().get(1);
        graphicsEngine().bindText(score.getParent(), "Score : " + currentLevel.getActualScore(),
                new Color(255,255,255), 20, true);
        graphicsEngine().bindScene(endGameView);
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

    public Map<String,Ghost> getGhosts() { return ghosts; }

    public ArrayList<Level> getLevels() { return levels; }

    // SETTERS //

    /**
     * Mettre à jour la peur des fantômes
     * et appel au changement de textures
     *
     * Mise en place d'un timer, à la fin de celui-ci les fantômes redeviennent normaux
     * @param fear
     */
    public void setGhostFear(boolean fear) {
        this.ghostFear = fear;
        updateFearGhostSkin();

        new Thread( () -> {
            long startTime = System.currentTimeMillis();
            while(((System.currentTimeMillis() - startTime)/1000) < 5) {;}

            this.ghostFear = false;
            updateFearGhostSkin();
        }).start();
    }
}
