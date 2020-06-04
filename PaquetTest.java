import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class PaquetTest {
	public void testMelangePaquet(){
		Paquet p = new Paquet(10);

		ArrayList<Integer> paquet = new ArrayList<>();
		paquet.addAll(p.paquet);
		p.melangePaquet();
		assertEquals(false, paquet.equals(p.paquet));
	}

	public void testPremiereCarte(){
		Paquet p = new Paquet(10);
		int carte = p.premiereCarte();
		assertEquals(true, p.paquetDefausse.get(0) == carte);
	}
}
