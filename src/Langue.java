import java.util.HashMap;
import java.util.Map;

public class Langue {
    private static final Map<ZoneType, String> frenchName = new HashMap<>();
    private static final Map<ZoneType, String> englishName = new HashMap<>();

    static {
        frenchName.put(ZoneType.NORMAL, "Désert");
        frenchName.put(ZoneType.CRASH, "Crash");
        frenchName.put(ZoneType.PISTE_DECOLLAGE, "Piste de décollage");
        frenchName.put(ZoneType.OEIL_TEMPETE, "Œil de la tempête");
        frenchName.put(ZoneType.OASIS, "Point d'eau");
        frenchName.put(ZoneType.TUNNEL, "Tunnel");
        frenchName.put(ZoneType.CAVE_OF_EMBERS, "Temple du feu");
        frenchName.put(ZoneType.HOWLING_GARDEN, "Temple du vent");
        frenchName.put(ZoneType.TEMPLE_OF_THE_SUN, "Temple de la terre");
        frenchName.put(ZoneType.TIDAL_PALACE, "Temple de l'eau");
        frenchName.put(ZoneType.AUCUN, "");

        englishName.put(ZoneType.NORMAL, "Desert");
        englishName.put(ZoneType.CRASH, "Crash");
        englishName.put(ZoneType.PISTE_DECOLLAGE, "Launch Pad");
        englishName.put(ZoneType.OEIL_TEMPETE, "Storm Eye");
        englishName.put(ZoneType.OASIS, "Water");
        englishName.put(ZoneType.TUNNEL, "Tunnel");
        englishName.put(ZoneType.CAVE_OF_EMBERS, "Fire Temple");
        englishName.put(ZoneType.HOWLING_GARDEN, "Wind Temple");
        englishName.put(ZoneType.TEMPLE_OF_THE_SUN, "Earth Temple");
        englishName.put(ZoneType.TIDAL_PALACE, "Water Temple");
        englishName.put(ZoneType.AUCUN, "");
    }

    public static String getNom(ZoneType type, String langue) {
        return switch (langue.toLowerCase()) {
            case "fr" -> frenchName.getOrDefault(type, "Inconnu");
            case "en" -> englishName.getOrDefault(type, "Unknown");
            default -> "Unknown";
        };
    }
}