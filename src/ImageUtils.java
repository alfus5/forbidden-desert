import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImageUtils {

    /**
     * Charge une image à partir d'un chemin donné.
     *
     * @param path Le chemin relatif de l'image (par exemple "ressources/zones/img_zone_HELIPORT.png").
     * @param width La largeur souhaitée.
     * @param height La hauteur souhaitée.
     * @return Un ImageIcon contenant l'image redimensionnée, ou null si l'image est introuvable.
     */
    public static ImageIcon loadImage(String path, int width, int height) {
        try {
            // Charger l'image
            URL imageUrl = ImageUtils.class.getClassLoader().getResource(path);
            if (imageUrl == null) {
                System.err.println("Image introuvable : " + path);
                return null;
            }

            // Redimensionner l'image
            ImageIcon originalIcon = new ImageIcon(imageUrl);
            Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image : " + path);
            e.printStackTrace();
            return null;
        }
    }
}