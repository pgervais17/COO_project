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

import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.Frame;

public class ChatWindow {

	private JFrame frmChat;
	private static User sender;
	private static User receiver;
	private static TCPConnect tcp_session;
	private JTextField textField;
	//for now we only consider messages as strings
	private String message;
	private JScrollPane scrollPane;
	private WindowAdapter windowAdapter = null;
	private JTextArea textArea;
	//private TCPConnect tcp_receiver_session;
	private TCPThread currentThread;
	//private TCPThread currentThread2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatWindow window = new ChatWindow(sender,receiver,tcp_session);
					window.frmChat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public ChatWindow(User s, User r, TCPConnect user_tcp_session) {
		setSender(s);
		setReceiver(r);
		this.tcp_session = user_tcp_session;
		/*tcp_session = new TCPConnect(sender);
		tcp_session.start();
		System.out.println("Session tcp started"); */
		
		//TEST
		//tcp_receiver_session = new TCPConnect(receiver);
		//tcp_receiver_session.start();
		
		tcp_session.connectTo(receiver);
		//tcp_receiver_session.connectTo(sender);
		currentThread = tcp_session.getTCPThreadWith(receiver);
		System.out.println("found thread");
		currentThread.setCurrentChat(this);
		
		//currentThread2 = tcp_receiver_session.getTCPThreadWith(sender);
		System.out.println("found thread");
		//currentThread2.setCurrentChat(this);
		initialize();
	}
	
	//used to put the frame back in front when it was minimized
		public void putInFront(){
			frmChat.setState(Frame.NORMAL);
			frmChat.toFront();
			frmChat.setVisible(true);
		}
	
	public void setSender(User u) {
		sender = u;
	}
	public void setReceiver(User u) {
		receiver = u;
	}
	
	public void displayMessage(String whoSent,String m){
		textArea.append(whoSent + ": " + m + "\n");
		
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChat = new JFrame();
		frmChat.setTitle("Chat with " + receiver.getLogin());
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int longueur = d.width *2/3;
		int hauteur = d.height *2/3;
		frmChat.setSize(d);
		frmChat.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmChat.setVisible(true);
		frmChat.getContentPane().setLayout(null);
		
		this.windowAdapter = new WindowAdapter() {
	        // WINDOW_CLOSING event handler
	        @Override
	        public void windowClosing(WindowEvent e) {
	            super.windowClosing(e);
	            tcp_session.closeThreadWith(receiver);
				//tcp_receiver_session.closeThreadWith(sender);                   
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
	    
	    frmChat.addWindowListener(this.windowAdapter);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 21));
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				message = textField.getText();
			}
		});
		textField.setBounds(468, 916, 969, 74);
		frmChat.getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton = new JButton("Send");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 21));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (message.length() > 0) {
						tcp_session.sendMessage(message, receiver);
						//displayMessage(sender.getLogin(),message);
						textField.setText(null);
						message = "";
					}
				} catch (Exception ex) {
					
				}
			}
		});
		btnNewButton.setBounds(1497, 939, 98, 29);
		frmChat.getContentPane().add(btnNewButton); 
		
		
		/*JButton btnNewButton_1 = new JButton("test");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tcp_receiver_session.sendMessage("Réponse!",sender);
			}
		});
		btnNewButton_1.setBounds(21, 932, 89, 23);
		frmChat.getContentPane().add(btnNewButton_1); */
		
		textArea = new JTextArea();
		textArea.setTabSize(20);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 21));
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setVisible(true);
		textArea.setBounds(141, 71, 89, 60);
		
		JScrollPane scrollPane_1 = new JScrollPane(textArea);
		scrollPane_1.setBounds(66, 36, 1784, 800);
		frmChat.getContentPane().add(scrollPane_1);
		
		
		
		
		
	}
}
