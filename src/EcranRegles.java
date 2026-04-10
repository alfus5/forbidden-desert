import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EcranRegles extends JFrame {
    private AudioPlayer audioPlayer = new AudioPlayer();
    public EcranRegles() {
        setTitle("Règles du jeu");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        audioPlayer.playMusicOnce("src/ressources/son/voix_off_regle.wav");

        JTextArea texte = new JTextArea();
        texte.setEditable(false);
        texte.setLineWrap(true);
        texte.setWrapStyleWord(true);
        texte.setFont(new Font("Arial", Font.PLAIN, 16));
        texte.setText(
                        """
                                🏜️ Objectif du jeu :
                                Récupérez les 4 pièces de la machine volante et échappez-vous en hélicoptère avant que la tempête ne vous ensevelisse !
                                
                                ⚙️ Déroulement d'un tour :
                                - Effectuez jusqu'à 4 actions :
                                  (se déplacer, enlever du sable, révéler une tuile, récupérer une pièce).
                                - Piochez des cartes Tempête.
                                - La tempête déplace les tuiles et ajoute du sable sur le plateau.
                                
                                🌪️ Le désert :
                                - Le sable s'accumule sur les cases.
                                - À 2 marqueurs de sable → la case est bloquée.
                                - Certaines zones spéciales existent :
                                  Point d’eau 💧, Tunnel 🕳, Crash 🚁, Piste de décollage 🛫
                                
                                🎭 Rôles spéciaux :
                                - Archéologue : enlève 2 marqueurs de sable sur une même tuile pour 1 action.
                                - Alpiniste : peut se déplacer sur les cases bloquées et emmener un joueur avec elle.
                                - Explorateur : peut se déplacer et agir en diagonale.
                                - Météorologue : peut contrôler les cartes Tempête.
                                - Navigatrice : peut déplacer les autres joueurs.
                                - Porteuse d’eau : récupère et donne de l’eau plus facilement.
                                
                                🔧 Pièces de la machine :
                                - Les pièces sont cachées dans le désert.
                                - Il faut révéler 2 indices pour localiser chaque pièce.
                                - Une fois localisée, la pièce apparaît sur une case précise et peut être récupérée.
                                
                                🚨 Conditions de défaite :
                                - Trop de sable (plus de marqueurs disponibles).
                                - La tempête devient incontrôlable.
                                - Un joueur meurt de soif.
                                
                                🏆 Victoire :
                                - Toutes les pièces récupérées.
                                - Tous les joueurs sur la piste de décollage.
                                - Utilisation de l’hélicoptère pour s’échapper.
                                
                                Bonne chance dans le désert !
                        """
                );
        texte.setBackground(new Color(255, 248, 220)); // couleur sable
        texte.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(texte);
        add(scrollPane);
    }
}
