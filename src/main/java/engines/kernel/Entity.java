package engines.kernel;

import engines.AI.AIEntity;
import engines.UI.UIEntity;
import engines.graphics.GraphicsEntity;
import engines.input_output.InputOutputEntity;
import engines.network.NetworkEntity;
import engines.physics.PhysicsEntity;
import engines.sound.SoundEntity;

/**
 * Entité générale
 */
public class Entity implements GraphicsDecorator, InputOutputDecorator {
    /**
     * Entité d'intelligence artificielle
     */
    private AIEntity artificialIntelligence;

    /**
     * Entité graphique
     */
    private GraphicsEntity graphics;

    /**
     * Entité d'entrée/sortie
     */
    private InputOutputEntity inputOutput;

    /**
     * Entité réseau
     */
    private NetworkEntity network;

    /**
     * Entité physique
     */
    private PhysicsEntity physics;

    /**
     * Entité son
     */
    private SoundEntity sound;

    /**
     * Entité interface utilisateur
     */
    private UIEntity userInterface;

    /**
     * Constructeur
     */
    public Entity() {}

    /**
     * Constructeur par clonage
     * @param clone clone
     */
    public Entity(Entity clone) {
//        artificialIntelligence = clone.artificialIntelligence;
        if (clone.graphics != null) graphics = clone.graphics.clone();
        inputOutput = clone.inputOutput;
//        network = clone.network;
//        physics = clone.physics;
//        sound = clone.sound;
//        userInterface = clone.userInterface;
    }

    /**
     * Cloner l'entité
     * @return clone
     */
    public Entity clone() { return new Entity(this); }

    /**
     * Initialiser l'entité d'intelligence artificielle
     */
    public void initArtificialIntelligence() {
        artificialIntelligence = new AIEntity();
    }

    /**
     * Initialiser l'entité graphique
     * @param height hauteur
     * @param width largeur
     */
    public void initGraphics(int height, int width) { graphics = new engines.graphics.Entity(height, width); }

    /**
     * Initialiser l'entité entrée/sortie
     */
    public void initInputOutput() { inputOutput = new engines.input_output.Entity(); }

    /**
     * Initialiser l'entité réseau
     */
    public void initNetwork() { network = new NetworkEntity(); }

    /**
     * Initialiser l'entité physique
     */
    public void initPhysics() { physics = new PhysicsEntity(); }

    /**
     * Initialiser l'entité son
     */
    public void initSound() { sound = new SoundEntity(); }

    /**
     * Initialiser l'entité interface utilisateur
     */
    public void initUserInterface() { userInterface = new UIEntity(); }

    // GETTERS //

    public AIEntity getArtificialIntelligence() { return artificialIntelligence; }

    public GraphicsEntity getGraphics() { return graphics; }

    public InputOutputEntity getInputOutput() { return inputOutput; }

    public NetworkEntity getNetwork() { return network; }

    public PhysicsEntity getPhysics() { return physics; }

    public SoundEntity getSound() { return sound; }

    public UIEntity getUserInterface() { return userInterface; }
}
