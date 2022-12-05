import java.awt.*;

public enum Direction {

    UP, DOWN, LEFT, RIGHT;

    public static Direction getDirection(Point lastMovementVector) {
        //each movement should have a norm of 1
        assert Math.abs(lastMovementVector.x) + Math.abs(lastMovementVector.y) == 1;
        if(lastMovementVector.x == -1) return LEFT;
        else if(lastMovementVector.x == 1) return RIGHT;
        else if(lastMovementVector.y == -1) return UP;
        else if(lastMovementVector.y == 1) return DOWN;
        else return UP;
    }
}
