public class Berry extends Ressource {

    private boolean isFermented;
    public static final double p_ferment = 0.02;
    public static final double p_spawn = 0.002;

    public Berry() {
        super("berry", 20);
        this.isFermented = false;
    }

    public void ferment() {
        if(Math.random() <= p_ferment) this.isFermented = true;
    }
}
