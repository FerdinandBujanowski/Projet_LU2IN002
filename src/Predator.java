import java.awt.*;
import java.util.ArrayList;

public class Predator extends Animal {

    public static final double p_spawn = 0.01;

    public Predator(int x, int y) {
        super(x, y);
        this.currentHealth = 20;
    }

    public static ArrayList<Point> getPredatorPositions(ArrayList<Predator> predators) {
        ArrayList<Point> positions = new ArrayList<>();
        for(Predator predator : predators) {
            positions.add(predator.getPosition());
        }
        return positions;
    }
    public static Predator getPredatorAtPosition(int x, int y, ArrayList<Predator> predators) {
        for(Predator predator : predators) {
            if(predator.getX() == x && predator.getY() == y) return predator;
        }
        return null;
    }

    @Override
    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        //Attacks any ants it's nearby
        for (Point pos: colonyData.getOtherAntPositions(this.getPosition())){
            if (this.touches(pos)) colonyData.requestDamageAnt(pos);
        }

        //Moves towards Queen
        Point posQueen = colonyData.getQueenPosition();
        Point vectorToQueen = new Point(posQueen.x - this.getX(), posQueen.y - this.getY());

        if(Simulation.iteration % 2 == 0) {
            tryMoving(vectorToQueen, barriers, predators, colonyData);
        }
    }

    public static Direction getPredatorDirection(Point predatorPosition, ArrayList<Predator> predators) {
        Predator predator = Predator.getPredatorAtPosition(predatorPosition.x, predatorPosition.y, predators);
        if(predator == null) return Direction.UP;
        return predator.currentDirection;
    }

}
