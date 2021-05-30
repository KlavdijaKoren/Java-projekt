package Tetris;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Zvok {
	private String mapaZzvokom = "zvok" + File.separator;
	private String po�istiVrstico = mapaZzvokom + "vrstica.wav"; //lokacija datoteke za zvok prazne vrstice
	private String novaStopnja = mapaZzvokom + "novaStopnja.wav"; //lokacija datoteke za zvok nove stopnje
	private String konecIgre = mapaZzvokom + "konecIgre.wav"; //lokacija datoteke za zvok konca igre
	
	private String themeSong = mapaZzvokom + "themeSong.wav"; //lokacija datoteke za zvok konca igre
	
	private Clip zvokPo�istiVrstico;
	private Clip zvokNovaStopnja;
	private Clip zvokKonecIgre;
	private Clip zvokThemeSong;
	
	
	public Zvok() {
		//dodati moramo getClip metodo, ki mora vsebovati try in catch error del
		try {
			zvokPo�istiVrstico = AudioSystem.getClip();
			//da lahko predvajamo zvok z Clip objektom, moramo dodati mape z zvoki
			//k objektom zvoka tako, da uvedemo novo opem metodo tem objektom
			//dodati moramo polno ime datoteke z zvokom
			zvokPo�istiVrstico.open(AudioSystem.getAudioInputStream(new File(po�istiVrstico).getAbsoluteFile()));
			
			zvokNovaStopnja = AudioSystem.getClip();
			zvokNovaStopnja.open(AudioSystem.getAudioInputStream(new File(novaStopnja).getAbsoluteFile()));
			
			zvokKonecIgre = AudioSystem.getClip();
			zvokKonecIgre.open(AudioSystem.getAudioInputStream(new File(konecIgre).getAbsoluteFile()));
			
			zvokThemeSong = AudioSystem.getClip();
			zvokThemeSong.open(AudioSystem.getAudioInputStream(new File(themeSong).getAbsoluteFile()));
			
		} catch (LineUnavailableException e) {
			Logger.getLogger(Zvok.class.getName()).log(Level.SEVERE, null,e);
		} catch (UnsupportedAudioFileException e) {
			//�e program ne more predvajati datoteke z zvokom
			Logger.getLogger(Zvok.class.getName()).log(Level.SEVERE, null,e);
		} catch (IOException e) {
			//�e program ne najde datoteke z zbokom
			Logger.getLogger(Zvok.class.getName()).log(Level.SEVERE, null,e);
		}
	}
	
	public void predvajajZvokPo�istiVrstico() {
		zvokPo�istiVrstico.setFramePosition(0); //vedno predvaja zvok od za�etka
		zvokPo�istiVrstico.start();
	}
	
	public void predvajajZvokNovaStopnja() {
		zvokNovaStopnja.setFramePosition(0); //vedno predvaja zvok od za�etka
		zvokNovaStopnja.start();
	}
	
	public void predvajajZvokKonecIgre() {
		zvokKonecIgre.setFramePosition(0);
		zvokKonecIgre.start();
	}
	
	public void predvajajZvokThemeSong() {
		zvokThemeSong.start();
		zvokThemeSong.loop(zvokThemeSong.LOOP_CONTINUOUSLY);
	}
	
	public void pavzaZvokThemeSong() {
		zvokThemeSong.stop();
	}
}	


