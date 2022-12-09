import java.awt.*;
import java.util.ArrayList;

public class QueenAnt extends Ant {

    /**
     *
     * @param antType
     */
    public QueenAnt(AntType antType) {
        super(Simulation.QUEEN_X, Simulation.QUEEN_Y, antType);
    }

    @Override
    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        super.tick(terrain, barriers, predators, colonyData);

        if(this.currentEnergy >= Simulation.EGG_COST && colonyData.getOtherAntPositions(this.getPosition()).size() < (Simulation.MAX_ANTS - 1)) {
            //hatch new egg
            this.currentEnergy -= Simulation.EGG_COST;
            Point freePosition = this.getFreePoint(this.getPosition(), barriers, predators, colonyData);
            if(freePosition != null) {
                if (Math.random() <= Colony.pWarriorSpawn) {
                    colonyData.onSpawnAntRequest(AntType.WARRIOR_ANT, freePosition.x, freePosition.y);
                }
                else colonyData.onSpawnAntRequest(AntType.GATHERER_ANT, freePosition.x, freePosition.y);
            }
        }

        //allowed to move every 2 steps
        if(Simulation.iteration % 2 == 0) {

            //if gatherers unable to get food to the queen
            if(colonyData.getGathererCount() == 0 || this.predatorInProximity) {
                Point closest = this.closestRessourcePosition;
                //eat ressource
                if(this.getX() == closest.x && this.getY() == closest.y) {
                    Ressource ressource = terrain.videCase(closest.x, closest.y);
                    assert ressource != null;
                    this.eat(ressource);
                } else {
                    //walk up to closest ressource
                    Point vectorToRessource = new Point(closest.x - this.getX(), closest.y - this.getY());
                    this.tryMoving(vectorToRessource, barriers, predators, colonyData);
                }
            } else {
                //move randomly
                Point freePosition = this.getFreePoint(this.getPosition(), barriers, predators, colonyData);
                if(freePosition != null) {
                    Point vector = new Point(freePosition.x - this.getX(), freePosition.y - this.getY());
                    this.tryMoving(vector, barriers, predators, colonyData);
                }
            }
        }
    }
}
