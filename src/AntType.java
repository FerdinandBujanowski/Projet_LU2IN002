public enum AntType {

    QUEEN_ANT(500, 1000),
    GATHERER_ANT(60, 20),
    WARRIOR_ANT(300, 50);

    final int maxEnergy, maxHealth;

    /**
     *
     * @param maxEnergy l'energie maximale
     * @param maxHealth la santé maximale
     * Constructuer: initialise les valeurs de maxHealth et maxEnergy
     */
    AntType(int maxEnergy, int maxHealth) {
        this.maxEnergy = maxEnergy;
        this.maxHealth = maxHealth;
    }

    /**
     *
     * @param x la position sur l'axe de coordonées
     * @param y la position sur l'axe de oordonées
     * @return une nouvelle fourmi
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
