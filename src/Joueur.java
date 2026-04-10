import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private Zone position;
    private int actionsRestantes;
    private final List<PieceType> pieces;
    private final List<String> piecesRecuperees;
    private final List<ActionSpeciale> actionsSpeciales;
    private Role role;
    private int eau;

    public Joueur(Zone position, Role role) {
        this.position = position;
        this.actionsRestantes = Config.NOMBRE_ACTIONS_PAR_JOUEUR;
        this.pieces = new ArrayList<>();
        this.piecesRecuperees = new ArrayList<>();
        this.actionsSpeciales = new ArrayList<>();
        this.role = role;
        this.eau = (role == Role.PORTEUSE_EAU) ? 5 : Config.EAU_MAX;
    }

    // Position
    public Zone getPosition() {
        return position;
    }

    public void setPosition(Zone position) {
        this.position = position;
    }

    // Actions
    public int getNbActions() {
        return actionsRestantes;
    }

    public void decrementNbActions() {
        if (actionsRestantes > 0) {
            actionsRestantes--;
        }
    }

    public void resetActionsRestantes() {
        this.actionsRestantes = Config.NOMBRE_ACTIONS_PAR_JOUEUR;
    }

    // Eau
    public int getEau() {
        return eau;
    }

    public void perdreEau(int quantite) {
        eau -= quantite;
        if (eau < 0) {
            eau = 0;
        }
    }

    public void gagnerEau(int quantite) {
        int max = (role == Role.PORTEUSE_EAU) ? 5 : Config.EAU_MAX;
        eau += quantite;
        if (eau > max) {
            eau = max;
        }
    }

    // Cartes pièce
    public List<PieceType> getPieces() {
        return pieces;
    }

    public void ajouterPiece(PieceType piece) {
        pieces.add(piece);
    }

    // Pièces déjà récupérées sur le plateau
    public List<String> getPiecesRecuperees() {
        return piecesRecuperees;
    }

    public void ajouterPieceRecuperee(String piece) {
        piecesRecuperees.add(piece);
    }

    // Rôle
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
        if (role == Role.PORTEUSE_EAU && eau < 5) {
            eau = 5;
        }
    }

    // Cartes spéciales
    public List<ActionSpeciale> getActionsSpeciales() {
        return actionsSpeciales;
    }

    public void ajouterActionSpeciale(ActionSpeciale action) {
        actionsSpeciales.add(action);
    }

    public void utiliserActionSpeciale(ActionSpeciale action) {
        actionsSpeciales.remove(action);
    }

    // Adjacence
    public boolean estAdjacent(Zone zone, List<Zone> zones, int colonnes) {
        int indexActuel = zones.indexOf(position);
        int indexCible = zones.indexOf(zone);

        if (indexActuel == -1 || indexCible == -1) return false;

        int ligneActuelle = indexActuel / colonnes;
        int colonneActuelle = indexActuel % colonnes;

        int ligneCible = indexCible / colonnes;
        int colonneCible = indexCible % colonnes;

        return Math.abs(ligneActuelle - ligneCible) + Math.abs(colonneActuelle - colonneCible) == 1;
    }

    public boolean estDiagonal(Zone zone, List<Zone> zones, int colonnes) {
        int indexActuel = zones.indexOf(position);
        int indexCible = zones.indexOf(zone);

        if (indexActuel == -1 || indexCible == -1) return false;

        int ligneActuelle = indexActuel / colonnes;
        int colonneActuelle = indexActuel % colonnes;

        int ligneCible = indexCible / colonnes;
        int colonneCible = indexCible % colonnes;

        return Math.abs(ligneActuelle - ligneCible) == 1
                && Math.abs(colonneActuelle - colonneCible) == 1;
    }

    // Vérifie si le joueur a 4 cartes de la bonne pièce
    public boolean hasPieces(PieceType pieceType) {
        long count = pieces.stream()
                .filter(piece -> piece == pieceType)
                .count();

        return count >= 4;
    }

    // Supprime 4 cartes de la bonne pièce
    public void removePieces(PieceType pieceType) {
        int retirees = 0;
        for (int i = pieces.size() - 1; i >= 0 && retirees < 4; i--) {
            if (pieces.get(i) == pieceType) {
                pieces.remove(i);
                retirees++;
            }
        }
    }

    @Override
    public String toString() {
        return "Joueur{" +
                "role=" + role +
                ", eau=" + eau +
                ", actionsRestantes=" + actionsRestantes +
                ", position=" + position +
                '}';
    }
}