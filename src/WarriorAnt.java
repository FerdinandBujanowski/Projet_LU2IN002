import java.awt.*;
import java.util.ArrayList;

public class WarriorAnt extends Ant {

    public WarriorAnt(int x, int y, AntType antType) {
        super(x, y, antType);
    }

    @Override
    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        super.tick(terrain, barriers, predators, colonyData);
        //Moves randomly if no predators in terrain
        if(predators.isEmpty()){
            int randX=(int) (Math.random()*Terrain.NBLIGNESMAX);
            int randY=(int) (Math.random()*Terrain.NBCOLONNESMAX);
            Point vectorSomewhereRandom = new Point(randX - this.getX(), randY - this.getY());
            this.tryMoving(vectorSomewhereRandom, barriers, predators, colonyData);
            if(!this.tryMoveAlongVector(vectorSomewhereRandom, barriers, predators, colonyData)) {
                //if movement blocked, moves somewhere else 
                Point freePoint = this.getFreePoint(this.getPosition(), barriers, predators, colonyData);
                if(freePoint != null) {
                    Point newVector = new Point(freePoint.x - this.getX(), freePoint.y - this.getY());
                    this.tryMoveAlongVector(newVector, barriers, predators, colonyData);
                }
            }
        }
        else{
            if (predatorInProximity){
                if (this.predatorInProximity){
                    if(this.touches(closestPredatorPosition))
                    //Attacks predator
                    Predator.getPredatorAtPosition(closestPredatorPosition.x, closestPredatorPosition.y, predators).currentHealth--;
                }
                else{
                    //If predator not nearby
                    Point predPosition=closestPredatorPosition;
                    System.out.println(predPosition);
                    this.tryMoving(predPosition, barriers, predators, colonyData);
    
                    if(!this.tryMoveAlongVector(predPosition, barriers, predators, colonyData)) {
                        //if movement blocked, moves somewhere else 
                        Point freePoint = this.getFreePoint(this.getPosition(), barriers, predators, colonyData);
                        if(freePoint != null) {
                            Point newVector = new Point(freePoint.x - this.getX(), freePoint.y - this.getY());
                            this.tryMoving(newVector, barriers, predators, colonyData);
                        }
                    }
                }
            }
        }
    }


}

