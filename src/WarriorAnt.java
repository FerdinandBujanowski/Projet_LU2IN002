import java.awt.*;
import java.util.ArrayList;

public class WarriorAnt extends Ant {

    public WarriorAnt(int x, int y, AntType antType) {
        super(x, y, antType);
    }

    @Override
    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        super.tick(terrain, barriers, predators, colonyData);

        //predator in proximity
        if(this.predatorInProximity) {
            if(this.touches(this.closestPredatorPosition)) {
                //attack predator
                Predator currentPredator = Predator.getPredatorAtPosition(this.closestPredatorPosition.x, this.closestPredatorPosition.y, predators);
                assert currentPredator != null;
                currentPredator.currentHealth--;
            } else {
                //walk up to predator
                Point predatorPosition = this.closestPredatorPosition;
                Point vectorToPredator = new Point(predatorPosition.x - this.getX(), predatorPosition.y - this.getY());
                this.tryMoving(vectorToPredator, barriers, predators, colonyData);
            }
        }
        else {
            Point queenPosition = colonyData.getQueenPosition();
            Point vectorToQueen = new Point(queenPosition.x - this.getX(), queenPosition.y - this.getY());
            //warrior close to queen
            if(this.queenInProximity) {
                Point randomPoint = new Point((int)(Math.random() * terrain.nbLignes), (int)(Math.random() * terrain.nbColonnes));
                Point randomVector = new Point(randomPoint.x - this.getX(), randomPoint.y - this.getY());
                this.tryMoving(randomVector, barriers, predators, colonyData);
            } else {
                //walk up to queen
                this.tryMoving(vectorToQueen, barriers, predators, colonyData);
            }
            //lose energy whenever not actively attacking
            if(!this.energyZero) this.currentEnergy--;
            else this.currentHealth--;
        }
    }
}

