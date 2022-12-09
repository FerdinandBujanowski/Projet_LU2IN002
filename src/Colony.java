import java.awt.*;
import java.util.ArrayList;

public class Colony implements ColonyData {

    private final QueenAnt queenAnt;
    private final ArrayList<Ant> ants;
    private final ArrayList<Ant> newbornAnts;

    public static double pWarriorSpawn = 0.4;

    /**
     *
     */
    public Colony() {
        this.ants = new ArrayList<>();
        this.newbornAnts = new ArrayList<>();
        try {
            this.onSpawnAntRequest(AntType.QUEEN_ANT, Simulation.QUEEN_X, Simulation.QUEEN_Y);
        } catch (QueenAlreadyExistsException e) {
            e.printStackTrace();
        }
        this.queenAnt = (QueenAnt) this.ants.get(0);
    }

    /**
     *
     * @param terrain
     * @param barriers
     * @param predators
     */
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

    /**
     *
     * @param pos
     * @return
     */
    private Ant getAntFromPos(Point pos) {
        for (Ant a: ants){
            if ((a.getPosition()).equals(pos)) return a;
        }
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public Point getQueenPosition() {
        return new Point(this.queenAnt.getX(), this.queenAnt.getY());
    }

    /**
     *
     * @param self
     * @return
     */
    @Override
    public ArrayList<Point> getOtherAntPositions(Point self) {
        ArrayList<Point> antPositions = new ArrayList<>();
        for(Ant ant : this.ants) {
            if(ant.getX() != self.getX() || ant.getY() != self.getY()) antPositions.add(ant.getPosition());
        }
        return antPositions;
    }

    /**
     *
     * @param antId
     */
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

    /**
     *
     * @param antType
     * @param x
     * @param y
     */
    @Override
    public void onSpawnAntRequest(AntType antType, int x, int y) throws QueenAlreadyExistsException {
        if(this.queenAnt != null && antType == AntType.QUEEN_ANT) throw new QueenAlreadyExistsException();
        this.newbornAnts.add(antType.createNewInstance(x, y));
    }

    /**
     *
     * @return
     */
    @Override
    public int getQueenHealth() {
        return this.queenAnt.currentHealth;
    }

    /**
     *
     * @param pos
     */
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

    /**
     *
     * @param antPosition
     * @return
     */
    @Override
    public Direction getAntDirection(Point antPosition) {
        Ant ant = this.getAntFromPos(antPosition);
        if(ant == null) return Direction.UP;
        return ant.currentDirection;
    }

    /**
     *
     * @param position
     * @return
     */
    @Override
    public AntType getAntType(Point position) {
        Ant ant = this.getAntFromPos(position);
        if(ant == null) return null;
        return ant.antType;
    }

    /**
     *
     * @return
     */
    @Override
    public int getGathererCount() {
        int counter = 0;
        for(Ant ant : this.ants) {
            if(ant instanceof GathererAnt) counter++;
        }
        return counter;
    }
}
