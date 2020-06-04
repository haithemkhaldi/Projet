import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	private static Joueur[] entrerJoueur(){
		Scanner sc = new Scanner(System.in);

		String nom;

		int nbJ, nbJ1;
		boolean erreur;
		do {
			try {
				erreur = false;
				System.out.print("Entrer nombre de joueur (1-9) : ");
				nbJ = sc.nextInt();
			} catch (java.util.InputMismatchException e) {
				sc.nextLine();
				System.out.println("Erreur.");
				nbJ = 0;
				erreur = true;
			}
			if(nbJ < 1 || nbJ > 9) erreur = true;
		}while(erreur);

		sc.nextLine();

		Joueur[] joueurs = new Joueur[nbJ];

		ArrayList<Integer> rolesAttribue = new ArrayList<>();

		for(int i = 0; i < nbJ; i++){
			System.out.print("Entrer le nom du joueur n°" + Integer.toString(i+1) + " : ");
			nom = sc.nextLine();

			System.out.println("\nRôle 0 : Aucun\nRôle 1 : Pilote : peut se déplacer vers une zone non submergée arbitraire (coûte une action).\n" +
					"Rôle 2 : Ingénieur :  peut assécher deux zones pour une seule action.\n" +
					"Rôle 3 : Explorateur : peut se déplacer et assécher diagonalement.\n" +
					"Rôle 4 : Navigateur : peut déplacer un autre joueur (coûte une action).\n" +
					"Rôle 5 : Plongeur : peut traverser une zone submergée (coûte une action).\n" +
					"Rôle 6 : Messager : peut donner une clé qu’il possède à un joueur distant (coûte une action).\n");

			do {
				try {
					erreur = false;
					System.out.print("Entrer role (0-6) : ");
					nbJ1 = sc.nextInt();
				} catch (java.util.InputMismatchException e) {
					sc.nextLine();
					System.out.println("Erreur.");
					nbJ1 = 0;
					erreur = true;
				}
				if(nbJ1 < 0 || nbJ1 > 6) erreur = true;
				else if(rolesAttribue.contains(nbJ1)) erreur = true;
				else{
					rolesAttribue.add(nbJ1);
					joueurs[i] = new Joueur(nom, nbJ1);
				}
			}while(erreur);
			sc.nextLine();
		}

		return joueurs;
	}

	public static void main(String[] args){
	
		Jeu j = new Jeu(9, 6, entrerJoueur());
		j.start();
		return;
	}
}
