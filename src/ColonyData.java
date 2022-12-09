import java.awt.*;
import java.util.ArrayList;

/**
 * Interface pour faciliter le transfert de données entre les fourmis / prédateurs / le GUI et la colonie
 * SANS que les différentes classes soient capables de modifier directement les autres fourmies / la reine
 * implémentée par Colony.java
 */
public interface ColonyData {

    Point getQueenPosition();
    ArrayList<Point> getOtherAntPositions(Point self);
    void onFeedQueenRequest(int antId);
    void onSpawnAntRequest(AntType antType, int x, int y) throws QueenAlreadyExistsException;
    int getQueenHealth();
    void requestDamageAnt(Point pos);
    Direction getAntDirection(Point antPosition);
    AntType getAntType(Point position);
    int getGathererCount();
}
