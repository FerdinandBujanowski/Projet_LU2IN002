import java.awt.*;
import java.util.ArrayList;

public class GathererAnt extends Ant {

    /**
     * Constructeur : construit une fourmi de type ouvrière
     * @param x la position initiale - x
     * @param y la position initiale - y
     * @param antType le type de fourmi (toujours AntType.GATHERER_ANT)
     */
    public GathererAnt(int x, int y, AntType antType) {
        super(x, y, antType);
    }

    /**
     * nouvelle itération : mise à jour de l'ouvrière en fonction des paramètres importants
     * @param terrain le terrain actuel avec toutes les ressources
     * @param barriers toutes les barrières présentes sur le terrain
     * @param predators la liste des prédateurs actuellement présents sur le terrain
     * @param colonyData les données actuelles importantes sur la colonie
     */
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
                    this.eat(this.inventory[this.getLastInventoryIndex()]);
                    this.inventory[this.getLastInventoryIndex()] = null;
                }
            }
        }
    }


    /** 
     * Fonction qui fait le calcule des couts de bouger de l'animal
     */
    @Override
    public void calculateMovingCosts() {
        if(!this.energyZero) this.currentEnergy--;
        else this.currentHealth--;
    }
}
