import java.util.ArrayList;

public class Barrier {

    public final int x, y;

    public Barrier(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static boolean barrierAt(ArrayList<Barrier> barrierList, int x, int y) {
        for(Barrier barrier : barrierList) {
            if(barrier.x == x && barrier.y == y) return true;
        }
        return false;
    }
}
