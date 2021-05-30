package Tetris;

import java.awt.Color;
import java.awt.Graphics;

public class Tetromina {

	public static final int �IRINA_IGRALNE_MRE�E = 10;//�irina igralne plo��e
	public static final int VI�INA_IGRALNE_MRE�E = 20;//vi�ina igralne plo��e
	public static final int VELIKOST_KVADRATKA = 30;//vi�ina igralne plo��e
	
	private int[][] koordinate;
	private Igralna_mre�a igralna_mre�a;
	private Color barve;
	
	//za�etna lokacija oblike
	private int x = 4;
	private int y = 0;
	
	//gibanje levo in desno
	private int deltaX = 0;

	private int normalno = 500;
	private int hitro = 50;
	private int zakasnitev = normalno;
	private long za�etni�as;

	//dodati moramo ustavitev oblike ob dnu igralne mre�e
	private boolean konec = false;
		
	//konstruktor
	public Tetromina(int[][] koordinate, Igralna_mre�a igralna_mre�a, Color barve) {
		this.koordinate = koordinate;
		this.igralna_mre�a = igralna_mre�a;
		this.barve = barve;
	}
	
	public void nastaviX(int x) {
		this.x = x;
	}

	public void nastaviY(int y) {
		this.y = y;
	}
	
	public void nastaviNaZa�etneVrednosti() {
		this.x = 4;
		this.y = 0;
		konec = false;
		zakasnitev = normalno - (igralna_mre�a.vrniStopnjo() - 1)*50;
	}
		
	public void premik() {
		if (zakasnitev != hitro) {
			zakasnitev = normalno - (igralna_mre�a.vrniStopnjo() - 1)*50;
		}
		if(konec) {
			//ko pride tetromina do dna oz. do druge tetromine, po�ljemo v polje naslednjo
			for(int vrstica = 0; vrstica < koordinate.length; vrstica++) {
				for(int stolpec = 0; stolpec < koordinate[0].length; stolpec++) {
					if(koordinate[vrstica][stolpec] != 0){
						igralna_mre�a.vrniIgralno_mre�o()[y + vrstica][x + stolpec] = barve;
					}
				}
			}
			polnaVrstica();
			igralna_mre�a.dolo�iTrenutnoTetomino();
			return;
		}
		

		
		//ob kliku tipk levo ali desno opravimo premik:
		//omejiti se moramo, da oblika ostane znotraj igrane mre�e
		boolean premikX = true;
		if(!(x + deltaX + koordinate[0].length > 10) && !(x + deltaX < 0)) {
			for(int vrstica = 0; vrstica < koordinate.length; vrstica++) {
				for(int stolpec = 0; stolpec < koordinate[vrstica].length; stolpec++) {
					if(koordinate[vrstica][stolpec] != 0) {
						if(igralna_mre�a.vrniIgralno_mre�o()[y + vrstica][x + deltaX + stolpec] != null) {
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
		
		//ob kliku navzdol opravimo hitrej�i premik
		if(System.currentTimeMillis() - za�etni�as > zakasnitev) {
			//premik v navpi�ni smeri naredimo �e smo �e znotraj mre�e tj. pridemo do dna
			if (!(y + 1 + koordinate.length > VI�INA_IGRALNE_MRE�E)) {
				for (int vrstica = 0; vrstica < koordinate.length; vrstica++) {
					for (int stolpec = 0; stolpec < koordinate[vrstica].length; stolpec++) {
						if (koordinate[vrstica][stolpec] != 0) {
							if(igralna_mre�a.vrniIgralno_mre�o()[y + 1 + vrstica][x + deltaX + stolpec] != null) {
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
			za�etni�as = System.currentTimeMillis();
		}
	}
	
	private void polnaVrstica() {
		int spodnjaVrstica = igralna_mre�a.vrniIgralno_mre�o().length - 1;
		for(int zgornjaVrstica = igralna_mre�a.vrniIgralno_mre�o().length - 1; zgornjaVrstica > 0; zgornjaVrstica --) {
			int �tevec = 0;
			for(int stolpec = 0; stolpec < igralna_mre�a.vrniIgralno_mre�o()[0].length; stolpec++) {
				if(igralna_mre�a.vrniIgralno_mre�o()[zgornjaVrstica][stolpec] != null) {
					�tevec++;
				}
				igralna_mre�a.vrniIgralno_mre�o()[spodnjaVrstica][stolpec] = igralna_mre�a.vrniIgralno_mre�o()[zgornjaVrstica][stolpec];
			}
			if(�tevec < igralna_mre�a.vrniIgralno_mre�o()[0].length) {
				spodnjaVrstica--;
			}
			if(�tevec == igralna_mre�a.vrniIgralno_mre�o()[0].length) {
				igralna_mre�a.dodajTo�ke();
			}
		}

	}
	
	public void rotacijaTetromine() {
		int[][] rotiranaTetromina = transponiranjeMatrike(koordinate);	
		obra�anjeVrstic(rotiranaTetromina);
		//pred rotacijo moramo paziti, da ne prekora�imo roba mre�e
		if((x + rotiranaTetromina[0].length > Igralna_mre�a.�IRINA_IGRALNE_MRE�E) || (y + rotiranaTetromina.length > 20)) {
			return;
		}
		//pred rotacijo moramo paziti, da ne pose�emo v drugo tetromino
		for (int vrstica = 0; vrstica < rotiranaTetromina.length; vrstica++) {
			for(int stolpec = 0; stolpec < rotiranaTetromina[vrstica].length; stolpec++) {
				if (rotiranaTetromina[vrstica][stolpec] != 0) {
					if(igralna_mre�a.vrniIgralno_mre�o()[y + vrstica][x + stolpec] != null) {
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
	
	private int[][] obra�anjeVrstic(int[][] matrika) {
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
	
	public void pospe�itev() {
		zakasnitev = hitro;
	}
	
	public void upo�asnitev() {
		zakasnitev = normalno - (igralna_mre�a.vrniStopnjo() - 1)*50;
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

