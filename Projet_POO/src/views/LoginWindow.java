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
	//variables de test
	/*private User testuser;
	private UDPConnect testudp;
	private User testuser2;
	private UDPConnect testudp2;*/
	
	private UserInterface userinterface=null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginWindow window = new LoginWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
	 * Create the application.
	 */
	public LoginWindow() {
		initialize();
	}
	
	public void closeWindow() {
		this.frame.setVisible(false);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		/*testuser = new User("paul");
		testudp = new UDPConnect(testuser);
		testudp.start();
		testuser2 = new User("john");
		testudp2 = new UDPConnect(testuser2);
		testudp2.start();*/
		
		
		frame = new JFrame();
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int longueur = d.width *2/3;
		int hauteur = d.height *2/3;
		frame.setSize(d);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
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
				create_new_user_session(login);
				System.out.println(login+": "+ current_user.getPort()+ ", " + current_user.getAddress());
				//on demande aux autres utilisateurs de vérifier le login entré
				/*session_udp.sendMessageBroadcast("Verify,"+login+","+current_user.getPort(), testudp.getPort());
				session_udp.sendMessageBroadcast("Verify,"+login+","+current_user.getPort(), testudp2.getPort());*/
				session_udp.sendMessageBroadcast("Verify,"+login+","+current_user.getPort());
				//Nécessaire pour laisser le temps aux utilisateurs d'envoyer leur login à session_udp et permettre à session_udp de traiter les réponses
				try {
					session_udp.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//si le login est déjà pris : getIsLoginValid renvoie false
				if (session_udp.getIsLoginValid().equals(false)){
					JOptionPane.showMessageDialog(null,"This login is already used, please choose another one.","Error",JOptionPane.ERROR_MESSAGE);

				} 
				//sinon getIsLoginValid renvoie true donc on peut prendre ce pseudo et se connecter
				else {
					session_udp.sendMessageBroadcast("Connected,"+login+","+current_user.getPort());
					//session_udp.sendMessageBroadcast("Connected,"+login+","+current_user.getPort());
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
