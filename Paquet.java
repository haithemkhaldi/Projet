import java.util.ArrayList;
import java.util.Random;

public class Paquet {
	protected ArrayList<Integer> paquet;
	protected ArrayList<Integer> paquetDefausse;
	private Random random = new Random();

	public Paquet(int size){
		paquet = new ArrayList<>();
		paquetDefausse = new ArrayList<>();

		for(int i = 0; i < size; i++)
			paquet.add(i);

		melangePaquet();
	}

	public void melangePaquet(){

		ArrayList<Integer> temp = new ArrayList<>(paquet.size());

		int tirage;
		while (!paquet.isEmpty()){

			tirage = random.nextInt(paquet.size());

			temp.add(paquet.get(tirage));

			paquet.remove(tirage);
		}
		paquet.addAll(temp);
	}

	public int premiereCarte(){
		if(paquet.isEmpty()) melangePaquetDefausse();

		int carte = paquet.get(paquet.size()-1);

		paquet.remove(paquet.size()-1);

		poseDefausse(carte);

		return carte;
	}

	public void poseDefausse(int carte){
		paquetDefausse.add(carte);
	}

	public void melangePaquetDefausse(){
		ArrayList<Integer> temp = new ArrayList<>(paquetDefausse.size());
		int tirage;
		while (!paquetDefausse.isEmpty()){
			tirage = random.nextInt(paquetDefausse.size());
			temp.add(paquetDefausse.get(tirage));
			paquetDefausse.remove(tirage);
		}
		paquet.addAll(temp);
	}
}
