import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;

public class JeuTest {
	public void testDeplacement(){
		Joueur[] liste = new Joueur[1];
		liste[0] = new Joueur("a", 0);
		Jeu jeu = new Jeu(5, 5, liste);

		jeu.joueurs[0].setCase(jeu.grille.getCase(3, 3));

		assertEquals(0, jeu.deplacement("H", jeu.joueurs[0]));
		assertEquals(2, jeu.joueurs[0].getPosy());

		assertEquals(0, jeu.deplacement("B", jeu.joueurs[0]));
		assertEquals(3, jeu.joueurs[0].getPosy());

		assertEquals(0, jeu.deplacement("D", jeu.joueurs[0]));
		assertEquals(4, jeu.joueurs[0].getPosx());

		assertEquals(0, jeu.deplacement("G", jeu.joueurs[0]));
		assertEquals(3, jeu.joueurs[0].getPosx());
	}

	public void testAssechement() {
		Joueur[] liste = new Joueur[1];
		liste[0] = new Joueur("a", 0);
		Jeu jeu = new Jeu(5, 5, liste);
		jeu.joueurs[0].getCase().downLevel();

		assertEquals(0, jeu.assechement("C", jeu.joueurs[0]));

		assertEquals(2, jeu.joueurs[0].getCase().getLevel());

		assertEquals(1, jeu.assechement("C", jeu.joueurs[0]));

		jeu.joueurs[0].getCase().downLevel();
		jeu.joueurs[0].getCase().downLevel();

		assertEquals(1, jeu.assechement("C", jeu.joueurs[0]));
	}

	@Test
	public void testRecupArtefact() {
		Joueur[] liste = new Joueur[1];
		liste[0] = new Joueur("a", 0);
		Jeu jeu = new Jeu(5, 5, liste);

		jeu.joueurs[0].setCase(jeu.grille.getCaseSpe(1));

		jeu.joueurs[0].addClef(0);

		assertEquals(0, jeu.recupArtefact(jeu.joueurs[0]));

		assertEquals(1, jeu.recupArtefact(jeu.joueurs[0]));

		assertEquals(true, jeu.joueurs[0].verifArtefact(0));
	}

	public void testSplitString() {
		Joueur[] liste = new Joueur[1];
		liste[0] = new Joueur("a", 0);
		Jeu jeu = new Jeu(5, 5, liste);

		ArrayList<Integer> l = jeu.splitString("1,2,3,4");

		ArrayList<Integer> l1 = new ArrayList<>() {{
			add(1);
			add(2);
			add(3);
			add(4);
		}};

		assertEquals(true, l1.equals(l));
	}

	public void testDonClef() {
		Joueur[] liste = new Joueur[2];
		liste[0] = new Joueur("a", 0);
		liste[1] = new Joueur("b", 0);
		Jeu jeu = new Jeu(5, 5, liste);

		jeu.joueurs[0].addClef(0);

		assertEquals(0, jeu.donClef("A", "1", jeu.joueurs[0]));

		assertEquals(true, jeu.joueurs[1].verifClef(0));
	}
}
