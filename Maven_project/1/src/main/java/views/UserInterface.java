package views;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;

import connection.TCPConnect;
import connection.UDPConnect;
import models.User;
import javax.swing.JFormattedTextField;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Toolkit;

import javax.swing.JTextPane;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class UserInterface {

	private JFrame frame;
	private JTextField txtChangeYourLogin;
	
	private String login;
	
	private User user;
	private UDPConnect udp_session;
	private TCPConnect tcp_session;
	
	private WindowAdapter windowAdapter = null;
	private HashMap<User,ChatWindow> chatStarted = new HashMap<User,ChatWindow>();
	private String[] connectedUsers = null;
	private JComboBox comboBox;
	private String newlogin = null;
	private JLabel lblMylogin;
	
	/**
	 * Create the application.
	 */
	public UserInterface(User u, UDPConnect session) {
		this.user=u;
		this.login = this.user.getLogin();
		this.udp_session = session;
		this.udp_session.setUserInterface(this);
		this.tcp_session = new TCPConnect(u,this);
		this.tcp_session.start();

		
		initialize();
	}
	
	public void set_User(User u) { 
		this.user=u;
	}
	
	public void set_UDPsession(UDPConnect u) { 
		this.udp_session=u;
	}
	public UDPConnect get_UDPsession() { 
		return this.udp_session;
	}
	
	public void updateListUsersAvailable() {
		this.connectedUsers = this.udp_session.getConnectedUsersName();
		comboBox.removeAllItems();
		createComboBox(this.connectedUsers);
	}
	
	public void createComboBox(String[] listusers){
		
		comboBox.addItem("Select user");
		for (String username : listusers){
			comboBox.addItem(username);
		}
		
	}
	public Boolean isChatStarted(String r) {
		User u = this.udp_session.getUserByName(r);
		return this.chatStarted.containsKey(u);
	}
	public void removeChatStarted(ChatWindow chat) {
		this.chatStarted.remove(chat.getReceiver());
	}
	public void addChatStarted(ChatWindow chat) {
		this.chatStarted.put(chat.getReceiver(),chat);
	}
	public void popUpChangeLogin(String previousLogin,String newLogin) {
		JOptionPane.showMessageDialog(null,"Info: the user " + previousLogin + " is now " + newLogin + " (login change)","Good",JOptionPane.INFORMATION_MESSAGE);
		User u = udp_session.getUserConnected(newLogin);
		if (isChatStarted(u.getLogin())) {
			this.chatStarted.get(u).setChatTitle(newLogin);;
		} 
	}
	//methode utilisée pour avertir de la déconnexion d'un utilisateur si un chat est en cours avec
	public void userDisconnected(String username) {
		User u = udp_session.getUserConnected(username);
		if (isChatStarted(u.getLogin())) {
			this.chatStarted.get(u).showPopUpDisconnectMessage();
			this.chatStarted.get(u).close();
		} else {
			JOptionPane.showMessageDialog(null,"User " + username + " has disconnected from the server.","Good",JOptionPane.INFORMATION_MESSAGE);
		}
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
				
		frame = new JFrame();
	
		//récuperer la dimension de l'écran
				Dimension tailleMoniteur = Toolkit.getDefaultToolkit().getScreenSize();
				int longueur = tailleMoniteur.width;
				int hauteur = tailleMoniteur.height;
				//régler la taille de JFrame à 2/3 la taille de l'écran
				frame.setBounds(0, 0, longueur,hauteur);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		frame.setVisible(true);
		
		this.windowAdapter = new WindowAdapter() {
	        // WINDOW_CLOSING event handler
	        @Override
	        public void windowClosing(WindowEvent e) {
	            super.windowClosing(e);
	            udp_session.sendMessageBroadcast("Disconnected," + login + "," + user.getPort());
	            udp_session.closeSession();    
	            tcp_session.closeSession();
	        }

	        // WINDOW_CLOSED event handler
	        @Override
	        public void windowClosed(WindowEvent e) {
	            super.windowClosed(e);
	        }
	    };
	    
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.addWindowListener(this.windowAdapter);
		
		JLabel lblNewLabel = new JLabel("Change your login here");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 21));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(1100, 31, 272, 40);
		frame.getContentPane().add(lblNewLabel);
		
		txtChangeYourLogin = new JTextField();
		txtChangeYourLogin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				newlogin = txtChangeYourLogin.getText();
				
			}
		});
		txtChangeYourLogin.setFont(new Font("Tahoma", Font.PLAIN, 21));
		txtChangeYourLogin.setBounds(1100, 82, 290, 40);
		frame.getContentPane().add(txtChangeYourLogin);
		txtChangeYourLogin.setColumns(10);
		
		JButton btnNewButton = new JButton("Change");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!newlogin.equals(null)){
					udp_session.sendMessageBroadcast("VerifyNewLogin,"+login+","+user.getPort()+","+newlogin);
					try {
						udp_session.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//System.out.println(udp_session.getIsLoginValid());
					//si le login est déjà pris : getIsLoginValid renvoie false
					if (udp_session.getIsLoginValid().equals(false)){
						JOptionPane.showMessageDialog(null,"This login is already used, please choose another one.","Error",JOptionPane.ERROR_MESSAGE);
						//on remet isLoginValid a true pour pouvoir retenter de changer de login
						udp_session.setIsLoginValid(true);
					} 
					//sinon getIsLoginValid renvoie true donc on peut prendre ce pseudo
					else {
						udp_session.sendMessageBroadcast("NotifyChange,"+udp_session.getLogin()+","+udp_session.getPort()+","+newlogin);
						login = newlogin;
						udp_session.setLogin(login);
						JOptionPane.showMessageDialog(null,"Login changed! Your new login is: "+udp_session.getLogin(),"Good",JOptionPane.INFORMATION_MESSAGE);
						lblMylogin.setText(login);
					}
				}		
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 21));
		
		btnNewButton.setBounds(1175, 138, 124, 40);
		frame.getContentPane().add(btnNewButton);
		
		lblMylogin = new JLabel(user.getLogin());
		lblMylogin.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblMylogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblMylogin.setBounds(0, 0, 986, 37);
		frame.getContentPane().add(lblMylogin);
		this.connectedUsers = udp_session.getConnectedUsersName();
		
		comboBox = new JComboBox();
		createComboBox(connectedUsers);
		final UserInterface ui = this;
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				 if (event.getStateChange() == ItemEvent.SELECTED) {
		                String receiverLogin = comboBox.getSelectedItem().toString();
		                if (receiverLogin.equals("Select user")) {
		                }
		                else {
		                	User receiver = udp_session.getUserConnected(receiverLogin);
		                	 //si le chat n'est pas déjà ouvert, on l'ouvre
		                	if (!chatStarted.containsKey(receiver)){		         
		                		
		                		ChatWindow chat = new ChatWindow(user,receiver,tcp_session,ui);
		                		addChatStarted(chat);
		                	}else {
		                		//s'il est déjà ouvert mais minimisé, on le réaffiche en premier plan
		                		frame.toBack();
		                		chatStarted.get(receiver).putInFront();
		                	}
		                }
			       }
				
			}
		});
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 21));
		comboBox.setBounds(42, 192, 159, 45);
		
		frame.getContentPane().add(comboBox);
		
		JLabel lblStartAChat = new JLabel("Start a chat with");
		lblStartAChat.setFont(new Font("Tahoma", Font.PLAIN, 21));
		lblStartAChat.setBounds(42, 136, 230, 45);
		frame.getContentPane().add(lblStartAChat);						
	}
}

