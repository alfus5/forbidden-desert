import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DesertController {
    private final Desert model;
    private DesertView view;
    private final int niveau;

    public DesertController(Desert model, DesertView view, int niveau) {
        this.model = model;
        this.niveau = niveau;
        this.view = view;
        if (view != null) {
            setListeners();
        }
    }

    public void setView(DesertView view) {
        this.view = view;
        setListeners();
    }

    private void setListeners() {
        view.getButton_fin_de_tour().addActionListener(_ -> {
            Joueur joueurActuel = model.getJoueurActuel();

            if (joueurActuel.getNbActions() > 0) {
                int choix = JOptionPane.showConfirmDialog(
                        view,
                        Config.MES_CONF_FIN_DE_TOUR,
                        "Fin de tour",
                        JOptionPane.YES_NO_OPTION
                );
                if (choix != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            joueurActuel.resetActionsRestantes();
            model.distribuerCarte();
            model.actionTempeteFinTour();
            model.verifierDefaite();

            if (!model.isPartiePerdue()) {
                model.nextJoueur();
            }

            rafraichirTout();
            testConditions();
        });

        view.getButton_actions_speciales().addActionListener(_ -> actionsSpeciales());
        view.getButton_inventaire().addActionListener(_ -> showInventaire());
        view.getButton_echanger_cle().addActionListener(_ -> echangerPiece());

        List<JLabel> zoneLabels = view.getZoneLabels();
        List<Zone> zones = model.getZones();

        for (int i = 0; i < zoneLabels.size(); i++) {
            JLabel label = zoneLabels.get(i);
            Zone zone = zones.get(i);

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        deplacerJoueur(zone);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        creuserZone(zone);
                    }
                }
            });
        }
    }

    public void assignerRoles() {
        List<Role> roles = new ArrayList<>(List.of(
                Role.ARCHEOLOGUE,
                Role.ALPINISTE,
                Role.EXPLORATEUR,
                Role.PORTEUSE_EAU,
                Role.NAVIGATRICE,
                Role.METEOROLOGUE
        ));
        Collections.shuffle(roles);

        for (int i = 0; i < model.getJoueurs().size() && i < roles.size(); i++) {
            model.getJoueurs().get(i).setRole(roles.get(i));
        }
    }

    public void afficherRoles() {
        StringBuilder message = new StringBuilder("Attribution des rôles :\n");
        for (int i = 0; i < model.getJoueurs().size(); i++) {
            Joueur joueur = model.getJoueurs().get(i);
            message.append("Joueur ")
                    .append(i + 1)
                    .append(" : ")
                    .append(joueur.getRole().getNom())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(view, message.toString(), "Rôles des joueurs", JOptionPane.INFORMATION_MESSAGE);
    }

    public void actionsSpeciales() {
        Joueur joueurActuel = model.getJoueurActuel();

        List<String> optionsList = new ArrayList<>();
        optionsList.add("Récupérer pièce");

        if (joueurActuel.getRole() == Role.NAVIGATRICE) {
            optionsList.add("Déplacer un autre joueur");
        }

        if (joueurActuel.getActionsSpeciales().contains(ActionSpeciale.JETPACK)) {
            optionsList.add("Jetpack");
        }
        if (joueurActuel.getActionsSpeciales().contains(ActionSpeciale.BLASTER)) {
            optionsList.add("Blaster");
        }
        if (joueurActuel.getActionsSpeciales().contains(ActionSpeciale.BOUCLIER_SOLAIRE)) {
            optionsList.add("Bouclier solaire");
        }
        if (joueurActuel.getActionsSpeciales().contains(ActionSpeciale.DETECTEUR)) {
            optionsList.add("Détecteur");
        }

        optionsList.add("Annuler");
        Object[] options = optionsList.toArray();

        int choix = JOptionPane.showOptionDialog(
                view,
                "Choisissez une action spéciale :",
                "Actions spéciales",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[options.length - 1]
        );

        if (choix < 0 || choix >= optionsList.size()) {
            return;
        }

        String actionChoisie = optionsList.get(choix);

        switch (actionChoisie) {
            case "Récupérer pièce" -> recupererPiece();
            case "Déplacer un autre joueur" -> actionNavigatrice();
            case "Jetpack" -> {
                utiliserJetpack();
                joueurActuel.utiliserActionSpeciale(ActionSpeciale.JETPACK);
            }
            case "Blaster" -> {
                utiliserBlaster();
                joueurActuel.utiliserActionSpeciale(ActionSpeciale.BLASTER);
            }
            case "Bouclier solaire" -> JOptionPane.showMessageDialog(
                    view,
                    "Bouclier solaire activé (effet simplifié)."
            );
            case "Détecteur" -> utiliserDetecteur();
            default -> {
            }
        }
    }

    private void utiliserJetpack() {
        List<Zone> zonesValides = model.getZones().stream()
                .filter(zone -> !zone.estBloquee())
                .toList();

        Object[] options = new Object[zonesValides.size() + 1];
        for (int i = 0; i < zonesValides.size(); i++) {
            options[i] = zonesValides.get(i).getNom(Config.LANG);
        }
        options[zonesValides.size()] = Config.MES_RETOUR;

        int choix = JOptionPane.showOptionDialog(
                view,
                "Où voulez-vous vous déplacer avec le Jetpack ?",
                "Jetpack",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[zonesValides.size()]
        );

        if (choix >= 0 && choix < zonesValides.size()) {
            model.getJoueurActuel().setPosition(zonesValides.get(choix));
            rafraichirTout();
        }
    }

    private void utiliserBlaster() {
        Joueur joueur = model.getJoueurActuel();
        Zone zone = joueur.getPosition();

        while (zone.getSable() > 0) {
            zone.enleverSable();
        }

        int index = model.getZones().indexOf(zone);
        view.updateViewZone(zone, index, model);
        view.updateViewLabel(joueur, model.getJoueurs(), model);
    }

    private void utiliserDetecteur() {
        List<Zone> zones = model.getZones();
        Object[] options = new Object[zones.size() + 1];

        for (int i = 0; i < zones.size(); i++) {
            options[i] = "Zone " + i;
        }
        options[zones.size()] = Config.MES_RETOUR;

        int choix = JOptionPane.showOptionDialog(
                view,
                "Quelle zone voulez-vous inspecter ?",
                "Détecteur",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[zones.size()]
        );

        if (choix >= 0 && choix < zones.size()) {
            Zone zone = zones.get(choix);
            JOptionPane.showMessageDialog(
                    view,
                    "Zone : " + zone.getNom(Config.LANG) +
                            "\nSable : " + zone.getSable() +
                            "\nPièce : " + zone.getPieceType(),
                    "Détecteur",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void actionNavigatrice() {
        Joueur navigatrice = model.getJoueurActuel();

        List<Joueur> autresJoueurs = model.getJoueurs().stream()
                .filter(j -> j != navigatrice)
                .toList();

        Object[] options = new Object[autresJoueurs.size() + 1];
        for (int i = 0; i < autresJoueurs.size(); i++) {
            Joueur j = autresJoueurs.get(i);
            options[i] = "Joueur " + (model.getJoueurs().indexOf(j) + 1) + " (" + j.getRole().getNom() + ")";
        }
        options[autresJoueurs.size()] = Config.MES_RETOUR;

        int choix = JOptionPane.showOptionDialog(
                view,
                "Quel joueur déplacer ?",
                "Navigatrice",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[autresJoueurs.size()]
        );

        if (choix >= 0 && choix < autresJoueurs.size()) {
            Joueur joueurADeplacer = autresJoueurs.get(choix);
            choisirZoneDeplacementNavigatrice(navigatrice, joueurADeplacer);
        }
    }

    private void choisirZoneDeplacementNavigatrice(Joueur navigatrice, Joueur joueurADeplacer) {
        List<Zone> zonesPossibles = model.getZonesAdjacentes(joueurADeplacer.getPosition()).stream()
                .filter(zone -> !zone.estBloquee())
                .toList();

        Object[] options = new Object[zonesPossibles.size() + 1];
        for (int i = 0; i < zonesPossibles.size(); i++) {
            options[i] = zonesPossibles.get(i).getNom(Config.LANG);
        }
        options[zonesPossibles.size()] = Config.MES_RETOUR;

        int choix = JOptionPane.showOptionDialog(
                view,
                "Où déplacer le joueur ?",
                "Navigatrice",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[zonesPossibles.size()]
        );

        if (choix >= 0 && choix < zonesPossibles.size()) {
            joueurADeplacer.setPosition(zonesPossibles.get(choix));
            navigatrice.decrementNbActions();
            rafraichirTout();
        }
    }

public void deplacerJoueur(Zone zone) {
    Joueur joueur = model.getJoueurActuel();

    if (joueur.getNbActions() <= 0) {
        JOptionPane.showMessageDialog(view, Config.MES_ACTION_NUL);
        return;
    }

    if (zone.estBloquee() && joueur.getRole() != Role.ALPINISTE) {
        JOptionPane.showMessageDialog(view, Config.MES_CASE_BLOQUEE);
        return;
    }

    boolean deplacementAutorise = false;

    // Explorateur (diagonale)
    if (joueur.getRole() == Role.EXPLORATEUR) {
        deplacementAutorise =
                joueur.estAdjacent(zone, model.getZones(), Config.GRID_COLUMNS)
                        || joueur.estDiagonal(zone, model.getZones(), Config.GRID_COLUMNS);
    }
    // Alpiniste
    else if (joueur.getRole() == Role.ALPINISTE) {
        deplacementAutorise =
                joueur.estAdjacent(zone, model.getZones(), Config.GRID_COLUMNS);
    }
    // Cas normal
    else {
        deplacementAutorise =
                joueur.estAdjacent(zone, model.getZones(), Config.GRID_COLUMNS)
                        && !zone.estBloquee();
    }

    if (!deplacementAutorise) {
        JOptionPane.showMessageDialog(view, Config.MES_DEPLACEMENT_IMPOSSIBLE);
        return;
    }

    // DÉPLACEMENT
    joueur.setPosition(zone);
    joueur.decrementNbActions();

    // RÉVÉLATION
    if (!zone.isRevelee()) {
        zone.setRevelee(true);

        if (zone.isPieceZone()) {
            JOptionPane.showMessageDialog(view,
                    "Indice découvert pour la pièce : " + zone.getPieceType().getNom());
        } else {
            JOptionPane.showMessageDialog(view,
                    "Zone révélée !");
        }
    }


    rafraichirTout();
    testConditions();
}

    public void creuserZone(Zone zone) {
        Joueur joueur = model.getJoueurActuel();

        if (joueur.getNbActions() <= 0) {
            JOptionPane.showMessageDialog(view, Config.MES_ACTION_NUL);
            return;
        }

        boolean autorise;
        if (joueur.getRole() == Role.EXPLORATEUR) {
            autorise = joueur.estAdjacent(zone, model.getZones(), Config.GRID_COLUMNS)
                    || joueur.estDiagonal(zone, model.getZones(), Config.GRID_COLUMNS)
                    || zone == joueur.getPosition();
        } else {
            autorise = joueur.estAdjacent(zone, model.getZones(), Config.GRID_COLUMNS)
                    || zone == joueur.getPosition();
        }

        if (!autorise || zone.getSable() <= 0) {
            JOptionPane.showMessageDialog(view, Config.MES_CREUSER_IMPOSSIBLE);
            return;
        }

        if (joueur.getRole() == Role.ARCHEOLOGUE && zone.getSable() >= 2) {
            zone.enleverSable();
            zone.enleverSable();
        } else {
            zone.enleverSable();
        }

        joueur.decrementNbActions();

        int index = model.getZones().indexOf(zone);
        view.updateViewZone(zone, index, model);
        view.updateViewLabel(joueur, model.getJoueurs(), model);
        testConditions();
    }

    public void recupererPiece() {
        Joueur joueur = model.getJoueurActuel();
        Zone zone = joueur.getPosition();

        if (joueur.getNbActions() <= 0) {
            JOptionPane.showMessageDialog(view, Config.MES_ACTION_NUL);
            return;
        }

        if (!zone.isPieceZone()) {
            JOptionPane.showMessageDialog(view, "Pas de pièce ici !");
            return;
        }

        if (zone.estBloquee()) {
            JOptionPane.showMessageDialog(view, "La zone est bloquée !");
            return;
        }

        PieceType pieceType = zone.getPieceType();
        boolean[] piecesRecuperees = model.getPiecesRecuperees();
        int pieceIndex = pieceType.ordinal();

        if (pieceIndex < 0 || pieceIndex >= piecesRecuperees.length) {
            return;
        }

        if (piecesRecuperees[pieceIndex]) {
            JOptionPane.showMessageDialog(view, "Cette pièce a déjà été récupérée.");
            return;
        }

        if (!joueur.hasPieces(pieceType)) {
            JOptionPane.showMessageDialog(view, "Vous n'avez pas 4 cartes de cette pièce.");
            return;
        }

        piecesRecuperees[pieceIndex] = true;
        joueur.ajouterPieceRecuperee(pieceType.getNom());
        joueur.removePieces(pieceType);
        joueur.decrementNbActions();

        JOptionPane.showMessageDialog(
                view,
                "Vous avez récupéré la pièce " + pieceType.getNom() + " !",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE
        );

        view.updatePieces(model.getPiecesRecuperees());
        view.updateViewLabel(joueur, model.getJoueurs(), model);
        testConditions();
    }

    public void showInventaire() {
        StringBuilder inventaire = new StringBuilder("Inventaire des joueurs :\n\n");

        for (int i = 0; i < model.getJoueurs().size(); i++) {
            Joueur joueur = model.getJoueurs().get(i);
            inventaire.append("Joueur ").append(i + 1).append(" :\n");

            inventaire.append("  Eau : ").append(joueur.getEau()).append("\n");

            if (!joueur.getPieces().isEmpty()) {
                inventaire.append("  Cartes pièce : ").append(joueur.getPieces()).append("\n");
            } else {
                inventaire.append("  Cartes pièce : -\n");
            }

            if (!joueur.getActionsSpeciales().isEmpty()) {
                inventaire.append("  Cartes spéciales : ");
                for (ActionSpeciale action : joueur.getActionsSpeciales()) {
                    inventaire.append(action.getNom()).append(", ");
                }
                inventaire.setLength(inventaire.length() - 2);
                inventaire.append("\n");
            } else {
                inventaire.append("  Cartes spéciales : -\n");
            }

            if (!joueur.getPiecesRecuperees().isEmpty()) {
                inventaire.append("  Pièces récupérées : ").append(joueur.getPiecesRecuperees()).append("\n");
            } else {
                inventaire.append("  Pièces récupérées : -\n");
            }

            inventaire.append("\n");
        }

        JOptionPane.showMessageDialog(view, inventaire.toString(), "Inventaire", JOptionPane.INFORMATION_MESSAGE);
    }

    public void echangerPiece() {
        Joueur joueurActuel = model.getJoueurActuel();

        if (joueurActuel.getNbActions() <= 0) {
            JOptionPane.showMessageDialog(view, Config.MES_ACTION_NUL);
            return;
        }

        if (joueurActuel.getPieces().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vous n'avez aucune carte pièce à échanger.");
            return;
        }

        List<Joueur> joueursReceveurs = model.getJoueurs().stream()
                .filter(j -> j != joueurActuel && j.getPosition() == joueurActuel.getPosition())
                .toList();

        if (joueursReceveurs.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Aucun joueur disponible pour l'échange.");
            return;
        }

        demanderChoixJoueur(joueurActuel, joueursReceveurs);
    }

    private void demanderChoixJoueur(Joueur joueurActuel, List<Joueur> joueursReceveurs) {
        Object[] options = new Object[joueursReceveurs.size() + 1];
        for (int i = 0; i < joueursReceveurs.size(); i++) {
            options[i] = "Joueur " + (model.getJoueurs().indexOf(joueursReceveurs.get(i)) + 1);
        }
        options[joueursReceveurs.size()] = "Abandonner";

        int choix = JOptionPane.showOptionDialog(
                view,
                "À quel joueur voulez-vous donner une carte pièce ?",
                "Échanger une carte pièce",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[joueursReceveurs.size()]
        );

        if (choix >= 0 && choix < joueursReceveurs.size()) {
            demanderChoixPiece(joueurActuel, joueursReceveurs.get(choix));
        }
    }

    private void demanderChoixPiece(Joueur joueurActuel, Joueur joueurReceveur) {
        Object[] options = new Object[joueurActuel.getPieces().size() + 1];
        for (int i = 0; i < joueurActuel.getPieces().size(); i++) {
            options[i] = joueurActuel.getPieces().get(i).getNom();
        }
        options[joueurActuel.getPieces().size()] = "Abandonner";

        int choix = JOptionPane.showOptionDialog(
                view,
                "Quelle carte pièce voulez-vous donner ?",
                "Échanger une carte pièce",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[joueurActuel.getPieces().size()]
        );

        if (choix >= 0 && choix < joueurActuel.getPieces().size()) {
            PieceType pieceDonnee = joueurActuel.getPieces().get(choix);
            joueurReceveur.ajouterPiece(pieceDonnee);
            joueurActuel.getPieces().remove(pieceDonnee);
            joueurActuel.decrementNbActions();
            view.updateViewLabel(joueurActuel, model.getJoueurs(), model);
        }
    }

    public void testConditions() {
        model.verifierDefaite();
        model.verifierVictoire();

        if (model.isPartieGagnee()) {
            JOptionPane.showMessageDialog(view, Config.MES_VICTOIRE, "Victoire", JOptionPane.INFORMATION_MESSAGE);
            stopGame();
            return;
        }

        if (model.isPartiePerdue()) {
            JOptionPane.showMessageDialog(view, "Partie perdue !", "Défaite", JOptionPane.ERROR_MESSAGE);
            stopGame();
        }
    }

    private void stopGame() {
        view.getButton_inventaire().setEnabled(false);
        view.getButton_fin_de_tour().setEnabled(false);
        view.getButton_recuperer_artefact().setEnabled(false);
    }

    private void rafraichirTout() {
        for (int i = 0; i < model.getZones().size(); i++) {
            view.updateViewZone(model.getZones().get(i), i, model);
        }
        view.updateViewLabel(model.getJoueurActuel(), model.getJoueurs(), model);
        view.updateViewJoueur(model.getJoueurs(), model.getZones(), model);
        view.updatePieces(model.getPiecesRecuperees());
    }
}