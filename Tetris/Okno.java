package Tetris;

import javax.swing.JFrame;

public class Okno extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final int �IRINA_OKNA =550, VI�INA_OKNA = 640;
	private Igralna_mre�a igralna_mre�a;
		
	//KONSTRUKTOR
	public Okno() {
		super("Tetris");
		this.setSize(�IRINA_OKNA, VI�INA_OKNA);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //kon�a se z zaprtjem okna
		this.setResizable(false); //ne moremo ro�no spreminjati velikosti okna z mi�ko
		this.setLocationRelativeTo(null);//za�etno okno se pojavi v centru ekrana
		
		igralna_mre�a = new Igralna_mre�a();
		this.add(igralna_mre�a);
		this.addKeyListener(igralna_mre�a);
		this.setVisible(true);
	}


	public static void main(String[] args) {
		new Okno();
	}
}
