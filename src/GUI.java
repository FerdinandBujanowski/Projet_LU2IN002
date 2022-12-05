import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends JFrame {

    public static final int STEP_PIXEL = 32;

    public GUI(Simulation simulation) {
        super("Ant Simulation");
        Terrain terrain = simulation.terrain;
        int width = terrain.nbLignes * STEP_PIXEL;
        int height = (int)Math.round((terrain.nbColonnes / (double)terrain.nbLignes) * width);
        this.setContentPane(new SimulationPanel(simulation, new Dimension(width, height)));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setResizable(false);
        this.setCorrectLocation();
        this.setVisible(true);
    }

    private void setCorrectLocation() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int posX = (screenSize.width - this.getWidth()) / 2;
        int posY = (screenSize.height - this.getHeight()) / 2 - 20;
        this.setLocation(posX, posY);
    }
}

class SimulationPanel extends JPanel {

    private final ColonyData colonyData;
    private final Terrain terrain;
    private final ArrayList<Barrier> barriers;
    private final ArrayList<Predator> predators;

    private Image QUEEN, WARRIOR, GATHERER, BERRY, BERRY_FERMENTED, GRAIN;

    public SimulationPanel(Simulation simulation, Dimension size) {
        super(null);
        this.setPreferredSize(size);
        this.colonyData = simulation.getAsColonyData();
        this.terrain = simulation.terrain;
        this.barriers = simulation.barriers;
        this.predators = simulation.predators;

        try {
            String prop=System.getProperty("user.dir");
            String withSrc= prop.contains("src") ? "" : File.separator + "src";
            String dir = System.getProperty("user.dir") + withSrc + File.separator + "images" + File.separator;
            QUEEN = ImageIO.read(new File(dir + "queen.png")).getScaledInstance(32, 32, 0);
            WARRIOR = ImageIO.read(new File(dir + "warrior.png")).getScaledInstance(32, 32, 0);
            GATHERER = ImageIO.read(new File(dir + "gatherer.png")).getScaledInstance(32, 32, 0);
            BERRY = ImageIO.read(new File(dir + "berry.png")).getScaledInstance(32, 32, 0);
            BERRY_FERMENTED = ImageIO.read(new File(dir + "berry_fermented.png")).getScaledInstance(32, 32, 0);
            GRAIN = ImageIO.read(new File(dir + "grain.png")).getScaledInstance(32, 32, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int stepX = this.getWidth() / terrain.nbLignes;
        int stepY = this.getHeight() / terrain.nbColonnes;

        g.setColor(Color.GREEN);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        Point queenPosition = colonyData.getQueenPosition();
        for(int x = 0; x < terrain.nbLignes; x++) {
            for(int y = 0; y < terrain.nbColonnes; y++) {
                boolean somethingThere = false;
                if(!terrain.caseEstVide(x, y)) {

                    Ressource ressource = terrain.getCase(x, y);
                    Image currentRessourceImage = GRAIN;
                    assert ressource != null;
                    if(ressource instanceof Berry) {
                        if(((Berry) ressource).isFermented()) currentRessourceImage = BERRY_FERMENTED;
                        else currentRessourceImage = BERRY;
                    }
                    g.drawImage(currentRessourceImage, x * stepX, y * stepY, null);

                }
                if(Predator.predatorAtPosition(x, y, predators)) {
                    somethingThere = true;
                    g.setColor(Color.BLUE);
                }
                else if(Barrier.barrierAt(barriers, x, y)) {
                    somethingThere = true;
                    g.setColor(Color.BLACK);
                }

                if(somethingThere) g.fillRect(x * stepX, y * stepY, stepX, stepY);
                g.drawImage(this.QUEEN, queenPosition.x * stepX, queenPosition.y * stepY, null);
                for(Point antPosition : colonyData.getOtherAntPositions(colonyData.getQueenPosition())) {
                    g.drawImage(this.GATHERER, antPosition.x * stepX, antPosition.y * stepY, null);
                }
            }
        }
    }
}
