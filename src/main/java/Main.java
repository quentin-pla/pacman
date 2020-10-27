import api.Scene;
import api.Window;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        Window window = Window.get();
        window.setTitle("PACMAN");
        window.setScene(new Scene(600,600));
        window.run();
    }
}
