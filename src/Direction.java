import java.awt.*;

public enum Direction {

    UP, DOWN, LEFT, RIGHT;

    /**
     *
     * @param lastMovementVector
     * @return
     */
    public static Direction getDirection(Point lastMovementVector) {
        //assurer que chaque déplacement est vraiment que d'un seul pixel
        assert Math.abs(lastMovementVector.x) + Math.abs(lastMovementVector.y) == 1;
        if(lastMovementVector.x == -1) return LEFT;
        else if(lastMovementVector.x == 1) return RIGHT;
        else if(lastMovementVector.y == -1) return UP;
        else if(lastMovementVector.y == 1) return DOWN;
        else return UP;
    }

    /**
     *
     * @return
     */
    public int getCorrespondingIndex() {
        return switch (this) {
            case UP -> 0;
            case RIGHT -> 1;
            case DOWN -> 2;
            case LEFT -> 3;
        };
    }
}
