public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                MainController mainController = new MainController();
                mainController.lancerApplication();
            } catch (Exception e) {
                System.err.println("Erreur au lancement de l'application");
                e.printStackTrace();
            }
        });
    }
}

