import java.awt.*;
import java.util.ArrayList;

public interface ColonyData {

    Point getQueenPosition();
    ArrayList<Point> getOtherAntPositions(Point self);
    boolean antAtPosition(int x, int y);
    void onFeedQueenRequest(int antId);
    void onSpawnAntRequest(AntType antType, int x, int y);
    int getQueenHealth();
}
