public enum PieceType {
    HELICE("Hélice"),
    BOITE_VITESSES("Boîte de vitesses"),
    CRISTAL_ENERGIE("Cristal d'énergie"),
    SYSTEME_NAVIGATION("Système de navigation");

    private final String nom;

    PieceType(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return nom;
    }
}