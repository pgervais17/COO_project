package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
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
				System.out.println(login+": "+ current_user.getPort());
				System.out.println(testuser.getLogin()+": "+testuser.getPort());
				System.out.println(session_udp);
				session_udp.sendMessageBroadcast("Verify,"+login+","+current_user.getPort(), testudp.getPort(),session_udp);
				System.out.println("OK");
			}
		});
		btnNewButton.setBounds(176, 82, 89, 23);
		frame.getContentPane().add(btnNewButton);
	}
}