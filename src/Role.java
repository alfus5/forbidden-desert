public enum Role {
    ARCHEOLOGUE,
    ALPINISTE,
    EXPLORATEUR,
    NAVIGATRICE,
    METEOROLOGUE,
    PORTEUSE_EAU;

    public String getNom() {
        return switch (this) {
            case ARCHEOLOGUE -> "Archéologue";
            case ALPINISTE -> "Alpiniste";
            case EXPLORATEUR -> "Explorateur";
            case NAVIGATRICE -> "Navigatrice";
            case METEOROLOGUE -> "Météorologue";
            case PORTEUSE_EAU -> "Porteuse d'eau";
        };
    }
}