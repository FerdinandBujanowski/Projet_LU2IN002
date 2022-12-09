import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends JFrame {

    public static final int STEP_PIXEL = 32;

    /**
     *
     * @param simulation
     */
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

    /**
     *
     */
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
    private final int maxHealth;

    private Image[] QUEEN, WARRIOR, GATHERER, PREDATOR;
    private Image BERRY, BERRY_FERMENTED, GRAIN;

    /**
     *
     * @param simulation
     * @param size
     */
    public SimulationPanel(Simulation simulation, Dimension size) {
        super(null);
        this.setPreferredSize(size);
        this.colonyData = simulation.getAsColonyData();
        this.terrain = simulation.terrain;
        this.barriers = simulation.barriers;
        this.predators = simulation.predators;
        this.maxHealth = colonyData.getQueenHealth();

        try {
            String prop = System.getProperty("user.dir");
            String withSrc = prop.contains("src") ? "" : File.separator + "src";
            String dir = System.getProperty("user.dir") + withSrc + File.separator + "images" + File.separator;
            QUEEN = this.getAllImageRotations(ImageIO.read(new File(dir + "queen.png")));
            WARRIOR = this.getAllImageRotations(ImageIO.read(new File(dir + "warrior.png")));
            GATHERER = this.getAllImageRotations(ImageIO.read(new File(dir + "gatherer.png")));
            PREDATOR = this.getAllImageRotations(ImageIO.read(new File(dir + "predator.png")));

            BERRY = this.getScaledInstance(ImageIO.read(new File(dir + "berry.png")));
            BERRY_FERMENTED = this.getScaledInstance(ImageIO.read(new File(dir + "berry_fermented.png")));
            GRAIN = this.getScaledInstance(ImageIO.read(new File(dir + "grain.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param image
     * @return
     */
    private Image[] getAllImageRotations(BufferedImage image) {
        Image[] images = new Image[4];
        images[0] = this.getScaledInstance(image);
        for(int i = 1; i < images.length; i++) {
            images[i] = this.getScaledInstance(this.rotate(image, i * 90));
        }
        return images;
    }

    /**
     *
     * @param original
     * @param angle
     * @return
     */
    private BufferedImage rotate(BufferedImage original, double angle) {

        int w = original.getWidth();
        int h = original.getHeight();

        BufferedImage rotated = new BufferedImage(w, h, original.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.rotate(Math.toRadians(angle), w/2., h/2.);
        graphic.drawImage(original, null, 0, 0);
        graphic.dispose();
        return rotated;
    }

    /**
     *
     * @param original
     * @return
     */
    private Image getScaledInstance(BufferedImage original) {
        return original.getScaledInstance(GUI.STEP_PIXEL, GUI.STEP_PIXEL, 0);
    }

    /**
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int stepX = this.getWidth() / terrain.nbLignes;
        int stepY = this.getHeight() / terrain.nbColonnes;

        g.setColor(new Color(45, 96, 40));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        for(int x = 0; x < terrain.nbLignes; x++) {
            for(int y = 0; y < terrain.nbColonnes; y++) {
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

                Point queenPosition = colonyData.getQueenPosition();
                Image queenImage = this.QUEEN[colonyData.getAntDirection(queenPosition).getCorrespondingIndex()];
                g.drawImage(queenImage, queenPosition.x * stepX, queenPosition.y * stepY, null);
                for(Point antPosition : colonyData.getOtherAntPositions(colonyData.getQueenPosition())) {
                    Image antImage = null;
                    AntType antType = colonyData.getAntType(antPosition);
                    assert antType != null;
                    int index = colonyData.getAntDirection(antPosition).getCorrespondingIndex();
                    if(antType == AntType.GATHERER_ANT) antImage = this.GATHERER[index];
                    else if(antType == AntType.WARRIOR_ANT) antImage = this.WARRIOR[index];
                    if(antImage != null) {
                        g.drawImage(antImage, antPosition.x * stepX, antPosition.y * stepY, null);
                    }
                }
                for(Point predatorPosition : Predator.getPredatorPositions(predators)) {
                    int predatorIndex = Predator.getPredatorDirection(predatorPosition, predators).getCorrespondingIndex();
                    g.drawImage(this.PREDATOR[predatorIndex], predatorPosition.x * stepX, predatorPosition.y * stepY, null);
                }

                double queenHealth = colonyData.getQueenHealth() / (double)this.maxHealth;
                g.setColor(Color.RED);
                Dimension healthBar = new Dimension((int)Math.round(GUI.STEP_PIXEL * queenHealth), 5);
                g.fillRect(queenPosition.x * stepX, queenPosition.y * stepY - GUI.STEP_PIXEL / 2, healthBar.width, healthBar.height);
            }
        }
    }
}
