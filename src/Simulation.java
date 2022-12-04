import java.util.ArrayList;

public class Simulation {

    public static long iteration = 0;

    public static final int QUEEN_X = 0, QUEEN_Y = 0;
    public static final int MAX_RESSOURCES = 35;
    public static final int EGG_COST = 50;

    private final Colony colony;
    public final Terrain terrain;
    public final ArrayList<Barrier> barriers;
    public final ArrayList<Predator> predators;

    public Simulation(int lines, int columns) {
        this.terrain = new Terrain(lines, columns);
        this.colony = new Colony();

        //this.colony.onSpawnAntRequest(AntType.GATHERER_ANT, 1, 1);
        this.barriers = new ArrayList<>();
        this.establishBorders();
        this.predators = new ArrayList<>();
    }

    private void establishBorders() {
        for(int x = -1; x < this.terrain.nbLignes + 1; x++) {
            for(int y = -1; y < this.terrain.nbColonnes + 1; y++) {
                if(x == -1 || y == -1 || x == this.terrain.nbLignes || y == this.terrain.nbColonnes) {
                    this.barriers.add(new Barrier(x, y));
                }
            }
        }
    }

    public void tick() {
        iteration++;

        // 1) générer de nouvelles ressources
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


        //generate predators

        //tick every ant
        this.colony.tick(this.terrain, this.barriers, this.predators);

        //tick every predator
    }

    public ColonyData getAsColonyData() {
        return this.colony;
    }

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
