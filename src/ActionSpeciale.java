public enum ActionSpeciale {
    JETPACK,
    BOUCLIER_SOLAIRE,
    BLASTER,
    DETECTEUR;

    public String getNom() {
        return switch (this) {
            case JETPACK -> "Jetpack";
            case BOUCLIER_SOLAIRE -> "Bouclier solaire";
            case BLASTER -> "Blaster";
            case DETECTEUR -> "Détecteur";
        };
    }
}
