package Tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.Timer;
import javax.swing.JPanel;

public class Igralna_mre�a extends JPanel implements KeyListener {
	private static final long serialVersionUID = 1L;
	public static final int �IRINA_IGRALNE_MRE�E = 10;//�irina igralne plo��e
	public static final int VI�INA_IGRALNE_MRE�E = 20;//vi�ina igralne plo��e
	public static final int VELIKOST_KVADRATKA = 30;//vi�ina igralne plo��e
	
	public static int IGRAJ = 0;
	public static int PAVZA = 1;
	public static int KONEC_IGRE = 2;
	
	//nastavimo za�etno stanje na igraj
	private int stanje = IGRAJ;
	
	//dodamo �e vrednosti zakasnitve (0.06sekunde)
	//ker mora biti int, definiramo z deljenjem
	//500=pol sekunde, 1000 = ena sekunda
	private static int Z = 60;
	private static int zakasnitev = Z / 1000;
	
	private Timer premikDol;
	private Color[][] igralna_mre�a = new Color[VI�INA_IGRALNE_MRE�E][�IRINA_IGRALNE_MRE�E];
	
	//naklju�na izbira tetromine
	private Random random;
	
	//dolo�imo barve tetromin
	private Color[] barve = {Color.decode("#ed1c24"), Color.decode("#ff7f27"), Color.decode("#fff200"), 
		        Color.decode("#22b14c"), Color.decode("#00a2e8"), Color.decode("#a349a4"), Color.decode("#3f48cc")};
	private Tetromina[] tetromina = new Tetromina[7]; //7 razli�nih tetromin
	private Tetromina trenutnaTetromina;
	
	//spremenljivke za to�ke
	private int �teviloIzbrisanihVrstic = 0;
	private int trenutnaStopnja = 1;
	private int stopnja = 1;
	private int to�ke = 0;
	
	//vpeljemo �e zvo�ne efekte
	private static Zvok zvok = new Zvok();
	
	//konstruktor
	public Igralna_mre�a() {
		zvokThemeSong();
		random = new Random();
		
		tetromina[0] = new Tetromina(new int[][] {
			{1,1,1,1}
		},this, barve[0]);
		
		tetromina[1] = new Tetromina(new int[][] {
			{1,1,1},
			{0,1,0},
		},this, barve[1]);
		
		tetromina[2] = new Tetromina(new int[][] {
			{1,1,1},
			{1,0,0},
		},this, barve[2]);
		
		tetromina[3] = new Tetromina(new int[][] {
			{1,1,1},
			{0,0,1},
		},this, barve[3]);
		
		tetromina[4] = new Tetromina(new int[][] {
			{0,1,1},
			{1,1,0},
		},this, barve[4]);
		
		tetromina[5] = new Tetromina(new int[][] {
			{1,1,0},
			{0,1,1},
		},this, barve[5]);

		tetromina[6] = new Tetromina(new int[][] {
			{1,1},
			{1,1},
		},this, barve[6]);
		
		trenutnaTetromina = tetromina[0];
		
		premikDol= new Timer(zakasnitev, new ActionListener() { 
			//spodnje se bo zgodilo vsak trenutek na "zakasnitev" �as
			//ta �as je hitrej�i ob pritisku navzdol
			@Override
			public void actionPerformed(ActionEvent e) {
				premik(); //izvedemo premik, v y ali x smeri
				repaint(); //pokli�emo paintComponent ter tako refresh-amo				
			}
		});
		premikDol.start();
	}
	
	private void premik() {
		if(stanje == IGRAJ) {
			trenutnaTetromina.premik();
		}		
	}
	
	public void dolo�iTrenutnoTetomino() {
		trenutnaTetromina = tetromina[random.nextInt(tetromina.length)];
		//za�eten polo�aj
		trenutnaTetromina.nastaviNaZa�etneVrednosti();
		preveriKonecIgre();
		starPritisk = true;	
	}
	
	//sestavimo metodo, ki preveri ali je igra zaklju�ena
	private void preveriKonecIgre() {
		int[][] koordinate = trenutnaTetromina.vrniKoordinate();
		for(int vrstica = 0; vrstica < koordinate.length; vrstica++) {
			for(int stolpec = 0; stolpec < koordinate[0].length; stolpec++) {
				if(koordinate[vrstica][stolpec] != 0) {
					if(igralna_mre�a[vrstica + trenutnaTetromina.vrniY()][stolpec + trenutnaTetromina.vrniX()] != null) {
						stanje = KONEC_IGRE;
						pavzaThemeSong();
						zvokKonecIgre();
					}
				}
			}
		}
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.white);//barva ozadja
		g.fillRect(0, 0, getWidth(), getHeight());

		trenutnaTetromina.risanjeTetromine(g);
	
		//vsako tetromino obarva s svojo barvo
		for(int vrstica = 0; vrstica < igralna_mre�a.length; vrstica++) {
			for(int stolpec = 0; stolpec < igralna_mre�a[vrstica].length; stolpec++) {
				if (igralna_mre�a[vrstica][stolpec] != null) {
					g.setColor(igralna_mre�a[vrstica][stolpec]);
					g.fillRect(stolpec * VELIKOST_KVADRATKA, vrstica * VELIKOST_KVADRATKA, VELIKOST_KVADRATKA, VELIKOST_KVADRATKA);					
				}
			}
		}
				
