import java.awt.*;
import java.util.ArrayList;
/**
 * La classe représentant un animal
 */
public abstract class Animal {

    protected int currentEnergy;
    protected int currentHealth;
    protected int drunkCooldown;

    private int x, y;
    protected Direction currentDirection;

    /**
     * Constructeur: initialisant les valeurs x et y (leur emplacement), et leur direction
     * @param x la position sur l'axe de coordonées
     * @param y la position sur l'axe de oordonées
     */
    public Animal(int x, int y) {
        this.x = x;
        this.y = y;
        this.currentDirection = Direction.UP;
    }

    /** 
     * @return la valeur de x associé a l'animal 
     */
    public int getX() {
        return this.x;
    }

    /** 
     * @return la valeur de y associé a l'animal 
     */
    public int getY() {
        return this.y;
    }

    /** 
     * @return la position (type Point) de l'animal
     */
    public Point getPosition() {
        return new Point(this.x, this.y);
    }

    /** 
     * @param x la position sur l'axe de coordonées
     * @param y la position sur l'axe de oordonées
     * @return la distance d'un animal a un autre
     */
    public double distance(int x, int y) {
        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
    }

    /** Fonction qui change l'emplacement de l'animal et changer ses valeurs de x et y
     *
     * @param newX la nouvelle position sur l'axe de coordonées
     * @param newY la nouvelle position sur l'axe de oordonées
     */
    public void seDeplacer(int newX, int newY) {
        this.x = newX;
        this.y = newY;
    }

    /** Fonction qui decrit le fonctionnement d'un animal apres chaque iteration (tick)
     *
     * @param terrain
     * @param barriers
     * @param predators
     * @param colonyData
     */
    public abstract void tick(Terrain terrain, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData);


    /** 
     *
     * @param vector point qui decrit la direction que l'animal va essayer de suivre
     * @param barriers toutes les barrières présentes sur le terrain
     * @param predators la liste des prédateurs actuellement présents sur le terrain
     * @param colonyData la colonie contenant toutes les fourmis dans la simulation
     * @return true si l'animal peut se deplacer vers le vecteur mis en parametre, et false s'il ne peut pas.
     */
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

    /** 
     *
     * @param currentPosition la position actuelle de l'animal
     * @param barriers toutes les barrières présentes sur le terrain
     * @param predators la liste des prédateurs actuellement présents sur le terrain
     * @param colonyData la colonie contenant toutes les fourmis dans la simulation
     * @return  une localisation disponible en relation de la position actuelle, en prenant en compte les localisations que les animaux occupent.
     */
    public Point getFreePoint(Point currentPosition, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        ArrayList<Point> freePoints = new ArrayList<>();

        ArrayList<Predator> otherPredators = new ArrayList<>(predators);
        if(this instanceof Predator) otherPredators.remove((Predator) this);
        for(int x = -1; x <= 1; x++) {
            for(int y = -1; y <= 1; y++) {
                Point newPosition = new Point(currentPosition.x + x, currentPosition.y + y);
                if(
                        !Animal.animalBlocking(newPosition, colonyData.getOtherAntPositions(this.getPosition()))
                        && !Animal.animalBlocking(newPosition, Predator.getPredatorPositions(otherPredators))
                        && !Barrier.barrierAt(barriers, newPosition.x, newPosition.y)
                        && (newPosition.x != currentPosition.x || newPosition.y != currentPosition.y)
                ) freePoints.add(newPosition);
            }
        }
        if(freePoints.size() == 0) return null;
        else return freePoints.get((int)(Math.random() * freePoints.size()));
    }

    /** 
     *
     * @param newPosition la nouvelle position
     * @param otherPositions la liste contenant les autres positions possible
     * @return true si un animal occuper la position newPosition, false sinon
     */
    private static boolean animalBlocking(Point newPosition, ArrayList<Point> otherPositions) {
        for(Point position : otherPositions) {
            if(position.x == newPosition.x && position.y == newPosition.y) return true;
        }
        return false;
    }

    /** 
     *
     * @param otherPosition l'autre position mise en compairaison 
     * @return true si l'animal 'touche' un autre position, false sinon
     */
    public boolean touches(Point otherPosition) {
        if(otherPosition == null) return false;
        return Math.abs(this.getX() - otherPosition.x) <= 1 && Math.abs(this.getY() - otherPosition.y) <= 1;
    }

    /** 
     *
     * @param vector point qui decrit la direction que l'animal va essayer de suivre
     * @param barriers toutes les barrières présentes sur le terrain
     * @param predators la liste des prédateurs actuellement présents sur le terrain
     * @param colonyData la colonie contenant toutes les fourmis dans la simulation
     * Fonction qui bouge l'animal vers un vecteur donné, et si c'est bloqué l'animal bouge vers une autre direction
     */
    public void tryMoving(Point vector, ArrayList<Barrier> barriers, ArrayList<Predator> predators, ColonyData colonyData) {
        if((this).drunkCooldown > 0) {
            Point randomPoint = this.getFreePoint(this.getPosition(), barriers, predators, colonyData);
            vector.setLocation(randomPoint.x - this.getX(), randomPoint.y - this.getY());
        }

        Point oldPosition = this.getPosition();
        if(this.tryMoveAlongVector(vector, barriers, predators, colonyData)) {
            //tentative de bouger vers le vecteur donné est une reussite ! 
            this.calculateMovingCosts();
            this.updateDirection(oldPosition);
        } else {
            //si tentative un echec, l'animal bouge vers un autre vecteur
            Point freePoint = this.getFreePoint(this.getPosition(), barriers, predators, colonyData);
            if(freePoint != null) {
                Point newVector = new Point(freePoint.x - this.getX(), freePoint.y - this.getY());
                if (this.tryMoveAlongVector(newVector, barriers, predators, colonyData)) {
                    this.calculateMovingCosts();
                    this.updateDirection(oldPosition);
                }
            }
        }
    }

    /** 
     *
     * @param oldPosition l'ancienne position de l'animal
     * Fonction qui fait la mise à jour de la direction de l'animal
     */
    private void updateDirection(Point oldPosition) {
        Point directionVector = new Point(this.getX() - oldPosition.x, this.getY() - oldPosition.y);
        this.currentDirection = Direction.getDirection(directionVector);
    }

    /** Fonction qui fait le calcule des couts de bouger de l'animal
     *
     */
    public void calculateMovingCosts() {}
}
