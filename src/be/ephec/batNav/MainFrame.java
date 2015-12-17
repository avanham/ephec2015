package be.ephec.batNav;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import be.ephec.serveur.MonServeur;
public class MainFrame extends JFrame implements MouseListener, ActionListener{
	private boolean jeu = false;
	private JPanel contentPane;
	private String[][] listeButtons = new String[Param.TAILLE_GRILLE+1][Param.TAILLE_GRILLE]; // +1 pour indiquer le joueur
	protected int batSelected; // pour le bateau sélectionné
	protected ImagePanel[] bat = new ImagePanel[3]; // pour les images des 3 bateaux
	protected ImagePanel[][] touch = new ImagePanel[8][8]; 
	protected ImagePanel[][] eau = new ImagePanel[8][8];
	protected ImagePanel[][] touchAdv = new ImagePanel[8][8];
	protected ImagePanel[][] eauAdv = new ImagePanel[8][8];
	protected ImagePanel grilleBas;
	protected ImagePanel grilleTop;
	protected JLabel jLabMess;
	protected int listBatPlacee[] = new int[6]; // liste provisoire, pour placer les bateaux (cache)
	private JButton btnDmarServ;
	protected int port;
	private JButton btnConnecterAuServeur;
	private String ip;
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private int player = 1;
	private Calcul c;
	private String lett = "ABCDEFGH"; // pour indiquer la ligne de la grille
	private int x;
	private int y;
	private boolean tour;
	private int nbTouch;
	private int nbTouchAdv;
	
	/**
	 * Lancer l'application
	 * @param args n'a pas besoin d'arguments 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Création de la frame
	 */
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Bataille navale");
		setBounds(800, 10, 544, 708);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.c = new Calcul();
		