		//RISANJE �RT MRE�E
		g.setColor(Color.black);
		//po vrsticah
		//1 dodamo, da dobimo zadnjo spodnjo vodoravno �rto
		for (int vrstica = 0; vrstica < VI�INA_IGRALNE_MRE�E + 1; vrstica++) {
			g.drawLine(0, VELIKOST_KVADRATKA * vrstica, VELIKOST_KVADRATKA * �IRINA_IGRALNE_MRE�E, VELIKOST_KVADRATKA * vrstica);
		}
		//po stolpcih
		//1 dodamo, da dobimo zadnjo desno navpi�no �rto
		for (int stolpec = 0; stolpec < �IRINA_IGRALNE_MRE�E + 1; stolpec++) {
			g.drawLine(stolpec * VELIKOST_KVADRATKA, 0, stolpec * VELIKOST_KVADRATKA, VELIKOST_KVADRATKA * VI�INA_IGRALNE_MRE�E);
		}
		
		//zapi�imo besedilo za �tevilo izbrisanih vrstic
		g.setColor(Color.blue);
        g.setFont(new Font("Georgia", Font.BOLD, 20));
        g.drawString("VRSTICE: ", 320, 320);
        g.drawString(�teviloIzbrisanihVrstic + "", 450, 320);
        
		//zapi�imo besedilo za stopnjo
		g.setColor(Color.blue);
        g.setFont(new Font("Georgia", Font.BOLD, 20));
        g.drawString("STOPNJA: ", 320, 350);
        g.drawString(stopnja + "", 450, 350);
        
		//zapi�imo besedilo za to�ke
		g.setColor(Color.blue);
        g.setFont(new Font("Georgia", Font.BOLD, 20));
        g.drawString("TO�KE: ", 320, 380);
        g.drawString(to�ke + "", 450, 380);
		
		//zapi�imo besedilo KONEC IGRE
		if (stanje == KONEC_IGRE) {
			g.setColor(Color.red);
			g.setFont(new Font("Georgia", Font.BOLD, 30));
			g.drawString("KONEC IGRE!", 310, 100);
			g.setFont(new Font("Georgia", Font.BOLD, 10));
			g.drawString("Pritisni SPACE za novo igro.", 320, 120);
		}
		
		//zapi�imo besedilo PAVZA
		if (stanje == PAVZA) {
			g.setColor(Color.red);
			g.setFont(new Font("Georgia", Font.BOLD, 30));
			g.drawString("PAVZA", 320, 100);
			g.setFont(new Font("Georgia",Font.BOLD, 10));
			g.drawString("Pritisni SPACE za nadaljevanje.", 320, 120);
		}
	}



	public Color[][] vrniIgralno_mre�o(){
		return igralna_mre�a;
	}
	
	//kaj se zgodi ob pritiskih na tipke
	@Override
	public void keyTyped(KeyEvent e) {}

	private boolean pritisk = false;
	private boolean starPritisk = false;
	
	@Override
	public void keyPressed(KeyEvent e) {
		//hitrej�i premik navzdol
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			//�e to tetromino prvi� pospe�i
			if (starPritisk == false) {
				pritisk = true;
				trenutnaTetromina.pospe�itev();
			}
		}
		//premik desno
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (pritisk == false){
			trenutnaTetromina.premikDesno();
			}
		}
		//premik levo
		else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(pritisk == false) {
			trenutnaTetromina.premikLevo();
			}
		}
		//premik navzgor je rotacija
		else if(e.getKeyCode() == KeyEvent.VK_UP) {
			if (pritisk == false) {
			trenutnaTetromina.rotacijaTetromine();
			}
		}
		
		//po koncu igre za�nemo novo z pritiskom na preslednico
		if(stanje == KONEC_IGRE) {
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				//pobri�emo mre�o
				for(int vrstica = 0; vrstica < igralna_mre�a.length; vrstica++) {
					for(int stolpec = 0; stolpec < igralna_mre�a[0].length; stolpec++) {
						igralna_mre�a[vrstica][stolpec] = null;
					}
				}
				//ponovno za�nemo igro
				�teviloIzbrisanihVrstic = 0;
				stopnja = 1;
				to�ke = 0;
				dolo�iTrenutnoTetomino();
				stanje = IGRAJ;
				zvokThemeSong();
			}
		}
		
		//ob pritisku na preslednico, postavimo igro na pavzo
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(stanje == IGRAJ) {
				stanje = PAVZA;
				pavzaThemeSong();
			} 
			else if(stanje == PAVZA) {
				stanje = IGRAJ;
				zvokThemeSong();
			}
		}
	}

	@Override
	//ob spustu tipke navzdol spet normalna hitrost padanja
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			starPritisk = false;
			pritisk = false;
			trenutnaTetromina.upo�asnitev();
		}
	}
	
	//metoda za predvajanje zvoka
	public static void zvokPo�i��eneVrstice() {
		zvok.predvajajZvokPo�istiVrstico();
	}	
	public static void zvokNoveStopnje() {
		zvok.predvajajZvokNovaStopnja() ;
	}
	public static void zvokKonecIgre() {
		zvok.predvajajZvokKonecIgre();
	}
	public static void zvokThemeSong() {
		zvok.predvajajZvokThemeSong();
	}
	public static void pavzaThemeSong() {
		zvok.pavzaZvokThemeSong();
	}
	
	
	//metoda za dolo�anje to�k
    public void dodajTo�ke() {
        �teviloIzbrisanihVrstic++;
        stopnja = (�teviloIzbrisanihVrstic / 10) + 1;
        if (trenutnaStopnja != stopnja) {
        	zvokNoveStopnje();
        	trenutnaStopnja = stopnja;
        }
        to�ke += stopnja * 40;  
        zvokPo�i��eneVrstice();
    }
    
	public int vrniStopnjo() {
		return stopnja;
	}

}
