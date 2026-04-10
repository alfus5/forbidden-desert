import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ConfigTest {

    @Test
    public void testConfigValues() {
        assertEquals(6, Config.NB_MAX_JOUEURS);
    }
}
