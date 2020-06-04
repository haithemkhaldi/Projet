import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Dessin extends JPanel{

	private class Figure{
		protected int x, y;
		protected String texte;
		protected Color color;
		public Figure(int x, int y, Color color){
			this.x = x;
			this.y = y;
			this.texte = "";
			this.color = color;
		}
	}

	private Figure[][] cases;
	private Figure[] pions;

	private Grille grille;
	private Joueur[] joueurs;

	private boolean afficheAide;

	private Joueur joueurActuel;
	private Color[] CNORMAL = {Color.white, Color.gray, Color.black};
	private Color[] CHELI = {new Color(255, 0, 0), new Color(77, 0, 0), Color.black};
	private Color[] CAIR = {new Color(0, 201, 255), new Color(0, 66, 84), Color.black};
	private Color[] CEAU = {new Color(79, 79, 255), new Color(0, 0, 128), Color.black};
	private Color[] CTERRE = {new Color(44, 231, 0), new Color(23, 95, 0), Color.black};
	private Color[] CFEU = {new Color(255, 114, 0), new Color(99, 49, 0), Color.black};
	private Color[] CJOUEUR = {Color.red, Color.blue, Color.cyan, Color.green, Color.magenta,
							   Color.orange, Color.darkGray, Color.pink, Color.yellow};

	public Dessin(Grille grille, Joueur[] j){
		this.grille = grille;
		cases = new Figure[this.grille.getTaillex()][this.grille.getTailley()];
		joueurs = j;
		pions = new Figure[j.length];
		afficheAide = true;
		return;
	}

	
	public void init(){
		
		for(int i = 0; i < grille.getTaillex(); i++){
			for(int j = 0; j < grille.getTailley(); j++)
				cases[i][j] =  new Figure(i, j, CNORMAL[0]);
		}

		for(int i = 0; i < pions.length; i++)
			pions[i] = new Figure(joueurs[i].getPosx(), joueurs[i].getPosy(), CJOUEUR[i]);

		creerFenetre("Projet POGL : Île Interdite v3.1");
		update();

		nomCases(false);

		return;
	}

	public void nomCases(boolean posCase){
		if(posCase){
			for(int i = 0; i < cases.length; i++){
				for(int j = 0; j < cases[i].length; j++)
					cases[i][j].texte = Integer.toString(j * grille.getTaillex() + i);
			}
		}
		else{
			if(cases[0][0].texte.equals("0")) {
				for (int i = 0; i < cases.length; i++) {
					for (int j = 0; j < cases[i].length; j++)
						cases[i][j].texte = "";
				}
			}
			
			cases[grille.getCaseSpe(0).getPosx()][grille.getCaseSpe(0).getPosy()].texte = "Héliport";
			cases[grille.getCaseSpe(1).getPosx()][grille.getCaseSpe(1).getPosy()].texte = "Air";
			cases[grille.getCaseSpe(2).getPosx()][grille.getCaseSpe(2).getPosy()].texte = "Eau";
			cases[grille.getCaseSpe(3).getPosx()][grille.getCaseSpe(3).getPosy()].texte = "Terre";
			cases[grille.getCaseSpe(4).getPosx()][grille.getCaseSpe(4).getPosy()].texte = "Feu";
		}
	}

	private void creerFenetre(String name) {
		JFrame fenetre = new JFrame(name);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenetre.getContentPane().add(this);
		fenetre.setPreferredSize(new Dimension(840, 600));
		fenetre.pack();
		fenetre.setVisible(true);
		return;
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if(afficheAide)
			drawNotice(g);
		else {
			
			int[] marge = new int[2];

			int tailleCase = calculMarge(marge, getHeight() / 10);

			drawGrille(g, marge[1], marge[0], getWidth() - marge[1], getHeight() - marge[0]);

			drawCases(g, tailleCase, marge[0], marge[1]);

			drawJoueurs(g, tailleCase, marge[0], marge[1]);

			drawLigneInfo(g, tailleCase);
		}
		return;
	}

	
	private int calculMarge(int[] tab, int margeMini){

		int tailleCase = (grille.getTaillex() - grille.getTailley()) * (getHeight() - margeMini*2) / grille.getTailley();

		if(getWidth() > getHeight() + tailleCase){

			tailleCase = (getHeight() - margeMini*2) / grille.getTailley();

			tab[0] = margeMini;

			tab[1] = (getWidth() - (tailleCase * grille.getTaillex())) / 2;
		}

		else{
			tailleCase = (getWidth() - margeMini*2) / grille.getTaillex();
			tab[0] = (getHeight() - (tailleCase * grille.getTailley())) / 2;
			tab[1] = margeMini;
		}
		return tailleCase;
	}

	private void drawGrille(Graphics g, int x0, int y0, int x1, int y1){
		
		for(int i = 0; i < grille.getTaillex()+1; i++) {
			int x0temp = x0 + ((x1 - x0) / (grille.getTaillex())) * i;
			int y0temp = y0;
			int x1temp = x0 + ((x1 - x0) / (grille.getTaillex())) * i;

			int y1temp = y0 + ((y1 - y0) / (grille.getTailley())) * (grille.getTailley());
			g.drawLine(x0temp, y0temp, x1temp, y1temp);
		}
		for(int i = 0; i < grille.getTailley()+1; i++) {
			int x0temp = x0;
			int y0temp = y0 + ((y1 - y0) / (grille.getTailley())) * i;
			int x1temp = x0 + ((x1 - x0) / (grille.getTaillex())) * (grille.getTaillex());
			int y1temp = y0 + ((y1 - y0) / (grille.getTailley())) * i;
			g.drawLine(x0temp, y0temp, x1temp, y1temp);
		}
		return;
	}

	private void drawCases(Graphics g, int tailleCase, int margeHB, int margeGD){
		Font fontCase = new Font("Arial", Font.BOLD, tailleCase/5);

		int tailleTexte;

		for(Figure[] tf : cases){
			for(Figure f : tf) {
				g.setColor(f.color);
				g.fillRect(f.x * tailleCase + margeGD + 1, f.y * tailleCase + margeHB + 1, tailleCase - 1, tailleCase - 1);

				if(f.texte != "") {

					g.setColor(Color.black);

					g.setFont(fontCase);

					tailleTexte = g.getFontMetrics().stringWidth(f.texte);

					g.drawString(f.texte, (f.x * tailleCase + margeGD + 1) + tailleCase / 2 - tailleTexte / 2, (f.y * tailleCase + margeHB + 1) + tailleCase / 2 + tailleCase/10);
				}
			}
		}
		return;
	}

	private void drawJoueurs(Graphics g, int tailleCase, int margeHB, int margeGD){
		
		ArrayList<Integer> pionsDis = new ArrayList();
		
		ArrayList<ArrayList> caseJ = new ArrayList<>();

		for(int i = 0; i < pions.length; i++)
			pionsDis.add(i);

		while(!pionsDis.isEmpty()){

			caseJ.add(new ArrayList<Figure>());

			caseJ.get(caseJ.size()-1).add(pions[pionsDis.get(0)]);

			for(int i = 1; i < pionsDis.size(); i++){

				if(pions[pionsDis.get(0)].x == pions[pionsDis.get(i)].x && pions[pionsDis.get(0)].y == pions[pionsDis.get(i)].y){

					caseJ.get(caseJ.size()-1).add(pions[pionsDis.get(i)]);

					pionsDis.remove(i);

					i--;
				}
			}
			pionsDis.remove(0);
		}

	
		int[] posPions;
		for(ArrayList<Figure> l : caseJ){
			if(l.size() == 1) 	   posPions = new int[]{5};
			else if(l.size() == 2) posPions = new int[]{7, 3};
			else if(l.size() == 3) posPions = new int[]{7, 5, 3};
			else if(l.size() == 4) posPions = new int[]{7, 1, 9, 3};
			else if(l.size() == 5) posPions = new int[]{7, 1, 9, 3, 5};
			else if(l.size() == 6) posPions = new int[]{7, 8, 9, 1, 2, 3};
			else if(l.size() == 7) posPions = new int[]{7, 8, 9, 1, 2, 3, 5};
			else if(l.size() == 8) posPions = new int[]{7, 8, 9, 1, 2, 3, 4, 6};
			else 				   posPions = new int[]{7, 8, 9, 1, 2, 3, 4, 5, 6};

			for(int i = 0; i < l.size(); i++)
				placePions(g, posPions[i], l.get(i), tailleCase, margeHB, margeGD);
		}
		return;
	}

	private void placePions(Graphics g, int pos, Figure pion, int tailleCase, int margeHB, int margeGD){
		
		int x = 0, y = 0;
		if(pos == 7 || pos == 8 || pos == 9) y = pion.y * tailleCase + margeHB + tailleCase * 3 / 16 - (tailleCase / 6);
		if(pos == 4 || pos == 5 || pos == 6) y = pion.y * tailleCase + margeHB + tailleCase / 2 - (tailleCase / 6);
		if(pos == 1 || pos == 2 || pos == 3) y = pion.y * tailleCase + margeHB + tailleCase * 13 / 16 - (tailleCase / 6);

		if(pos == 7 || pos == 4 || pos == 1) x = pion.x * tailleCase + margeGD + tailleCase * 3 / 16 - (tailleCase / 6);
		if(pos == 8 || pos == 5 || pos == 2) x = pion.x * tailleCase + margeGD + tailleCase / 2 - (tailleCase / 6);
		if(pos == 9 || pos == 6 || pos == 3) x = pion.x * tailleCase + margeGD + tailleCase * 13 / 16 - (tailleCase / 6);

		g.setColor(pion.color);
		g.fillOval(x, y, tailleCase / 3, tailleCase / 3);
		g.setColor(Color.black);
		g.drawOval(x, y, tailleCase / 3, tailleCase / 3);

		return;
	}

	private void drawNotice(Graphics g){

		
		g.setColor(CAIR[0]);
		g.fillRect(getWidth()/2-25-75, getHeight()/2-100, 50, 50);

		g.setColor(CEAU[0]);
		g.fillRect(getWidth()/2-25-25, getHeight()/2-100, 50, 50);

		g.setColor(CTERRE[0]);
		g.fillRect(getWidth()/2-25+25, getHeight()/2-100, 50, 50);

		g.setColor(CFEU[0]);
		g.fillRect(getWidth()/2-25+75, getHeight()/2-100, 50, 50);

		g.setColor(CNORMAL[0]);
		g.fillRect(getWidth()/2-25-50, getHeight()/2-50, 50, 50);

		g.setColor(CNORMAL[1]);
		g.fillRect(getWidth()/2-25+00, getHeight()/2-50, 50, 50);

		g.setColor(CNORMAL[2]);
		g.fillRect(getWidth()/2-25+50, getHeight()/2-50, 50, 50);

		g.setColor(Color.black);
		g.drawRect(getWidth()/2-25+25, getHeight()/2-100, 50, 50);
		g.drawRect(getWidth()/2-25-25, getHeight()/2-100, 50, 50);
		g.drawRect(getWidth()/2-25-75, getHeight()/2-100, 50, 50);
		g.drawRect(getWidth()/2-25+75, getHeight()/2-100, 50, 50);

		g.drawRect(getWidth()/2-25-50, getHeight()/2-50, 50, 50);
		g.drawRect(getWidth()/2-25-0, getHeight()/2-50, 50, 50);
		g.drawRect(getWidth()/2-25+50, getHeight()/2-50, 50, 50);

		g.drawString("Projet POGL : Île Interdite",   getWidth()/2-65, getHeight()/2-120);

		g.drawString("Air",   getWidth()/2-25-75+18, getHeight()/2-75);
		g.drawString("Eau",   getWidth()/2-25-25+15, getHeight()/2-75);
		g.drawString("Terre", getWidth()/2-25+25+12, getHeight()/2-75);
		g.drawString("Feu",   getWidth()/2-25+75+15, getHeight()/2-75);

		g.drawString("Normale",   getWidth()/2-25-50+1, getHeight()/2-25);
		g.drawString("Inondé",   getWidth()/2-25-00+6, getHeight()/2-25);
		g.setColor(Color.white);
		g.drawString("Sub", getWidth()/2-25+50+15, getHeight()/2-25);

		String aEcrire;

		for(int i = 0; i < joueurs.length; i++){
			g.setColor(CJOUEUR[i]);
			g.fillOval(getWidth()/2-50, getHeight()/2 + (i*30) + 10, 20, 20);
			g.setColor(Color.black);
			g.drawOval(getWidth()/2-50, getHeight()/2 + (i*30) + 10, 20, 20);

			aEcrire = ": " + joueurs[i].getName();

			if(joueurs[i].getRole() == 1) aEcrire += " - le Pilote";
			else if(joueurs[i].getRole() == 2) aEcrire += " - l'Ingénieur";
			else if(joueurs[i].getRole() == 3) aEcrire += " - l'Explorateur";
			else if(joueurs[i].getRole() == 4) aEcrire += " - le Navigateur";
			else if(joueurs[i].getRole() == 5) aEcrire += " - le Plongeur";
			else if(joueurs[i].getRole() == 6) aEcrire += " - le Messager";

			g.drawString(aEcrire, getWidth()/2-25, getHeight()/2 + (i*30) + 25);
		}

		g.drawRect(getWidth()/2-150, getHeight()/2 - 150, 300, joueurs.length*30 + 160);

		return;
	}

	public void setJoueurActuel(Joueur j){
		joueurActuel = j;
		return;
	}

	private void drawLigneInfo(Graphics g, int tailleCase){
		
		g.setColor(new Color(182, 182, 182));

		g.fillRect(0, 0, this.getWidth(), tailleCase / 2);
		g.fillRect(0, this.getHeight() - tailleCase / 2, this.getWidth(), tailleCase / 2);

		int posJoueur;
		for(posJoueur = 0; posJoueur < joueurs.length; posJoueur++){
			if(joueurs[posJoueur] == joueurActuel)
				break;
		}

		String aEcrire = "C'est au tour de : " + joueurActuel.getName();

		if(joueurActuel.getRole() == 1) aEcrire += " - le Pilote";
		else if(joueurActuel.getRole() == 2) aEcrire += " - l'Ingénieur";
		else if(joueurActuel.getRole() == 3) aEcrire += " - l'Explorateur";
		else if(joueurActuel.getRole() == 4) aEcrire += " - le Navigateur";
		else if(joueurActuel.getRole() == 5) aEcrire += " - le Plongeur";
		else if(joueurActuel.getRole() == 6) aEcrire += " - le Messager";

		Font fontCase = new Font("Arial", Font.BOLD, tailleCase/5);
		int tailleTexte = g.getFontMetrics().stringWidth(aEcrire);

		g.setColor(Color.black);
		g.setFont(fontCase);
		g.drawString(aEcrire, this.getWidth() / 2 - tailleTexte / 2, tailleCase / 3);
		g.setColor(pions[posJoueur].color);
		g.fillOval(this.getWidth() / 2 + tailleTexte / 2 + this.getWidth() / 100, tailleCase / 9, tailleCase / 3, tailleCase / 3);
		g.setColor(Color.black);
		g.drawOval(this.getWidth() / 2 + tailleTexte / 2 + this.getWidth() / 100, tailleCase / 9, tailleCase / 3, tailleCase / 3);

		aEcrire = "  Clefs :";
		tailleTexte = g.getFontMetrics().stringWidth(aEcrire);

		g.drawString(aEcrire, 0, this.getHeight() - tailleCase / 6);

		g.setColor(Color.white);
		if(joueurActuel.getClef(0) < 4)
			g.fillRect(tailleTexte + this.getWidth() / 100, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

		if(joueurActuel.getClef(1) < 4)
			g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

		if(joueurActuel.getClef(2) < 4)
			g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

		if(joueurActuel.getClef(3) < 4)
			g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);


		g.setColor(CAIR[0]);

		if(joueurActuel.getClef(0) >= 4)
			g.fillRect(tailleTexte + this.getWidth() / 100, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

		else if(joueurActuel.getClef(0) != 0){

			if (joueurActuel.getClef(0) >= 1)
				g.fillRect(tailleTexte + this.getWidth() / 100, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);

			if (joueurActuel.getClef(0) >= 2)
				g.fillRect(tailleTexte + this.getWidth() / 100, this.getHeight() - tailleCase / 2 + tailleCase / 4, tailleCase / 4, tailleCase / 4);


			if (joueurActuel.getClef(0) >= 3)
				g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase / 4, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
		}


		g.setColor(CEAU[0]);
		if(joueurActuel.getClef(1) >= 4)
			g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
		else if(joueurActuel.getClef(1) != 0){
			if (joueurActuel.getClef(1) >= 1)
				g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
			if (joueurActuel.getClef(1) >= 2)
				g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase / 2, this.getHeight() - tailleCase / 2 + tailleCase / 4, tailleCase / 4, tailleCase / 4);
			if (joueurActuel.getClef(1) >= 3)
				g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase / 2 + tailleCase / 4, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
		}

		g.setColor(CTERRE[0]);
		if(joueurActuel.getClef(2) >= 4)
			g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
		else if(joueurActuel.getClef(2) != 0){
			if (joueurActuel.getClef(2) >= 1)
				g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
			if (joueurActuel.getClef(2) >= 2)
				g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase, this.getHeight() - tailleCase / 2 + tailleCase / 4, tailleCase / 4, tailleCase / 4);
			if (joueurActuel.getClef(2) >= 3)
				g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 4, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
		}


		g.setColor(CFEU[0]);
		if(joueurActuel.getClef(3) >= 4)
			g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
		else if(joueurActuel.getClef(3) != 0){
			if (joueurActuel.getClef(3) >= 1)
				g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
			if (joueurActuel.getClef(3) >= 2)
				g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 2, this.getHeight() - tailleCase / 2 + tailleCase / 4, tailleCase / 4, tailleCase / 4);
			if (joueurActuel.getClef(3) >= 3)
				g.fillRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 2 + tailleCase / 4, this.getHeight() - tailleCase / 2, tailleCase / 4, tailleCase / 4);
		}

		g.setColor(Color.black);
		g.drawRect(tailleTexte + this.getWidth() / 100, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
		g.drawRect(tailleTexte + this.getWidth() / 100 + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
		g.drawRect(tailleTexte + this.getWidth() / 100 + tailleCase, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
		g.drawRect(tailleTexte + this.getWidth() / 100 + tailleCase + tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);


		aEcrire = ": Artefacts  ";
		tailleTexte = g.getFontMetrics().stringWidth(aEcrire);

		g.drawString(aEcrire, this.getWidth() - tailleTexte, this.getHeight() - tailleCase / 6);

		g.setColor(joueurActuel.verifArtefact(0) ? CAIR[0] : Color.white);
		g.fillRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100 - tailleCase - tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

		g.setColor(joueurActuel.verifArtefact(1) ? CEAU[0] : Color.white);
		g.fillRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100 - tailleCase, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

		g.setColor(joueurActuel.verifArtefact(2) ? CTERRE[0] : Color.white);
		g.fillRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100 - tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

		g.setColor(joueurActuel.verifArtefact(3) ? CFEU[0] : Color.white);
		g.fillRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

		g.setColor(Color.black);
		g.drawRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100 - tailleCase - tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
		g.drawRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100 - tailleCase, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
		g.drawRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100 - tailleCase / 2, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);
		g.drawRect(this.getWidth() - tailleTexte - tailleCase/2 - this.getWidth() / 100, this.getHeight() - tailleCase / 2, tailleCase / 2, tailleCase / 2);

		return;
	}

	public void update(){
		Case casse;

		for(int i = 0; i < grille.getTaillex(); i++){
			for(int j = 0; j < grille.getTailley(); j++) {
				casse = grille.getCase(i, j);

				if(casse.isHeliport())
					cases[i][j].color = casse.getLevel() == 2 ? CHELI[0] : casse.getLevel() == 1 ? CHELI[1] : CHELI[2];

				else if(casse.getArtefact() != 0) {
					if (casse.getArtefact() == 1)
						cases[i][j].color = casse.getLevel() == 2 ? CAIR[0] : casse.getLevel() == 1 ? CAIR[1] : CAIR[2];
					else if (casse.getArtefact() == 2)
						cases[i][j].color = casse.getLevel() == 2 ? CEAU[0] : casse.getLevel() == 1 ? CEAU[1] : CEAU[2];
					else if (casse.getArtefact() == 3)
						cases[i][j].color = casse.getLevel() == 2 ? CTERRE[0] : casse.getLevel() == 1 ? CTERRE[1] : CTERRE[2];
					else if (casse.getArtefact() == 4)
						cases[i][j].color = casse.getLevel() == 2 ? CFEU[0] : casse.getLevel() == 1 ? CFEU[1] : CFEU[2];
				}

				else
					cases[i][j].color = casse.getLevel() == 2 ? CNORMAL[0] : casse.getLevel() == 1 ? CNORMAL[1] : CNORMAL[2];
			}
		}

		for(int i = 0; i < pions.length; i++){
			pions[i].x = joueurs[i].getPosx();
			pions[i].y = joueurs[i].getPosy();
		}

		repaint();
		return;
	}

	public void afficherAide(){
		afficheAide = afficheAide ? false : true;
		return;
	}
}
