import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CleTypeTest {

    @Test
    public void testCleTypeValues() {
        assertNotNull(PieceType.values());
        assertTrue(PieceType.values().length > 0);
    }
}
