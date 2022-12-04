import java.awt.*;
import java.util.ArrayList;

public class Predator extends Animal {

    public Predator(int x, int y) {
        super(x, y);
    }

    public static ArrayList<Point> getPredatorPositions(ArrayList<Predator> predators) {
        ArrayList<Point> positions = new ArrayList<>();
        for(Predator predator : predators) {
            positions.add(predator.getPosition());
        }
        return positions;
    }

    public static boolean predatorAtPosition(int x, int y, ArrayList<Predator> predators) {
        for(Predator predator : predators) {
            if(predator.getX() == x && predator.getY() == y) return true;
        }
        return false;
    }
}
