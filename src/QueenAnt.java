import java.awt.*;
import java.util.ArrayList;

public class QueenAnt extends Ant {

    public QueenAnt(AntType antType) {
        super(Simulation.QUEEN_X, Simulation.QUEEN_Y, antType);
    }

    @Override
    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        super.tick(terrain, barriers, predators, colonyData);

        if(this.currentEnergy > Simulation.EGG_COST && colonyData.getOtherAntPositions(this.getPosition()).size() < (Simulation.MAX_ANTS - 1)) {
            this.currentEnergy -= Simulation.EGG_COST;
            Point freePosition = this.getFreePoint(this.getPosition(), barriers, predators, colonyData);
            if(freePosition != null) {
                //colonyData.onSpawnAntRequest(AntType.GATHERER_ANT, freePosition.x, freePosition.y);
                colonyData.onSpawnAntRequest(AntType.WARRIOR_ANT, freePosition.x, freePosition.y);
            }
        }
        if(Simulation.iteration % 2 == 0) {
            Point freePosition = this.getFreePoint(this.getPosition(), barriers, predators, colonyData);
            if(freePosition != null) {

                Point vector = new Point(freePosition.x - this.getX(), freePosition.y - this.getY());
                this.tryMoving(vector, barriers, predators, colonyData);
            }
        }
    }
}
