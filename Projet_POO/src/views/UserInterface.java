package views;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;

import connection.UDPConnect;
import models.User;
import javax.swing.JFormattedTextField;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;

import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JComboBox;

public class UserInterface {

	private JFrame frame;
	private JTextField txtChangeYourLogin;
	
	private String login;
	
	private static User user;
	private static UDPConnect udp_session;
	
	private WindowAdapter windowAdapter = null;
	private HashMap<User,ChatWindow> chatStarted = new HashMap<User,ChatWindow>();
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
	
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int longueur = d.width *2/3;
		int hauteur = d.height *2/3;
		frame.setSize(d);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		frame.setVisible(true);
		
		this.windowAdapter = new WindowAdapter() {
	        // WINDOW_CLOSING event handler
	        @Override
	        public void windowClosing(WindowEvent e) {
	            super.windowClosing(e);
	            udp_session.closeSession();                   
	        }

	        // WINDOW_CLOSED event handler
	        @Override
	        public void windowClosed(WindowEvent e) {
	            super.windowClosed(e);
	            // Close application if you want to with System.exit(0)
	            // but don't forget to dispose of all resources 
	            // like child frames, threads, ...
	            // System.exit(0);
	        }
	    };
	    
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.addWindowListener(this.windowAdapter);
		
		JLabel lblNewLabel = new JLabel("Change your login here");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 21));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(1565, 31, 272, 40);
		frame.getContentPane().add(lblNewLabel);
		
		txtChangeYourLogin = new JTextField();
		txtChangeYourLogin.setFont(new Font("Tahoma", Font.PLAIN, 21));
		txtChangeYourLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login = txtChangeYourLogin.getText();
			}
		});
		txtChangeYourLogin.setBounds(1565, 82, 290, 40);
		frame.getContentPane().add(txtChangeYourLogin);
		txtChangeYourLogin.setColumns(10);
		
		JButton btnNewButton = new JButton("Change");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 21));
		
		btnNewButton.setBounds(1657, 138, 124, 40);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblMylogin = new JLabel(user.getLogin());
		lblMylogin.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblMylogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblMylogin.setBounds(0, 0, 986, 37);
		frame.getContentPane().add(lblMylogin);
		String[] connectedUsers = udp_session.getConnectedUsersName();
		
		JComboBox comboBox = new JComboBox(connectedUsers);
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 21));
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("STARTING A CHAT WITH " + comboBox.getSelectedItem());
				String receiverLogin = comboBox.getSelectedItem().toString();
				User receiver = udp_session.getUserConnected(receiverLogin);
				//si le chat n'est pas déjà ouvert, on l'ouvre
				if (!chatStarted.containsKey(receiver)){
					ChatWindow chat = new ChatWindow(user,receiver);
					chatStarted.put(receiver, chat);
				} else {
					//s'il est déjà ouvert mais minimisé, on le réaffiche en premier plan
					frame.toBack();
					chatStarted.get(receiver).putInFront();
				}
			}
		});
		comboBox.setBounds(42, 192, 159, 45);
		
		frame.getContentPane().add(comboBox);
		
		JLabel lblStartAChat = new JLabel("Start a chat with");
		lblStartAChat.setFont(new Font("Tahoma", Font.PLAIN, 21));
		lblStartAChat.setBounds(42, 136, 230, 45);
		frame.getContentPane().add(lblStartAChat);
		
		//String[] data = udp_session.getConnectedUsersName();
		
	}
}

