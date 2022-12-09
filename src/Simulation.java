import java.util.ArrayList;

public class Simulation {

    public static long iteration = 0;

    public static final int QUEEN_X = 0, QUEEN_Y = 0;
    public static final int MAX_RESSOURCES = 15;
    public static final int MAX_ANTS = 20;
    public static final int MAX_PREDATORS = 5;
    public static final int EGG_COST = 100;
    public static final int PROXIMITY = 10;
    public static final int LOW_HEALTH = 10;
    public static final int MAX_INVENTORY = 2;

    private final Colony colony;
    public final Terrain terrain;
    public final ArrayList<Barrier> barriers;
    public final ArrayList<Predator> predators;
    public final ArrayList<Predator> deadPredators;

    /**
     *
     * @param lines
     * @param columns
     */
    public Simulation(int lines, int columns) {
        this.terrain = new Terrain(lines, columns);
        this.colony = new Colony();

        this.barriers = new ArrayList<>();
        this.establishBorders();
        this.predators = new ArrayList<>();
        this.deadPredators=new ArrayList<>();
    }

    /**
     *
     */
    private void establishBorders() {
        for(int x = -1; x < this.terrain.nbLignes + 1; x++) {
            for(int y = -1; y < this.terrain.nbColonnes + 1; y++) {
                if(x == -1 || y == -1 || x == this.terrain.nbLignes || y == this.terrain.nbColonnes) {
                    this.barriers.add(new Barrier(x, y));
                }
            }
        }
    }

    /**
     *
     */
    public void tick() {
        iteration++;

        // 1) générer nouvelles ressources
        if(!this.ressourcesMax()) {
            for(int x = 0; x < this.terrain.nbLignes; x++) {
                for(int y = 0; y < this.terrain.nbColonnes; y++) {
                    Ressource currentRessource = this.terrain.getCase(x, y);
                    if(currentRessource == null) {
                        Ressource r = null;
                        if(Math.random() <= Grain.p_spawn) {
                            r = new Grain();
                        } else if(Math.random() <= Berry.p_spawn) {
                            r = new Berry();
                        }
                        if(r != null) this.terrain.setCase(x, y, r);
                    } else {
                        if(currentRessource.getClass() == Berry.class) {
                            if(Math.random() <= Berry.p_ferment) ((Berry) currentRessource).ferment();
                        }
                    }
                }
            }
        }

        //2) rafraichir chaque fourmi
        this.colony.tick(this.terrain, this.barriers, this.predators);

        //générer prédateurs
        if (Math.random() <= Predator.p_spawn && this.predators.size() < Simulation.MAX_PREDATORS) {
            int randX = (int)(Math.random() * this.terrain.nbLignes);
            int randY = (int)(Math.random() * this.terrain.nbColonnes);
            Predator p = new Predator(randX, randY);
            this.predators.add(p);
        }
        //rafraichir chaque prédateur
        for(Predator p : this.predators) {
            p.tick(null, this.barriers, this.predators, this.colony);
            if (p.currentHealth <= 0) {
                this.deadPredators.add(p);
            }
        }
        this.predators.removeAll(this.deadPredators);
        this.deadPredators.removeAll(this.deadPredators);
    }

    /**
     *
     * @return
     */
    public ColonyData getAsColonyData() {
        return this.colony;
    }

    /**
     *
     * @return
     */
    private boolean ressourcesMax() {
        int counter = 0;
        for(int x = 0; x < terrain.nbLignes; x++) {
            for(int y = 0; y < terrain.nbColonnes; y++) {
                if(!terrain.caseEstVide(x, y)) counter++;
            }
        }
        return counter >= Simulation.MAX_RESSOURCES;
    }
}
