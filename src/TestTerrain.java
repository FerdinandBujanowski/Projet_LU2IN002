/**
 * @author Ferdinand Bujanowski et Daniel Panariti (LU2IN002 2022oct)
 * Gestion d'un terrain
 */

public class TestTerrain {

	public static void main(String[] args) {

		Simulation simulation = new Simulation(Terrain.NBLIGNESMAX, Terrain.NBCOLONNESMAX);
		GUI gui = new GUI(simulation);

		while(simulation.getAsColonyData().getQueenHealth() > 0) {
			simulation.tick();
			gui.repaint();
			try {
				Thread.sleep(75);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		gui.dispose();
		simulation.terrain.affiche(2);
	}
}
