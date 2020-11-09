package engines.kernel;


import engines.AI.AIEntity;
import engines.AI.AIObject;
import engines.UI.UIEntity;
import engines.UI.UIObject;
import engines.graphics.GraphicsEntity;
import engines.graphics.GraphicsObject;
import engines.input_output.InputOutputEntity;
import engines.input_output.InputOutputObject;
import engines.network.NetworkEntity;
import engines.network.NetworkObject;
import engines.physics.PhysicsEntity;
import engines.physics.PhysicsObject;
import engines.sound.SoundEntity;
import engines.sound.SoundObject;

/**
 * Entité globale
 */
public class Entity implements AIDecorator, GraphicsDecorator, InputOutputDecorator, NetworkDecorator,
        PhysicsDecorator, SoundDecorator, UIDecorator {
    /**
     * Objet d'intelligence artificielle
     */
    private AIObject artificialIntelligence;

    /**
     * Objet graphique
     */
    private GraphicsObject graphics;

    /**
     * Objet d'entrée/sortie
     */
    private InputOutputObject inputOutput;

    /**
     * Objet réseau
     */
    private NetworkObject network;

    /**
     * Objet physique
     */
    private PhysicsObject physics;

    /**
     * Objet son
     */
    private SoundObject sound;

    /**
     * Objet interface utilisateur
     */
    private UIObject userInterface;

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
    public AIObject getAI() {
        if (artificialIntelligence == null) artificialIntelligence = AIEntity.generateEntity();
        return artificialIntelligence;
    }

    @Override
    public GraphicsObject getGraphics() {
        if (graphics == null) graphics = GraphicsEntity.generateEntity();
        return graphics;
    }

    @Override
    public InputOutputObject getInputOutput() {
        if (inputOutput == null) inputOutput = InputOutputEntity.generateEntity();
        return inputOutput;
    }

    @Override
    public NetworkObject getNetwork() {
        if (network == null) network = NetworkEntity.generateEntity();
        return network;
    }

    @Override
    public PhysicsObject getPhysics() {
        if (physics == null) physics = PhysicsEntity.generateEntity();
        return physics;
    }

    @Override
    public SoundObject getAudio() {
        if (sound == null) sound = SoundEntity.generateEntity();
        return sound;
    }

    @Override
    public UIObject getUI() {
        if (userInterface == null) userInterface = UIEntity.generateEntity();
        return userInterface;
    }
}
