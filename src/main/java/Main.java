import gameplay.Gameplay;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        //Gameplay
        Gameplay gameplay = new Gameplay();

        gameplay.playLevel(gameplay.getLevels().get(0));
    }
}
