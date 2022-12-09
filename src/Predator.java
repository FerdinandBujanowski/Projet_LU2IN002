import java.awt.*;
import java.util.ArrayList;

public class Predator extends Animal implements Cloneable {

    public static final double p_spawn = 0.01;
    public static final double p_special_attack=0.2;
    public Boolean specialAnt;

    /**
     *
     * @param x
     * @param y
     */
    public Predator(int x, int y) {
        super(x, y);
        this.currentHealth = 20;
        this.specialAnt=false; 
    }

    /**
     *
     * @param predators
     * @return
     */
    public static ArrayList<Point> getPredatorPositions(ArrayList<Predator> predators) {
        ArrayList<Point> positions = new ArrayList<>();
        for(Predator predator : predators) {
            positions.add(predator.getPosition());
        }
        return positions;
    }

    /**
     *
     * @param x
     * @param y
     * @param predators
     * @return
     */
    public static Predator getPredatorAtPosition(int x, int y, ArrayList<Predator> predators) {
        for(Predator predator : predators) {
            if(predator.getX() == x && predator.getY() == y) return predator;
        }
        return null;
    }

    /**
     *
     * @param terrain
     * @param barriers
     * @param predators
     * @param colonyData
     */
    @Override
    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        //Attacks any ants it's nearby
        for (Point pos: colonyData.getOtherAntPositions(this.getPosition())){
            if (this.touches(pos)) {
                if (specialAnt) {
                    colonyData.requestDamageAntSpecial(pos);
                }
                else colonyData.requestDamageAnt(pos);
            }
        }

        //Moves towards Queen
        Point posQueen = colonyData.getQueenPosition();
        Point vectorToQueen = new Point(posQueen.x - this.getX(), posQueen.y - this.getY());

        if(Simulation.iteration % 2 == 0) {
            tryMoving(vectorToQueen, barriers, predators, colonyData);
        }
    }

    /**
     *
     * @param predatorPosition
     * @param predators
     * @return
     */
    public static Direction getPredatorDirection(Point predatorPosition, ArrayList<Predator> predators) {
        Predator predator = Predator.getPredatorAtPosition(predatorPosition.x, predatorPosition.y, predators);
        if(predator == null) return Direction.UP;
        return predator.currentDirection;
    }
    @Override
    public Predator clone(){
        Predator pred=new Predator(this.getX(),this.getY());
        //health splits in half
        pred.currentHealth=this.currentHealth/2; 
        this.currentHealth/=2;
        pred.currentDirection=this.currentDirection;
        //they are now special uwu
        this.specialAnt=true;
        pred.specialAnt=true;
        return pred;
    }
}
