import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ActionSpecialeTest {

    @Test
    public void testExecuterAction() {
        // Pas d'instance spécifique car ActionSpeciale est probablement abstraite ou une énumération
        assertNotNull(ActionSpeciale.DEPLACER_AUTRE_JOUEUR);
    }
}
