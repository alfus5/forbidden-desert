import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DesertTest {

    private Desert ile;

    @BeforeEach
    public void setUp() {
        ile = new Desert();
    }

    @Test
    public void testCreationZones() {
        assertNotNull(ile.getZones());
        assertTrue(ile.getZones().size() > 0);
    }

    @Test
    public void testRecupererZone() {
        Zone z = ile.recupererZone("Temple du Feu");
        assertNull(z); // dépendra de l'initialisation
    }
}
