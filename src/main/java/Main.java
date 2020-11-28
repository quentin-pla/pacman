import gameplay.Gameplay;
import gameplay.Level;

/**
 * Main
 */
public class Main {
    public static void main(String[] args) {
        //Gameplay
        Gameplay gameplay = new Gameplay();

        //Level par défaut
        Level defaultLevel = gameplay.generateLevel(21,19);

        //Génération des murs
        for (int j = 0; j < 19; j++) defaultLevel.addWall(0,j);
        for (int j = 0; j < 19; j++) defaultLevel.addWall(20,j);

        for (int i = 0; i < 6; i++) defaultLevel.addWall(i,0);
        defaultLevel.addWall(8,0);
        defaultLevel.addWall(10,0);
        for (int i = 12; i < 20; i++) defaultLevel.addWall(i,0);

        for (int i = 0; i < 6; i++) defaultLevel.addWall(i,18);
        defaultLevel.addWall(8,18);
        defaultLevel.addWall(10,18);
        for (int i = 12; i < 20; i++) defaultLevel.addWall(i,18);

        defaultLevel.addWall(1,9);

        defaultLevel.addWall(2,2);
        defaultLevel.addWall(2,3);
        defaultLevel.addWall(2,5);
        defaultLevel.addWall(2,6);
        defaultLevel.addWall(2,7);
        defaultLevel.addWall(2,9);
        defaultLevel.addWall(2,11);
        defaultLevel.addWall(2,12);
        defaultLevel.addWall(2,13);
        defaultLevel.addWall(2,15);
        defaultLevel.addWall(2,16);

        defaultLevel.addWall(4,2);
        defaultLevel.addWall(4,3);
        defaultLevel.addWall(4,5);
        defaultLevel.addWall(4,7);
        defaultLevel.addWall(4,8);
        defaultLevel.addWall(4,9);
        defaultLevel.addWall(4,10);
        defaultLevel.addWall(4,11);
        defaultLevel.addWall(4,13);
        defaultLevel.addWall(4,15);
        defaultLevel.addWall(4,16);

        defaultLevel.addWall(5,5);
        defaultLevel.addWall(5,9);
        defaultLevel.addWall(5,13);

        defaultLevel.addWall(6,0);
        defaultLevel.addWall(6,1);
        defaultLevel.addWall(6,2);
        defaultLevel.addWall(6,3);
        defaultLevel.addWall(6,5);
        defaultLevel.addWall(6,6);
        defaultLevel.addWall(6,7);
        defaultLevel.addWall(6,9);
        defaultLevel.addWall(6,11);
        defaultLevel.addWall(6,12);
        defaultLevel.addWall(6,13);
        defaultLevel.addWall(6,15);
        defaultLevel.addWall(6,16);
        defaultLevel.addWall(6,17);
        defaultLevel.addWall(6,18);

        defaultLevel.addWall(7,3);
        defaultLevel.addWall(7,5);
        defaultLevel.addWall(7,13);
        defaultLevel.addWall(7,15);

        defaultLevel.addWall(8,1);
        defaultLevel.addWall(8,2);
        defaultLevel.addWall(8,3);
        defaultLevel.addWall(8,5);
        defaultLevel.addWall(8,7);
        defaultLevel.addWall(8,8);
        defaultLevel.addWall(8,9);
        defaultLevel.addWall(8,10);
        defaultLevel.addWall(8,11);
        defaultLevel.addWall(8,13);
        defaultLevel.addWall(8,15);
        defaultLevel.addWall(8,16);
        defaultLevel.addWall(8,17);

        defaultLevel.addWall(9,7);
        defaultLevel.addWall(9,11);

        defaultLevel.addWall(10,1);
        defaultLevel.addWall(10,2);
        defaultLevel.addWall(10,3);
        defaultLevel.addWall(10,5);
        defaultLevel.addWall(10,7);
        defaultLevel.addWall(10,8);
        defaultLevel.addWall(10,9);
        defaultLevel.addWall(10,10);
        defaultLevel.addWall(10,11);
        defaultLevel.addWall(10,13);
        defaultLevel.addWall(10,15);
        defaultLevel.addWall(10,16);
        defaultLevel.addWall(10,17);

        defaultLevel.addWall(11,3);
        defaultLevel.addWall(11,5);
        defaultLevel.addWall(11,13);
        defaultLevel.addWall(11,15);

        defaultLevel.addWall(12,1);
        defaultLevel.addWall(12,2);
        defaultLevel.addWall(12,3);
        defaultLevel.addWall(12,5);
        defaultLevel.addWall(12,7);
        defaultLevel.addWall(12,8);
        defaultLevel.addWall(12,9);
        defaultLevel.addWall(12,10);
        defaultLevel.addWall(12,11);
        defaultLevel.addWall(12,13);
        defaultLevel.addWall(12,15);
        defaultLevel.addWall(12,16);
        defaultLevel.addWall(12,17);

        defaultLevel.addWall(13,9);

        defaultLevel.addWall(14,2);
        defaultLevel.addWall(14,3);
        defaultLevel.addWall(14,5);
        defaultLevel.addWall(14,6);
        defaultLevel.addWall(14,7);
        defaultLevel.addWall(14,9);
        defaultLevel.addWall(14,11);
        defaultLevel.addWall(14,12);
        defaultLevel.addWall(14,13);
        defaultLevel.addWall(14,15);
        defaultLevel.addWall(14,16);

        defaultLevel.addWall(15,3);
        defaultLevel.addWall(15,15);

        defaultLevel.addWall(16,1);
        defaultLevel.addWall(16,3);
        defaultLevel.addWall(16,5);
        defaultLevel.addWall(16,7);
        defaultLevel.addWall(16,8);
        defaultLevel.addWall(16,9);
        defaultLevel.addWall(16,10);
        defaultLevel.addWall(16,11);
        defaultLevel.addWall(16,13);
        defaultLevel.addWall(16,15);
        defaultLevel.addWall(16,17);

        defaultLevel.addWall(17,5);
        defaultLevel.addWall(17,9);
        defaultLevel.addWall(17,13);

        defaultLevel.addWall(18,2);
        defaultLevel.addWall(18,3);
        defaultLevel.addWall(18,4);
        defaultLevel.addWall(18,5);
        defaultLevel.addWall(18,6);
        defaultLevel.addWall(18,7);
        defaultLevel.addWall(18,9);
        defaultLevel.addWall(18,11);
        defaultLevel.addWall(18,12);
        defaultLevel.addWall(18,13);
        defaultLevel.addWall(18,14);
        defaultLevel.addWall(18,15);
        defaultLevel.addWall(18,16);

        gameplay.playLevel(defaultLevel);
    }
}
