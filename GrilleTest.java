import static org.junit.Assert.*;
import org.junit.Test;

public class GrilleTest {
	@Test
	public void testContains(){
		Grille g = new Grille(5, 5);
		int[] liste = new int[]{1, 2, 3, 4};

		
		assertEquals(true, g.contains(1, 2, liste));
		assertEquals(true, g.contains(3, 4, liste));
		assertEquals(false, g.contains(2, 3, liste));
	}
	@Test
	public void testCaseInonde(){
		Grille g = new Grille(5, 5);

		assertEquals(true, g.caseInonde(2));
		assertEquals(false, g.caseInonde(1));
		assertEquals(false, g.caseInonde(0));

		g.getCase(0, 0).downLevel();

		assertEquals(true, g.caseInonde(2));
		assertEquals(true, g.caseInonde(1));
		assertEquals(false, g.caseInonde(0));

		g.getCase(0, 0).downLevel();

		assertEquals(true, g.caseInonde(2));
		assertEquals(false, g.caseInonde(1));
		assertEquals(true, g.caseInonde(0));

		g.getCase(0, 1).downLevel();

		assertEquals(true, g.caseInonde(2));
		assertEquals(true, g.caseInonde(1));
		assertEquals(true, g.caseInonde(0));

	}
}
