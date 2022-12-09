import java.awt.*;
import java.util.ArrayList;

public class Predator extends Animal implements Cloneable {

    public static final double p_spawn = 0.01;
    public static final double p_special_attack=0.2;
    public Boolean specialPred;

    /** Constructeur de type predateur, initialise sa santé a 20 et son etat de predateur special à false
     *
     * @param x
     * @param y
     */
    public Predator(int x, int y) {
        super(x, y);
        this.currentHealth = 20;
        this.specialPred=false; 
    }

    /** Renvoie les positions de chaque predator dans l'ArrayList predators
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

    /** Renvoie le predateur à une position donné s'il est dans l'Arraylist predators
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
                colonyData.requestDamageAnt(pos);
            }
        }

        //Moves towards Queen
        Point posQueen = colonyData.getQueenPosition();
        Point vectorToQueen = new Point(posQueen.x - this.getX(), posQueen.y - this.getY());

        if(Simulation.iteration % 2 == 0) {
            tryMoving(vectorToQueen, barriers, predators, colonyData);
        }
    }

    /** Renvoie la direction du predateur
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
    /** Fait un clonage d'un predateur, en initalisant tous les valeurs du clone a ceux du predateur cloné
     * @return
     */
    public Predator clone(){
        Predator pred=new Predator(this.getX(),this.getY());
        pred.currentHealth=this.currentHealth;
        pred.currentDirection=this.currentDirection;
        pred.specialPred=false;
        return pred;
    }
}
