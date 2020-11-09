package engines.kernel;


import engines.AI.AIEngine;
import engines.AI.AIEntity;
import engines.UI.UIEngine;
import engines.UI.UIEntity;
import engines.graphics.GraphicsEngine;
import engines.graphics.GraphicsEntity;
import engines.input_output.InputOutputEngine;
import engines.input_output.InputOutputEntity;
import engines.network.NetworkEngine;
import engines.network.NetworkEntity;
import engines.physics.PhysicsEngine;
import engines.physics.PhysicsEntity;
import engines.sound.SoundEngine;
import engines.sound.SoundEntity;

/**
 * Entité globale
 */
public class Entity implements AIDecorator, GraphicsDecorator, InputOutputDecorator, NetworkDecorator,
        PhysicsDecorator, SoundDecorator, UIDecorator {
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
        if (clone.artificialIntelligence != null)
            artificialIntelligence = clone.artificialIntelligence.clone();
        if (clone.graphics != null)
            graphics = clone.graphics.clone();
        if (clone.inputOutput != null)
            inputOutput = clone.inputOutput.clone();
        if (clone.network != null)
            network = clone.network.clone();
        if (clone.physics != null)
            physics = clone.physics.clone();
        if (clone.sound != null)
            sound = clone.sound.clone();
        if (clone.userInterface != null)
            userInterface = clone.userInterface.clone();
    }

    /**
     * Cloner l'entité
     * @return clone
     */
    public Entity clone() { return new Entity(this); }

    @Override
    public AIEntity getAI() {
        if (artificialIntelligence == null) artificialIntelligence = AIEngine.generateEntity();
        return artificialIntelligence;
    }

    @Override
    public GraphicsEntity getGraphics() {
        if (graphics == null) graphics = GraphicsEngine.generateEntity();
        return graphics;
    }

    @Override
    public InputOutputEntity getInputOutput() {
        if (inputOutput == null) inputOutput = InputOutputEngine.generateEntity();
        return inputOutput;
    }

    @Override
    public NetworkEntity getNetwork() {
        if (network == null) network = NetworkEngine.generateEntity();
        return network;
    }

    @Override
    public PhysicsEntity getPhysics() {
        if (physics == null) physics = PhysicsEngine.generateEntity();
        return physics;
    }

    @Override
    public SoundEntity getAudio() {
        if (sound == null) sound = SoundEngine.generateEntity();
        return sound;
    }

    @Override
    public UIEntity getUI() {
        if (userInterface == null) userInterface = UIEngine.generateEntity();
        return userInterface;
    }
}
