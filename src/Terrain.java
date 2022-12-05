import java.util.ArrayList;

public final class Terrain {
    public static final int NBLIGNESMAX = 20;
    public static final int NBCOLONNESMAX = 20;
    public final int nbLignes;
    public final int nbColonnes;
    private Ressource[][] terrain;

    public Terrain() {
        this(20, 20);
    }

    public Terrain(int nblig, int nbcol) {
        if (nblig > 20) {
            this.nbLignes = 20;
        } else if (nblig <= 0) {
            this.nbLignes = 1;
        } else {
            this.nbLignes = nblig;
        }

        if (nbcol > 20) {
            this.nbColonnes = 20;
        } else if (nbcol <= 0) {
            this.nbColonnes = 1;
        } else {
            this.nbColonnes = nbcol;
        }

        this.terrain = new Ressource[this.nbLignes][this.nbColonnes];
    }

    public Ressource getCase(int lig, int col) {
        return this.sontValides(lig, col) ? this.terrain[lig][col] : null;
    }

    public Ressource videCase(int lig, int col) {
        if (this.sontValides(lig, col) && this.terrain[lig][col] != null) {
            Ressource elt = this.terrain[lig][col];
            elt.initialisePosition();
            this.terrain[lig][col] = null;
            return elt;
        } else {
            return null;
        }
    }

    public boolean setCase(int lig, int col, Ressource ress) {
        if (this.sontValides(lig, col)) {
            if (this.terrain[lig][col] != null) {
                this.terrain[lig][col].initialisePosition();
            }

            this.terrain[lig][col] = ress;
            ress.setPosition(lig, col);
            return true;
        } else {
            return false;
        }
    }

    public boolean caseEstVide(int lig, int col) {
        if (this.sontValides(lig, col)) {
            return this.terrain[lig][col] == null;
        } else {
            return true;
        }
    }

    public ArrayList<Ressource> lesRessources() {
        ArrayList<Ressource> list = new ArrayList<>();

        for(int lig = 0; lig < this.nbLignes; ++lig) {
            for(int col = 0; col < this.nbColonnes; ++col) {
                if (this.terrain[lig][col] != null) {
                    list.add(this.terrain[lig][col]);
                }
            }
        }

        return list;
    }

    public boolean sontValides(int lig, int col) {
        return lig >= 0 && lig < this.nbLignes && col >= 0 && col < this.nbColonnes;
    }

    public void affiche(int nbCaracteres) {
        String sortie = "";
        String cadre = ":";
        String ligne = "";
        int nbCar = Math.max(nbCaracteres, 1);

        int lig;
        for(lig = 0; lig < nbCar; ++lig) {
            ligne = ligne + "-";
        }

        for(lig = 0; lig < this.nbColonnes; ++lig) {
            cadre = cadre + ligne + ":";
        }

        cadre = cadre + "\n";
        sortie = cadre;

        for(lig = 0; lig < this.nbLignes; ++lig) {
            for(int col = 0; col < this.nbColonnes; ++col) {
                if (this.terrain[lig][col] == null) {
                    sortie = sortie + "|" + String.format("%-" + nbCar + "s", " ");
                } else {
                    sortie = sortie + "|" + this.premiersCar(this.terrain[lig][col].type, nbCar);
                }
            }

            sortie = sortie + "|\n" + cadre;
        }

        System.out.println(sortie);
    }

    public String toString() {
        int compte = 0;

        for(int i = 0; i < this.nbLignes; ++i) {
            for(int j = 0; j < this.nbColonnes; ++j) {
                if (this.terrain[i][j] != null) {
                    ++compte;
                }
            }
        }

        String sortie = "Terrain de " + this.nbLignes + "x" + this.nbColonnes + " cases: ";
        if (compte == 0) {
            sortie = sortie + "toutes les cases sont libres.";
        } else if (compte == 1) {
            sortie = sortie + "il y a une case occupée.";
        } else {
            sortie = sortie + "il y a " + compte + " cases occupées.";
        }

        return sortie;
    }

    private String premiersCar(String s, int nbCar) {
        String sExtended = String.format("%-" + nbCar + "s", s);
        return sExtended.substring(0, nbCar);
    }
}
