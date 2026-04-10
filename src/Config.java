public class Config {
    public static final String LANG = "fr";

    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 1000;

    public static final int GRID_COLUMNS = 5;
    public static final int GRID_ROWS = 5;
    public static final int ZONE_COUNT = GRID_COLUMNS * GRID_ROWS;

    public static final char[][] PLATEAU = {
            {'o', 'z', 'a', 'z', 'p'},
            {'z', 'f', 'z', 'v', 'z'},
            {'z', 'z', 't', 'z', 'z'},
            {'z', 'e', 'z', 'w', 'z'},
            {'c', 'z', 'a', 'z', 'u'}
    };

    public static final String NORMAL_ZONE_COLOR = "#F5DEB3";
    public static final String SABLE_1_ZONE_COLOR = "#E6C27A";
    public static final String SABLE_2_ZONE_COLOR = "#C68642";
    public static final String OEIL_ZONE_COLOR = "#D3D3D3";
    public static final String OASIS_ZONE_COLOR = "#87CEEB";
    public static final String PISTE_ZONE_COLOR = "#98FB98";
    public static final String CRASH_ZONE_COLOR = "#FFCC99";
    public static final String TUNNEL_ZONE_COLOR = "#D8BFD8";
    public static final String ARTEFACT_ZONE_COLOR = "#FFF176";

    public static final int NOMBRE_ACTIONS_PAR_JOUEUR = 4;
    public static final int NOMBRE_JOUEURS = 4;

    public static final double NIVEAU_TEMPETE_INITIAL = 2.0;
    public static final double NIVEAU_TEMPETE_MAX = 7.0;
    public static final int SABLE_MAX = 43;

    public static final int EAU_MAX = 4;
    public static final int BONUS_EAU_OASIS = 2;

    public static final int[][] SABLE_INITIAL = {
            {0, 2},
            {1, 1}, {1, 3},
            {2, 0}, {2, 4},
            {3, 1}, {3, 3},
            {4, 2}
    };

    public static final String MES_CONF_FIN_DE_TOUR =
            "Il vous reste des actions. Voulez-vous terminer votre tour ?";
    public static final String MES_DEPLACEMENT_IMPOSSIBLE =
            "Déplacement impossible";
    public static final String MES_CREUSER_IMPOSSIBLE =
            "Impossible d'enlever du sable ici";
    public static final String MES_ACTION_NUL =
            "Vous n'avez plus d'actions restantes";
    public static final String MES_CASE_BLOQUEE =
            "La case est bloquée par le sable";
    public static final String MES_PAS_ARTEFACT =
            "Il n'y a pas de pièce à récupérer ici";
    public static final String MES_ARTEFACT_BLOQUE =
            "La case est bloquée, impossible de récupérer la pièce";
    public static final String MES_MORT_SOIF =
            "Un joueur est mort de soif";
    public static final String MES_TEMPETE_MAX =
            "La tempête est devenue incontrôlable";
    public static final String MES_TROP_SABLE =
            "Il n'y a plus assez de sable disponible";
    public static final String MES_VICTOIRE =
            "Victoire ! Toute l'équipe s'échappe du désert";
    public static final String MES_RETOUR = "Annuler";
}