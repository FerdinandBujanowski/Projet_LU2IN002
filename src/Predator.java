import java.awt.*;
import java.util.ArrayList;

public class Predator extends Animal implements Cloneable {

    public static final double p_spawn = 0.01;
    public static final double p_special_attack=0.2;

    /** 
     * @param x la position sur l'axe de coordonées du predateur 
     * @param y la position sur l'axe de oordonées du predateur 
     * Constructeur: initialise la santé a 20 et son etat de predateur
     */
    public Predator(int x, int y) {
        super(x, y);
        this.currentHealth = 20;
    }

    /** 
     *
     * @param predators la liste de tous les predateurs dans la simulation
     * @return les positions de chaque predator dans l'ArrayList predators
     */
    public static ArrayList<Point> getPredatorPositions(ArrayList<Predator> predators) {
        ArrayList<Point> positions = new ArrayList<>();
        for(Predator predator : predators) {
            positions.add(predator.getPosition());
        }
        return positions;
    }

    /** 
     * @param x la position sur l'axe de coordonées du predateur 
     * @param y la position sur l'axe de oordonées du predateur 
     * @param predators la liste de tous les predateurs dans la simulation
     * @return le predateur à une position donné s'il est dans la liste des predators
     */
    public static Predator getPredatorAtPosition(int x, int y, ArrayList<Predator> predators) {
        for(Predator predator : predators) {
            if(predator.getX() == x && predator.getY() == y) return predator;
        }
        return null;
    }

    /**
     * nouvelle itération : mise à jour du prédateur en fonction des paramètres importants
     * @param terrain le terrain actuel avec toutes les ressources
     * @param barriers toutes les barrières présentes sur le terrain
     * @param predators la liste des prédateurs actuellement présents sur le terrain
     * @param colonyData les données actuelles importantes sur la colonie
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

    /**
     * @param predatorPosition la position du predateur cherché
     * @param predators la liste de tous les predateurs dans la simulation
     * @return la direction du predateur cherché
     */
    public static Direction getPredatorDirection(Point predatorPosition, ArrayList<Predator> predators) {
        Predator predator = Predator.getPredatorAtPosition(predatorPosition.x, predatorPosition.y, predators);
        if(predator == null) return Direction.UP;
        return predator.currentDirection;
    }
    @Override
    /** 
     * @return un clone du predateur, en initalisant tous les valeurs du clone a ceux du predateur cloné
     */
    public Predator clone(){
        Predator pred=new Predator(this.getX(),this.getY());
        pred.currentHealth=this.currentHealth;
        pred.currentDirection=this.currentDirection;
        return pred;
    }
}
