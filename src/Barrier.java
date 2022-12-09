import java.util.ArrayList;

/**
 * des barrières sont utilisées pour assurer que les éléments qui bougent sur le terrain ne peuvent pas y quitter
 * originalement conceptualisé pour un nouveau type des fourmis : des fourmis constructrices
 */
public class Barrier {

    public final int x, y;

    /**
     * @param x la position finale de la barrière - x
     * @param y la position finale de la barrière - y
     */
    public Barrier(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * function statique pour chercher une barrière à une position donnée
     * @param barrierList une liste dynamique des barrières
     * @param x la position de la barrière théorique - x
     * @param y la position de la barrière théorique - y
     * @return true s'il existe une barrière dans barrierList à la position donnée
     */
    public static boolean barrierAt(ArrayList<Barrier> barrierList, int x, int y) {
        for(Barrier barrier : barrierList) {
            if(barrier.x == x && barrier.y == y) return true;
        }
        return false;
    }
}
