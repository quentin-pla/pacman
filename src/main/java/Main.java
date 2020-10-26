import api.Window;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        Window window = Window.get();
        window.setSize(600,600);
        window.setTitle("PACMAN");
        window.run();
    }
}
