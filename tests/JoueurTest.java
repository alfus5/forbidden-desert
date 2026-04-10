import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JoueurTest {

    private Joueur joueur;

    @BeforeEach
    public void setUp() {
        joueur = new Joueur("Alice", Role.PILOTE);
    }

    @Test
    public void testGetNom() {
        assertEquals("Alice", joueur.getNom());
    }

    @Test
    public void testGetRole() {
        assertEquals(Role.PILOTE, joueur.getRole());
    }
}
