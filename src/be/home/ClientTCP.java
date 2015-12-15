package be.home;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientTCP {

	public static void main(String[] args) {
		int numPort = 37500;
			Socket client;
			try {
				client = new Socket("127.0.0.1",numPort);
				ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
				oos.writeObject("Bonjour du client");
				System.out.println(ois.readObject());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	
	}

}
