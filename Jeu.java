import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Jeu {

	private boolean DEBUG;
	protected Grille grille;
	private Dessin dessin;
	protected Joueur[] joueurs;
	private Paquet paquetZone;
	private Paquet paquetSpe;
	Scanner sc = new Scanner(System.in);

	private String[] CJOUEUR = {"Pion Rouge", "Pion Bleu", "Pion Cyan", "Pion Vert", "Pion Magenta",
			"Pion Orange", "Pion Gris", "Pion Rose", "Pion Jaune"};

	public Jeu(int taillex, int tailley, Joueur[] joueurs){

		if(joueurs[0].getName().equals("DEBUG")) {
			System.out.print("-----\nMode debug.\nDéplacement à travers les cases submergé autorisé\nPossibilité d'inonder les cases\nActions infinies\n-----\n");
			DEBUG = true;
		}
		else{
			DEBUG = false;
		}

		grille = new Grille(taillex, tailley);

		this.joueurs = joueurs.clone();

		dessin = new Dessin(grille, joueurs);

		Case heliport = grille.init();

		for(int i = 0; i < joueurs.length; i++)
			joueurs[i].setCase(heliport);

		paquetZone = new Paquet(taillex*tailley);
		paquetSpe = new Paquet(28);

		return;
	}

	public void start(){

		dessin.init();

		System.out.println("\nAffichage de l'aide, appuyer sur \"Entrer\" pour continuer.");
		sc.nextLine();
		dessin.afficherAide();
		dessin.update();

		int compt = 0;
		String str;
		do {
			str = tour(compt);
			compt++;
		}while(str == "");
		System.out.println(str + "\nAppuyer sur \"Entrer\" pour quitter.");
		sc.nextLine();
		System.exit(0);
		return;
	}

	protected String tour(int nbTour){
		String str;

		int passe;

		boolean assecheInge = false;

		clearConsole();
		System.out.println("--------\nTour n°" + Integer.toString(nbTour+1));

		for(Joueur j : joueurs){

			dessin.setJoueurActuel(j);

			dessin.update();

			for(int i = 0; i < 3; i++) {
				do {
					if(DEBUG) System.out.println("Mode Debug");
					System.out.print("--------\nAction n°" + Integer.toString(i+1) + "\n--------\nAffichage de l'aide (H) - Fin de votre tour (Q)\n" +
							"Déplacement du pion (D) - Assèchement d'une case adjacente (A)\nDon de clef (C)" +
							(j.getCase().getArtefact() != 0 ? " - Recherche artefact (R) :" : ""));

					if(j.getRole() == 1) System.out.print("\nPilote : Hélico (T)");
					if(j.getRole() == 4) System.out.print("\nNavigateur : Déplacer un autre joueur (J)");

					System.out.print("\n\nVotre choix : ");
					str = sc.nextLine();
					passe = action(str, j);

					if(passe == 2) i = 3;

					dessin.update();

				}while(passe != 0 && passe != 2);

				if((str.equals("A") || str.equals("a")) && j.getRole() == 2 && !assecheInge) {
					i--;
					assecheInge = true;
				}

				if(DEBUG)i--;
			}

			int recup = rechercheClef(j);

			if(recup == 0)
				System.out.println("Votre case prend l'eau !");

			else if(recup <= 4)
				System.out.println("Ajout d'une clef " + (recup == 1 ? "Air" : recup == 2 ? "Eau" : recup == 3 ? "Terre" : "Feu") + ".\n");

			else if(recup == 5)
				System.out.println("Vous avez pris l'hélico !");

			else
				System.out.println("La case à été asséchée.");

			inondation();

			dessin.update();

			System.out.println("Appuyer sur \"Entrer\" pour continuer.\n");
			sc.nextLine();

			str = verifPerdu();

			if(str != "") return str;
		}
		return "";
	}

	private int action(String action, Joueur j){

		int passe;

		if (action.equals("D") || action.equals("d")) {

			if(j.getRole() != 3)
				System.out.print("\n   (H)\n(G)-|-(D)\n   (B)\nVotre choix : ");
			else
				System.out.print("\n(HG)(H)(HD)\n(G) -|- (D)\n(BG)(B)(BD)\nVotre choix : ");

			action = sc.nextLine();

			passe = deplacement(action, j);

			if (passe == 1) System.out.println("Impossible d'effectuer le déplacement " + action + ".\n");
			else System.out.println("Déplacement effectué.\n");
			return passe;
		}

		else if (action.equals("A") || action.equals("a")) {

			if(j.getRole() != 3)
				System.out.print("\n   (H)\n(G)(C)(D)\n   (B)\nVotre choix : ");
			else
				System.out.print("\n(HG)(H)(HD)\n(G) (C) (D)\n(BG)(B)(BD)\nVotre choix : ");

			action = sc.nextLine();

			passe = assechement(action, j);

			if (passe == 1) System.out.println("Impossible d'assécher la case.\n");
			else System.out.println("Assèchement effectué.\n");
			return passe;
		}

		else if (action.equals("R") || action.equals("r")) {
			passe = recupArtefact(j);
			if (passe == 1) System.out.println("Impossible de récuperer un artefact ici.\n");
			else System.out.println("Vous avez récupéré un artefact !\n");
			return passe;
		}

		else if (action.equals("H") || action.equals("h")) {
			dessin.afficherAide();

			dessin.update();

			System.out.println("Appuyer sur \"Entrer\" pour continuer.\n");
			sc.nextLine();

			dessin.afficherAide();

			dessin.update();

			return 1;
		}

		else if(action.equals("C") || action.equals("c")) {
			System.out.print("\nClef Air (A) - Clef Eau (E) - Clef Terre (T) - Clef Feu (F) \nVotre choix : ");
			action = sc.nextLine();

			System.out.println("\nA quel joueur ?");
			for(int k = 0; k < joueurs.length; k++) {
				if(j.getRole() == 6)
					System.out.println(joueurs[k].getName() + " - " + CJOUEUR[k] + " (" + Integer.toString(k) + ")");
				else {
					if (joueurs[k].getCase().equals(j.getCase()))
						System.out.println(joueurs[k].getName() + " - " + CJOUEUR[k] + " (" + Integer.toString(k) + ")");
				}
			}

			System.out.print("\nVotre choix : ");
			String str = sc.nextLine();

			passe = donClef(action, str, j);
			if (passe == 1) System.out.println("Don impossible à effectuer.\n");
			else System.out.println("Vous avez donné une clef !\n");

			return passe;
		}

		else if(action.equals("Q") || action.equals("q")) {
			return 2;
		}

		else if((action.equals("T") || action.equals("t")) && j.getRole() == 1){
			teleporte(j, true);
			System.out.println("Déplacement effectué.\n");
			return 0;
		}

		else if((action.equals("J") || action.equals("j")) && j.getRole() == 4){

			System.out.println("\nDéplacer quel joueur ?");
			for(int k = 0; k < joueurs.length; k++)
				System.out.println(joueurs[k].getName() + " - " + CJOUEUR[k] + " (" + Integer.toString(k) + ")");

			System.out.print("\nVotre choix : ");
			String str = sc.nextLine();

			int aDonner;
			try {
				aDonner = Integer.decode(str);
			}
			catch(java.lang.NumberFormatException e){
				return 1;
			}

			if(aDonner < 0 || aDonner >= joueurs.length) return 1;

			if(j.equals(joueurs[aDonner])) return 1;

			System.out.print("\n   (H)\n(G)-|-(D)\n   (B)\nVotre choix : ");

			action = sc.nextLine();

			passe = deplacement(action, joueurs[aDonner]);

			if (passe == 1) System.out.println("Impossible d'effectuer le déplacement " + action + ".\n");
			else System.out.println("Déplacement effectué.\n");
			return passe;
		}

		else if(DEBUG) {
			if(action.equals("0")) j.addClef(0);
			else if(action.equals("1")) j.addClef(1);
			else if(action.equals("2")) j.addClef(2);
			else if(action.equals("3")) j.addClef(3);
			return 1;
		}

		else return 1;
	}

	protected int deplacement(String HBGD, Joueur j){

		if(HBGD.equals("H") || HBGD.equals("h")){

			if(j.getPosy() == 0) return 1;

			if(grille.getCase(j.getPosx(), j.getPosy()-1).getLevel() == 0 && !DEBUG && j.getRole() != 5) return 1;

			j.setCase(grille.getCase(j.getPosx(), j.getPosy()-1));
		}

		else if(HBGD.equals("B") || HBGD.equals("b")){
			if(j.getPosy() + 1 == grille.getTailley()) return 1;
			if(grille.getCase(j.getPosx(), j.getPosy()+1).getLevel() == 0 && !DEBUG && j.getRole() != 5) return 1;
			j.setCase(grille.getCase(j.getPosx(), j.getPosy()+1));
		}

		else if(HBGD.equals("G") || HBGD.equals("g")){
			if(j.getPosx() == 0) return 1;
			if(grille.getCase(j.getPosx()-1, j.getPosy()).getLevel() == 0 && !DEBUG && j.getRole() != 5) return 1;
			j.setCase(grille.getCase(j.getPosx()-1, j.getPosy()));
		}

		else if(HBGD.equals("D") || HBGD.equals("d")){
			if(j.getPosx() + 1 == grille.getTaillex()) return 1;
			if(grille.getCase(j.getPosx()+1, j.getPosy()).getLevel() == 0 && !DEBUG && j.getRole() != 5) return 1;
			j.setCase(grille.getCase(j.getPosx()+1, j.getPosy()));
		}

		else if(j.getRole() == 3){
			if(HBGD.equals("HD") || HBGD.equals("hd")){
				if(j.getPosx() + 1 == grille.getTaillex() || j.getPosy() == 0) return 1;
				if(grille.getCase(j.getPosx()+1, j.getPosy()-1).getLevel() == 0 && !DEBUG) return 1;
				j.setCase(grille.getCase(j.getPosx()+1, j.getPosy()-1));
			}

			else if(HBGD.equals("HG") || HBGD.equals("hg")){
				if(j.getPosx() == 0 || j.getPosy() == 0) return 1;
				if(grille.getCase(j.getPosx()-1, j.getPosy()-1).getLevel() == 0 && !DEBUG) return 1;
				j.setCase(grille.getCase(j.getPosx()-1, j.getPosy()-1));
			}

			else if(HBGD.equals("BD") || HBGD.equals("bd")){
				if(j.getPosx() + 1 == grille.getTaillex() || j.getPosy() + 1 == grille.getTailley()) return 1;
				if(grille.getCase(j.getPosx()+1, j.getPosy()+1).getLevel() == 0 && !DEBUG) return 1;
				j.setCase(grille.getCase(j.getPosx()+1, j.getPosy()+1));
			}

			else if(HBGD.equals("BG") || HBGD.equals("bg")){
				if(j.getPosx() == 0 || j.getPosy() + 1 == grille.getTailley()) return 1;
				if(grille.getCase(j.getPosx()-1, j.getPosy()+1).getLevel() == 0 && !DEBUG) return 1;
				j.setCase(grille.getCase(j.getPosx()-1, j.getPosy()+1));
			}
		}

		else return 1;

		return 0;
	}

	
	protected int assechement(String HBGD, Joueur j){

		if(HBGD.equals("H") || HBGD.equals("h")){
			if(j.getPosy() == 0) return 1;
			if(grille.getCase(j.getPosx(), j.getPosy()-1).getLevel() == 0) return 1;
			return grille.getCase(j.getPosx(), j.getPosy()-1).upLevel();
		}

		else if(HBGD.equals("B") || HBGD.equals("b")){
			if(j.getPosy() + 1 == grille.getTailley()) return 1;
			if(grille.getCase(j.getPosx(), j.getPosy()+1).getLevel() == 0) return 1;
			return grille.getCase(j.getPosx(), j.getPosy()+1).upLevel();
		}

		else if(HBGD.equals("G") || HBGD.equals("g")){
			if(j.getPosx() == 0) return 1;
			if(grille.getCase(j.getPosx()-1, j.getPosy()).getLevel() == 0) return 1;
			return grille.getCase(j.getPosx()-1, j.getPosy()).upLevel();
		}

		else if(HBGD.equals("D") || HBGD.equals("d")){
			if(j.getPosx() + 1 == grille.getTaillex()) return 1;
			if(grille.getCase(j.getPosx()+1, j.getPosy()).getLevel() == 0) return 1;
			return grille.getCase(j.getPosx()+1, j.getPosy()).upLevel();
		}

		else if(HBGD.equals("C") || HBGD.equals("c")){
			
			if(DEBUG){
				if(j.getCase().getLevel() == 2 || j.getCase().getLevel() == 1) return j.getCase().downLevel();
				if(j.getCase().getLevel() == 0) return j.getCase().upLevel();
			}
			if(j.getCase().getLevel() == 0) return 1;

			return j.getCase().upLevel();
		}

		else if(j.getRole() == 3){
			if(HBGD.equals("HD") || HBGD.equals("hd")){
				if(j.getPosx() + 1 == grille.getTaillex() || j.getPosy() == 0) return 1;
				if(grille.getCase(j.getPosx()+1, j.getPosy()-1).getLevel() == 0) return 1;
				return grille.getCase(j.getPosx()+1, j.getPosy()-1).upLevel();
			}

			else if(HBGD.equals("HG") || HBGD.equals("hg")){
				if(j.getPosx() == 0 || j.getPosy() == 0) return 1;
				if(grille.getCase(j.getPosx()-1, j.getPosy()-1).getLevel() == 0) return 1;
				return grille.getCase(j.getPosx()-1, j.getPosy()-1).upLevel();
			}

			else if(HBGD.equals("BD") || HBGD.equals("bd")){
				if(j.getPosx() + 1 == grille.getTaillex() || j.getPosy() + 1 == grille.getTailley()) return 1;
				if(grille.getCase(j.getPosx()+1, j.getPosy()+1).getLevel() == 0) return 1;
				return grille.getCase(j.getPosx()+1, j.getPosy()+1).upLevel();
			}

			else if(HBGD.equals("BG") || HBGD.equals("bg")){
				if(j.getPosx() == 0 || j.getPosy() + 1 == grille.getTailley()) return 1;
				if(grille.getCase(j.getPosx()-1, j.getPosy()+1).getLevel() == 0) return 1;
				return grille.getCase(j.getPosx()-1, j.getPosy()+1).upLevel();
			}
			else return 1;
		}

		else return 1;
	}

	protected int recupArtefact(Joueur j){

		int artefactCase = j.getCase().getArtefact();

		if(artefactCase != 0){

			if(j.getClef(artefactCase-1) >= 4){

				j.getCase().removeArtefact();

				j.addArtefact(artefactCase-1);
				return 0;
			}
		}
		return 1;
	}

	protected int rechercheClef(Joueur j){

		int alea = paquetSpe.premiereCarte();
		while(!grille.caseInonde(1) && alea > 25)
			alea = paquetSpe.premiereCarte();

		if(alea <= 2) {
			j.getCase().downLevel();
			paquetZone.melangePaquetDefausse();
			return 0;
		}

		else if(alea <= 7) {
			j.addClef(0);
			return 1;
		}

		else if(alea <= 12) {
			j.addClef(1);
			return 2;
		}

		else if(alea <= 17) {
			j.addClef(2);
			return 3;
		}

		else if(alea <= 22) {
			j.addClef(3);
			return 4;
		}

		else if(alea <= 25) {
			System.out.println("\nHélico :\nVous pouvez aller où vous voulez.");
			teleporte(j, false);
			return 5;
		}
		else{
			System.out.println("\nSac de sable :\nVous pouvez assecher la case que vous voulez.");
			int nbJ;

			boolean erreur;

			dessin.nomCases(true);
			dessin.update();

			do {
				try {
					erreur = false;
					System.out.print("\nMettre la position de la case à assecher : ");
					nbJ = sc.nextInt();
				} catch (java.util.InputMismatchException e) {
					sc.nextLine();
					System.out.println("Erreur.");
					nbJ = 0;
					erreur = true;
				}
				if(nbJ < 0 || nbJ > (grille.getTaillex() * grille.getTailley()) - 1) erreur = true;

				if(!erreur && grille.getCase(nbJ % grille.getTaillex(), nbJ / grille.getTaillex()).getLevel() != 1) erreur = true;
			}while(erreur);

			sc.nextLine();

			grille.getCase(nbJ % grille.getTaillex(), nbJ / grille.getTaillex()).upLevel();

			dessin.nomCases(false);
			return 6;
		}
	}

	private void teleporte(Joueur j, boolean role) {
		int nbJ;

		boolean erreur;

		dessin.nomCases(true);
		dessin.update();

		do {
			try {
				erreur = false;
				System.out.print("\nMettre la position de la case où se déplacer : ");
				nbJ = sc.nextInt();
			} catch (java.util.InputMismatchException e) {
				sc.nextLine();
				System.out.println("Erreur.");
				nbJ = 0;
				erreur = true;
			}
			if (nbJ < 0 || nbJ > (grille.getTaillex() * grille.getTailley()) - 1) erreur = true;

			if (!erreur && grille.getCase(nbJ % grille.getTaillex(), nbJ / grille.getTaillex()).getLevel() == 0)
				erreur = true;
		} while (erreur);

		sc.nextLine();

		dessin.nomCases(false);
		dessin.update();

		for (int k = 0; k < joueurs.length; k++) {
			if (joueurs[k].getCase().equals(j.getCase()) && !joueurs[k].equals(j)) {
				erreur = true;
				break;
			}
		}

		if (erreur && !role) {
			do {

				System.out.println("\nAvec quels joueurs ? (exemple format : 0,1,2 ou rien si aucun joueur)");
				for (int k = 0; k < joueurs.length; k++) {

					if (joueurs[k].getCase().equals(j.getCase()))
						System.out.println(joueurs[k].getName() + " - " + CJOUEUR[k] + " (" + Integer.toString(k) + ")");
				}

				System.out.print("\nVotre choix : ");
				String str = sc.nextLine();

				if (str.equals("")) erreur = false;

				else {
					ArrayList<Integer> joueursQuiViens = splitString(str);

					if (joueursQuiViens.size() != 0) {
						erreur = false;

						for (Integer i : joueursQuiViens) {

							if (i < 0 || i >= joueurs.length) {
								erreur = true;
								break;
							}
							else if (!joueurs[i].getCase().equals(j.getCase())) {
								erreur = true;
								break;
							}
						}
						if (!erreur) {
							for (Integer i : joueursQuiViens) {
								joueurs[i].setCase(grille.getCase(nbJ % grille.getTaillex(), nbJ / grille.getTaillex()));
							}
						}
					}
				}

			} while (erreur);
		}

		j.setCase(grille.getCase(nbJ % grille.getTaillex(), nbJ / grille.getTaillex()));
	}

	protected ArrayList<Integer> splitString(String str){
		ArrayList<Integer> ints = new ArrayList<>();

		String[] lstr = str.split(",");

		for(String s : lstr){
			try{
				ints.add(Integer.decode(s));
			}
			catch(java.lang.NumberFormatException e){
				ints.clear();
				return ints;
			}
		}
		return ints;
	}

	protected void inondation(){
		int tirage = 0;
		for(int i = 0; i < 3; i++) {
			tirage = paquetZone.premiereCarte();

			while (grille.getCase(tirage % grille.getTaillex(), tirage / grille.getTaillex()).downLevel() == 1)
				tirage = paquetZone.premiereCarte();
		}
		return;
	}

	protected int donClef(String str, String str1, Joueur j){

		int aDonner;
		try {
			aDonner = Integer.decode(str1);
		}
		catch(java.lang.NumberFormatException e){
			return 1;
		}

		if(aDonner < 0 || aDonner >= joueurs.length) return 1;

		if(j.getRole() != 6 && !joueurs[aDonner].getCase().equals(j.getCase())) return 1;

		if(str.equals("A") || str.equals("a")){
			if(j.delClef(0) == 1) return 1;
			return joueurs[aDonner].addClef(0);
		}

		else if(str.equals("E") || str.equals("e")){
			if(j.delClef(1) == 1) return 1;
			return joueurs[aDonner].addClef(1);
		}

		else if(str.equals("T") || str.equals("t")){
			if(j.delClef(2) == 1) return 1;
			return joueurs[aDonner].addClef(2);
		}

		else if(str.equals("F") || str.equals("f")){
			if(j.delClef(3) == 1) return 1;
			return joueurs[aDonner].addClef(3);
		}

		else return 1;
	}

	private void clearConsole() {
		for(int i = 0; i < 20; i++){
			System.out.print("\n");
		}
	}

	protected String verifPerdu(){
		int compt = 0, max = 0;

		for(Joueur j : joueurs){
			if(j.getCase().isHeliport()) compt++;
		}

		for(int i = 0; i < 5; i++) max += grille.getCaseSpe(i).getArtefact();

		if(compt == joueurs.length && max == 0)
			return "Vous avez gagné !";

		for(Joueur j : joueurs) {

			if (j.getPosy() != 0) {
				max++;
				if (grille.getCase(j.getPosx(), j.getPosy() - 1).getLevel() == 0)
					compt++;
			}

			if (j.getPosy() + 1 != grille.getTailley()) {
				max++;
				if (grille.getCase(j.getPosx(), j.getPosy() + 1).getLevel() == 0)
					compt++;
			}

			if (j.getPosx() != 0) {
				max++;
				if (grille.getCase(j.getPosx() - 1, j.getPosy()).getLevel() == 0)
					compt++;
			}

			if (j.getPosx() + 1 != grille.getTaillex()) {
				max++;
				if (grille.getCase(j.getPosx() + 1, j.getPosy()).getLevel() == 0)
					compt++;
			}

			if(max - compt == 0) return "Vous avez perdu !\nLe joueur " + j.getName() + " est bloqué !";
			max = 0; compt = 0;
		}

		for(int i = 0; i < 5; i++) {
			if (grille.getCaseSpe(i).getPosy() != 0) {
				max++;
				if (grille.getCase(grille.getCaseSpe(i).getPosx(), grille.getCaseSpe(i).getPosy() - 1).getLevel() == 0)
					compt++;
			}

			if (grille.getCaseSpe(i).getPosy() + 1 != grille.getTailley()) {
				max++;
				if (grille.getCase(grille.getCaseSpe(i).getPosx(), grille.getCaseSpe(i).getPosy() + 1).getLevel() == 0)
					compt++;
			}

			if (grille.getCaseSpe(i).getPosx() != 0) {
				max++;
				if (grille.getCase(grille.getCaseSpe(i).getPosx() - 1, grille.getCaseSpe(i).getPosy()).getLevel() == 0)
					compt++;
			}

			if (grille.getCaseSpe(i).getPosx() + 1 != grille.getTaillex()) {
				max++;
				if (grille.getCase(grille.getCaseSpe(i).getPosx() + 1, grille.getCaseSpe(i).getPosy()).getLevel() == 0)
					compt++;
			}

			if (max - compt == 0 && (grille.getCaseSpe(i).getArtefact() != 0 || grille.getCaseSpe(i).isHeliport())) return "Vous avez perdu !\nUne case spéciale est inaccessible !";
			max = 0; compt = 0;
		}

		for(int i = 0; i < 5; i++){
			if(grille.getCaseSpe(i).isHeliport() || grille.getCaseSpe(i).getArtefact() != 0){
				if(grille.getCaseSpe(i).getLevel() == 0){
					return "Vous avez perdu !\nUne case spécial est bloquée !";
				}
			}
		}

		return "";
	}
}
