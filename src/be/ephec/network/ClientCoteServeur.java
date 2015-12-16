package be.ephec.network;
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
}