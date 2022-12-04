import java.awt.*;
import java.util.ArrayList;

public class Colony implements ColonyData {

    private final QueenAnt queenAnt;
    private final ArrayList<Ant> ants;
    private final ArrayList<Ant> newbornAnts;

    public Colony() {
        this.queenAnt = (QueenAnt) AntType.QUEEN_ANT.createNewInstance(0, 0);
        this.ants = new ArrayList<>();
        this.newbornAnts = new ArrayList<>();
        this.ants.add(this.queenAnt);
    }

    public void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators) {
        ArrayList<Ant> deadAnts = new ArrayList<>();
        for(Ant ant : this.ants) {
            ant.tick(terrain, barriers, predators, this);
            if(ant.currentHealth <= 0) deadAnts.add(ant);
        }
        for(Ant ant : deadAnts) this.ants.remove(ant);
        this.ants.addAll(this.newbornAnts);
        this.newbornAnts.removeAll(this.newbornAnts);
    }

    @Override
    public Point getQueenPosition() {
        return new Point(this.queenAnt.getX(), this.queenAnt.getY());
    }

    @Override
    public ArrayList<Point> getOtherAntPositions(Point self) {
        ArrayList<Point> antPositions = new ArrayList<>();
        for(Ant ant : this.ants) {
            if(ant.getX() != self.getX() || ant.getY() != self.getY()) antPositions.add(ant.getPosition());
        }
        return antPositions;
    }

    @Override
    public boolean antAtPosition(int x, int y) {
        for(Ant ant : this.ants) {
            if(ant.getX() == x && ant.getY() == y) return true;
        }
        return false;
    }

    @Override
    public void onFeedQueenRequest(int antId) {
        for(Ant ant : this.ants) {
            if(ant.id == antId) {
                if(ant.inventory[ant.getLastInventoryIndex()] != null) {
                    Ressource currentRessource = (Ressource) ant.inventory[ant.getLastInventoryIndex()];
                    this.queenAnt.currentEnergy += currentRessource.getQuantite();
                    ant.inventory[ant.getLastInventoryIndex()] = null;
                }
            }
        }
    }

    @Override
    public void onSpawnAntRequest(AntType antType, int x, int y) {
        this.newbornAnts.add(antType.createNewInstance(x, y));
    }

    @Override
    public int getQueenHealth() {
        return this.queenAnt.currentHealth;
    }

}
