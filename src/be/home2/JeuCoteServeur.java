package be.home2;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class JeuCoteServeur extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JeuCoteServeur frame = new JeuCoteServeur();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		ServerSocket monSS;
		try {
			monSS = new ServerSocket(37500);
			System.out.println("Serveur à lécoute sur le port "+monSS.getLocalPort());
			Socket socket = monSS.accept();
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("Message bien reçu "+ois.readObject());
			oos.writeObject("message recu");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public JeuCoteServeur() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 10, 544, 708);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//URL de l'image
	    //String imgUrl="img/grille.jpg";
	    //ImageIcon icone = new ImageIcon(imgUrl);
	    
	    
		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("Button.background"));
		panel.setBounds(10, 73, 300, 274);
		contentPane.add(panel);
		
		JLabel jLabTop = new JLabel(new ImageIcon("img/grille.jpg"));
		JLabel jLabBottom = new JLabel(new ImageIcon("img/grille.jpg"));
		//jLabTop.addMouseListener(souris);
		//panel.add(jLabTop);
		
		jLabTop.setText("");
		jLabTop.setToolTipText("");
		jLabTop.setVerticalAlignment(SwingConstants.TOP);
		jLabTop.setHorizontalAlignment(SwingConstants.LEFT);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(UIManager.getColor("Button.background"));
		panel_1.setBounds(10, 358, 300, 274);
		contentPane.add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(255, 255, 255));
		panel_2.setBounds(75, 26, 359, 40);
		contentPane.add(panel_2);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(0, 255, 255));
		panel_3.setBounds(354, 492, 146, 58);
		contentPane.add(panel_3);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(0, 255, 255));
		panel_4.setBounds(354, 422, 146, 59);
		contentPane.add(panel_4);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(new Color(0, 255, 255));
		panel_5.setBounds(354, 358, 146, 58);
		contentPane.add(panel_5);
		
		JButton btnNouvellePartie = new JButton("Nouvelle Partie");
		btnNouvellePartie.setBounds(325, 110, 139, 34);
		contentPane.add(btnNouvellePartie);
		
		JButton btnArrter = new JButton("Arr\u00EAter");
		btnArrter.setBounds(325, 179, 139, 40);
		contentPane.add(btnArrter);
		
		MouseListener souris = new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("x : "+arg0.getX()+"\ny : "+arg0.getY());
			}
		};
		
		jLabTop.addMouseListener(souris);
		jLabBottom.addMouseListener(souris);
		panel.add(jLabTop);
		panel_1.add(jLabBottom);
	}
}