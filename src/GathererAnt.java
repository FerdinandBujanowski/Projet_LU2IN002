import java.awt.*;
import java.util.ArrayList;

public class GathererAnt extends Ant {

    public GathererAnt(int x, int y, AntType antType) {
        super(x, y, antType);
    }

    @Override
    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        super.tick(terrain, barriers, predators, colonyData);
        if (this.predatorInProximity && this.closestPredatorPosition != null) {
            // run away from predator
            Point vectorToPredator = new Point(this.closestPredatorPosition.x - this.getX(), this.closestPredatorPosition.y - this.getY());
            //invert vector pointing to predator
            Point vectorFromPredator = new Point(vectorToPredator.x * -1, vectorToPredator.y * -1);
            this.tryMoving(vectorFromPredator, barriers, predators, colonyData);
        }
        else {
            if(this.getLastInventoryIndex() == this.inventory.length - 1) {
                //bring food to queen
                Point queenPosition = colonyData.getQueenPosition();
                if(this.touches(queenPosition)) {
                    colonyData.onFeedQueenRequest(this.id);
                } else {
                    Point vectorToQueen = new Point(queenPosition.x - this.getX(), queenPosition.y - this.getY());
                    this.tryMoving(vectorToQueen, barriers, predators, colonyData);
                }
            } else {
                //gather more food
                Point closest = this.closestRessourcePosition;
                if(closest != null) {
                    if(closest.x == this.getX() && closest.y == this.getY()) {
                        this.inventory[this.getLastInventoryIndex() + 1] = terrain.videCase(closest.x, closest.y);
                    } else {
                        Point vectorToRessource = new Point(closest.x - this.getX(), closest.y - this.getY());
                        this.tryMoving(vectorToRessource, barriers, predators, colonyData);
                    }
                }
            }
            if(this.energyZero) {
                //if food equipped eat food
                if(this.getLastInventoryIndex() > -1) {
                    this.currentEnergy += this.inventory[this.getLastInventoryIndex()].getQuantite();
                    if(this.inventory[this.getLastInventoryIndex()] instanceof Berry) {
                        if(((Berry) this.inventory[this.getLastInventoryIndex()]).isFermented()) {
                            this.drunkCooldown += Berry.DRUNK_TICKS;
                        }
                    }
                    this.inventory[this.getLastInventoryIndex()] = null;
                }
            }
        }
    }
    @Override
    public void calculateMovingCosts() {
        if(!this.energyZero) this.currentEnergy--;
        else this.currentHealth--;
    }
}
