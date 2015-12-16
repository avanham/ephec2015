package be.ephec.projet.shipBattle;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import be.ephec.network.MonServeur;
public class MainFrame extends JFrame implements MouseListener, ActionListener{
	private boolean jeu = false;
	private JPanel contentPane;
	private String[][] listeButtons = new String[Param.TAILLE_GRILLE][Param.TAILLE_GRILLE];
	protected int batSelected;
	protected ImagePanel bat2 = null;
	protected ImagePanel bat3 = null;
	protected ImagePanel bat4 = null;
	protected ImagePanel grilleBas;
	protected ImagePanel grilleTop;
	protected JLabel jLabMess;
	protected int listBatPlacee[] = new int[6];
	private JButton btnDmarServ;
	protected int port;
	private JButton btnConnecterAuServeur;
	private String ip;
	protected Socket s;
	protected ObjectInputStream ois;
	protected ObjectOutputStream oos;
	private int player;
	
	/**
	 * Lancer l'application
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
	//@SuppressWarnings("deprecation")
	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Bataille navale");
		setBounds(100, 10, 544, 708);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//BufferedImage img = null;
		Image img = null;
		Image imgB2 = null;
		Image imgB3 = null;
		Image imgB4 = null;
		
		try {
			img = ImageIO.read(new File("img/grille.jpg"));
			imgB2 = ImageIO.read(new File("img/b2.jpg"));
			imgB3 = ImageIO.read(new File("img/b3.jpg"));
			imgB4 = ImageIO.read(new File("img/b4.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		this.bat2 = new ImagePanel(imgB2);
		this.bat3 = new ImagePanel(imgB3);
		this.bat4 = new ImagePanel(imgB4);
		
		jLabMess = new JLabel("");
		jLabMess.setBackground(Color.WHITE);
		jLabMess.setBounds(10, 21, 300, 41);
		jLabMess.setAlignmentY(CENTER_ALIGNMENT);
		contentPane.add(jLabMess);
		
		grilleTop = new ImagePanel(img);
		grilleTop.setBounds(Param.DEBUT_X_GRILLE_TOP, Param.DEBUT_Y_GRILLE_TOP, Param.LARGEUR_X_GRILLE, Param.LARGEUR_Y_GRILLE);
		grilleTop.setName("gTop");
		contentPane.add(grilleTop);
		
		grilleBas = new ImagePanel(img);
		grilleBas.setBounds(Param.DEBUT_X_GRILLE_BAS, Param.DEBUT_Y_GRILLE_BAS, Param.LARGEUR_X_GRILLE, Param.LARGEUR_Y_GRILLE);
		grilleBas.setName("gBas");
		grilleBas.setActionMap(null);
		grilleBas.addMouseListener(this);
		contentPane.add(grilleBas);
		
		bat2.setBounds(357, 411, 66, 34);
		bat2.addMouseListener(this);
		contentPane.add(bat2);
		
		bat3.setBounds(357, 451, 99, 34);
		bat3.addMouseListener(this);
		contentPane.add(bat3);
		
		bat4.setBounds(357, 491, 132, 34);
		bat4.addMouseListener(this);
		contentPane.add(bat4);
		
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
		btnOk.setActionCommand("ok");
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
	@Override
	public void mouseClicked(MouseEvent e) {
		//System.out.println("x: "+e.getX()+"\ny: "+e.getY());
		int elem = e.getComponent().getSize().width; // pas très propre, mais permet d'identifier la frame
		if(elem == 300){ // les grilles ont une largeur de 300 px
			int x = (int) Math.floor((e.getX()-24)/33.125); // calcule la case X sélectionné
			int y = ((e.getY()-24)/30);  // calcule la case Y sélectionné
			if(jeu){
				try {
					this.oos.writeObject(player+""+x+""+y);
					this.setComponentZOrder(jLabMess, 0);
					this.jLabMess.setText(x+""+y);
					//this.bat3.setComponentZOrder(comp, index);
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					this.oos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}else{
			
				if (this.batSelected == 66){ // bat2
					if ( x>=0 && x<=6 && y>=0 && y<=7){
						this.bat2.setBounds((int) (x*33.125+25),(int) (y * 30+19), 66, 34);
						this.grilleBas.setComponentZOrder(this.bat2, 0);
						this.listBatPlacee[0] = x;
						this.listBatPlacee[1] = y;
					}else{
						JOptionPane.showMessageDialog(this, "Vous êtes sori de la grille", "erreur", JOptionPane.ERROR_MESSAGE);
					}
				}	
				if (this.batSelected == 99){ // bat3
					if ( x>=0 && x<=5 && y>=0 && y<=7){
						this.bat3.setBounds((int) (x*33.125+25),(int) ((y * 30+19)), 99, 34);
						this.grilleBas.setComponentZOrder(this.bat3, 0);
						this.listBatPlacee[2] = x;
						this.listBatPlacee[3] = y;
					}else{
						//System.out.println("Vous êtes sori de la grille");
						JOptionPane.showMessageDialog(this, "Vous êtes sori de la grille", "erreur", JOptionPane.ERROR_MESSAGE);//("Vous êtes sori de la grille");
						//this.jLabMess.setText("Vous êtes sori de la grille");
						//this.repaint();
					}
				}
				if (this.batSelected == 132){ // bat4
					if ( x>=0 && x<=4 && y>=0 && y<=7){
						this.bat4.setBounds((int) (x*33.125+25),(int) (y * 30+19), 132, 34);
						this.grilleBas.setComponentZOrder(this.bat4, 0);
						this.listBatPlacee[4] = x;
						this.listBatPlacee[5] = y;
					}else{
						JOptionPane.showMessageDialog(this, "Vous êtes sori de la grille", "erreur", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}else { // sinon c'est un bateau
			batSelected = elem;
			//System.out.println("bateau"+elem/33);
		}
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent arg0) {
		switch (arg0.getActionCommand()) {
		case "arret":
			System.out.println("arret");
			//this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
			break;
		case "ok":
			this.grilleBas.removeMouseListener(this);
			this.bat2.removeMouseListener(this);
			this.bat3.removeMouseListener(this);
			this.bat4.removeMouseListener(this);
			this.grilleTop.addMouseListener(this);
			this.jeu = true;
			//this.batSelected=0;
			for(int i=0; i<2; i++){
				System.out.println(this.listBatPlacee[0]+i);
				this.listeButtons[this.listBatPlacee[0]+i][this.listBatPlacee[1]] = "bat2";
			}
			for(int i=0; i<3; i++){
				this.listeButtons[this.listBatPlacee[2]+i][this.listBatPlacee[3]] = "bat3";
			}
			for(int i=0; i<4; i++){
				this.listeButtons[this.listBatPlacee[4]+i][this.listBatPlacee[5]] = "bat4";
			}
			for (int l=0; l<8; l++){
				for (int c=0; c<8; c++){
					System.out.println(this.listeButtons[c][l]);
				}
			}
			
			try {
				System.out.println(this.listeButtons[0][0]);
				System.out.println(this.listeButtons);
				this.oos.writeObject(this.listeButtons);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			// envoyer la liste au serveur
			
			//this.listeButtons = new String [Param.TAILLE_GRILLE][Param.TAILLE_GRILLE]; // vider la liste, pour placer les résultats obtenus
			break;
		case "nPart":
			System.out.println("nouvelle Partie");
			this.listeButtons = new String[Param.TAILLE_GRILLE][Param.TAILLE_GRILLE];
			this.grilleBas.addMouseListener(this);
			this.bat2.addMouseListener(this);
			this.bat3.addMouseListener(this);
			this.bat4.addMouseListener(this);
			this.grilleTop.removeMouseListener(this);
			this.jeu = false;
			
			//this.bat2.setBounds(357, 411, 66, 34);
			//this.bat3.setBounds(357, 451, 99, 34);
			//this.bat4.setBounds(357, 491, 132, 34);
			
			break;
		case "serveur":
			this.port = Integer.parseInt((String) JOptionPane.showInputDialog(this, "Numéro de port :", "Serveur", JOptionPane.QUESTION_MESSAGE, null, null, Param.NUMPORTDEBASE));
			try {
				this.creeServeur(this.port);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "connect":
			this.ip = (String) JOptionPane.showInputDialog(this, "Numéro d'ip :", "Connexion", JOptionPane.QUESTION_MESSAGE, null, null, "127.0.0.1");
			this.port = Integer.parseInt((String) JOptionPane.showInputDialog(this, "Numéro de port :", "Serveur", JOptionPane.QUESTION_MESSAGE, null, null, Param.NUMPORTDEBASE));
			try {
				this.connectServer(this.ip, this.port);
				player = (int) this.ois.readObject();
				this.setTitle("Joueur n°"+player);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
	}
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
	public void connectServer(String ip, int port) throws UnknownHostException, IOException{
		this.s = new Socket(ip, port);
		this.ois = new ObjectInputStream(s.getInputStream());
		this.oos = new ObjectOutputStream(s.getOutputStream());
		//new Thread( new Capture(outSock)).start();
		//new Thread( new PlayAudio(inSock)).start();
		this.oos.writeObject("Connexion");
		//System.out.println(this.ois.read());
	}
}