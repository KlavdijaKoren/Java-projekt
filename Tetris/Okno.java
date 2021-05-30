package Tetris;

import javax.swing.JFrame;

public class Okno extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final int ŠIRINA_OKNA =550, VIŠINA_OKNA = 640;
	private Igralna_mreža igralna_mreža;
		
	//KONSTRUKTOR
	public Okno() {
		super("Tetris");
		this.setSize(ŠIRINA_OKNA, VIŠINA_OKNA);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //konèa se z zaprtjem okna
		this.setResizable(false); //ne moremo roèno spreminjati velikosti okna z miško
		this.setLocationRelativeTo(null);//zaèetno okno se pojavi v centru ekrana
		
		igralna_mreža = new Igralna_mreža();
		this.add(igralna_mreža);
		this.addKeyListener(igralna_mreža);
		this.setVisible(true);
	}


	public static void main(String[] args) {
		new Okno();
	}
}
