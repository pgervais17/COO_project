package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import connection.UDPConnect;
import models.User;

public class LoginWindow {
	//variables swing
	private JFrame frame;
	private JTextField textField;
	//autres variables
	private String login;
	private UDPConnect session_udp;
	private User current_user;
	//variables de test
	private User testuser;
	private UDPConnect testudp;
	
	private UserInterface userinterface;
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
		try {
			current_user = new User(l);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		session_udp = new UDPConnect(current_user);
		session_udp.start();
	}
	
	
	/**
	 * Create the application.
	 */
	public LoginWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//POUR TEST UNIQUEMENT
		try{
			testuser = new User("paul");
			testudp = new UDPConnect(testuser);
			testudp.start();
		}catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		
		
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Please enter login");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(143, 23, 145, 14);
		frame.getContentPane().add(lblNewLabel);
		
		textField = new JTextField();
		textField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login = textField.getText();
			}
		});
		textField.setBounds(176, 49, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Log in");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				create_new_user_session(login);
				System.out.println(login+": "+ current_user.getPort()+ ", " + current_user.getAddress());
				System.out.println(testuser.getLogin()+": "+testuser.getPort() + ", "+ testuser.getAddress());
				//on demande aux autres utilisateurs de v�rifier le login entr�
				session_udp.sendMessageBroadcast("Verify,"+login+","+current_user.getPort(), testudp.getPort());
				//N�cessaire pour laisser le temps aux utilisateurs d'envoyer leur login � session_udp et permettre � session_udp de traiter les r�ponses
				try {
					session_udp.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//si le login est d�j� pris : getIsLoginValid renvoie false
				if (session_udp.getIsLoginValid().equals(false)){
					JOptionPane.showMessageDialog(null,"This login is already used, please choose another one.","Error",JOptionPane.ERROR_MESSAGE);

				} 
				//sinon getIsLoginValid renvoie true donc on peut prendre ce pseudo et se connecter
				else {
					session_udp.sendMessageBroadcast("Connected,"+login+","+current_user.getPort(), testudp.getPort());
					JOptionPane.showMessageDialog(null,"Connection done! Your login is: "+session_udp.getLogin(),"Good",JOptionPane.INFORMATION_MESSAGE);
					UserInterface window = new UserInterface(current_user, session_udp);
				}
			}
		});
		btnNewButton.setBounds(176, 82, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("fermer sockets");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				session_udp.closeSession();
				testudp.closeSession();
			}
		});
		btnNewButton_1.setBounds(74, 182, 122, 23);
		frame.getContentPane().add(btnNewButton_1);
	}
}
