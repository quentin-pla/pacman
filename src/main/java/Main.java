import api.Window;
import engines.GraphicsEngine;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        Window.get().setTitle("PACMAN");
        GraphicsEngine.get().uploadSpriteSheet("sprite_sheet.png", 11, 11);
        GraphicsEngine.get().drawColorTile(50,0,0,new float[]{1,1,1,1});
        GraphicsEngine.get().drawSpriteSheetTile(50,30,0,"sprite_sheet.png", 1, 3);
        Window.get().run();
    }
}
