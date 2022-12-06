import java.awt.*;
import java.util.ArrayList;

public interface ColonyData {

    Point getQueenPosition();
    ArrayList<Point> getOtherAntPositions(Point self);
    void onFeedQueenRequest(int antId);
    void onSpawnAntRequest(AntType antType, int x, int y);
    int getQueenHealth();
    void requestDamageAnt(Point pos);
    Direction getAntDirection(Point antPosition);
    AntType getAntType(Point position);
    int getGathererCount();
}
