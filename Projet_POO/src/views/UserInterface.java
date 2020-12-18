package views;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import connection.UDPConnect;
import models.User;
import javax.swing.JFormattedTextField;
import java.awt.TextArea;
import java.awt.TextField;
import javax.swing.JTextPane;
import java.awt.Font;

public class UserInterface {

	private JFrame frame;
	private JTextField txtChangeYourLogin;
	
	private String login;
	
	private static User user;
	private static UDPConnect udp_session;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserInterface window = new UserInterface(user,udp_session);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public UserInterface(User u, UDPConnect session) {
		this.user=u;
		this.udp_session = session;
		initialize();
	}
	
	public void set_User(User u) { 
		this.user=u;
	}
	
	public void set_UDPsession(UDPConnect u) { 
		this.udp_session=u;
	}
	
	public void ListConnect() {
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
				
		frame = new JFrame();
	
		frame.setBounds(100, 100, 1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		frame.setVisible(true);
		
		JLabel lblNewLabel = new JLabel("Change your login here");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(833, 45, 153, 14);
		frame.getContentPane().add(lblNewLabel);
		
		txtChangeYourLogin = new JTextField();
		txtChangeYourLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login = txtChangeYourLogin.getText();
			}
		});
		txtChangeYourLogin.setBounds(875, 65, 86, 20);
		frame.getContentPane().add(txtChangeYourLogin);
		txtChangeYourLogin.setColumns(10);
		
		JButton btnNewButton = new JButton("Change");
		
		btnNewButton.setBounds(875, 90, 86, 23);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblMylogin = new JLabel("Mylogin");
		lblMylogin.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblMylogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblMylogin.setBounds(0, 0, 986, 37);
		frame.getContentPane().add(lblMylogin);
	}
}
