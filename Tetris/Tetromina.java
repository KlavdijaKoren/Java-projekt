package Tetris;

import java.awt.Color;
import java.awt.Graphics;

public class Tetromina {

	public static final int ŠIRINA_IGRALNE_MREŽE = 10;//širina igralne plošèe
	public static final int VIŠINA_IGRALNE_MREŽE = 20;//višina igralne plošèe
	public static final int VELIKOST_KVADRATKA = 30;//višina igralne plošèe
	
	private int[][] koordinate;
	private Igralna_mreža igralna_mreža;
	private Color barve;
	
	//zaèetna lokacija oblike
	private int x = 4;
	private int y = 0;
	
	//gibanje levo in desno
	private int deltaX = 0;

	private int normalno = 500;
	private int hitro = 50;
	private int zakasnitev = normalno;
	private long zaèetniÈas;

	//dodati moramo ustavitev oblike ob dnu igralne mreže
	private boolean konec = false;
		
	//konstruktor
	public Tetromina(int[][] koordinate, Igralna_mreža igralna_mreža, Color barve) {
		this.koordinate = koordinate;
		this.igralna_mreža = igralna_mreža;
		this.barve = barve;
	}
	
	public void nastaviX(int x) {
		this.x = x;
	}

	public void nastaviY(int y) {
		this.y = y;
	}
	
	public void nastaviNaZaèetneVrednosti() {
		this.x = 4;
		this.y = 0;
		konec = false;
		zakasnitev = normalno - (igralna_mreža.vrniStopnjo() - 1)*50;
	}
		
	public void premik() {
		if (zakasnitev != hitro) {
			zakasnitev = normalno - (igralna_mreža.vrniStopnjo() - 1)*50;
		}
		if(konec) {
			//ko pride tetromina do dna oz. do druge tetromine, pošljemo v polje naslednjo
			for(int vrstica = 0; vrstica < koordinate.length; vrstica++) {
				for(int stolpec = 0; stolpec < koordinate[0].length; stolpec++) {
					if(koordinate[vrstica][stolpec] != 0){
						igralna_mreža.vrniIgralno_mrežo()[y + vrstica][x + stolpec] = barve;
					}
				}
			}
			polnaVrstica();
			igralna_mreža.doloèiTrenutnoTetomino();
			return;
		}
		

		
		//ob kliku tipk levo ali desno opravimo premik:
		//omejiti se moramo, da oblika ostane znotraj igrane mreže
		boolean premikX = true;
		if(!(x + deltaX + koordinate[0].length > 10) && !(x + deltaX < 0)) {
			for(int vrstica = 0; vrstica < koordinate.length; vrstica++) {
				for(int stolpec = 0; stolpec < koordinate[vrstica].length; stolpec++) {
					if(koordinate[vrstica][stolpec] != 0) {
						if(igralna_mreža.vrniIgralno_mrežo()[y + vrstica][x + deltaX + stolpec] != null) {
							premikX = false;
						}
					}
				}
			}
			if(premikX) {
				x += deltaX; //x premaknemo za delta X spremembo
			}
		}		
		

		
		
		deltaX = 0; //in ponastavimo vrednost
		
		//ob kliku navzdol opravimo hitrejši premik
		if(System.currentTimeMillis() - zaèetniÈas > zakasnitev) {
			//premik v navpièni smeri naredimo èe smo še znotraj mreže tj. pridemo do dna
			if (!(y + 1 + koordinate.length > VIŠINA_IGRALNE_MREŽE)) {
				for (int vrstica = 0; vrstica < koordinate.length; vrstica++) {
					for (int stolpec = 0; stolpec < koordinate[vrstica].length; stolpec++) {
						if (koordinate[vrstica][stolpec] != 0) {
							if(igralna_mreža.vrniIgralno_mrežo()[y + 1 + vrstica][x + deltaX + stolpec] != null) {
								konec = true;
							}
						}
					}
				}
				if(!konec) {
					y++;
				}
				
			} else {
				konec = true;
			}
			zaèetniÈas = System.currentTimeMillis();
		}
	}
	
