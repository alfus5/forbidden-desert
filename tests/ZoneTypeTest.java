import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ZoneTypeTest {

    @Test
    public void testZoneTypeValues() {
        assertNotNull(ZoneType.values());
        assertTrue(ZoneType.values().length > 0);
    }
}