		Image img = null; // image de grille
		Image[] imges = new Image[3]; // images des bateaux
		Image imgEau = null;
		Image imgTouch = null;
		try { //importe les images
			img = ImageIO.read(new File("./img/grille.jpg"));
			imges[0] = ImageIO.read(new File("./img/b2.jpg"));
			imges[1] = ImageIO.read(new File("./img/b3.jpg"));
			imges[2] = ImageIO.read(new File("./img/b4.jpg"));
			imgEau = ImageIO.read(new File("./img/eau.jpg"));
			imgTouch = ImageIO.read(new File("./img/touch.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// une boucle qui crée tout les croix et points qui vont être visible quand on clicke dans une case pendant la bataille
		for(int i=0; i<8; i++){
			for(int a=0; a<8; a++){
				int coordX = c.getCaseX(i);
				int coordY = c.getCaseY(a);
				touch[i][a] = new ImagePanel(imgTouch);
				eau[i][a] = new ImagePanel(imgEau);
				touch[i][a].setBounds(Param.DEBUT_X_GRILLE_TOP+coordX, Param.DEBUT_Y_GRILLE_TOP+coordY, 33, 33);
				eau[i][a].setBounds(Param.DEBUT_X_GRILLE_TOP+coordX, Param.DEBUT_Y_GRILLE_TOP+coordY, 33, 33);
				touch[i][a].setVisible(false);
				eau[i][a].setVisible(false);
				contentPane.add(touch[i][a]);
				contentPane.add(eau[i][a]);
				
				touchAdv[i][a] = new ImagePanel(imgTouch);
				eauAdv[i][a] = new ImagePanel(imgEau);
				touchAdv[i][a].setBounds(Param.DEBUT_X_GRILLE_BAS+coordX, Param.DEBUT_Y_GRILLE_BAS+coordY, 33, 33);
				eauAdv[i][a].setBounds(Param.DEBUT_X_GRILLE_BAS+coordX, Param.DEBUT_Y_GRILLE_BAS+coordY, 33, 33);
				touchAdv[i][a].setVisible(false);
				eauAdv[i][a].setVisible(false);
				contentPane.add(touchAdv[i][a]);
				contentPane.add(eauAdv[i][a]);
			}
		}
		
		//création des 3 bateaux
		for(int i=0; i<3;i++){
			bat[i] = new ImagePanel(imges[i]);
			bat[i].setBounds(357, 411+(i*40), 66+(i*33), 34);
			contentPane.add(bat[i]);
		}
		
		jLabMess = new JLabel("Bienvenue, créez/connectez vous au serveur");
		jLabMess.setBackground(Color.WHITE);
		jLabMess.setBounds(10, 21, 300, 41);
		jLabMess.setAlignmentY(CENTER_ALIGNMENT);
		contentPane.add(jLabMess);
		
		grilleTop = new ImagePanel(img);
		grilleTop.setBounds(Param.DEBUT_X_GRILLE_TOP, Param.DEBUT_Y_GRILLE_TOP, Param.LARGEUR_X_GRILLE, Param.LARGEUR_Y_GRILLE);
		contentPane.add(grilleTop);
		
		grilleBas = new ImagePanel(img);
		grilleBas.setBounds(Param.DEBUT_X_GRILLE_BAS, Param.DEBUT_Y_GRILLE_BAS, Param.LARGEUR_X_GRILLE, Param.LARGEUR_Y_GRILLE);
		contentPane.add(grilleBas);
		
		JButton btnNouvellePartie = new JButton("Nouvelle Partie");
		btnNouvellePartie.setBounds(357, 73, 145, 34);
		btnNouvellePartie.setActionCommand("nPart");
		btnNouvellePartie.addActionListener(this);
		contentPane.add(btnNouvellePartie);
		
		JButton btnArrter = new JButton("Arr\u00EAter");
		btnArrter.setBounds(357, 258, 145, 34);
		btnArrter.setActionCommand("arret");
		btnArrter.addActionListener(this);
		contentPane.add(btnArrter);
		
		JButton btnOk = new JButton("Pr\u00EAt");
		btnOk.setBounds(358, 213, 144, 34);
		btnOk.setActionCommand("pret");
		btnOk.addActionListener(this);
		contentPane.add(btnOk);
		
		btnDmarServ = new JButton("Nouveau serveur");
		btnDmarServ.addActionListener(this);
		btnDmarServ.setActionCommand("serveur");
		btnDmarServ.setBounds(357, 118, 145, 34);
		contentPane.add(btnDmarServ);
		
		btnConnecterAuServeur = new JButton("Connecter au serveur");
		btnConnecterAuServeur.addActionListener(this);
		btnConnecterAuServeur.setActionCommand("connect");
		btnConnecterAuServeur.setBounds(357, 163, 145, 34);
		contentPane.add(btnConnecterAuServeur);
	}
	/**
	 * Prend en charge les actions qu'il faut faire quand-on click sur un bateau ou dans une grille
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		int elem = e.getComponent().getSize().width; // pas très propre, mais permet d'identifier la frame
		if(elem == 300){ // les grilles ont une largeur de 300 px
			this.x = this.c.getClickX(e.getX());
			this.y = this.c.getClickY(e.getY());
			if(jeu){ // quand le jeu a commencé:
				try {
					if(tour){ // si c'est à son tour
						if(this.listeButtons[y][x] == null){ // vérifie, si on a pas déja clické sur la case
							this.oos.writeObject(x+""+y); // envoit le x et y de la case
							this.jLabMess.setText("Vous avez tiré sur "+this.lett.charAt(y)+" "+(x+1));
							this.tour = false; // pour finir son tour
						}
						else{
							this.jLabMess.setText("Vous avez déja tiré sur "+this.lett.charAt(y)+" "+(x+1));
						}
					}else{
						this.jLabMess.setText("C'est pas à votre tour, attendez");
					}
				}catch (IOException e2) {
					e2.printStackTrace();
				}
			}else{ // sinon on place les bateaux
				int coordX = this.c.getCaseX(x);
				int coordY = this.c.getCaseY(y);
				if ( x>=0 && x<=6-this.batSelected && y>=0 && y<=7){ // vérifie que les bateaux ne sortent pas de la grille
					this.bat[this.batSelected].setBounds((int) coordX + Param.DEBUT_X_GRILLE_BAS,(int) coordY + Param.DEBUT_Y_GRILLE_BAS, (this.batSelected*33)+66, 34); //les place dans la grille
					this.listBatPlacee[2*this.batSelected] = x; // les mets dans la liste provisoire, pour si jamais il y a un changement
					this.listBatPlacee[(2*this.batSelected)+1] = y;
				}else{
					this.jLabMess.setText("Vous êtes sorti de la grille");
				}
			}
		}else { // sinon c'est un bateau
			batSelected = (elem-66)/33; // les bateaux ont une taille que se différencient de 33 pixels
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	/**
	 * Prend en charge les actions qu'il faut faire quand-on click sur les bouttons du menu
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
		case "arret":
			try {
				this.oos.writeObject("close");
				this.s.close();
			} catch (IOException e3) {
				e3.printStackTrace();
			}
			this.dispose();
			break;
		case "pret":
			this.grilleBas.removeMouseListener(this);
			for(int i=0; i<3;i++){
				bat[i].removeMouseListener(this);
			}
			this.grilleTop.addMouseListener(this);
			this.jeu = true;
			
			// place les bateaux dans la liste principale, pour l'envoyer au serveur
			for(int i=0; i<2; i++){
				this.listeButtons[this.listBatPlacee[0]+i][this.listBatPlacee[1]] = "bateau2";
			}
			for(int i=0; i<3; i++){
				this.listeButtons[this.listBatPlacee[2]+i][this.listBatPlacee[3]] = "bateau3";
			}
			for(int i=0; i<4; i++){
				this.listeButtons[this.listBatPlacee[4]+i][this.listBatPlacee[5]] = "bateau4";
			}
			this.listeButtons[8][0] = ""+this.player;
			
			try {
				this.oos.writeObject(this.listeButtons);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
			this.listeButtons = new String [Param.TAILLE_GRILLE+1][Param.TAILLE_GRILLE]; // vider la liste, pour placer les résultats obtenus
			
			this.ecouter();
			this.jLabMess.setText("Attendez le 2ème joueur");
			break;
		case "nPart":
			this.listeButtons = new String[Param.TAILLE_GRILLE+1][Param.TAILLE_GRILLE];
			this.grilleBas.addMouseListener(this);
			this.grilleTop.removeMouseListener(this);
			this.jeu = false;
			for(int i=0; i<3;i++){
				bat[i].setBounds(357, 411+(i*40), 66+(i*33), 34);
				bat[i].addMouseListener(this);
			}
			for(int i=0; i<8; i++){
				for(int a=0; a<8; a++){
					touch[i][a].setVisible(false);
					eau[i][a].setVisible(false);
					touchAdv[i][a].setVisible(false);
					eauAdv[i][a].setVisible(false);
				}
			}
			break;
		case "serveur":
			this.port = Integer.parseInt((String) JOptionPane.showInputDialog(this, "Numéro de port :", "Serveur", JOptionPane.QUESTION_MESSAGE, null, null, Param.NUMPORTDEBASE));
			try {
				this.creeServeur(this.port);
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case "connect":
			this.ip = (String) JOptionPane.showInputDialog(this, "Numéro d'ip :", "Connexion", JOptionPane.QUESTION_MESSAGE, null, null, "127.0.0.1");
			this.port = Integer.parseInt((String) JOptionPane.showInputDialog(this, "Numéro de port :", "Serveur", JOptionPane.QUESTION_MESSAGE, null, null, Param.NUMPORTDEBASE));
			try {
				this.connectServer(this.ip, this.port);
				player = (int) this.ois.readObject(); // reçoit directement son numéro de joueur
				this.setTitle("Joueur n°"+player);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			for(int i=0; i<3;i++){
				bat[i].addMouseListener(this);
			}
			this.grilleBas.addMouseListener(this);
			this.jLabMess.setText("Vous pouvez placer vos bateaux");
			break;
		}
		
	}
	
	/**
	 * @param numPort est le numéro de port avec lequel le serveur va établir la connexion
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void creeServeur(final int numPort) throws IOException, ClassNotFoundException{
		Runnable r = new Runnable() {
	         public void run() {
	        	 try {
					try {
						MonServeur ms = new MonServeur(numPort);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
	         }
	     };
	     new Thread(r).start();
	}
	
	/**
	 * @param ip numéro d'ip avec lequel on se connecte au serveur
	 * @param port numéro de port avec lequel on se connecte au serveur
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void connectServer(String ip, int port) throws UnknownHostException, IOException{
		this.s = new Socket(ip, port);
		this.ois = new ObjectInputStream(s.getInputStream());
		this.oos = new ObjectOutputStream(s.getOutputStream());
		this.oos.writeObject("Connexion");
	}
	
	/**
	 * 
	 * @return renvoi tout ce qu'il reçoit du input stream
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public Object lire() throws ClassNotFoundException, IOException{
		return ois.readObject();
	}
	
	/**
	 * 
	 * @param text place le texte dans le jLabel de la fenêtre principale
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void ecrire(String text) throws ClassNotFoundException, IOException{
		this.jLabMess.setText(text);
	}
	
	/**
	 * crée un nouveau Thread dans lequel il écoute le serveur et traite les signaux comme demandé
	 */
	public void ecouter(){
		Runnable r = new Runnable() {
	         public void run() {
	        	 try {
	        		 while(!s.isClosed()){
		        		 String retour = (String) lire();
						 if(retour.substring(0, 1).equalsIgnoreCase("0")){ 
							 // 0 -> message pas important
						 }else if(retour.substring(0, 1).equalsIgnoreCase("1")){ // 1 -> message de bataille
							 ecrire("Vous avez tiré"+retour.substring(1));
							 listeButtons[y][x] = retour.substring(1);
							 if(retour.substring(2, 3).equalsIgnoreCase("s")){ // s -> sur un bateau -> X
								 touch[x][y].setVisible(true);
								 nbTouch++;
								 if (nbTouch == 9){
									 ecrire("close");
									 ecrire("Vous avez gagné");
									 s.close();
								 }
							 }else if(retour.substring(2, 3).equalsIgnoreCase("d")){ //d -> dans l'eau -> O
								 eau[x][y].setVisible(true);
							 }
						 }else if(retour.substring(0, 1).equalsIgnoreCase("2")){ // 2 -> message de tour
							 ecrire(retour.substring(1));
							 tour = true;
						 }else if(retour.substring(0, 1).equalsIgnoreCase("3")){ // 3 -> l'adversaire a tiré dans l'eau
							 eauAdv[Integer.parseInt(retour.substring(1, 2))][Integer.parseInt(retour.substring(2, 3))].setVisible(true);
						 }else if(retour.substring(0, 1).equalsIgnoreCase("4")){ // 4 -> l'adversaire a tiré sur un bateau
							 touchAdv[Integer.parseInt(retour.substring(1, 2))][Integer.parseInt(retour.substring(2, 3))].setVisible(true);
							 nbTouchAdv++;
							 if (nbTouchAdv == 9){
								 ecrire("close");
								 ecrire("Vous avez perdu");
								 s.close();
							 }
						 }
						 else{
							 ecrire(retour);
						 }
	        		 }
	        	}catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
	     };
	     new Thread(r).start();
	}
}