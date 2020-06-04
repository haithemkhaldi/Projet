import static org.junit.Assert.*;
import org.junit.Test;

public class JoueurTest {
	public void testAddClefs(){
		Joueur j = new Joueur("a", 0);
		j.addClef(0);
		assertEquals(true, j.verifClef(0));

		j.addClef(1);
		assertEquals(true, j.verifClef(1));

		j.addClef(2);
		assertEquals(true, j.verifClef(2));

		assertEquals(false, j.verifClef(3));


		assertEquals(1, j.addClef(4));

	}

	public void testAddArtefact(){
		Joueur j = new Joueur("a", 0);
		j.addArtefact(0);
		assertEquals(true, j.verifArtefact(0));

		j.addArtefact(1);
		assertEquals(true, j.verifArtefact(1));

		j.addArtefact(2);
		assertEquals(true, j.verifArtefact(2));

		assertEquals(false, j.verifArtefact(3));

		assertEquals(1, j.addArtefact(4));
	}
}
