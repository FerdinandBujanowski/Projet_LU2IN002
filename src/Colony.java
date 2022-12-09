import java.awt.*;
import java.util.ArrayList;

public class Colony implements ColonyData {

    private final QueenAnt queenAnt;
    private final ArrayList<Ant> ants;
    private final ArrayList<Ant> newbornAnts;

    public static double pWarriorSpawn = 0.4;

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

    private Ant getAntFromPos(Point pos) {
        for (Ant a: ants){
            if ((a.getPosition()).equals(pos)) return a;
        }
        return null;
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
    public void onFeedQueenRequest(int antId) {
        for(Ant ant : this.ants) {
            if(ant.id == antId) {
                if(ant.inventory[ant.getLastInventoryIndex()] != null) {
                    Ressource currentRessource = ant.inventory[ant.getLastInventoryIndex()];
                    this.queenAnt.eat(currentRessource);
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

    @Override
    public void requestDamageAnt(Point pos) {
        Ant ant = this.getAntFromPos(pos);
        if (ant == null) return;
        if (ant instanceof GathererAnt) ant.currentHealth = 0;
        else ant.currentHealth--;
    }
    @Override
    public void requestDamageAntSpecial(Point pos) {
        Ant ant = this.getAntFromPos(pos);
        if (ant == null) return;
        if (ant instanceof GathererAnt) ant.currentHealth = 0;
        if (ant instanceof QueenAnt) ant.currentHealth-=10;
        else ant.currentHealth-=2;
    }

    @Override
    public Direction getAntDirection(Point antPosition) {
        Ant ant = this.getAntFromPos(antPosition);
        if(ant == null) return Direction.UP;
        return ant.currentDirection;
    }

    @Override
    public AntType getAntType(Point position) {
        Ant ant = this.getAntFromPos(position);
        if(ant == null) return null;
        return ant.antType;
    }

    @Override
    public int getGathererCount() {
        int counter = 0;
        for(Ant ant : this.ants) {
            if(ant instanceof GathererAnt) counter++;
        }
        return counter;
    }
}
