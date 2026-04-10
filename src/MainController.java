import javax.swing.JOptionPane;

public class MainController {
    private EcranTitre ecranTitre;
    private final AudioPlayer audioPlayer = new AudioPlayer();

    public void lancerApplication() {
        ecranTitre = new EcranTitre(this);
        ecranTitre.setVisible(true);

        audioPlayer.playMusicLoop("src/ressources/son/musique_de_fond.wav");
        audioPlayer.playMusicOnce("src/ressources/son/voix_off.wav");
    }

    public void lancerPartie() {
        int niveau = ecranTitre.getNiveauSelectionne();

        ecranTitre.dispose();

        afficherRegles(niveau);

        Desert desert = new Desert(niveau);

        DesertController desertController = new DesertController(desert, null, niveau);
        DesertView desertView = new DesertView(desert);
        desertController.setView(desertView);

        desertController.assignerRoles();
        desertController.afficherRoles();

        desertView.setVisible(true);
        desertView.updateView(desert);
    }

    public void afficherRegles(int niveau) {
        String message = switch (niveau) {
            case 1 -> """
                    Niveau 1 - Novice :
                    - 4 actions par tour.
                    - Les cases avec 2 sables ou plus sont bloquées.
                    - La tempête est modérée.
                    - Les joueurs doivent récupérer les 4 pièces de la machine et rejoindre la piste de décollage.
                    """;
            case 2 -> """
                    Niveau 2 - Normal :
                    - Même règles que le niveau 1.
                    - La tempête devient plus dangereuse.
                    - Les joueurs perdent plus vite le contrôle du désert.
                    """;
            case 3 -> """
                    Niveau 3 - Elite :
                    - Même règles que le niveau 2.
                    - La gestion des cartes spéciales devient importante.
                    - La moindre erreur peut bloquer l'équipe.
                    """;
            case 4 -> """
                    Niveau 4 - Légendaire :
                    - Tempête très agressive.
                    - Les cases se bloquent rapidement sous le sable.
                    - La survie dépend fortement de la coopération et des rôles.
                    """;
            default -> "Niveau inconnu.";
        };

        JOptionPane.showMessageDialog(
                null,
                message,
                "Règles du niveau " + niveau,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void afficherInformations() {
        JOptionPane.showMessageDialog(
                null,
                """
                Projet PO&GL 2025
                Jeu : Le Désert Interdit

                Développeurs : EL KADDARI IKRAM et SIDIBE ALPGA
                """,
                "Informations",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
