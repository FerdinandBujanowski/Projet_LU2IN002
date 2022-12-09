/**
 * Enum utilisée afin d'établir de manière constante les différentes types possibles de fourmi
 * ainsi que d'initialiser des valeurs différentes pour chaque type
 *
 */
public enum AntType {

    QUEEN_ANT(500, 1000),
    GATHERER_ANT(60, 20),
    WARRIOR_ANT(300, 50);

    final int maxEnergy, maxHealth;

    /**
     * @param maxEnergy le nombre des points 'énergie' d'un type de fourmi
     * @param maxHealth le nombre des points 'santé' d'un type de fourmi

     */
    AntType(int maxEnergy, int maxHealth) {
        this.maxEnergy = maxEnergy;
        this.maxHealth = maxHealth;
    }

    /**
     * @param x position initale - x
     * @param y position initiale - y
     * @return une nouvelle instance de fourmi en fonction de son type
     */
    public Ant createNewInstance(int x, int y) {
        switch (this) {
            case QUEEN_ANT : return new QueenAnt(x, y, this);
            case GATHERER_ANT : return new GathererAnt(x, y, this);
            case WARRIOR_ANT : return new WarriorAnt(x, y, this);
        }
        return null;
    }
}
