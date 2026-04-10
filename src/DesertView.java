import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class DesertView extends JFrame {
    private final JButton button_fin_de_tour;
    private final JButton button_actions_speciales;
    private final JButton button_inventaire;
    private final JButton button_echanger_cle;
    private final JPanel gridPanel;
    private final JLabel joueurActuelLabel;
    private final JLabel nbActionsLabel;

    public DesertView(Desert desert) {
        setTitle("Le Désert Interdit");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        gridPanel = new JPanel(new GridLayout(Config.GRID_ROWS, Config.GRID_COLUMNS));

        for (Zone zone : desert.getZones()) {
            JLabel zoneLabel = new JLabel("", SwingConstants.CENTER);
            zoneLabel.setOpaque(true);
            zoneLabel.setBackground(getColorForZone(zone));
            zoneLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            gridPanel.add(zoneLabel);
        }

        button_actions_speciales = new JButton("Actions spéciales");
        button_fin_de_tour = new JButton("Fin de tour");
        button_echanger_cle = new JButton("Échanger pièce");
        button_inventaire = new JButton("Inventaire");

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4));
        buttonPanel.add(button_actions_speciales);
        buttonPanel.add(button_fin_de_tour);
        buttonPanel.add(button_echanger_cle);
        buttonPanel.add(button_inventaire);

        joueurActuelLabel = new JLabel("Joueur actuel : 1", SwingConstants.LEFT);
        nbActionsLabel = new JLabel("Nb actions : " + Config.NOMBRE_ACTIONS_PAR_JOUEUR, SwingConstants.LEFT);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(joueurActuelLabel);
        infoPanel.add(nbActionsLabel);

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(infoPanel, BorderLayout.NORTH);
    }

    public List<JLabel> getZoneLabels() {
        List<JLabel> labels = new ArrayList<>();
        for (Component component : gridPanel.getComponents()) {
            if (component instanceof JLabel label) {
                labels.add(label);
            }
        }
        return labels;
    }

    public JButton getButton_actions_speciales() { return button_actions_speciales; }
    public JButton getButton_fin_de_tour() { return button_fin_de_tour; }
    public JButton getButton_inventaire() { return button_inventaire; }
    public JButton getButton_echanger_cle() { return button_echanger_cle; }

    public JButton getButton_recuperer_artefact() {
        return button_actions_speciales;
    }

    private Color getColorForZone(Zone zone) {
        if (zone.getTypeSpecial() == ZoneType.OEIL_TEMPETE) {
            return Color.decode(Config.OEIL_ZONE_COLOR);
        }
        if (zone.getTypeSpecial() == ZoneType.OASIS) {
            return Color.decode(Config.OASIS_ZONE_COLOR);
        }
        if (zone.getTypeSpecial() == ZoneType.PISTE_DECOLLAGE) {
            return Color.decode(Config.PISTE_ZONE_COLOR);
        }
        if (zone.getTypeSpecial() == ZoneType.CRASH) {
            return Color.decode(Config.CRASH_ZONE_COLOR);
        }
        if (zone.getTypeSpecial() == ZoneType.TUNNEL) {
            return Color.decode(Config.TUNNEL_ZONE_COLOR);
        }
        if (zone.isPieceZone()) {
            return Color.decode(Config.ARTEFACT_ZONE_COLOR);
        }

        if (zone.getSable() >= 2) {
            return Color.decode(Config.SABLE_2_ZONE_COLOR);
        } else if (zone.getSable() == 1) {
            return Color.decode(Config.SABLE_1_ZONE_COLOR);
        } else {
            return Color.decode(Config.NORMAL_ZONE_COLOR);
        }
    }

    public void updateView(Desert desert) {
        for (int i = 0; i < gridPanel.getComponentCount(); i++) {
            Component component = gridPanel.getComponent(i);
            if (component instanceof JLabel label) {
                Zone zone = desert.getZones().get(i);

                String imagePath = getImagePath(zone);
                ImageIcon zoneImage = null;

                if (imagePath != null) {
                    int f = 2;
                    zoneImage = ImageUtils.loadImage(imagePath, 60 * f, 75 * f);
                }

                if (zoneImage != null) {
                    if (zone.getSable() == 1) {
                        zoneImage = applyFilter(zoneImage, new Color(194, 145, 66, 80));
                    } else if (zone.getSable() >= 2) {
                        zoneImage = applyFilter(zoneImage, new Color(140, 90, 20, 130));
                    }

                    label.setIcon(zoneImage);
                } else {
                    label.setIcon(null);
                }

                label.setBackground(getColorForZone(zone));
                label.setText(construireTexteZone(zone, desert));
                label.setVerticalTextPosition(SwingConstants.CENTER);
                label.setHorizontalTextPosition(SwingConstants.CENTER);
                label.setForeground(Color.BLACK);
            }
        }

        //updatePieces(desert.getPiecesRecuperees());

        Joueur joueurActuel = desert.getJoueurActuel();
        joueurActuelLabel.setText(
                "Joueur actuel : " +
                        (desert.getJoueurs().indexOf(joueurActuel) + 1) +
                        " (" + joueurActuel.getRole().getNom() + ")"
        );
        nbActionsLabel.setText("Actions restantes : " + joueurActuel.getNbActions());

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    public void updateViewZone(Zone zone, int index, Desert desert) {
        Component component = gridPanel.getComponent(index);
        if (component instanceof JLabel label) {
            String imagePath = getImagePath(zone);
            ImageIcon zoneImage = null;

            if (imagePath != null) {
                int f = 2;
                zoneImage = ImageUtils.loadImage(imagePath, 60 * f, 75 * f);
            }

            if (zoneImage != null) {
                if (zone.getSable() == 1) {
                    zoneImage = applyFilter(zoneImage, new Color(194, 145, 66, 80));
                } else if (zone.getSable() >= 2) {
                    zoneImage = applyFilter(zoneImage, new Color(140, 90, 20, 130));
                }
                label.setIcon(zoneImage);
            } else {
                label.setIcon(null);
            }

            label.setBackground(getColorForZone(zone));
            label.setText(construireTexteZone(zone, desert));
            label.setForeground(Color.BLACK);
        }

       // updatePieces(desert.getPiecesRecuperees());
    }

    public void updateViewJoueur(List<Joueur> joueurs, List<Zone> zones, Desert desert) {
        for (int i = 0; i < gridPanel.getComponentCount(); i++) {
            Component component = gridPanel.getComponent(i);
            if (component instanceof JLabel label) {
                Zone zone = zones.get(i);
                label.setText(construireTexteZone(zone, desert));
                label.setForeground(Color.BLACK);
            }
        }

       // updatePieces(desert.getPiecesRecuperees());
    }

    public void updateViewLabel(Joueur joueurActuel, List<Joueur> joueurs, Desert desert) {
        int joueurIndex = joueurs.indexOf(joueurActuel);
        joueurActuelLabel.setText(
                "Joueur actuel : " + (joueurIndex + 1) +
                        " (" + joueurActuel.getRole().getNom() + ")"
        );
        nbActionsLabel.setText("Actions restantes : " + joueurActuel.getNbActions());
       // updatePieces(desert.getPiecesRecuperees());
    }

    private String getImagePath(Zone zone) {
        if (zone.getTypeSpecial() == null
                || zone.getTypeSpecial() == ZoneType.AUCUN
                || zone.getTypeSpecial() == ZoneType.NORMAL) {
            return null;
        }
        return "ressources/zones/img_zone_" + zone.getTypeSpecial() + ".png";
    }

    private String construireTexteZone(Zone zone, Desert desert) {
        StringBuilder texte = new StringBuilder("<html><div style='text-align:center;color:black;font-weight:bold;'>");
        if (zone.getNom(Config.LANG) != null && !zone.getNom(Config.LANG).isBlank()) {
            texte.append(zone.getNom(Config.LANG)).append("<br>");
        }
        if (zone.getIconeTexte() != null && !zone.getIconeTexte().isEmpty()) {
            texte.append(zone.getIconeTexte()).append("<br>");
        }

        if (zone.getSable() > 0) {
            texte.append("Sable: ").append(zone.getSable());
            if (zone.estBloquee()) {
                texte.append(" (bloquée)");
            }
            texte.append("<br>");
        }

        List<String> joueursSurCase = new ArrayList<>();
        for (Joueur joueur : desert.getJoueurs()) {
            if (joueur.getPosition() == zone) {
                joueursSurCase.add("J" + (desert.getJoueurs().indexOf(joueur) + 1));
            }
        }
        if (!joueursSurCase.isEmpty()) {
            texte.append("").append(String.join("", joueursSurCase)).append("<br>");
        }
           texte.append("<div><html>");
          return texte.toString();
    }
    private ImageIcon applyFilter(ImageIcon icon, Color color) {

        if (icon == null || icon.getImage() == null) {

            return null;

        }


        Image originalImage = icon.getImage();

        int width = originalImage.getWidth(null);

        int height = originalImage.getHeight(null);


        if (width <= 0 || height <= 0) {

            return icon;

        }


        BufferedImage filteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = filteredImage.createGraphics();


        g.drawImage(originalImage, 0, 0, null);

        g.setColor(color);

        g.fillRect(0, 0, width, height);


        g.dispose();

        return new ImageIcon(filteredImage);

    }

    public void updatePieces(boolean[] piecesRecuperees) {
//        int hautGauche = 0;
//        int hautDroite = Config.GRID_COLUMNS - 1;
//        int basGauche = (Config.GRID_ROWS - 1) * Config.GRID_COLUMNS;
//        int basDroite = (Config.GRID_ROWS * Config.GRID_COLUMNS) - 1;
//
//        JLabel labelHautGauche = (JLabel) gridPanel.getComponent(hautGauche);
//        labelHautGauche.setIcon(null);
//        labelHautGauche.setText(piecesRecuperees[0] ? "✔ Hélice" : "Hélice");
//
//        JLabel labelHautDroite = (JLabel) gridPanel.getComponent(hautDroite);
//        labelHautDroite.setIcon(null);
//        labelHautDroite.setText(piecesRecuperees[1] ? "✔ Boîte" : "Boîte");
//
//        JLabel labelBasGauche = (JLabel) gridPanel.getComponent(basGauche);
//        labelBasGauche.setIcon(null);
//        labelBasGauche.setText(piecesRecuperees[2] ? "✔ Cristal" : "Cristal");
//
//        JLabel labelBasDroite = (JLabel) gridPanel.getComponent(basDroite);
//        labelBasDroite.setIcon(null);
//        labelBasDroite.setText(piecesRecuperees[3] ? "✔ Navigation" : "Navigation");
    }
}