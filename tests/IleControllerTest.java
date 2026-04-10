import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IleControllerTest {

    private DesertController controller;

    @BeforeEach
    public void setUp() {
        controller = new DesertController();
    }

    @Test
    public void testInitialisation() {
        assertNotNull(controller.getIle());
    }
}
