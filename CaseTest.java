import static org.junit.Assert.*;
import org.junit.Test;

public class CaseTest {
	@Test
	public void testLevel(){
		Case c = new Case(false, 0, 0, 0);

		// On ne peut pas baisser le niveau de l'eau s'il est au plus bas.
		assertEquals(1, c.upLevel());

		//On monte deux fois le niveau de l'eau.
		assertEquals(0, c.downLevel());
		assertEquals(0, c.downLevel());

		// On ne peux plus monter le niveau de l'eau.
		assertEquals(1, c.downLevel());

	}
}
