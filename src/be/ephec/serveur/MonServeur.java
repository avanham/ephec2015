package be.ephec.serveur;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import be.ephec.batNav.Param;
public class MonServeur extends ServerSocket implements Runnable{
	private ArrayList<ClientCoteServeur> listeClients = new ArrayList<ClientCoteServeur>();
	private int nbClient = 0;
	private boolean jeu;
	private String[][] listBJ1 ;//= new String[Param.TAILLE_GRILLE+1][Param.TAILLE_GRILLE];
	private String[][] listBJ2 ;//= new String[Param.TAILLE_GRILLE+1][Param.TAILLE_GRILLE];
	private String j1; // garde en mémoire la dernière information reçu de joueur 1
	private String j2; // garde en mémoire la dernière information reçu de joueur 2
	private boolean t = true; // true -> joueur1 , false -> joueur2
	
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
				this.listeClients.add(new ClientCoteServeur(this.accept(), ++this.nbClient));
				System.out.println("Client n°"+this.nbClient+"> "+this.listeClients.get(this.nbClient-1).lire());
				for(ClientCoteServeur client: listeClients){
					client.ecrire("0nous avons un nouveau client - soit au total "+this.listeClients.size()); 
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
				if(!jeu){
					this.listBJ1 = (String[][]) listeClients.get(0).lire();
					this.listBJ2 = (String[][]) listeClients.get(1).lire();
					//System.out.println(this.listBJ1[8][0]+""+this.listBJ2[8][0]);
					if(this.listBJ1[8][0] != null & this.listBJ2[8][0] != null){
						if(Integer.parseInt(this.listBJ1[8][0]) == 1 & Integer.parseInt(this.listBJ2[8][0]) == 2){
							this.jeu = true; // le jeu peut commencer, le serveur a reçu les 2 listes
							this.listeClients.get(0).ecrire("2Vous pouvez commencer");
							this.listeClients.get(1).ecrire("Joueur 1 commence");
							listeClients.get(1).runListener();
							listeClients.get(0).runListener();
						}
					}
				} else{
					if(this.t){ // Joueur 1
						listeClients.get(0).setTour(true);
						String recu = listeClients.get(0).getRecu();
						if(recu != this.j1){
							String batEau = listBJ2[Integer.parseInt(recu.substring(0, 1))][Integer.parseInt(recu.substring(1, 2))];
							if (batEau == null){
								this.listeClients.get(0).ecrire("1 dans l'eau");
								this.listeClients.get(1).ecrire("3"+recu.substring(0, 1)+recu.substring(1 ,2));
							}else{
								this.listeClients.get(0).ecrire("1 sur le "+batEau);
								this.listeClients.get(1).ecrire("4"+recu.substring(0, 1)+recu.substring(1 ,2));
							}
							this.listeClients.get(1).ecrire("2C'est à votre tour");
							this.j1 = recu;
							this.t = false;
							listeClients.get(0).setTour(false);
						}
					}
					else{ // Joueur 2
						listeClients.get(1).setTour(true);
						String recu = listeClients.get(1).getRecu();
						if(recu != this.j2){
							String batEau = listBJ1[Integer.parseInt(recu.substring(0, 1))][Integer.parseInt(recu.substring(1, 2))];
							if (batEau == null){
								this.listeClients.get(1).ecrire("1 dans l'eau");
								this.listeClients.get(0).ecrire("3"+recu.substring(0, 1)+recu.substring(1 ,2));
							}else{
								this.listeClients.get(1).ecrire("1 sur le "+batEau);
								this.listeClients.get(0).ecrire("4"+recu.substring(0, 1)+recu.substring(1 ,2));
							}
							this.listeClients.get(0).ecrire("2C'est à votre tour");
							this.j2 = recu;
							this.t = true;
							listeClients.get(1).setTour(false);
						}
					}
				}
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}