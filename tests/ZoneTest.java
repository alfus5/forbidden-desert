import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ZoneTest {

    private Zone zone;

    @BeforeEach
    public void setUp() {
        zone = new Zone("Temple du Feu", ZoneType.TEMPLE);
    }

    @Test
    public void testGetNom() {
        assertEquals("Temple du Feu", zone.getNom());
    }

    @Test
    public void testChangerEtat() {
        zone.inonder();
        assertTrue(zone.estInondee());
        zone.submerger();
        assertTrue(zone.estSubmergee());
    }
}
