import java.awt.*;
import java.util.ArrayList;

/**
 * La classe représentant la colonie avec la reine et tous ses fourmies 'guerrières' et 'ouvrières'
 */
public class Colony implements ColonyData {

    private final QueenAnt queenAnt;
    private final ArrayList<Ant> ants;
    private final ArrayList<Ant> newbornAnts;

    public static double pWarriorSpawn = 0.4;

    /**
     * Constructeur : initialise la liste dynamique des fourmis, crée la reine
     */
    public Colony() {
        this.ants = new ArrayList<>();
        this.newbornAnts = new ArrayList<>();
        this.queenAnt = (QueenAnt) AntType.QUEEN_ANT.createNewInstance(Simulation.QUEEN_X, Simulation.QUEEN_Y);
        this.ants.add(this.queenAnt);
    }

    /**
     * nouvelle itération : mise à jour de toutes les fourmis en fonction des paramètres importants
     * @param terrain le terrain actuel avec toutes les ressources
     * @param barriers toutes les barrières présentes sur le terrain
     * @param predators la liste des prédateurs actuellement présents sur le terrain
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
     * @param pos la position théorique d'une fourmi
     * @return si elle existe, la fourmi actuellement à cette position
     */
    private Ant getAntFromPos(Point pos) {
        for (Ant a: ants){
            if ((a.getPosition()).equals(pos)) return a;
        }
        return null;
    }

    /**
     * @return la position actuelle de la reine
     */
    @Override
    public Point getQueenPosition() {
        return new Point(this.queenAnt.getX(), this.queenAnt.getY());
    }

    /**
     * @param self la position actuelle de la classe fourmi appelant cette fonction
     * @return une liste des positions actuelles des fourmis sauf celle de la fourmi appelant cette fonction
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
     * fonction appelée lorsqu'une fourmi essaie de nourrir la reine
     * @param antId identifiant de la fourmi appelant cette fonction
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
     * fonction appelée lorsque la reine essaie de générer une nouvelle fourmi
     * @param antType le type de la nouvelle fourmi
     * @param x position de la nouvelle fourmi (x)
     * @param y position de la nouvelle fourmi (y)
     * @throws QueenAlreadyExistsException si une nouvelle reine est sur le point d'être créée bien qu'il y ait déjà une reine
     */
    @Override
    public void onSpawnAntRequest(AntType antType, int x, int y) throws QueenAlreadyExistsException {
        if(this.queenAnt != null && antType == AntType.QUEEN_ANT) throw new QueenAlreadyExistsException();
        this.newbornAnts.add(antType.createNewInstance(x, y));
    }

    /**
     * @return la valeur entière représentant la santé actuelle de la reine (si 0 alors la reine meurt)
     */
    @Override
    public int getQueenHealth() {
        return this.queenAnt.currentHealth;
    }

    /**
     * fonction appelée si un prédateur essaie d'attaquer une fourmi
     * @param pos la position théorique de la fourmi attaquée
     */
    @Override
    public void requestDamageAnt(Point pos) {
        Ant ant = this.getAntFromPos(pos);
        if (ant == null) return;
        if (ant instanceof GathererAnt) ant.currentHealth = 0;
        else ant.currentHealth--;
    }

    /**
     * @param antPosition la position d'une fourmi
     * @return la direction dans laquelle une fourmi à une certaine position pointe
     */
    @Override
    public Direction getAntDirection(Point antPosition) {
        Ant ant = this.getAntFromPos(antPosition);
        if(ant == null) return Direction.UP;
        return ant.currentDirection;
    }

    /**
     * @param position la position d'une fourmi
     * @return le type de la fourmi qui se situe à une position donnée
     */
    @Override
    public AntType getAntType(Point position) {
        Ant ant = this.getAntFromPos(position);
        if(ant == null) return null;
        return ant.antType;
    }

    /**
     * @return le nombre des fourmis de type 'ouvrière'
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
