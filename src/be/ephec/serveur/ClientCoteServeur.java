package be.ephec.serveur;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class ClientCoteServeur {
	private Socket socket;
	private ObjectInput ois;
	private ObjectOutput oos;
	private int num;
	private boolean tour;
	private String recu;
	
	public String getRecu() {
		return recu;
	}

	public void setRecu(String recu) {
		this.recu = recu;
	}

	public ClientCoteServeur(Socket socket, int num) throws IOException{
		this.socket = socket;
		this.num = num;
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		oos.writeObject(num);
	}
	
	public void ecrire(Object o){
		try {
			oos.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Object lire() throws ClassNotFoundException, IOException{
		return ois.readObject();
	}
	
	public void runListener() {
		Runnable r = new Runnable() {
	        public void run() {
	       	 try {
	       		 while(!socket.isClosed()){
	        		 String retour = (String) lire();
	        		 if(isTour()){
	        			 setRecu(retour);
	        		 }else if(retour.equalsIgnoreCase("close")){
	        			 socket.close();
	        		 }
					 //System.out.println(retour);
	       		 	}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    };
	    new Thread(r).start();
	}

	public boolean isTour() {
		return tour;
	}

	public void setTour(boolean tour) {
		this.tour = tour;
	}
	
	
}