import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI extends JFrame {

    public GUI(Simulation simulation) {
        super("Ant Simulation");
        Terrain terrain = simulation.terrain;
        int width = terrain.nbLignes * 25;
        int height = (int)Math.round((terrain.nbColonnes / (double)terrain.nbLignes) * width);
        this.setContentPane(new SimulationPanel(simulation, new Dimension(width, height)));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }
}

class SimulationPanel extends JPanel {

    private final ColonyData colonyData;
    private final Terrain terrain;
    private final ArrayList<Barrier> barriers;
    private final ArrayList<Predator> predators;

    public SimulationPanel(Simulation simulation, Dimension size) {
        super(null);
        this.setPreferredSize(size);
        this.colonyData = simulation.getAsColonyData();
        this.terrain = simulation.terrain;
        this.barriers = simulation.barriers;
        this.predators = simulation.predators;
    }

    @Override
    public void paintComponent(Graphics g) {
        int stepX = this.getWidth() / terrain.nbLignes;
        int stepY = this.getHeight() / terrain.nbColonnes;


        g.setColor(Color.GREEN);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        for(int x = 0; x < terrain.nbLignes; x++) {
            for(int y = 0; y < terrain.nbColonnes; y++) {
                boolean somethingThere = false;
                if(!terrain.caseEstVide(x, y)) {
                    somethingThere = true;
                    Ressource ressource = terrain.getCase(x, y);
                    assert ressource != null;
                    if(ressource instanceof Berry) g.setColor(Color.RED);
                    else g.setColor(Color.WHITE);
                }
                if(colonyData.antAtPosition(x, y)) {
                    somethingThere = true;
                    Point queenPosition = colonyData.getQueenPosition();
                    if(queenPosition.x == x && queenPosition.y == y) {
                        g.setColor(Color.YELLOW);
                    } else g.setColor(Color.DARK_GRAY);
                } else if(Predator.predatorAtPosition(x, y, predators)) {
                    somethingThere = true;
                    g.setColor(Color.BLUE);
                }
                else if(Barrier.barrierAt(barriers, x, y)) {
                    somethingThere = true;
                    g.setColor(Color.BLACK);
                }

                if(somethingThere) g.fillRect(x * stepX, y * stepY, stepX, stepY);
            }
        }
    }
}
