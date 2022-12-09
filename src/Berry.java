public class Berry extends Ressource {

    private boolean isFermented;
    public static final double p_ferment = 0.2;
    public static final double p_spawn = 0.001; 
    public static final int DRUNK_TICKS = 10;

    /**
     * initialise une nouvelle ressource de type 'baie' (qui n'est pas encore fermentée)
     */
    public Berry() {
        super("berry", 20);
        this.isFermented = false;
    }

    /**
     * avec une certaine probabilité, la baie peut fermenter
     */
    public void ferment() {
        if(Math.random() <= p_ferment) this.isFermented = true;
    }

    /**
     * @return true si la baie est fermentée, rendant la fourmi qui la mange intoxiqué (false sinon)
     */
    public boolean isFermented() {
        return this.isFermented;
    }
}
