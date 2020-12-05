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

import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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
     * Service d'exécution
     */
    private ExecutorService executorService;

    /**
     * Tâches actives
     */
    private final ArrayList<Future<Void>> tasks;

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
    private final ArrayList<Level> levels;

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

    /**
     * Liste des entités à suivre
     */
    private final Map<TARGETS,Entity> targets = new HashMap<>();

    /**
     * Fantomes
     */
    private Map<GHOSTS,Ghost> ghosts;

    /**
     * Durée par défaut du pouvoir pour manger les fantomes
     */
    private final int eatPowerUpInitialTime = 8;

    /**
     * Durée par défaut du pouvoir pour casser les murs
     */
    private final int breakPowerUpInitialTime = 5;

    /**
     * Booléen pour savoir si le pouvoir pour manger les fantomes est actif
     */
    private final AtomicBoolean isEatPowerUpEnabled;

    /**
     * Booléen pour savoir si le pouvoir de briser les murs est actif
     */
    private final AtomicBoolean isBreakPowerUpEnabled;

    /**
     * Temps restant pour le pouvoir de manger les fantomes
     */
    private final AtomicInteger eatPowerUpTimeout;

    /**
     * Temps restant pour le pouvoir de casser les murs
     */
    private final AtomicInteger breakPowerUpTimeout;

    /**
     * Chronomètre
     */
    private final AtomicInteger timer;

    /**
     * Fantomes disponibles
     */
    public enum GHOSTS {RED, BLUE, ORANGE, PINK}

    /**
     * Lieux spécifiques
     */
    public enum TARGETS {TOP_L,TOP_R,BOT_L,BOT_R,BASE}

    /**
     * Constructeur
     */
    public Gameplay() {
        kernelEngine = new KernelEngine();
        textures = kernelEngine.getGraphicsEngine().loadSpriteSheet("assets/sprite_sheet.png", 12, 11);
        levels = new ArrayList<>();
        tasks = new ArrayList<>();
        isEatPowerUpEnabled = new AtomicBoolean(false);
        eatPowerUpTimeout = new AtomicInteger(eatPowerUpInitialTime);
        isBreakPowerUpEnabled = new AtomicBoolean(false);
        breakPowerUpTimeout = new AtomicInteger(breakPowerUpInitialTime);
        timer = new AtomicInteger(0);
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
     * Initialiser les joueurs
     */
    private void initPlayers() {
        pacman = new Pacman(this);
        ghosts = new HashMap<>();
        ghosts.put(GHOSTS.RED,    new Ghost(this, GHOSTS.RED));
        ghosts.put(GHOSTS.BLUE,   new Ghost(this, GHOSTS.BLUE));
        ghosts.put(GHOSTS.PINK,   new Ghost(this, GHOSTS.PINK));
        ghosts.put(GHOSTS.ORANGE, new Ghost(this, GHOSTS.ORANGE));
    }

    /**
     * Initialiser les évènements du jeu
     */
    private void initEvents() {
        //Jouer un niveau
        kernelEngine.addEvent("playLevel", () -> playLevel(levels.get(0)));
        //Rejouer un niveau
        kernelEngine.addEvent("restartLevel", this::restartLevel);
        //Déplacer le fantome rouge
        kernelEngine.addEvent("moveREDGhost", this::applyRedGhostAI);
        //Déplacer le fantome Bleu
        kernelEngine.addEvent("moveBLUEGhost", this::applyBlueGhostAI);
        //Déplacer le fantome orange
        kernelEngine.addEvent("moveORANGEGhost", this::applyOrangeGhostAI);
        //Déplacer fantome rose
        kernelEngine.addEvent("movePINKGhost", this::applyPinkGhostAI);
        //Déplacement fantome bleu craintif
        kernelEngine.addEvent("moveFearBLUEGhost", () -> applyGhostFearAI(ghosts.get(GHOSTS.BLUE)));
        //Déplacement fantome rouge craintif
        kernelEngine.addEvent("moveFearREDGhost", () -> applyGhostFearAI(ghosts.get(GHOSTS.RED)));
        //Déplacement fantome orange craintif
        kernelEngine.addEvent("moveFearORANGEGhost", () -> applyGhostFearAI(ghosts.get(GHOSTS.ORANGE)));
        //Déplacement fantome rose craintif
        kernelEngine.addEvent("moveFearPINKGhost", () -> applyGhostFearAI(ghosts.get(GHOSTS.PINK)));
        //Déplacement fantôme Bleu vers la base
        kernelEngine.addEvent("moveBaseBLUEGhost", () -> applyBaseAI(ghosts.get(GHOSTS.BLUE)));
        //Déplacement fantôme Rouge vers la base
        kernelEngine.addEvent("moveBaseREDGhost", () -> applyBaseAI(ghosts.get(GHOSTS.RED)));
        //Déplacement fantôme Orange vers la base
        kernelEngine.addEvent("moveBaseORANGEGhost", () -> applyBaseAI(ghosts.get(GHOSTS.ORANGE)));
        //Déplacement fantôme Rose vers la base
        kernelEngine.addEvent("moveBasePINKGhost", () -> applyBaseAI(ghosts.get(GHOSTS.PINK)));
        //Se déplacer vers le haut
        kernelEngine.addEvent("pacmanGoUp", () -> switchPacmanDirection(MoveDirection.UP));
        //Se déplacer vers la droite
        kernelEngine.addEvent("pacmanGoRight", () -> switchPacmanDirection(MoveDirection.RIGHT));
        //Se déplacer vers le bas
        kernelEngine.addEvent("pacmanGoDown", () -> switchPacmanDirection(MoveDirection.DOWN));
        //Se déplacer vers la gauche
        kernelEngine.addEvent("pacmanGoLeft", () -> switchPacmanDirection(MoveDirection.LEFT));
        //Augmenter le volume
        kernelEngine.addEvent("upVolume", this::incrementGlobalVolume);
        //Baisser le volume
        kernelEngine.addEvent("downVolume", this::decrementGlobalVolume);
        //Lorsqu'il y a une collision avec pacman
        kernelEngine.addEvent("pacmanOnCollision", this::checkPacmanCollisions);

        //Liaison des évènements du niveau
        ioEngine().bindEventOnLastKey(KeyEvent.VK_UP, "pacmanGoUp");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_RIGHT, "pacmanGoRight");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_DOWN, "pacmanGoDown");
        ioEngine().bindEventOnLastKey(KeyEvent.VK_LEFT, "pacmanGoLeft");
        physicsEngine().bindEventOnCollision(pacman, "pacmanOnCollision");
        for (Ghost ghost : ghosts.values())
            bindGhostInitialAI(ghost);
    }

    /**
     * Initialiser les sons du jeu
     */
    private void initSounds() {
        soundEngine().loadSound("munch_1.wav","munch1");
        soundEngine().loadSound("munch_2.wav","munch2");
        soundEngine().loadSound("game_start.wav","gameStart");
        soundEngine().loadSound("death_1.wav","death1");
        soundEngine().loadSound("death_2.wav","death2");
        soundEngine().loadSound("siren_1.wav", "siren1");
        soundEngine().loadSound("siren_2.wav", "siren2");
        soundEngine().loadSound("power_pellet.wav", "powerup");
        soundEngine().loadSound("eat_ghost.wav", "eatGhost");
        soundEngine().loadSound("eat_fruit.wav", "eatGomme");
        soundEngine().loadSound("intermission.wav", "win");
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
        ioEngine().bindEventOnClick(volumeMinus,"upVolume");

        physicsEngine().resize(currentVolume,200,50);
        physicsEngine().move(currentVolume, 100,337);
        graphicsEngine().bindColor(currentVolume,50,50,50);
        graphicsEngine().bindText(currentVolume, "Volume is : " + soundEngine().getGlobalVolume(),
                new Color(255,255,255), 20, true);
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
        ioEngine().bindEventOnClick(newGame,"restartLevel");
    }

    /**
     * Initialiser le niveau par défaut
     */
    private void initDefaultLevel() {
        //Level par défaut
        Level defaultLevel = generateLevel(21,21);

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
        wallRows.put(8, new int[]{-1, 0, 1, 2, 3, 5, 7, 8, 10, 11, 13, 15, 16, 17, 18, 19});
        wallRows.put(9, new int[]{7, 11});
        wallRows.put(10, new int[]{-1, 0, 1, 2, 3, 5, 7, 8, 9, 10, 11, 13, 15, 16, 17, 18, 19});
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
                defaultLevel.addWall(row.getKey(),col + 1);

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
        ballRows.put(15, new int[]{1, 2, 4, 5, 6, 7, 10, 11, 12, 13, 14, 16, 17});
        ballRows.put(16, new int[]{2, 4, 6, 12, 14, 16});
        ballRows.put(17, new int[]{1, 2, 3, 4, 6, 7, 8, 10, 11, 12, 14, 15, 16, 17});
        ballRows.put(18, new int[]{1, 8, 10, 17});
        ballRows.put(19, new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});

        for (Map.Entry<Integer,int[]> row : ballRows.entrySet())
            for (int col : row.getValue())
                defaultLevel.addBall(row.getKey(),col + 1);

        //Ajout de la barrière
        defaultLevel.addFence(8, 10);

        //Ajout des portails de téléportation
        defaultLevel.addTeleportationPortal(9, 0, 9, 20, MoveDirection.LEFT);
        defaultLevel.addTeleportationPortal(9, 20, 9, 0, MoveDirection.RIGHT);

        // Ajout des super gommes
        defaultLevel.addGomme(1,2);
        defaultLevel.addGomme(19,2);
        defaultLevel.addGomme(1,18);
        defaultLevel.addGomme(19,18);

        //Ajout du super pouvoir pour casser les murs
        defaultLevel.addBreaker(15,9);

        //ajout target pour scatter (patrouille)
        targets.put(TARGETS.TOP_L, defaultLevel.getMatrixEntity(1,2));
        targets.put(TARGETS.TOP_R,defaultLevel.getMatrixEntity(1,18));
        targets.put(TARGETS.BOT_L,defaultLevel.getMatrixEntity(19,2));
        targets.put(TARGETS.BOT_R,defaultLevel.getMatrixEntity(19,18));
        targets.put(TARGETS.BASE, defaultLevel.getMatrixEntity(7,10));

        //Réduction de la taille pour ne pas voir les extrémités
        defaultLevel.setVisiblePart(30,0,
                defaultLevel.getScene().getHeight(),defaultLevel.getScene().getWidth() - 30);
    }

    /**
     * Téléporter une entité
     */
    public void teleportPlayer(Player player, int x, int y, MoveDirection direction) {
        if (player.getCurrentDirection() == direction) {
            physicsEngine().move(player, x, y);
            setEntityNextDirection(player, direction);
        }
    }

    /**
     * Définir le volume global
     * @param volume volume
     */
    public void setGlobalVolume(int volume) {
        soundEngine().setGlobalVolume(volume);
        graphicsEngine().bindText(currentVolume, "Volume is : " + soundEngine().getGlobalVolume(),
                new Color(255,255,255), 20, true);
    }

    /**
     * Atteindre une entité
     * @param ghost fantome
     * @param target entité
     */
    private void reachTarget(Ghost ghost, Entity target){
        int xDistance = physicsEngine().getHorizontalDistance(ghost,target);
        int yDistance = physicsEngine().getVerticalDistance(ghost,target);

        //Définition des directions horizontales et verticales
        MoveDirection xDirection = Math.abs(xDistance) <= 1 ? null
                : xDistance > 0 ? MoveDirection.LEFT : MoveDirection.RIGHT;
        MoveDirection yDirection = Math.abs(yDistance) <= 1 ? null
                : yDistance > 0 ? MoveDirection.UP : MoveDirection.DOWN;

        Set<MoveDirection> forbiddenDirections = ghost.getForbiddenDirection();

        //Vérification des collisions
        boolean somethingUP    = physicsEngine().isSomethingUp(ghost)    != null;
        boolean somethingRIGHT = physicsEngine().isSomethingRight(ghost) != null;
        boolean somethingDOWN  = physicsEngine().isSomethingDown(ghost)  != null;
        boolean somethingLEFT  = physicsEngine().isSomethingLeft(ghost)  != null;

        //Détermination de la prochaine direction
        if (physicsEngine().getDistance(ghost, target) <= 1) {
            ghost.setCurrentDirection(null);
        } else if (yDirection == MoveDirection.UP && !forbiddenDirections.contains(MoveDirection.UP) && !somethingUP) {
            ghost.setCurrentDirection(MoveDirection.UP);
            forbiddenDirections.clear();
        } else if (yDirection == MoveDirection.DOWN && !forbiddenDirections.contains(MoveDirection.DOWN) && !somethingDOWN) {
            ghost.setCurrentDirection(MoveDirection.DOWN);
            forbiddenDirections.clear();
        } else if (xDirection == MoveDirection.LEFT && !forbiddenDirections.contains(MoveDirection.LEFT) && !somethingLEFT) {
            ghost.setCurrentDirection(MoveDirection.LEFT);
            forbiddenDirections.clear();
        } else if (xDirection == MoveDirection.RIGHT && !forbiddenDirections.contains(MoveDirection.RIGHT) && !somethingRIGHT) {
            ghost.setCurrentDirection(MoveDirection.RIGHT);
            forbiddenDirections.clear();
        } else if (yDirection != null) {
            if (forbiddenDirections.size() == 1 && forbiddenDirections.contains(yDirection))
                if (!somethingUP && yDirection == MoveDirection.UP
                        || !somethingDOWN && yDirection == MoveDirection.DOWN)
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
                if (yDirection == MoveDirection.UP) {
                    forbiddenDirections.add(MoveDirection.UP);
                    if (!somethingDOWN) ghost.setCurrentDirection(MoveDirection.DOWN);
                    else removeXDirections = true;
                } else {
                    forbiddenDirections.add(MoveDirection.DOWN);
                    if (!somethingUP) ghost.setCurrentDirection(MoveDirection.UP);
                    else removeXDirections = true;
                }
                if (removeXDirections)
                    forbiddenDirections.removeAll(Arrays.asList(MoveDirection.LEFT, MoveDirection.RIGHT));
            }
        } else {
            if (forbiddenDirections.size() == 1 && forbiddenDirections.contains(xDirection))
                if (!somethingLEFT && xDirection == MoveDirection.LEFT
                        || !somethingRIGHT && xDirection == MoveDirection.RIGHT)
                    forbiddenDirections.clear();

            if (somethingUP) forbiddenDirections.add(MoveDirection.UP);
            if (somethingDOWN) forbiddenDirections.add(MoveDirection.DOWN);

            if (!forbiddenDirections.contains(MoveDirection.UP)) {
                ghost.setCurrentDirection(MoveDirection.UP);
                forbiddenDirections.add(MoveDirection.DOWN);
            } else if (!forbiddenDirections.contains(MoveDirection.DOWN)) {
                ghost.setCurrentDirection(MoveDirection.DOWN);
                forbiddenDirections.add(MoveDirection.UP);
            } else {
                boolean removeYDirections = false;
                if (xDirection == MoveDirection.LEFT) {
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
        if (ghost.getCurrentDirection() != null) {
            callEventFromDirection(ghost, ghost.getCurrentDirection());
            updateGhostAnimation(ghost);
        }
    }

    /**
     * Mettre à jour l'animation d'un fantome
     * @param ghost fantome
     */
    private void updateGhostAnimation(Ghost ghost) {
        if (ghost.getCurrentDirection() != null) {
            String animationName = ghost.getCurrentDirection().name();
            if (ghost.getEaten().get())
                animationName = "eaten" + animationName;
            else if (isEatPowerUpEnabled.get() && !ghost.getEaten().get() && !ghost.getReturnBase().get())
                animationName = eatPowerUpTimeout.get() > 2 ? "fear" : "fearEnd";
            graphicsEngine().bindAnimation(ghost, ghost.getAnimations().get(animationName));
        }
    }

    /**
     * Appliquer l'intelligence artificielle au fantome rouge
     */
    private void applyRedGhostAI() {
        reachTarget(ghosts.get(GHOSTS.RED), pacman);
    }

    /**
     * Appliquer l'intelligence artificielle au fantome rose
     */
    private void applyPinkGhostAI() {
        reachTarget(ghosts.get(GHOSTS.PINK), pacman);
    }

    /**
     * Appliquer l'intelligence artificielle au fantome orange
     */
    private void applyOrangeGhostAI() {
        Ghost ghost = ghosts.get(GHOSTS.ORANGE);
        targetNearestCorner(ghost);
    }

    /**
     * Intelligence artificielle pour retourner à la barrière blanche
     * @param ghost fantome
     */
    private void applyBaseAI(Ghost ghost) {
        reachTarget(ghost, targets.get(TARGETS.BASE));
    }

    /**
     * Intelligence artificielle du fantome craintif
     * @param ghost fantome
     */
    protected void applyGhostFearAI(Ghost ghost) {
        targetNearestCorner(ghost);
    }

    /**
     * Diriger un fantome vers le coin le plus proche
     * @param ghost fantome
     */
    public void targetNearestCorner(Ghost ghost) {
        PhysicEntity playerPhysic = pacman.getPhysicEntity();
        PhysicEntity ghostPhysic = ghost.getPhysicEntity();

        //Calcul de la distance horizontale et verticale entre pacman et le fantome
        int playerXmiddle = (playerPhysic.getX() + playerPhysic.getWidth()) / 2;
        int playerYmiddle = (playerPhysic.getY() + playerPhysic.getHeight()) / 2;
        int ghostXmiddle = (ghostPhysic.getX() + ghostPhysic.getWidth()) / 2;
        int ghostYmiddle = (ghostPhysic.getY() + ghostPhysic.getHeight()) / 2;

        //distance entre joueur et fantome
        int distanceJoueurFantome = (int) Math.sqrt(((ghostXmiddle - playerXmiddle)*(ghostXmiddle - playerXmiddle))
                + ((ghostYmiddle - playerYmiddle)*(ghostYmiddle - playerYmiddle)));

        if (distanceJoueurFantome <= 120){
            //target un des coins
            if (playerXmiddle <= ghostXmiddle && playerYmiddle < ghostYmiddle)
                reachTarget(ghost,targets.get(TARGETS.BOT_R));
            else if (playerXmiddle > ghostXmiddle && playerYmiddle <= ghostYmiddle)
                reachTarget(ghost,targets.get(TARGETS.BOT_L));
            else if (playerXmiddle < ghostXmiddle && playerYmiddle <= ghostYmiddle)
                reachTarget(ghost,targets.get(TARGETS.TOP_R));
            else reachTarget(ghost,targets.get(TARGETS.TOP_L));
        }
        else {
            ghost.setCurrentDirection(updateGhostDirectionWithRandomness(ghost));
            if (ghost.getCurrentDirection() != null) {
                callEventFromDirection(ghost, ghost.getCurrentDirection());
                updateGhostAnimation(ghost);
            }
        }
    }

    /**
     * Appliquer l'intelligence artificielle au fantome bleu
     */
    private void applyBlueGhostAI() {
        Ghost ghost = ghosts.get(GHOSTS.BLUE);
        //setting new patrol zone depending on score
        if(currentLevel.getActualScore() == 0){
            ghost.getScatterPatrolZones().put(TARGETS.TOP_R,false);
            ghost.getScatterPatrolZones().put(TARGETS.TOP_L,false);
            ghost.getScatterPatrolZones().put(TARGETS.BOT_R,true);
            ghost.getScatterPatrolZones().put(TARGETS.BOT_L,false);
            ghost.setPatroleZoneReached(false);
        }else if(currentLevel.getActualScore() == 350){
            ghost.getScatterPatrolZones().put(TARGETS.TOP_R,false);
            ghost.getScatterPatrolZones().put(TARGETS.TOP_L,false);
            ghost.getScatterPatrolZones().put(TARGETS.BOT_R,false);
            ghost.getScatterPatrolZones().put(TARGETS.BOT_L,true);
            ghost.setPatroleZoneReached(false);
        }else if(currentLevel.getActualScore() == 700){
            ghost.getScatterPatrolZones().put(TARGETS.TOP_R,true);
            ghost.getScatterPatrolZones().put(TARGETS.TOP_L,false);
            ghost.getScatterPatrolZones().put(TARGETS.BOT_R,false);
            ghost.getScatterPatrolZones().put(TARGETS.BOT_L,false);
            ghost.setPatroleZoneReached(false);
        }else if (currentLevel.getActualScore() == 1200){
            ghost.getScatterPatrolZones().put(TARGETS.TOP_R,false);
            ghost.getScatterPatrolZones().put(TARGETS.TOP_L,true);
            ghost.getScatterPatrolZones().put(TARGETS.BOT_R,false);
            ghost.getScatterPatrolZones().put(TARGETS.BOT_L,false);
            ghost.setPatroleZoneReached(false);
        }
        if (ghost.isPatroleZoneReached()) {
            ghost.setCurrentDirection(updateGhostDirectionWithRandomness(ghost));
            if (ghost.getCurrentDirection() != null) {
                callEventFromDirection(ghost, ghost.getCurrentDirection());
                updateGhostAnimation(ghost);
            }
        } else {
            if (ghost.getScatterPatrolZones().get(TARGETS.TOP_R))
                checkPatrolZoneReached(ghost, targets.get(TARGETS.TOP_R));
            else if (ghost.getScatterPatrolZones().get(TARGETS.TOP_L))
                checkPatrolZoneReached(ghost, targets.get(TARGETS.TOP_L));
            else if (ghost.getScatterPatrolZones().get(TARGETS.BOT_R))
                checkPatrolZoneReached(ghost, targets.get(TARGETS.BOT_R));
            else if (ghost.getScatterPatrolZones().get(TARGETS.BOT_L))
                checkPatrolZoneReached(ghost, targets.get(TARGETS.BOT_L));
        }
    }

    /**
     * Vérifier si un fantome a atteint une zone de controle
     * @param ghost fantome
     * @param zone zone de controle
     */
    public void checkPatrolZoneReached(Ghost ghost, Entity zone) {
        PhysicEntity zonePhysic = zone.getPhysicEntity();
        PhysicEntity ghostPhysic = ghost.getPhysicEntity();
        reachTarget(ghost,zone);
        int targetX = (zonePhysic.getX());
        int targetY = (zonePhysic.getY());
        if ((ghostPhysic.getX() <= (targetX+4)) && (ghostPhysic.getX() >= (targetX-4))
                && (ghostPhysic.getY() <= (targetY+4)) && (ghostPhysic.getY() >= (targetY-4)))
            ghost.setPatroleZoneReached(true);
    }

    /**
     * Mettre à jour la direction du fantome aléatoirement
     * @param ghost fantome
     * @return direction
     */
    private MoveDirection updateGhostDirectionWithRandomness(Ghost ghost) {
        boolean somethingUP     = physicsEngine().isSomethingUp(ghost) != null;
        boolean somethingRIGHT  = physicsEngine().isSomethingRight(ghost) != null;
        boolean somethingDOWN   = physicsEngine().isSomethingDown(ghost) != null;
        boolean somethingLEFT   = physicsEngine().isSomethingLeft(ghost) != null;



        int random = 1 + (int)(Math.random() * ((4 - 1) + 1));

        HashMap<String,Boolean> keepDirection = ghost.getKeepDirection();

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


        //améliore le comportement
        //si le fantome est dans un espace ouvert a cause du cassage de murs
        //n'est pas infaillible mais permet de débloquer la situation a un moment ou un autre
       if (!somethingDOWN && !somethingUP && !somethingLEFT && !somethingRIGHT && currentLevel.isWallsAlreadyBroken() && !ghost.isHasbeenStuckInOpenSpace()){
            ghost.setHasbeenStuckInOpenSpace(true);
            if (ghost.getPreviousDirection() == MoveDirection.UP){
                ghost.getKeepDirection().put("KeepUp",true);
                ghost.setHasbeenStuckInOpenSpace(false);
                return MoveDirection.UP;
            }
            else if (ghost.getPreviousDirection() == MoveDirection.LEFT){
                ghost.getKeepDirection().put("KeepLeft",true);
                ghost.setHasbeenStuckInOpenSpace(false);
                return MoveDirection.LEFT;
            }
            else if (ghost.getPreviousDirection() == MoveDirection.RIGHT){
                ghost.getKeepDirection().put("KeepRight",true);
                ghost.setHasbeenStuckInOpenSpace(false);
                return MoveDirection.RIGHT;
            }
            else{
                ghost.getKeepDirection().put("KeepDown",true);
                ghost.setHasbeenStuckInOpenSpace(false);
                return MoveDirection.DOWN;
            }
        }
        else if (ghost.isHasbeenStuckInOpenSpace()){
            if (!somethingUP && ghost.getPreviousDirection() != MoveDirection.DOWN){
                ghost.getKeepDirection().put("KeepUp",true);
                ghost.setHasbeenStuckInOpenSpace(false);
                return MoveDirection.UP;

            }
            else if (!somethingLEFT && ghost.getPreviousDirection() != MoveDirection.RIGHT){
                ghost.getKeepDirection().put("KeepLeft",true);
                ghost.setHasbeenStuckInOpenSpace(false);
                return MoveDirection.LEFT;
            }
            else if (!somethingRIGHT && ghost.getPreviousDirection() != MoveDirection.LEFT){
                ghost.getKeepDirection().put("KeepRight",true);
                ghost.setHasbeenStuckInOpenSpace(false);
                return MoveDirection.RIGHT;
            }
            else if (!somethingDOWN && ghost.getPreviousDirection() != MoveDirection.UP){
                ghost.getKeepDirection().put("KeepDown",true);
                ghost.setHasbeenStuckInOpenSpace(false);
                return MoveDirection.DOWN;
            }
        }

        if (random == 1 && !somethingUP && ghost.getPreviousDirection() != MoveDirection.DOWN){
            ghost.getKeepDirection().put("KeepUp",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.UP;
        }
        else if (random == 1 && !somethingRIGHT && ghost.getPreviousDirection() != MoveDirection.LEFT){
            ghost.getKeepDirection().put("KeepRight",true);
            ghost.setPreviousDirection(MoveDirection.RIGHT);
            return MoveDirection.RIGHT;
        }
        else if (random == 1 && !somethingLEFT && ghost.getPreviousDirection() != MoveDirection.RIGHT){
            ghost.getKeepDirection().put("KeepLeft",true);
            ghost.setPreviousDirection(MoveDirection.LEFT);
            return MoveDirection.LEFT;
        }
        else if (random == 1){
            ghost.getKeepDirection().put("KeepDown",true);
            ghost.setPreviousDirection(MoveDirection.DOWN);
            return MoveDirection.DOWN;
        }

        if (random == 2 && !somethingRIGHT  && ghost.getPreviousDirection() != MoveDirection.LEFT){
            ghost.getKeepDirection().put("KeepRight",true);
            ghost.setPreviousDirection(MoveDirection.RIGHT);
            return MoveDirection.RIGHT;
        }
        else if (random == 2 && !somethingDOWN && ghost.getPreviousDirection() != MoveDirection.UP){
            ghost.getKeepDirection().put("KeepDown",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.DOWN;
        }
        else if (random == 2 && !somethingUP && ghost.getPreviousDirection() != MoveDirection.DOWN){
            ghost.getKeepDirection().put("KeepUp",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.UP;
        }
        else if (random == 2) {
            ghost.getKeepDirection().put("KeepLeft",true);
            ghost.setPreviousDirection(MoveDirection.LEFT);
            return MoveDirection.LEFT;
        }

        if (random == 3 && !somethingDOWN && ghost.getPreviousDirection() != MoveDirection.UP){
            ghost.getKeepDirection().put("KeepDown",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.DOWN;
        }
        else if (random == 3 && !somethingLEFT && ghost.getPreviousDirection() != MoveDirection.RIGHT){
            ghost.getKeepDirection().put("KeepLeft",true);
            ghost.setPreviousDirection(MoveDirection.LEFT);
            return MoveDirection.LEFT;
        }
        else if (random == 3 && !somethingRIGHT  && ghost.getPreviousDirection() != MoveDirection.LEFT){
            ghost.getKeepDirection().put("KeepRight",true);
            ghost.setPreviousDirection(MoveDirection.RIGHT);
            return MoveDirection.RIGHT;
        }
        else if (random == 3){
            ghost.getKeepDirection().put("KeepUp",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.UP;
        }

        if (random == 4 && !somethingLEFT && ghost.getPreviousDirection() != MoveDirection.RIGHT){
            ghost.getKeepDirection().put("KeepLeft",true);
            ghost.setPreviousDirection(MoveDirection.LEFT);
            return MoveDirection.LEFT;
        }
        else if (random == 4 && !somethingDOWN && ghost.getPreviousDirection() != MoveDirection.UP){
            ghost.getKeepDirection().put("KeepDown",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.DOWN;
        }
        else if (random == 4 && !somethingUP  && ghost.getPreviousDirection() != MoveDirection.DOWN){
            ghost.getKeepDirection().put("KeepUp",true);
            ghost.setPreviousDirection(MoveDirection.UP);
            return MoveDirection.UP;
        }
        else if (random == 4){
            ghost.getKeepDirection().put("KeepRight",true);
            ghost.setPreviousDirection(MoveDirection.RIGHT);
            return MoveDirection.RIGHT;
        }
        ghost.setPreviousDirection(MoveDirection.RIGHT);
        return MoveDirection.RIGHT;
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
        if (pacman.getCurrentAnimationID() != 0)
            if (graphicsEngine().getAnimation(pacman.getCurrentAnimationID()).isPlaying())
                graphicsEngine().playPauseAnimation(pacman.getCurrentAnimationID());
    }

    /**
     * Collision entre pacman et un fantome
     * @param ghost fantome
     */
    public void pacmanGhostCollision(Ghost ghost) {
        if (isEatPowerUpEnabled.get()) eatGhost(ghost);
        else decreasePacmanLife();
    }

    /**
     * Collision entre pacman et un mur lors du pouvoir breaker
     * @param wall mur
     */
    public void pacmanWallCollision(Entity wall) {
        if (isBreakPowerUpEnabled.get()) {
            System.out.println("BREAK");
            breakWall(wall);
        }
    }

    /**
     * Décrémenter la vie de pacman lorsqu'il se fait toucher par un fantome
     */
    private void decreasePacmanLife() {
        kernelEngine.pauseEvents();
        clearActiveTasks();
        soundEngine().clearSounds();
        moveGhostsOut();
        currentLevel.updateLives();
        executeParallelTask(() -> {
            graphicsEngine().bindAnimation(pacman, pacman.getAnimations().get("death"));
            soundEngine().playSound("death1");
            try { sleep(700); } catch (InterruptedException e) { e.printStackTrace(); }
            soundEngine().stopSound("death1");
            soundEngine().playSound("death2");
            try { sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
            if (currentLevel.getLivesCount().get() > 0) playLevel(currentLevel);
            else {
                kernelEngine.stopTimer("chrono");
                showEndGameView("YOU LOST !", new Color(255,0,0));
            }
        });
    }

    /**
     * Placer les fantomes hors du niveau
     */
    private void moveGhostsOut() {
        for (Ghost ghost1 : ghosts.values())
            physicsEngine().move(ghost1, -100, -100);
    }

    /**
     * Manger un fantome
     * @param ghost fantome
     */
    private void eatGhost(Ghost ghost) {
        if (!ghost.getEaten().get()) {
            soundEngine().playSound("eatGhost");
            currentLevel.updateActualScore(currentLevel.getActualScore() + 250);
            ghost.getEaten().getAndSet(true);
            ghost.getReturnBase().getAndSet(false);
            bindGhostBaseAI(ghost);
            executeParallelTask(() -> {
                while(true) {
                    if (kernelEngine.isEventsPaused()) break;
                    if (physicsEngine().getDistance(ghost, targets.get(TARGETS.BASE)) <= 1)
                        break;
                }
                ghost.getEaten().getAndSet(false);
                ghost.getReturnBase().getAndSet(isEatPowerUpEnabled.get());
                updateGhostAnimation(ghost);
                bindGhostInitialAI(ghost);
            });
        }
    }

    /**
     * Casser un mur
     * @param wall mur
     */
    private void breakWall(Entity wall) {
        int[] wallPosition = currentLevel.getMatrixEntityPosition(wall);
        currentLevel.getWalls()[wallPosition[0]][wallPosition[1]] = false;
        currentLevel.applyWallTextures();
        kernelEngine.removeEntity(wall);
    }

    /**
     * Incrémente le volume 5 par 5
     */
    private void incrementGlobalVolume(){
        soundEngine().incrementGlobalVolume();
        graphicsEngine().bindText(currentVolume, "Volume is : " + soundEngine().getGlobalVolume(),
                new Color(255,255,255), 20, true);
    }

    /**
     * Décrémente le son 5 par 5
     * le son étant un logarithme la décrémentation ne se fait pas très bien et
     * nous nous retrouvons avec des nombre du style 24 de volume  mais cela n'est pas important
     */
    private void decrementGlobalVolume(){
        soundEngine().decrementGlobalVolume();
        graphicsEngine().bindText(currentVolume, "Volume is : " + soundEngine().getGlobalVolume(),
                new Color(255,255,255), 20, true);
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
        currentLevel.spawnPlayer(15,10);
        currentLevel.spawnGhost(ghosts.get(GHOSTS.RED),7,10);
        currentLevel.spawnGhost(ghosts.get(GHOSTS.BLUE),9,9);
        currentLevel.spawnGhost(ghosts.get(GHOSTS.PINK),9,10);
        currentLevel.spawnGhost(ghosts.get(GHOSTS.ORANGE),9,11);
    }

    /**
     * Libérer les fantomes de l'enclos 1 à 1
     */
    public void freeGhosts() {
        PhysicEntity base = targets.get(TARGETS.BASE).getPhysicEntity();
        GHOSTS[] order = new GHOSTS[]{GHOSTS.BLUE,GHOSTS.ORANGE,GHOSTS.PINK};
        executeParallelTask(() -> {
            int count = 0;
            while (count < 3) {
                if (kernelEngine.isEventsPaused()) break;
                if (timer.get() == 5 * (count + 1)) {
                    if (!kernelEngine.isEventsPaused())
                        physicsEngine().move(ghosts.get(order[count]), base.getX(), base.getY());
                    ++count;
                }
                try { sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }
            }
        });
    }

    /**
     * Rejouer un niveau
     */
    protected void restartLevel() {
        levels.clear();
        initDefaultLevel();
        playLevel(levels.get(0));
    }

    /**
     * Jouer un niveau
     * @param level level
     */
    protected void playLevel(Level level) {
        currentLevel = level;
        executorService = Executors.newFixedThreadPool(8);

        isEatPowerUpEnabled.getAndSet(false);
        eatPowerUpTimeout.getAndSet(eatPowerUpInitialTime);
        isBreakPowerUpEnabled.getAndSet(false);
        breakPowerUpTimeout.getAndSet(breakPowerUpInitialTime);

        for (Ghost ghost : ghosts.values())
            if (ghost.getEaten().get()) ghost.getEaten().getAndSet(false);

        soundEngine().clearSounds();
        ioEngine().resetLastPressedKey();
        spawnPlayersOnLevel();
        kernelEngine().switchScene(currentLevel.getScene());

        if (currentLevel.getLivesCount().get() == 3)
            soundEngine().playSound("gameStart");

        executeParallelTask(() -> {
            kernelEngine.pauseEvents();
            timer.getAndSet(0);
            try { sleep(currentLevel.getLivesCount().get() == 3 ? 4000 : 1000); }
            catch (InterruptedException e) { e.printStackTrace(); }
            kernelEngine.resumeEvents();
            kernelEngine.startTimer("chrono", 1000, timer::getAndIncrement);
            soundEngine().loopSound("siren1");
            freeGhosts();
        });
    }

    /**
     * Attacher l'IA de crainte aux fantomes
     * @param ghost fantome
     */
    private void bindGhostFearAI(Ghost ghost) {
        aiEngine().bindEvent(ghost,"moveFear" + ghost.getColor() + "Ghost");
    }

    /**
     * Attacher l'IA de base à un fantome
     * @param ghost fantome
     */
    private void bindGhostInitialAI(Ghost ghost) {
        aiEngine().bindEvent(ghost, "move" + ghost.getColor() + "Ghost");
    }

    /**
     * Attacher l'IA pour aller à la base à un fantome
     * @param ghost fantome
     */
    private void bindGhostBaseAI(Ghost ghost) {
        aiEngine().bindEvent(ghost, "moveBase" + ghost.getColor() + "Ghost");
    }

    /**
     * Activer le super pouvoir de manger les fantomes
     */
    public void enableEatPowerUP() {
        eatPowerUpTimeout.getAndSet(eatPowerUpInitialTime);
        isEatPowerUpEnabled.getAndSet(true);
        for (Ghost ghost : ghosts.values()) {
            ghost.getReturnBase().getAndSet(false);
            bindGhostFearAI(ghost);
        }
        soundEngine().clearSounds();
        soundEngine().loopSound("powerup");
        soundEngine().playSound("eatGomme");
        executeParallelTask(() -> {
            while (eatPowerUpTimeout.get() > 0) {
                if (kernelEngine.isEventsPaused()) break;
                try { sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                eatPowerUpTimeout.decrementAndGet();
            }
            isEatPowerUpEnabled.getAndSet(false);
            soundEngine().clearSounds();
            soundEngine().loopSound("siren1");
            for (Ghost ghost : ghosts.values())
                if (!ghost.getEaten().get())
                    bindGhostInitialAI(ghost);
        });
    }

    /**
     * Activer le super pouvoir de casser les murs pour 5 secondes
     */
    public void enableBreakPowerUp() {
        breakPowerUpTimeout.getAndSet(breakPowerUpInitialTime);
        isBreakPowerUpEnabled.getAndSet(true);
        soundEngine().clearSounds();
        soundEngine().loopSound("powerup");
        soundEngine().playSound("eatGomme");
        executeParallelTask(() -> {
            while (breakPowerUpTimeout.get() > 0) {
                if (kernelEngine.isEventsPaused()) break;
                try { sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
                breakPowerUpTimeout.decrementAndGet();
            }
            isBreakPowerUpEnabled.getAndSet(false);
            soundEngine().clearSounds();
            soundEngine().loopSound("siren1");
        });
    }

    /**
     * Afficher la vue de fin de jeu
     * @param title titre
     * @param color couleur du titre
     */
    protected void showEndGameView(String title, Color color) {
        soundEngine().clearSounds();
        ioEngine().resetLastPressedKey();
        GraphicEntity titleEntity = endGameView.getEntities().get(0);
        graphicsEngine().bindText(titleEntity.getParent(), title, color, 20, true);
        GraphicEntity scoreEntity = endGameView.getEntities().get(1);
        graphicsEngine().bindText(scoreEntity.getParent(), "Score : " + currentLevel.getActualScore(),
                new Color(255,255,255), 20, true);
        kernelEngine().switchScene(endGameView);
        kernelEngine.resumeEvents();
    }

    /**
     * Lancer le jeu
     */
    public void start() {
        kernelEngine().switchScene(menuView);
        kernelEngine.start();
        setGlobalVolume(50);
    }

    /**
     * Exécuter une tâche en parallèle
     * @param task tache
     */
    public void executeParallelTask(Runnable task) {
        tasks.add(executorService.submit(task, null));
    }

    /**
     * Supprimer toutes les taches en cours
     */
    public void clearActiveTasks() {
        for (Future<Void> task : tasks)
            task.cancel(false);
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

    public Map<GHOSTS,Ghost> getGhosts() { return ghosts; }
}
