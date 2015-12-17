package be.ephec.batNav;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JLabel;

public class Calcul {/*
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private JLabel jLabMess;
	private static int player;
	private Socket s;
	public Core(Socket s,ObjectInputStream ois, ObjectOutputStream oos, JLabel jMess, int joueur) {
		this.ois = ois;
		this.oos = oos;
		this.jLabMess = jMess;
		this.player = joueur;
		this.s = s;
		
	}
	*/
	public Calcul(){
		
	}
	
	public int getClickX(int xRec){
		return (int) Math.floor((xRec-24)/33.125); // calcule la case X sélectionné
	}
	
	public int getClickY(int yRec){
		return (int)  ((yRec-24)/30);  // calcule la case Y sélectionné
	}
	
	public int getCaseX(int x){
		return (int) (x*33.125+25);  // calcule l'endroit de la case X en pixels
	}
	
	public int getCaseY(int y){
		return (int) (y * 30+19);  // calcule l'endroit de la case Y en pixels
	}
}
