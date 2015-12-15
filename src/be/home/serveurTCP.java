package be.home;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class serveurTCP {

	public static void main(String[] args) {
			ServerSocket monSS;
			try {
				monSS = new ServerSocket(37500);
				System.out.println("Serveur à lécoute sur le port "+monSS.getLocalPort());
				Socket socket = monSS.accept();
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				System.out.println("Message bien reçu : \""+ ois.readObject()+"\"");
				oos.writeObject("message recu");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}



	}
	
	public void acceptePlusieursClients(){
		
	}
	
	public void run(){
		
	}

}
