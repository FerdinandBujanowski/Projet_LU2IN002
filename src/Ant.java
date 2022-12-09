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
    protected boolean queenInProximity;

    protected final AntType antType;

    /** Construit une fourmi avec son type (antType) et sa positionnement (valeurs x et y)
     *
     * @param x
     * @param y
     * @param antType
     */
    public Ant(int x, int y, AntType antType) {
        super(x, y);
        this.id = antCounter;
        antCounter++;
        this.inventory = new Ressource[Simulation.MAX_INVENTORY];
        this.antType = antType;
        this.currentHealth = this.antType.maxHealth;
        this.currentEnergy = this.antType.maxEnergy;
    }

    /**
     *
     * @param terrain
     * @param barriers
     * @param predators
     * @param colonyData
     */
    @Override
    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        this.energyZero = this.currentEnergy == 0;
        this.healthLow = this.currentHealth < Simulation.LOW_HEALTH;

        Point queenPosition = colonyData.getQueenPosition();
        assert queenPosition != null;
        this.queenInProximity = this.distance(queenPosition.x - this.getX(), queenPosition.y - this.getY()) <= Simulation.PROXIMITY;

        this.closestPredatorPosition = this.updateClosestPredatorPosition(predators);
        if(this.closestPredatorPosition != null) {
            this.predatorInProximity = this.distance(this.closestPredatorPosition.x, this.closestPredatorPosition.y) <= Simulation.PROXIMITY;
        } else this.predatorInProximity = false;

        this.closestRessourcePosition = this.updateClosestRessourcePosition(terrain);

        if(this.drunkCooldown > 0) this.drunkCooldown--;
    }

    /** La fourmi consomme la ressource donné en parametre, si c'est un bé fermenté, le temps drunkCooldown (qui decrit le nombre des ticks pour qu'une fourmi ivre) se reinitialise
     *
     * @param ressource
     */
    protected void eat(Ressource ressource) {
        this.currentEnergy += ressource.getQuantite();
        if(ressource instanceof Berry && ((Berry)ressource).isFermented()) {
            this.drunkCooldown += Berry.DRUNK_TICKS;
        }
    }

    /** Renvoie la position du predateur le plus proche de l'animale
     *
     * @param predators
     * @return
     */
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

    /** Renvoie la position de la ressource la plus proche de l'animal
     *
     * @param terrain
     * @return
     */
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

    /** Renvoie la derniere ressource mis dans le tableau inventory de l'animal 
     *
     * @return
     */
    protected int getLastInventoryIndex() {
        int lastIndex = -1;
        for(int i = 0; i < this.inventory.length; i++) {
            if(this.inventory[i] != null) lastIndex = i;
        }
        return lastIndex;
    }
}
