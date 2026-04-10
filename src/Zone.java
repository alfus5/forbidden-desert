import java.util.HashMap;
import java.util.Map;

public class Zone {

    private int sable;
    private ZoneType typeSpecial;
    private boolean revelee;

    public Zone() {
        this.sable = 0;
        this.typeSpecial = ZoneType.AUCUN;
        this.revelee = false;
    }

    public int getSable() {
        return sable;
    }

    public void ajouterSable() {
        sable++;
    }

    public void enleverSable() {
        if (sable > 0) sable--;
    }

    public boolean estBloquee() {
        return sable >= 2;
    }

    public ZoneType getTypeSpecial() {
        return typeSpecial;
    }

    public void setTypeSpecial(ZoneType typeSpecial) {
        this.typeSpecial = typeSpecial;
    }

    public boolean isSpecial() {
        return typeSpecial != ZoneType.NORMAL && typeSpecial != ZoneType.AUCUN;
    }

    public String getNom(String langue) {
        return Langue.getNom(typeSpecial, langue);
    }

    public boolean isPiste() {
        return typeSpecial == ZoneType.PISTE_DECOLLAGE;
    }

    public boolean isRevelee() {
        return revelee;
    }

    public void setRevelee(boolean revelee) {
        this.revelee = revelee;
    }

    public boolean isPieceZone() {
        return pieceMapping.containsKey(typeSpecial);
    }

    public PieceType getPieceType() {
        return pieceMapping.getOrDefault(typeSpecial, null);
    }

    public String getIconeTexte() {
        return switch (typeSpecial) {
            case OEIL_TEMPETE -> "🌪";
            case CRASH -> "🚁";
            case PISTE_DECOLLAGE -> "🛫";
            case OASIS -> "💧";
            case TUNNEL -> "🕳";
            case CAVE_OF_EMBERS, HOWLING_GARDEN, TEMPLE_OF_THE_SUN, TIDAL_PALACE -> "⚙";
            default -> "";
        };
    }

    @Override
    public String toString() {
        return getNom("fr") + " sable=" + sable;
    }

    private static final Map<ZoneType, PieceType> pieceMapping = new HashMap<>();

    static {
        pieceMapping.put(ZoneType.CAVE_OF_EMBERS, PieceType.HELICE);
        pieceMapping.put(ZoneType.HOWLING_GARDEN, PieceType.BOITE_VITESSES);
        pieceMapping.put(ZoneType.TEMPLE_OF_THE_SUN, PieceType.CRISTAL_ENERGIE);
        pieceMapping.put(ZoneType.TIDAL_PALACE, PieceType.SYSTEME_NAVIGATION);
    }
}