package be.ephec.network;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import be.ephec.projet.shipBattle.Param;
public class MonServeur extends ServerSocket implements Runnable{
	private ArrayList<ClientCoteServeur> listeClients = new ArrayList<ClientCoteServeur>();
	private int nbClient = 0;
	
	public MonServeur() throws IOException, ClassNotFoundException{
		super(Param.NUMPORTDEBASE);
		System.out.println("Se serveur ecoute sur le port "+this.getLocalPort());
		acceptePlusieursClient();
	}
	
	public MonServeur(int numPort) throws IOException, ClassNotFoundException{
		super(numPort);
		System.out.println("Se serveur ecoute sur le port "+this.getLocalPort());
		acceptePlusieursClient();
		
	}
	
	public void ecouter(){
		Runnable r = new Runnable() {
	         public void run() {
	        	 try {
					System.out.println(listeClients.get(0).lire());
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
	
	public static MonServeur getServeurPortLibre() throws ClassNotFoundException{
		MonServeur ms = null;
		for(int numPort = Param.NUMPORTDEBASE; numPort <=65535; numPort++){
			try{
				ms = new MonServeur(numPort);
				ms.acceptePlusieursClient();
				break;
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return ms;
	}
	
	public static MonServeur getServeurPortLibre(int numPortDepart) throws ClassNotFoundException{
		MonServeur ms = null;
		for(int numPort = numPortDepart; numPort <=65535; numPort++){
			try{
				ms = new MonServeur(numPort);
				ms.acceptePlusieursClient();
				break;
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return ms;
	}
	
	public static void main(String[] args) throws ClassNotFoundException{
		MonServeur ms = MonServeur.getServeurPortLibre();
	}
	
	public void acceptePlusieursClient() throws ClassNotFoundException, IOException{
		while(this.nbClient < Param.NB_JOUEURS){
			try {
				this.listeClients.add(new ClientCoteServeur(this.accept(), ++nbClient));
				System.out.println("Client n°"+nbClient+"> "+this.listeClients.get(nbClient-1).lire());
				for(ClientCoteServeur client: listeClients){
					client.ecrire(this.listeClients.size()); //"nous avons un nouveau client - soit au total "+
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		this.run();
	}
	@Override
	public void run() {
		while(!this.isClosed()){
			try {
				int i=0;
				for(ClientCoteServeur client: listeClients){
					System.out.println("Client n°"+i+1+"> "+client.lire());
					i++;
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}