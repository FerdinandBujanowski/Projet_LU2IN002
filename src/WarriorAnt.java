import java.awt.*;
import java.util.ArrayList;

public class WarriorAnt extends Ant {

    public WarriorAnt(int x, int y, AntType antType) {
        super(x, y, antType);
    }

    @Override
    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        super.tick(terrain, barriers, predators, colonyData);
        //Moves randomly if no predators in terrain // or not nearby
        if(predators.isEmpty() || !this.predatorInProximity){
            int randX=(int) (Math.random()*Terrain.NBLIGNESMAX);
            int randY=(int) (Math.random()*Terrain.NBCOLONNESMAX);
            Point vectorSomewhereRandom = new Point(randX - this.getX(), randY - this.getY());
            this.tryMoving(vectorSomewhereRandom, barriers, predators, colonyData);
            
            //Loses energy whenever its not actively attacking 
            this.calculateMovingCosts();
        }
        else{
            //Predators exist
            if(this.touches(closestPredatorPosition)) {
                //Attacks predator
                Predator.getPredatorAtPosition(closestPredatorPosition.x, closestPredatorPosition.y, predators).currentHealth--;
            }
            else{
                //If predator not nearby
                Point predPosition=closestPredatorPosition;
                this.tryMoving(predPosition, barriers, predators, colonyData);
            }
        }
    }
    @Override
    public void calculateMovingCosts() {
        if(!this.energyZero) this.currentEnergy--;
        else this.currentHealth--;
    }

}

