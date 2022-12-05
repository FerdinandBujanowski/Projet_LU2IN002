import java.awt.*;
import java.util.ArrayList;

public class Predator extends Animal {

    public Predator(int x, int y) {
        super(x, y);
        this.currentHealth=20;
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
    public static Predator getPredatorAtPosition(int x, int y, ArrayList<Predator> predators) {
        for(Predator predator : predators) {
            if(predator.getX() == x && predator.getY() == y) return predator;
        }
        return null;
    }

    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        //Attacks any ants it's nearby
        for (Point pos: colonyData.getOtherAntPositions(this.getPosition())){
            if (this.touches(pos)) colonyData.requestDamageAnt(pos);
        }

        //Moves towards Queen
        Point posQueen=colonyData.getQueenPosition();
        Point vectorToQueen = new Point(posQueen.x - this.getX(), posQueen.y - this.getY());

        if(!this.tryMoveAlongVector(vectorToQueen, barriers, predators, colonyData)) {
            //if movement blocked, moves somewhere else
            Point freePoint = this.getFreePoint(this.getPosition(), barriers, predators, colonyData);
            if(freePoint != null) {
                Point newVector = new Point(freePoint.x - this.getX(), freePoint.y - this.getY());
                this.tryMoveAlongVector(newVector, barriers, predators, colonyData);
            }
        }
    }

}
