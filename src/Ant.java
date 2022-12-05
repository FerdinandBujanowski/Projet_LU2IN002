import java.awt.*;
import java.util.ArrayList;

public abstract class Ant extends Animal {

    public static int antCounter = 0;
    public final int id;

    protected Ressource[] inventory;

    protected boolean energyZero, healthLow;
    protected Point closestRessourcePosition;
    protected Point closestPredatorPosition;
    protected boolean predatorInProximity;

    protected final AntType antType;

    public Ant(int x, int y, AntType antType) {
        super(x, y);
        this.id = antCounter;
        antCounter++;
        this.inventory = new Ressource[2]; //TODO magic number to global constant
        this.antType = antType;
        this.currentHealth = this.antType.maxHealth;
        this.currentEnergy = this.antType.maxEnergy;
    }

    @Override
    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        this.energyZero = this.currentEnergy == 0;
        this.healthLow = this.currentHealth < 10; //TODO magic number to global constant

        this.closestPredatorPosition = this.updateClosestPredatorPosition(predators);
        if(this.closestPredatorPosition != null) {
            this.predatorInProximity = this.distance(this.closestPredatorPosition.x, this.closestPredatorPosition.y) < 10; //TODO magic number to global constant
        }

        this.closestRessourcePosition = this.updateClosestRessourcePosition(terrain);
    }

    private Point updateClosestPredatorPosition(ArrayList<Predator> predators) {
        double shortestDistance = -1;
        Point closestPosition = null;
        for(Predator predator : predators) {
            double currentDistance = predator.distance(this.getX(), this.getY());
            if(shortestDistance == -1 || currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                closestPosition = predator.getPosition();
            }
        }
        return closestPosition;
    }

    private Point updateClosestRessourcePosition(Terrain terrain) {
        double shortestDistance = -1;
        Point closestPosition = null;
        for(int x = 0; x < terrain.nbLignes; x++) {
            for(int y = 0; y < terrain.nbColonnes; y++) {
                if(!terrain.caseEstVide(x, y)) {
                    double currentDistance = this.distance(x, y);
                    if(shortestDistance == -1 || currentDistance < shortestDistance) {
                        shortestDistance = currentDistance;
                        closestPosition = new Point(x, y);
                    }
                }
            }
        }
        return closestPosition;
    }

    protected int getLastInventoryIndex() {
        int lastIndex = -1;
        for(int i = 0; i < this.inventory.length; i++) {
            if(this.inventory[i] != null) lastIndex = i;
        }
        return lastIndex;
    }
}