	private void polnaVrstica() {
		int spodnjaVrstica = igralna_mreža.vrniIgralno_mrežo().length - 1;
		for(int zgornjaVrstica = igralna_mreža.vrniIgralno_mrežo().length - 1; zgornjaVrstica > 0; zgornjaVrstica --) {
			int števec = 0;
			for(int stolpec = 0; stolpec < igralna_mreža.vrniIgralno_mrežo()[0].length; stolpec++) {
				if(igralna_mreža.vrniIgralno_mrežo()[zgornjaVrstica][stolpec] != null) {
					števec++;
				}
				igralna_mreža.vrniIgralno_mrežo()[spodnjaVrstica][stolpec] = igralna_mreža.vrniIgralno_mrežo()[zgornjaVrstica][stolpec];
			}
			if(števec < igralna_mreža.vrniIgralno_mrežo()[0].length) {
				spodnjaVrstica--;
			}
			if(števec == igralna_mreža.vrniIgralno_mrežo()[0].length) {
				igralna_mreža.dodajToèke();
			}
		}

	}
	
	public void rotacijaTetromine() {
		int[][] rotiranaTetromina = transponiranjeMatrike(koordinate);	
		obraèanjeVrstic(rotiranaTetromina);
		//pred rotacijo moramo paziti, da ne prekoraèimo roba mreže
		if((x + rotiranaTetromina[0].length > Igralna_mreža.ŠIRINA_IGRALNE_MREŽE) || (y + rotiranaTetromina.length > 20)) {
			return;
		}
		//pred rotacijo moramo paziti, da ne posežemo v drugo tetromino
		for (int vrstica = 0; vrstica < rotiranaTetromina.length; vrstica++) {
			for(int stolpec = 0; stolpec < rotiranaTetromina[vrstica].length; stolpec++) {
				if (rotiranaTetromina[vrstica][stolpec] != 0) {
					if(igralna_mreža.vrniIgralno_mrežo()[y + vrstica][x + stolpec] != null) {
						return;
					}
				}
				
			}
		}
		koordinate = rotiranaTetromina;
	}
	
	//funkcija za transponiranje matrike, ki vre novo matriko
	//kot parameter pa vzame metriko
	private int[][] transponiranjeMatrike(int[][] matrika) {
		int[][] oblika = new int[matrika[0].length][matrika.length];
		for(int vrstica = 0; vrstica < matrika.length; vrstica++) {
			for(int stolpec = 0; stolpec < matrika[0].length; stolpec++) {
				//zamenjajo se stolpci in vrstice
				oblika[stolpec][vrstica] = matrika[vrstica][stolpec];
			}
		}
		return oblika;
	}
	
	private int[][] obraèanjeVrstic(int[][] matrika) {
		int sredina = matrika.length / 2;
		for (int vrstica = 0; vrstica < sredina; vrstica++) {
			int[] oblika = matrika[vrstica];
			matrika[vrstica] = matrika[matrika.length - vrstica - 1];
			matrika[matrika.length - vrstica - 1] = oblika;
		}	
		return matrika;	
	}
	
	
	
	public void risanjeTetromine(Graphics g) {
		for(int vrstica = 0; vrstica < koordinate.length; vrstica++) {
			for(int stolpec = 0; stolpec < koordinate[0].length; stolpec++) {
				if (koordinate[vrstica][stolpec] != 0) {
					g.setColor(barve);
					g.fillRect(stolpec * VELIKOST_KVADRATKA + x * VELIKOST_KVADRATKA, vrstica * VELIKOST_KVADRATKA + y * VELIKOST_KVADRATKA, VELIKOST_KVADRATKA, VELIKOST_KVADRATKA);					
				}
			}
		}
	}
	
	public void pospešitev() {
		zakasnitev = hitro;
	}
	
	public void upoèasnitev() {
		zakasnitev = normalno - (igralna_mreža.vrniStopnjo() - 1)*50;
	}
	
	public void premikDesno() {
		deltaX = 1;
	}
	
	public void premikLevo() {
		deltaX = -1;
	}
	
	public int[][] vrniKoordinate(){
		return koordinate;
	}
	
	public int vrniY() {
		return y;
	}
	
	public int vrniX() {
		return x;
	}

}

