public class Ressource {

    private static int nbRessourcesCreees = 0;
    public final int ident;
    public final String type;
    private int quantite;
    private int x;
    private int y;

    public Ressource(String type, int quantite) {
        this.type = type;
        this.quantite = quantite;
        this.ident = nbRessourcesCreees++;
        this.x = -1;
        this.y = -1;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getQuantite() {
        return this.quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setPosition(int lig, int col) {
        this.x = lig;
        this.y = col;
    }

    public void initialisePosition() {
        this.x = -1;
        this.y = -1;
    }

    public String toString() {
        String sortie = this.type + "[id:" + this.ident + " quantite: " + this.quantite + "] ";
        if (this.x != -1 && this.y != -1) {
            sortie = sortie + " en position (" + this.x + ", " + this.y + ")";
        } else {
            sortie = sortie + " n'est pas sur le terrain.";
        }

        return sortie;
    }
}