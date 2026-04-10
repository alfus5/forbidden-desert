import javax.swing.*;
import java.awt.*;

public class EcranTitre extends JFrame {
    private final MainController controller;
    private int niveauSelectionne = 1; // niveau par défaut

    public EcranTitre(MainController controller) {
        this.controller = controller;

        setTitle("Le Désert Interdit");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 222, 179)); // couleur sable

        JLabel titre = new JLabel("Le Désert Interdit", SwingConstants.CENTER);
        titre.setFont(new Font("Serif", Font.BOLD, 36));
        titre.setForeground(new Color(120, 70, 15));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sousTitre = new JLabel("Survivez à la tempête, récupérez les artefacts, fuyez le désert !", SwingConstants.CENTER);
        sousTitre.setFont(new Font("Arial", Font.PLAIN, 16));
        sousTitre.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton boutonLancer = new JButton("Commencer une partie");
        boutonLancer.setFont(new Font("Arial", Font.PLAIN, 18));
        boutonLancer.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonLancer.addActionListener(e -> choisirNiveau());

        JButton boutonInfos = new JButton("Informations");
        boutonInfos.setFont(new Font("Arial", Font.PLAIN, 18));
        boutonInfos.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonInfos.addActionListener(e -> controller.afficherInformations());

        JButton boutonRegles = new JButton("Règles");
        boutonRegles.setFont(new Font("Arial", Font.PLAIN, 18));
        boutonRegles.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonRegles.addActionListener(e -> ouvrirEcranRegles());

        panel.add(Box.createVerticalStrut(50));
        panel.add(titre);
        panel.add(Box.createVerticalStrut(15));
        panel.add(sousTitre);
        panel.add(Box.createVerticalStrut(40));
        panel.add(boutonLancer);
        panel.add(Box.createVerticalStrut(20));
        panel.add(boutonInfos);
        panel.add(Box.createVerticalStrut(20));
        panel.add(boutonRegles);

        add(panel);
    }

    private void choisirNiveau() {
        Object[] options = {"Novice", "Normal", "Elite", "Légendaire"};

        int choix = JOptionPane.showOptionDialog(
                this,
                "Choisissez votre niveau de difficulté :",
                "Sélection du niveau",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choix >= 0 && choix <= 3) {
            niveauSelectionne = choix + 1;
            controller.lancerPartie();
        }
    }

    private void ouvrirEcranRegles() {
        EcranRegles ecranRegles = new EcranRegles();
        ecranRegles.setVisible(true);
    }

    public int getNiveauSelectionne() {
        return niveauSelectionne;
    }
}