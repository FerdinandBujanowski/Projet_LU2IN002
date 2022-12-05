import java.awt.*;
import java.util.ArrayList;

public abstract class Animal {

    protected int currentEnergy;
    protected int currentHealth;

    private int x, y, z;
    protected Direction currentDirection;

    public Animal(int x, int y) {
        this.x = x;
        this.y = y;
        this.currentDirection = Direction.UP;
    }

    public int getCurrentEnergy() {
        return this.currentEnergy;
    }
    public int getCurrentHealth() {
        return this.currentHealth;
    }

    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }
    public Point getPosition() {
        return new Point(this.x, this.y);
    }
    public double distance(int x, int y) {
        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }
    public void seDeplacer(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }
    public boolean tryMoveAlongVector(Point vector, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        int absX = Math.abs(vector.x);
        int absY = Math.abs(vector.y);
        Point relativeMovement = absX >= absY ?
                new Point(vector.x / (absX != 0 ? absX : 1), 0)
                : new Point(0, vector.y / (absY != 0 ? absY : 1));
        Point newPosition = new Point(this.x + relativeMovement.x, this.y + relativeMovement.y);

        if(Animal.animalBlocking(newPosition, colonyData.getOtherAntPositions(this.getPosition()))) return false;
        if(Barrier.barrierAt(barriers, newPosition.x, newPosition.y)) return false;

        ArrayList<Predator> otherPredators = new ArrayList<>(predators);
        if(this instanceof Predator) otherPredators.remove((Predator) this);
        if(Animal.animalBlocking(newPosition, Predator.getPredatorPositions(otherPredators))) return false;

        seDeplacer(newPosition.x, newPosition.y);
        return true;
    }

    public Point getFreePoint(Point currentPosition, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        ArrayList<Point> freePoints = new ArrayList<>();

        ArrayList<Predator> otherPredators = new ArrayList<>(predators);
        if(this instanceof Predator) otherPredators.remove((Predator) this);
        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                Point newPosition = new Point(currentPosition.x + x, currentPosition.x + y);
                if(
                        !Animal.animalBlocking(newPosition, colonyData.getOtherAntPositions(this.getPosition()))
                        && !Animal.animalBlocking(newPosition, Predator.getPredatorPositions(otherPredators))
                        && !Barrier.barrierAt(barriers, newPosition.x, newPosition.y)
                        && (newPosition.x != currentPosition.x && newPosition.y != currentPosition.y)
                ) freePoints.add(newPosition);
            }
        }
        if(freePoints.size() == 0) return null;
        else return freePoints.get((int)(Math.random() * freePoints.size()));
    }

    private static boolean animalBlocking(Point newPosition, ArrayList<Point> otherPositions) {
        for(Point position : otherPositions) {
            if(position.x == newPosition.x && position.y == newPosition.y) return true;
        }
        return false;
    }

    public boolean touches(Point otherPosition) {
        return Math.abs(this.getX() - otherPosition.x) <= 1 && Math.abs(this.getY() - otherPosition.y) <= 1;
    }

    public void tryMoving(Point vector, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        Point oldPosition = this.getPosition();
        if(this.tryMoveAlongVector(vector, barriers, predators, colonyData)) {
            //moving succeeded
            this.calculateMovingCosts();
        } else {
            //if movement blocked, move other direction
            Point freePoint = this.getFreePoint(this.getPosition(), barriers, predators, colonyData);
            if(freePoint != null) {
                Point newVector = new Point(freePoint.x - this.getX(), freePoint.y - this.getY());
                this.tryMoveAlongVector(newVector, barriers, predators, colonyData);
                this.calculateMovingCosts();
            }
        }
        Point directionVector = new Point(this.getX() - oldPosition.x, this.getY() - oldPosition.y);
        this.currentDirection = Direction.getDirection(directionVector);
    }

    public void calculateMovingCosts() {}
}
