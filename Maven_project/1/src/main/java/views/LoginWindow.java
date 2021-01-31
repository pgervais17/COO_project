package views;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import connection.UDPConnect;
import database.Database_config;
import models.User;
import java.awt.Font;

public class LoginWindow {
	//variables swing
	private JFrame frame;
	private JTextField textField;
	//autres variables
	private String login;
	private UDPConnect session_udp;
	private User current_user;
	
	private UserInterface userinterface=null;

	public LoginWindow() {
		Database_config database = new Database_config();
		database.configureDatabase();
		initialize();	
	}
	
	public void closeWindow() {
		this.frame.setVisible(false);
	}
	
	public void create_new_user_session(String l) {
		Integer size = l.length();
		if (!size.equals(0)) {
			current_user = new User(l);;
			session_udp = new UDPConnect(current_user);
			session_udp.start();
		} else {
			JOptionPane.showMessageDialog(null,"You need to enter at least one character!","Error",JOptionPane.ERROR_MESSAGE);
		}
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		//r�cuperer la dimension de l'�cran
		Dimension tailleMoniteur = Toolkit.getDefaultToolkit().getScreenSize();
		int longueur = tailleMoniteur.width;
		int hauteur = tailleMoniteur.height;
		//r�gler la taille de JFrame � 2/3 la taille de l'�cran
		frame.setBounds(0, 0, longueur,hauteur);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		
		
		JLabel lblNewLabel = new JLabel("Please enter login");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(855, 337, 225, 38);
		frame.getContentPane().add(lblNewLabel);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 27));
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				login = textField.getText();
			}
		});
		
		textField.setBounds(805, 410, 325, 38);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Log in");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 21));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//il faut diff�rencier le cas o� on clique sur log in pour lere fois ou si on a d�j� cliqu� avant
				//car la premi�re fois, on cr�e un user et une session udp, les autres fois il faut juste changer les logins
				if (session_udp == null) {
					create_new_user_session(login);
					System.out.println("Une session a �t� cr��e pour l'utilisateur " + login + " (� v�rifier)");
				}
				else {
					System.out.println("Une session a d�j� �t� cr��e mais a �chou�, on change juste le login");
					current_user.setLogin(login);
					session_udp.setLogin(login);
				}
				//on demande aux autres utilisateurs de v�rifier le login entr�
				session_udp.sendMessageBroadcast("Verify,"+login+","+current_user.getPort());
				//N�cessaire pour laisser le temps aux utilisateurs d'envoyer leur login � session_udp et permettre � session_udp de traiter les r�ponses
				try {
					session_udp.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//si le login est d�j� pris : getIsLoginValid renvoie false
				if (session_udp.getIsLoginValid().equals(false)){
					JOptionPane.showMessageDialog(null,"This login is already used, please choose another one.","Error",JOptionPane.ERROR_MESSAGE);
					//On remet isLoginValid � true, sinon getIsLoginValid renverra toujours false
					session_udp.setIsLoginValid(true);
				} 
				//sinon getIsLoginValid renvoie true donc on peut prendre ce pseudo et se connecter
				else {
					session_udp.sendMessageBroadcast("Connected,"+login+","+current_user.getPort());
					JOptionPane.showMessageDialog(null,"Connection done! Your login is: "+session_udp.getLogin(),"Good",JOptionPane.INFORMATION_MESSAGE);
					userinterface = new UserInterface(current_user, session_udp);
					closeWindow();
				}
			}
		});
		btnNewButton.setBounds(917, 482, 100, 38);
		frame.getContentPane().add(btnNewButton);
		
	}
}
