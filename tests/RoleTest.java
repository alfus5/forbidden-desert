import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    public void testRoleValues() {
        assertNotNull(Role.values());
        assertTrue(Role.values().length > 0);
    }
}
