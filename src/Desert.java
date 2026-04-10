import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Desert {
    private final List<Zone> zones;
    private final List<Joueur> joueurs = new ArrayList<>();
    private int joueurActuel;

    // HELICE, BOITE_VITESSES, CRISTAL_ENERGIE, SYSTEME_NAVIGATION
    private final boolean[] piecesRecuperees = new boolean[4];

    private final int niveau;
    private double niveauTempete;
    private boolean partiePerdue;
    private boolean partieGagnee;

    private final Random random = new Random();

    public Desert(int niveau) {
        this.niveau = niveau;
        this.niveauTempete = Config.NIVEAU_TEMPETE_INITIAL;
        this.partiePerdue = false;
        this.partieGagnee = false;
        this.zones = new ArrayList<>();

        initialiserPlateau();
        initialiserSable();
        initialiserJoueurs();
    }

    private void initialiserPlateau() {
        zones.clear();

        for (int i = 0; i < Config.PLATEAU.length; i++) {
            for (int j = 0; j < Config.PLATEAU[i].length; j++) {
                char type = Config.PLATEAU[i][j];
                Zone zone = new Zone();

                switch (type) {
                    case 'o' -> zone.setTypeSpecial(ZoneType.OEIL_TEMPETE);
                    case 'c' -> zone.setTypeSpecial(ZoneType.CRASH);
                    case 'p' -> zone.setTypeSpecial(ZoneType.PISTE_DECOLLAGE);
                    case 'a' -> zone.setTypeSpecial(ZoneType.OASIS);
                    case 'u' -> zone.setTypeSpecial(ZoneType.TUNNEL);

                    case 'f' -> zone.setTypeSpecial(ZoneType.CAVE_OF_EMBERS);      // Hélice
                    case 'v' -> zone.setTypeSpecial(ZoneType.HOWLING_GARDEN);      // Boîte de vitesses
                    case 'e' -> zone.setTypeSpecial(ZoneType.TEMPLE_OF_THE_SUN);   // Cristal d'énergie
                    case 'w' -> zone.setTypeSpecial(ZoneType.TIDAL_PALACE);        // Système de navigation

                    default -> zone.setTypeSpecial(ZoneType.NORMAL);
                }

                zones.add(zone);
            }
        }
    }

    private void initialiserSable() {
        for (int[] pos : Config.SABLE_INITIAL) {
            int index = pos[0] * Config.GRID_COLUMNS + pos[1];
            if (index >= 0 && index < zones.size()) {
                zones.get(index).ajouterSable();
            }
        }
    }

    private void initialiserJoueurs() {
        joueurs.clear();

        List<Role> roles = List.of(
                Role.ARCHEOLOGUE,
                Role.ALPINISTE,
                Role.EXPLORATEUR,
                Role.PORTEUSE_EAU
        );

        Zone zoneCrash = zones.stream()
                .filter(z -> z.getTypeSpecial() == ZoneType.CRASH)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Zone crash introuvable"));

        for (int i = 0; i < Config.NOMBRE_JOUEURS; i++) {
            joueurs.add(new Joueur(zoneCrash, roles.get(i)));
        }
    }

    public void distribuerCarte() {
        Joueur joueur = getJoueurActuel();
        int nbCartes = (niveau == 4) ? 1 : 2;

        for (int i = 0; i < nbCartes; i++) {
            int chance = random.nextInt(100);

            if (chance < 70) {
                PieceType[] types = PieceType.values();
                PieceType pieceTiree = types[random.nextInt(types.length)];
                joueur.ajouterPiece(pieceTiree);
            } else {
                ActionSpeciale[] actions = ActionSpeciale.values();
                ActionSpeciale actionTiree = actions[random.nextInt(actions.length)];
                joueur.ajouterActionSpeciale(actionTiree);
            }
        }
    }

    public void actionTempeteFinTour() {
        int nbActionsTempete = (int) niveauTempete;

        for (int i = 0; i < nbActionsTempete; i++) {
            int tirage = random.nextInt(100);

            if (tirage < 60) {
                ventSouffle();
            } else if (tirage < 85) {
                vagueChaleur();
            } else {
                tempeteAugmente();
            }

            verifierDefaite();
            if (partiePerdue) {
                return;
            }
        }
    }

    private void ventSouffle() {
        int force = random.nextInt(3) + 1;

        List<Zone> zonesValides = new ArrayList<>(zones);
        Collections.shuffle(zonesValides);

        int compte = 0;
        for (Zone zone : zonesValides) {
            if (zone.getTypeSpecial() != ZoneType.OEIL_TEMPETE) {
                zone.ajouterSable();
                compte++;
            }
            if (compte >= force) {
                break;
            }
        }

        System.out.println("🌪️ Le vent souffle : +" + force + " sable");
    }

    private void vagueChaleur() {
        for (Joueur joueur : joueurs) {
            Zone position = joueur.getPosition();

            boolean protege = position.getTypeSpecial() == ZoneType.TUNNEL;
            if (!protege) {
                joueur.perdreEau(1);
            }
        }

        System.out.println("🔥 Vague de chaleur");
    }

    private void tempeteAugmente() {
        niveauTempete += 0.5;
        System.out.println("⚠️ La tempête se déchaîne : niveau = " + niveauTempete);
    }

    public void recupererPiece(Zone zone) {
        if (!zone.isPieceZone()) {
            return;
        }

        PieceType type = zone.getPieceType();
        switch (type) {
            case HELICE -> piecesRecuperees[0] = true;
            case BOITE_VITESSES -> piecesRecuperees[1] = true;
            case CRISTAL_ENERGIE -> piecesRecuperees[2] = true;
            case SYSTEME_NAVIGATION -> piecesRecuperees[3] = true;
        }
    }

    public boolean toutesPiecesRecuperees() {
        for (boolean b : piecesRecuperees) {
            if (!b) return false;
        }
        return true;
    }

    public Zone getPisteDecollage() {
        return zones.stream()
                .filter(z -> z.getTypeSpecial() == ZoneType.PISTE_DECOLLAGE)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Piste de décollage introuvable"));
    }

    public Zone getCrash() {
        return zones.stream()
                .filter(z -> z.getTypeSpecial() == ZoneType.CRASH)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Crash introuvable"));
    }

    public List<Zone> getZonesAdjacentes(Zone zone) {
        List<Zone> adjacentes = new ArrayList<>();
        int index = zones.indexOf(zone);
        int colonnes = Config.GRID_COLUMNS;

        if (index == -1) return adjacentes;

        if (index >= colonnes) adjacentes.add(zones.get(index - colonnes));
        if (index < zones.size() - colonnes) adjacentes.add(zones.get(index + colonnes));
        if (index % colonnes != 0) adjacentes.add(zones.get(index - 1));
        if ((index + 1) % colonnes != 0) adjacentes.add(zones.get(index + 1));

        return adjacentes;
    }

    public int getTotalSable() {
        int total = 0;
        for (Zone zone : zones) {
            total += zone.getSable();
        }
        return total;
    }

    public void verifierDefaite() {
        if (getTotalSable() > Config.SABLE_MAX) {
            partiePerdue = true;
            System.out.println(Config.MES_TROP_SABLE);
            return;
        }

        if (niveauTempete >= Config.NIVEAU_TEMPETE_MAX) {
            partiePerdue = true;
            System.out.println(Config.MES_TEMPETE_MAX);
            return;
        }

        for (Joueur joueur : joueurs) {
            if (joueur.getEau() <= 0) {
                partiePerdue = true;
                System.out.println(Config.MES_MORT_SOIF);
                return;
            }
        }
    }

    public void verifierVictoire() {
        if (!toutesPiecesRecuperees()) {
            partieGagnee = false;
            return;
        }

        Zone piste = getPisteDecollage();
        if (piste.estBloquee()) {
            partieGagnee = false;
            return;
        }

        boolean tousSurPiste = true;
        for (Joueur joueur : joueurs) {
            if (joueur.getPosition() != piste) {
                tousSurPiste = false;
                break;
            }
        }

        partieGagnee = tousSurPiste;
        if (partieGagnee) {
            System.out.println(Config.MES_VICTOIRE);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < zones.size(); i++) {
            sb.append("Zone ").append(i).append(" : ").append(zones.get(i)).append("\n");
        }
        return sb.toString();
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    public Joueur getJoueurActuel() {
        return joueurs.get(joueurActuel);
    }

    public void nextJoueur() {
        joueurActuel = (joueurActuel + 1) % joueurs.size();
    }

    public List<Zone> getZones() {
        return zones;
    }

    public boolean[] getPiecesRecuperees() {
        return piecesRecuperees;
    }

    public double getNiveauTempete() {
        return niveauTempete;
    }

    public boolean isPartiePerdue() {
        return partiePerdue;
    }

    public boolean isPartieGagnee() {
        return partieGagnee;
    }
}