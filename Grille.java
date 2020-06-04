import java.util.Random;

public class Grille {
	private int sizex, sizey;
	private Case[][] tabCase;
	private Random random = new Random();
	private Case[] caseSpe;

	public Grille(int x, int y){
		sizex = x;
		sizey = y;
		caseSpe = new Case[5];

		tabCase = new Case[x][y];
		for(int i = 0; i < x; i++){
			for(int j = 0; j < y; j++)
				tabCase[i][j] = new Case(false, 0, i, j);
		}
		return;
	}

	public Case init(){
		int[] pos = new int[10];

		int compt = 0, x, y;
		Case caseAeroport = null;

		while(compt < 5){

			do{
				x = random.nextInt(sizex);
				y = random.nextInt(sizey);
			}
			while(contains(x, y, pos));

			if(compt == 0) {
				tabCase[x][y] = new Case(true, 0, x, y);
				caseAeroport = tabCase[x][y];
			}

			else
				tabCase[x][y] = new Case(false, compt, x, y);

			pos[compt*2] = x;
			pos[compt*2+1] = y;

			caseSpe[compt] = tabCase[x][y];
			compt++;
		}
		return caseAeroport;
	}

	protected boolean contains(int x, int y, int[] pos){
		for(int i = 1; i < pos.length; i+=2){
			if(pos[i-1] == x && pos[i] == y) return true;
		}
		return false;
	}

	protected boolean caseInonde(int level){
		for(Case[] l : tabCase){
			for(Case c : l)
				if(c.getLevel() == level) return true;
		}
		return false;
	}

	public Case getCase(int x, int y){
		if(x < 0 || x >= sizex || y < 0 || y >= sizey) return new Case(false, 0, -1, -1);
		return tabCase[x][y];
	}

	public int getTaillex() {
		return sizex;
	}

	public int getTailley(){
		return sizey;
	}

	public Case getCaseSpe(int pos){
		if(pos < 0 || pos > 4) return new Case(false, 0, -1, -1);
		return caseSpe[pos];
	}
}
