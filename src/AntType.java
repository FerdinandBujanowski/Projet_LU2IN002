public enum AntType {

    QUEEN_ANT(1000, 1000),
    GATHERER_ANT(60, 20),
    WARRIOR_ANT(300, 50);

    final int maxEnergy, maxHealth;

    AntType(int maxEnergy, int maxHealth) {
        this.maxEnergy = maxEnergy;
        this.maxHealth = maxHealth;
    }

    public Ant createNewInstance(int x, int y) {
        switch (this) {
            case QUEEN_ANT : return new QueenAnt(this);
            case GATHERER_ANT : return new GathererAnt(x, y, this);
            case WARRIOR_ANT : return new WarriorAnt(x, y, this);
        }
        return null;
    }
}
