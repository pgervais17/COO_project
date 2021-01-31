package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

import models.User;
import javax.swing.JLabel;

import connection.TCPConnect;
import connection.TCPThread;
import database.Database_Message;

import javax.swing.JTextField;
import javax.swing.WindowConstants;

import models.Message;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Frame;

public class ChatWindow {

	private JFrame frmChat;
	private User sender;
	private User receiver;
	private TCPConnect tcp_session;
	private JTextField textField;
	//for now we only consider messages as strings
	private String message;
	private JScrollPane scrollPane;
	private WindowAdapter windowAdapter = null;
	private JTextArea textArea;
	private TCPThread currentThread;
	
	public ChatWindow(User s, User r, TCPConnect user_tcp_session) {
		setSender(s);
		this.receiver = r;
		this.tcp_session = user_tcp_session;
		this.tcp_session.connectTo(receiver,this);
		
		initialize();
	}
	public ChatWindow(User s, TCPConnect user_tcp_session) {
		setSender(s);
		this.tcp_session = user_tcp_session;
		initialize();
	}

	//used to put the frame back in front when it was minimized
		public void putInFront() {
			frmChat.setState(Frame.NORMAL);
			frmChat.toFront();
			frmChat.setVisible(true);
		}
	
	public void setSender(User u) {
		sender = u;
	}
	public void setReceiver(String name) {
		System.out.println("Tried to change ChatWindow title");
		this.receiver = tcp_session.getUserInterface().get_UDPsession().getUserByName(name);
		frmChat.setTitle("Chat with " + receiver.getLogin());
	}
	
	public void displayMessage(String whoSent,String m){
		textArea.append(whoSent + ": " + m + "\n");
		
	}
	
	public void retrieveHistory() {
		Database_Message db = this.tcp_session.getDatabase();
		try {
			ArrayList<Message> history = db.getHistory(sender.getAddress().toString(), receiver.getAddress().toString());
			System.out.println("Retrieving previous messages...");
			for (Message m : history) {
				displayMessage(m.Get_Sender(),m.Get_Content());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChat = new JFrame();
		
		if (this.receiver != null) {
			frmChat.setTitle("Chat with " + receiver.getLogin());
		}
		
		//récuperer la dimension de l'écran
				Dimension tailleMoniteur = Toolkit.getDefaultToolkit().getScreenSize();
				int longueur = tailleMoniteur.width;
				int hauteur = tailleMoniteur.height;
				//régler la taille de JFrame à 2/3 la taille de l'écran
				frmChat.setBounds(0, 0, longueur,hauteur);

		frmChat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmChat.setVisible(true);
		frmChat.getContentPane().setLayout(null);
		
		this.windowAdapter = new WindowAdapter() {
	        // WINDOW_CLOSING event handler
	        @Override
	        public void windowClosing(WindowEvent e) {
	            super.windowClosing(e);
	            tcp_session.closeThreadWith(receiver);                   
	        }

	        // WINDOW_CLOSED event handler
	        @Override
	        public void windowClosed(WindowEvent e) {
	            super.windowClosed(e);
	        }
	    };
	    
	    frmChat.addWindowListener(this.windowAdapter);
		
	    
	    
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 21));
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				message = textField.getText();
			}
		});
		textField.setBounds(480, 605, 969, 74);
		frmChat.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 21));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("On est entrés dans le send");
				try {
					if (message.length() > 0) {
						System.out.println("Envoi du message " + message);
						tcp_session.sendMessage(message, receiver);
						System.out.println("Message envoyé");
						textField.setText(null);
						message = "";
					}
				} catch (Exception ex) {
					
				}
			}
		});
		btnNewButton.setBounds(1025, 758, 98, 29);
		frmChat.getContentPane().add(btnNewButton); 
			
		textArea = new JTextArea();
		textArea.setTabSize(20);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 21));
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setVisible(true);
		textArea.setBounds(141, 71, 89, 60);
		if (this.receiver != null) {
			retrieveHistory();
		}
		JScrollPane scrollPane_1 = new JScrollPane(textArea);
		scrollPane_1.setBounds(66, 36, 1410, 533);
		frmChat.getContentPane().add(scrollPane_1);	
	}
}
