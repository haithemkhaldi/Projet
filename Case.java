public class Case {
	private boolean heliport;
	private int artefact; //0 rien, 1, 2, 3, 4 unique artefact.
	private int level = 2; // 2 normal, 1 inondé, 0 sub.
	private int posx, posy;


	public Case(boolean isHeliport, int artefact, int posx, int posy){
		this.heliport = isHeliport;
		this.artefact = artefact;
		this.posx = posx;
		this.posy = posy;
		return;
	}

	
	public int upLevel(){
		if(level >= 2) return 1;
		level++;
		return 0;
	}

	
	public int downLevel(){
		if(level <= 0) return 1;
		level--;
		return 0;
	}

	
	public void removeArtefact(){
		artefact = 0;
		return;
	}

	public int getPosx(){
		return posx;
	}

	public int getPosy(){
		return posy;
	}

	public int getArtefact(){
		return artefact;
	}

	public int getLevel(){
		return level;
	}

	public boolean isHeliport() {
		return heliport;
	}

}
