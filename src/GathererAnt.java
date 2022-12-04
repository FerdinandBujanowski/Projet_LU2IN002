import java.awt.*;
import java.util.ArrayList;

public class GathererAnt extends Ant {

    public GathererAnt(int x, int y, AntType antType) {
        super(x, y, antType);
    }

    @Override
    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        super.tick(terrain, barriers, predators, colonyData);
        if(this.predatorInProximity) {
            // run away from predator
            assert this.closestPredatorPosition != null;
            Point vectorToPredator = new Point(this.closestPredatorPosition.x - this.getX(), this.closestPredatorPosition.y - this.getY());
            //invert vector pointing to predator
            Point vectorFromPredator = new Point(vectorToPredator.x * -1, vectorToPredator.y * -1);
            this.tryMoving(vectorFromPredator, barriers, predators, colonyData);

        } else {
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
                    this.inventory[this.getLastInventoryIndex()] = null;
                }
            }
        }
    }

    //TODO put tryMoving() and calculateMovingCosts() into Animal class
    private void tryMoving(Point vector, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        if(this.tryMoveAlongVector(vector, barriers, predators, colonyData)) {
            //moving succeeded
            this.calculateMovingCosts();
        } else {
            //if movement blocked, move other direction
            Point freePoint = this.getFreePoint(this.getPosition(), barriers, predators, colonyData);
            if(freePoint != null) {
                Point newVector = new Point(freePoint.x - this.getX(), freePoint.y - this.getY());
                this.tryMoveAlongVector(newVector, barriers, predators, colonyData);
                this.calculateMovingCosts();
            }
        }
    }

    private void calculateMovingCosts() {
        if(!this.energyZero) this.currentEnergy--;
        else this.currentHealth--;
    }
}
